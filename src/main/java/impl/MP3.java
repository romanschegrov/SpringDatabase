package impl;

import interfaces.Music;

/**
 * Created by ramon on 10.10.2016.
 */
public class MP3 implements Music {

    private int id;
    private String name;
    private Author author;

    @Override
    public String toString() {
        return "MP3{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author=" + author +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
