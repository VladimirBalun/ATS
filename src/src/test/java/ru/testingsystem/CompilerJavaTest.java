package ru.testingsystem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.testingsystem.checkingTask.ResultCheckingProgram;
import ru.testingsystem.checkingTask.compilers.Compiler;
import ru.testingsystem.data.entity.TestData;

import java.util.ArrayList;
import java.util.List;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring-context.xml")
public class CompilerJavaTest {

    @Autowired
    @Qualifier("compilerJava")
    private Compiler compilerJava;

    @Test
    public void testErrorCompilation(){
        // Test program with mistake in syntax
        String source = "class main{ \n" +
                        "   public static void main(String args[]){ \n" +
                        "        illegal syntax \n" +
                        "   } \n" +
                        "}";
        ResultCheckingProgram result = compilerJava.checkProgram("Test", source, null);
        Assert.assertEquals(ResultCheckingProgram.ERROR_COMPILATION, result);
    }

    @Test
    public void testErrorRunningProgram(){
        // Test program calculates count letters in the word,
        // but outputs the incorrect result
        String source = "class main{\n" +
                        "   public static void main(String args[]){\n" +
                        "       System.out.println(args[0].length() + 5);\n" +
                        "   }\n" +
                        "}";
        // Test input and output data for program
        List<TestData> testDataForProgram = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            String randomData = String.valueOf((int) (Math.random() * 1000000));
            TestData testData = new TestData(randomData, String.valueOf(randomData.length()), null);
            testDataForProgram.add(testData);
        }
        ResultCheckingProgram result = compilerJava.checkProgram("Test", source, testDataForProgram);
        Assert.assertEquals(ResultCheckingProgram.ERROR_RUNNING_PROGRAM, result);
    }

    @Test
    public void testSuccessRunningProgram(){
        // Test program calculates count letters in the word
        String source = "class main{\n" +
                        "   public static void main(String args[]){\n" +
                        "       System.out.println(args[0].length());\n" +
                        "   }\n" +
                        "}";
        // Test input and output data for program
        List<TestData> testDataForProgram = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            String randomData = String.valueOf((int) (Math.random() * 1000000));
            TestData testData = new TestData(randomData, String.valueOf(randomData.length()), null);
            testDataForProgram.add(testData);
        }
        ResultCheckingProgram result = compilerJava.checkProgram("Test", source, testDataForProgram);
        Assert.assertEquals(ResultCheckingProgram.SUCCESS, result);
    }

}
