package com.gp.abcpro;

public class Book {
    int bookId;
    String title;
    String coverImg;
    String link;

    public Book(int bookId, String title, String coverImg, String link) {
        this.bookId = bookId;
        this.title = title;
        this.coverImg = coverImg;
        this.link = link;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public String getLink() {
        return link;
    }
}
