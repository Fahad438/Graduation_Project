package com.gp.abcpro;

public class Lesson {

    int chapterNum, lessonNum;
    String lessonName, lessonURL;

    public Lesson(int chapterNum, int lessonNum, String lessonName, String lessonURL) {
        this.chapterNum = chapterNum;
        this.lessonNum = lessonNum;
        this.lessonName = lessonName;
        this.lessonURL = lessonURL;
    }

    public int getChapterNum() {
        return chapterNum;
    }

    public int getLessonNum() {
        return lessonNum;
    }

    public String getLessonName() {
        return lessonName;
    }

    public String getLessonURL() {
        return lessonURL;
    }
}
