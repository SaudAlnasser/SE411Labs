package edu.psu.se411.lab09_inventory;

import java.util.Objects;

public final class SearchById implements SearchStrategy<Item> {
    private final String value;

    public SearchById(String id) {
        this.value = Objects.requireNonNull(id, "id");
    }

    @Override
    public boolean matches(Item item) {
        return item.getId().equals(value);
    }
}
