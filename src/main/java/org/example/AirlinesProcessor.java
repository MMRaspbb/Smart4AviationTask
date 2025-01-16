package org.example;

import org.example.utils.ParserException;
import org.example.utils.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class AirlinesProcessor {
    private int planesNum;
    private int[] planesAddition;
    private int[] planesSum;
    private Question[] questions;
    private int questionsNum;

    public void solve() throws ParserException {
        String input = standardInputToString();
        fillDataFromString(input);
        simulate();
    }

    private String standardInputToString() throws ParserException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            String firstLine = reader.readLine();
            stringBuilder.append(firstLine);
            String[] splittedFirstLine = firstLine.split(" ");
            if (splittedFirstLine.length != 2)
                throw new ParserException("Wrong number of arguments in the first line.");

            int questionNumber = Integer.parseInt(splittedFirstLine[1]);
            stringBuilder.append("\n");
            for (int i = 0; i < questionNumber + 1; i++) {
                stringBuilder.append(reader.readLine()).append("\n");
            }
        } catch (IOException e) {
            throw new ParserException("Stardard input error.");
        } catch (NumberFormatException e) {
            throw new ParserException("String to int error, incorrect format!");
        }
        return stringBuilder.toString();
    }

    private void fillDataFromString(String input) throws ParserException {
        String[] lines = input.split("\n");
        String[] splittedFirstLine = lines[0].split(" ");
        try {
            planesNum = Integer.parseInt(splittedFirstLine[0]);
            planesAddition = Arrays.stream(lines[1].split(" ")).mapToInt(Integer::parseInt).toArray();
        } catch (NumberFormatException e) {
            throw new ParserException("String to int error, incorrect format!");
        }
        if (planesNum != planesAddition.length) {
            throw new ParserException("Incorrect planes number.");
        }
        planesSum = new int[planesNum];
        questionsNum = Integer.parseInt(splittedFirstLine[1]);
        if (questionsNum != lines.length - 2) {
            throw new ParserException("Incorrect questions number.");
        }
        questions = new Question[questionsNum];
        for (int i = 0; i < questionsNum; i++) {
            String[] splittedLine = lines[i + 2].split(" ");
            if (!splittedLine[0].equals("P") && !splittedLine[0].equals("C") && !splittedLine[0].equals("A") && !splittedLine[0].equals("Q")) {
                throw new ParserException("Question number: " + (i + 1) + " has incorrect type.");
            }
            if ((splittedLine[0].equals("C") && splittedLine.length != 3) || (!splittedLine[0].equals("C") && splittedLine.length != 4)) {
                throw new ParserException("Given Question has too much or too little data.");
            }
            char questionType = splittedLine[0].charAt(0);
            int[] parameters;
            try {
                parameters = Arrays.stream(Arrays.copyOfRange(splittedLine, 1, splittedLine.length)).mapToInt(Integer::parseInt).toArray();
            } catch (NumberFormatException e) {
                throw new ParserException("String to int error, incorrect format!");
            }
            questions[i] = new Question(questionType, parameters);
        }
    }

    private void incrementPlanesSum(int multiplier) {
        if (multiplier == 0) return;
        for (int i = 0; i < planesNum; i++) {
            planesSum[i] += planesAddition[i] * multiplier;
        }
    }

    private void printGivenPlanesSum(int i, int j) {
        int sum = 0;
        for (int l = i - 1; l < j; l++) {
            sum += planesSum[l];
        }
        System.out.print(sum + "\n");
    }

    private void simulate() {
        int time = 0;
        int planeNumber, newPassengerNumber, newTime, startPlane, endPlane;
        for (Question question : questions) {
            switch (question.questionType()) {
                case 'P', 'A':
                    planeNumber = question.parameters()[0] - 1;
                    newPassengerNumber = question.parameters()[1];
                    newTime = question.parameters()[2];
                    incrementPlanesSum(newTime - time);
                    time = newTime;
                    planesAddition[planeNumber] = newPassengerNumber;
                    break;
                case 'C':
                    planeNumber = question.parameters()[0] - 1;
                    newTime = question.parameters()[1];
                    incrementPlanesSum(newTime - time);
                    time = newTime;
                    planesAddition[planeNumber] = 0;
                    planesSum[planeNumber] = 0;
                    break;
                case 'Q':
                    startPlane = question.parameters()[0];
                    endPlane = question.parameters()[1];
                    newTime = question.parameters()[2];
                    incrementPlanesSum(newTime - time);
                    time = newTime;
                    printGivenPlanesSum(startPlane, endPlane);
            }
        }
    }
}
