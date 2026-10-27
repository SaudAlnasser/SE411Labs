package edu.psu.se411.lab09_inventory;

import java.util.Objects;

public final class Book extends Item {
    private final String author;

    public Book(String id, String name, String author) {
        super(id, name);
        this.author = Objects.requireNonNull(author, "author");
    }

    public String getAuthor() { return author; }

    @Override
    public String toString() {
        return "Book [id=" + getId() + ", name=" + getName() + ", author=" + author + "]";
    }
}
