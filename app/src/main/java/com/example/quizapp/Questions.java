package com.example.quizapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Questions {
    static class QnA {
        private final String question;
        private final String answer;
        private final String[] choices;

        QnA(String question, String answer, String[] choices) {
            this.question = question;
            this.answer = answer;
            this.choices = choices;
        }

        String getQuestion() {
            return question;
        }

        String getAnswer() {
            return answer;
        }

        String[] getChoices() {
            // random shuffle?
            return choices;
        }
    }
    private List<QnA> questionList;
//    private QuestionAndAnswer curQuestion;
    private int idx = 0;

    Questions(Map<String, String> questionListMap) {
        questionList = new ArrayList<>(questionListMap.size());

        for (Map.Entry<String, String> entry : questionListMap.entrySet()) {
            String[] choices = entry.getValue().split(":");
            String answer = choices[0];
            questionList.add(new QnA(entry.getKey(), answer, choices));
        }
    }

    QnA next() {
        if (idx >= questionList.size()) idx = 0;
        return questionList.get(idx++);
    }

    int size() {
        return questionList.size();
    }
}
