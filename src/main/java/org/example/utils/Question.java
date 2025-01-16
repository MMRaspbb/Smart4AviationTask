package org.example.utils;

public record Question(char questionType, int[] parameters) {
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(questionType).append(" ");
        for(int parameter: parameters){
            stringBuilder.append(parameter).append(" ");
        }
        return stringBuilder.toString();
    }
}
