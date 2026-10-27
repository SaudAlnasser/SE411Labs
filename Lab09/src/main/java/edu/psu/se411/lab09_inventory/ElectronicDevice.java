package edu.psu.se411.lab09_inventory;

import java.util.Objects;

public final class ElectronicDevice extends Item {
    private final String category;

    public ElectronicDevice(String id, String name, String category) {
        super(id, name);
        this.category = Objects.requireNonNull(category, "category");
    }

    public String getCategory() { return category; }

    @Override
    public String toString() {
        return "ElectronicDevice [id=" + getId() + ", name=" + getName() + ", category=" + category + "]";
    }
}
