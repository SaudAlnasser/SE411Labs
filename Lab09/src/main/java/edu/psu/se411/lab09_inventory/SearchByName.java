package edu.psu.se411.lab09_inventory;

import java.util.Objects;

public final class SearchByName implements SearchStrategy<Item> {
    private final String value;

    public SearchByName(String name) {
        this.value = Objects.requireNonNull(name, "name");
    }

    @Override
    public boolean matches(Item item) {
        return item.getName().equals(value);
    }
}
