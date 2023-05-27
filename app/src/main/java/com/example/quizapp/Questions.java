package com.example.quizapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            Collections.shuffle(Arrays.asList(choices));
            return choices;
        }
    }
    private List<QnA> questionList;
//    private QuestionAndAnswer curQuestion;
    private int currentQnAidx = -1;

    /**
     * 생성자
     * @param questionListMap 문제 정보를 담고 있는 Map 객체
     */
    Questions(Map<String, String> questionListMap) {
        int questionListSize = questionListMap.size();
        questionList = new ArrayList<>(questionListSize);

        Integer[] idxArray = new Integer[questionListSize];
        for (int i = 0; i < questionListSize; i++) {
            idxArray[i] = i;
        }

        // 문제집을 섞은 다음 questionList에 추가
        List<Integer> idxList = Arrays.asList(idxArray);
        Collections.shuffle(idxList);

        Log.d("Questions", "idxList: " + idxList.toString());

        Map.Entry<String, String>[] questionSet = new Map.Entry[questionListSize];
        questionSet = questionListMap.entrySet().toArray(questionSet);

        for (int nextIdx : idxList) {
            String[] choices = questionSet[nextIdx].getValue().split(":");
            String answer = choices[0];
            questionList.add(new QnA(questionSet[nextIdx].getKey(), answer, choices));
        }

//        for (Map.Entry<String, String> entry : questionListMap.entrySet()) {
//            String[] choices = entry.getValue().split(":");
//            String answer = choices[0];
//            questionList.add(new QnA(entry.getKey(), answer, choices));
//        }
    }

    /**
     * currentQnAidx를 하나 증가시킨 후 그 인덱스에 해당하는 다음 문제(QnA) 객체를 반환합니다.
     * 처음 불려질 경우 첫번째 문제 객체를 반환합니다.
     * @return QnA 객체
     */
    QnA next() {
        if (currentQnAidx == questionList.size() - 1) currentQnAidx = -1;
        currentQnAidx++;
        return questionList.get(currentQnAidx);
    }

    /**
     * 현재 문제 객체를 반환합니다.
     * @return QnA 객체
     */
    QnA current() {
        return questionList.get(currentQnAidx);
    }

    /**
     * 문제 총 개수를 반환합니다.
     * @return 문제 총 개수
     */
    int size() {
        return questionList.size();
    }
}
