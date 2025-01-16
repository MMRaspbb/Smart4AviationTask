package org.example;

import org.example.utils.ParserException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AirlinesProcessorTest {
    private final AirlinesProcessor airlinesProcessor = new AirlinesProcessor();
    private static final PrintStream originalOut = System.out;
    private static final InputStream originalIn = System.in;

    private static JSONObject testData;

    @BeforeAll
    static void loadTestData() throws IOException {
        File file = new File("src/test/resources/test_data.json");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        StringBuilder jsonContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonContent.append(line);
        }
        reader.close();
        testData = new JSONObject(jsonContent.toString());
    }

    @Test
    void correctInputTest1() {
        try {
            String input = testData.getString("sample_data_1");
            String output = runSimulation(input);
            String correctOutput = testData.getString("sample_data_1_answer");
            assertEquals(correctOutput, output);
        } catch (ParserException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void correctInputTest2() {
        try {
            String input = testData.getString("sample_data_2");
            String output = runSimulation(input);
            String correctOutput = testData.getString("sample_data_2_answer");
            assertEquals(correctOutput, output);
        } catch (ParserException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void tooMuchDataFirstLineTest() {
        try {
            String input = testData.getString("too_much_data_first_line");
            runSimulation(input);
            fail("Didn't get ParserException.");
        } catch (ParserException e) {
            assertEquals(e.getMessage(), "Wrong number of arguments in the first line.");
        }
    }

    @Test
    void tooLittleDataFirstLineTest() {
        try {
            String input = testData.getString("too_little_data_first_line");
            runSimulation(input);
            fail("Didn't get ParserException.");
        } catch (ParserException e) {
            assertEquals(e.getMessage(), "Wrong number of arguments in the first line.");
        }
    }

    @Test
    void tooMuchPlanesTest() {
        try {
            String input = testData.getString("too_much_planes_data");
            runSimulation(input);
            fail("Didn't get ParserException.");
        } catch (ParserException e) {
            assertEquals(e.getMessage(), "Incorrect planes number.");
        }
    }

    @Test
    void tooLittlePlanesTest() {
        try {
            String input = testData.getString("too_little_planes_data");
            runSimulation(input);
            fail("Didn't get ParserException.");
        } catch (ParserException e) {
            assertEquals(e.getMessage(), "Incorrect planes number.");
        }
    }

    @Test
    void incorrectIntInputTest1() {
        try {
            String input = testData.getString("incorrect_int_data_1");
            runSimulation(input);
            fail("Didn't get ParserException.");
        } catch (ParserException e) {
            assertEquals(e.getMessage(), "String to int error, incorrect format!");
        }
    }

    @Test
    void incorrectIntInputTest2() {
        try {
            String input = testData.getString("incorrect_int_data_2");
            runSimulation(input);
            fail("Didn't get ParserException.");
        } catch (ParserException e) {
            assertEquals(e.getMessage(), "String to int error, incorrect format!");
        }
    }

    @Test
    void incorrectQuestionTypeTest() {
        try {
            String input = testData.getString("incorrect_question_data");
            runSimulation(input);
            fail("Didn't get ParserException.");
        } catch (ParserException e) {
            assertEquals(e.getMessage(), "Question number: 1 has incorrect type.");
        }
    }

    private String runSimulation(String input) throws ParserException {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        airlinesProcessor.solve();
        System.setOut(originalOut);
        System.setIn(originalIn);
        return outputStream.toString();
    }
}