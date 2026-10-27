package edu.psu.se411.lab09_inventory;

import java.util.Objects;

public final class SearchByCategory implements SearchStrategy<Item> {
    private final String value;

    public SearchByCategory(String category) {
        this.value = Objects.requireNonNull(category, "category");
    }

    @Override
    public boolean matches(Item item) {
        return item instanceof ElectronicDevice && ((ElectronicDevice) item).getCategory().equals(value);
    }
}
