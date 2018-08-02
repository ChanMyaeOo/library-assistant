/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.DAO;

/**
 *
 * @author ChanMyaeOo
 */
public class Book {

    private String id, title, author, publisher, is_available;

    public Book(String id, String title, String author, String publisher) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    public Book(String id, String title, String author, String publisher, String is_available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.is_available = is_available;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIs_available() {
        return is_available;
    }
}
