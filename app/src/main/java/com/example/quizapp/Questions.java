package com.example.quizapp;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Questions {
    static abstract class QnA {
        protected String question;
        protected String answer;
        protected boolean correct;
        String getQuestion() {
            return question;
        }

        String getAnswer() {
            return answer;
        }
        void correct() { correct = true; }
    }

    // 객관식 문제 클래스
    static class MultipleChoiceQnA extends QnA {
        private final String[] choices;
        MultipleChoiceQnA(String question, String answer, String[] choices) {
            this.question = question;
            this.answer = answer;
            this.choices = choices;
        }

        String[] getChoices() {
            Collections.shuffle(Arrays.asList(choices));
            return choices;
        }
    }

    // 주관식 문제 클래스
    static class SubjectiveQnA extends QnA {
        SubjectiveQnA(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }
    private final List<QnA> questionList; // 문제 리스트
//    private QuestionAndAnswer curQuestion;
    private int currentQnAidx = -1; // 현재 문제의 인덱스

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

        Log.d("Questions", "idxList: " + idxList);

        Map.Entry<String, String>[] questionSet = new Map.Entry[questionListSize];
        questionSet = questionListMap.entrySet().toArray(questionSet);

        // questionSet에서 문제를 하나씩 추출
        for (int nextIdx : idxList) {
            String[] q = questionSet[nextIdx].getKey().split(":");
            String question = q[1];
            String value = questionSet[nextIdx].getValue();
            // 객관식일 경우
            if (q[0].equals("M")) {
                String[] choices = value.split(":");
                String answer = choices[0];
                questionList.add(new MultipleChoiceQnA(question, answer, choices));
            }
            // 주관식일 경우
            else if (q[0].equals("S")) {
                questionList.add(new SubjectiveQnA(question, value));
            }
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

    /**
     * 인자로 주어진 파일로부터 문제들을 읽어옵니다.
     * @param fileId 문제들이 저장되어 있는 파일 id
     * @return Questions 객체
     */
    public static Questions readQuizFromFile(Resources resources, int fileId) {
        Map<String, String> qmap = new HashMap<>();
        try {
            InputStream is = resources.openRawResource(fileId);
//            FileInputStream fis = new FileInputStream(filename);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                int delimIdx = line.indexOf('-');
                String q = line.substring(0, delimIdx);
                String ans = line.substring(delimIdx + 1);
//                String[] splitted = line.split("-");
//                qmap.put(splitted[0], splitted[1]);
                qmap.put(q, ans);
            }

            is.close();

        } catch (IOException ioe) {
            Log.d("Questions", ioe.toString());
        }

        return new Questions(qmap);
    }
}
