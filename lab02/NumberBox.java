import java.util.List;

public class NumberBox<T extends Number> {
    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    // A double result supports mixed numeric types without an unsafe cast to T.
    public double add(Number other) {
        return item.doubleValue() + other.doubleValue();
    }

    public static double sumNumbers(List<? extends Number> numbers) {
        double sum = 0;
        for (Number number : numbers) {
            sum += number.doubleValue();
        }
        return sum;
    }
}
