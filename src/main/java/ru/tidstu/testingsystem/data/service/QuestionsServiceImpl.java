package ru.tidstu.testingsystem.data.service;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.tidstu.testingsystem.data.dao.QuestionsDAO;
import ru.tidstu.testingsystem.data.entity.Question;

import java.util.List;

@Log4j
public class QuestionsServiceImpl implements QuestionsService {

    @Autowired
    private QuestionsDAO questionsDAO;

    @Transactional
    public void addQuestion(Question question) {
        questionsDAO.addQuestion(question);
    }

    @Transactional
    public void removeQuestion(String titleQuestion) {
        questionsDAO.removeQuestion(titleQuestion);
    }

    @Transactional
    public void changeQuestion(String nameQuestion, String newTitle, String newText) {
        questionsDAO.changeQuestion(nameQuestion, newTitle, newText);
    }

    public Question getQuestion(String titleQuestion) {
        return questionsDAO.getQuestion(titleQuestion);
    }

    public List<Question> getAllQuestions() {
        return questionsDAO.getAllQuestions();
    }

}
