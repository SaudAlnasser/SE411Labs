package edu.psu.se411.lab09_inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inventory<T> {
    private final List<T> items = new ArrayList<>();

    public void addItem(T item) { items.add(Objects.requireNonNull(item, "item")); }
    public boolean removeItem(T item) { return items.remove(item); }
    public List<T> getAllItems() { return new ArrayList<>(items); }

    public List<T> findItems(SearchStrategy<? super T> strategy) {
        Objects.requireNonNull(strategy, "strategy");
        List<T> matches = new ArrayList<>();
        for (T item : items) {
            if (strategy.matches(item)) {
                matches.add(item);
            }
        }
        return matches;
    }
}
