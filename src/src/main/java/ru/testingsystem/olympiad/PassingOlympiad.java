package ru.testingsystem.olympiad;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.testingsystem.data.entity.Log;
import ru.testingsystem.data.entity.TestData;
import ru.testingsystem.data.entity.User;
import ru.testingsystem.data.service.TestDataService;
import ru.testingsystem.utils.compilers.Compiler;
import ru.testingsystem.utils.compilers.ResultRunningProgram;
import ru.testingsystem.data.entity.Question;
import ru.testingsystem.data.service.QuestionsService;

import java.util.*;

@Log4j
@Component
public class PassingOlympiad implements Olympiad {

    private final int MAX_COUNT_LOGS_IN_JOURNAL = 9;

    private final QuestionsService questionsService;
    private final TestDataService testDataService;
    private final Compiler compiler;

    private User currentUser;
    private List<Question> questions;
    private Queue<Log> logsOfRunningOlympiad;

    @Autowired
    public PassingOlympiad(QuestionsService questionsService, TestDataService testDataService, @Qualifier("compilerC") Compiler compiler) {
        this.questionsService = questionsService;
        this.testDataService = testDataService;
        this.compiler = compiler;
        questions = questionsService.getQuestions();
        logsOfRunningOlympiad = new LinkedList<Log>();
    }

    public void startOlympiad(String login, String password){
        currentUser = User.builder()
                .login(login)
                .password(password)
                .countTrueAnswers(0)
                .countQuestions(questionsService.getCountQuestions())
                .build();
    }

    public String getStatisticUser() {
        return String.valueOf(currentUser.getCountTrueAnswers() + " / " + currentUser.getCountQuestions());
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getQuestion(int number){
        for (Question question : questions) {
            if(question.getNumber() == number){
                System.out.println(question.toString());
                return question;
            }
        }
        return questions.get(0);
    }

    public Queue<Log> getLogsOfRunningTest(){
        return logsOfRunningOlympiad;
    }

    public ResultRunningProgram checkTask(String nameQuestion, String textProgram){
        List<TestData> testData = testDataService.getTestDataForQuestion(nameQuestion);
        if(!compiler.compileProgram(textProgram)){
            addLog(new Log("Ошибка компиляции в задании " + nameQuestion, getCurrentTime()));
            return ResultRunningProgram.ERROR_COMPILATION;
        }
        if(!compiler.runProgram(testData)){
            addLog(new Log("Ошибка в тестах для задания " + nameQuestion, getCurrentTime()));
            return ResultRunningProgram.LOGIC_ERROR_IN_PROGRAM;
        } else {
            addLog(new Log("Задание " + nameQuestion + " выполнено", getCurrentTime()));
            delQuestion(nameQuestion);
            currentUser.addTrueAnswer();
            return ResultRunningProgram.SUCCESS;
        }
    }

    private void addLog(Log log){
        if(logsOfRunningOlympiad.size() > MAX_COUNT_LOGS_IN_JOURNAL){
            logsOfRunningOlympiad.remove();
        }
        logsOfRunningOlympiad.add(log);
    }

    private String getCurrentTime() {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.HOUR) + ":" +
                calendar.get(Calendar.MINUTE) + ":" +
                calendar.get(Calendar.SECOND);
    }

    private void delQuestion(String title){
        for (Question question : questions) {
            if(question.getTitle().equals(title)){
                questions.remove(question);
                return;
            }
        }
    }

    public void finishOlympiad(){
        questions.clear();
        logsOfRunningOlympiad.clear();
    }

}