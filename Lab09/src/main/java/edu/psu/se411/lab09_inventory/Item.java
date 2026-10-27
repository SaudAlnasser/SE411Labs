package edu.psu.se411.lab09_inventory;

import java.util.Objects;

public abstract class Item {
    private final String id;
    private final String name;

    protected Item(String id, String name) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getId() { return id; }
    public String getName() { return name; }
}
