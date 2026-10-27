package edu.psu.se411.lab09_inventory;

import java.util.Objects;

public final class SearchByAuthor implements SearchStrategy<Item> {
    private final String value;

    public SearchByAuthor(String author) {
        this.value = Objects.requireNonNull(author, "author");
    }

    @Override
    public boolean matches(Item item) {
        return item instanceof Book && ((Book) item).getAuthor().equals(value);
    }
}
