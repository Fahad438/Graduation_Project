package com.gp.abcpro;

import androidx.annotation.NonNull;

public class Exercise {
    private int chapterNum, lessonNum;
    private String question, ans1, ans2, ans3, ans4, correctAns;

    public Exercise(int chapterNum, int lessonNum) {
        this.chapterNum = chapterNum;
        this.lessonNum = lessonNum;
    }
    public Exercise(int lessonNum, String question, String ans1, String ans2, String ans3, String ans4) {
        this.lessonNum = lessonNum;
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
    }
    public Exercise(int lessonNum, String question, String ans1, String ans2, String ans3, String ans4, String correctAns) {
        this.lessonNum = lessonNum;
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.correctAns = correctAns;
    }
    public Exercise(String question, String ans1, String ans2, String ans3, String ans4, String correctAns) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.correctAns = correctAns;
    }
    public Exercise(int lessonNum, String correctAns){
        this.lessonNum = lessonNum;
        this.correctAns = correctAns;
    }

    public int getChapterNum() {
        return chapterNum;
    }
    public int getLessonNum() {
        return lessonNum;
    }
    public String getQuestion() {
        return question;
    }
    public String getAns1() {
        return ans1;
    }
    public String getAns2() {
        return ans2;
    }
    public String getAns3() {
        return ans3;
    }
    public String getAns4() {
        return ans4;
    }
    public String getCorrectAns() {
        return correctAns;
    }

    @NonNull
    public String toString(){
        return getAns1() + ", " + getAns2() + ", " + getAns3() + ", " + getAns4();
    }
}

