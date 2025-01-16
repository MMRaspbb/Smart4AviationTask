package org.example;

import org.example.utils.ParserException;

public class Main {
    public static void main(String[] args) throws ParserException {
        AirlinesProcessor airlinesProcessor = new AirlinesProcessor();
        airlinesProcessor.solve();
    }
}