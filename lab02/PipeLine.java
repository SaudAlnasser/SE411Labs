import java.util.Objects;

/** An immutable, reusable sequence from initial input T to current output R. */
public class PipeLine<T, R> {
    private final Transformer<T, R> steps;

    private PipeLine(Transformer<T, R> steps) {
        this.steps = steps;
    }

    public static <T> PipeLine<T, T> start() {
        return new PipeLine<>(input -> input);
    }

    // Composition stores the ordered sequence while preserving type safety.
    public <U> PipeLine<T, U> add(Transformer<? super R, ? extends U> next) {
        Objects.requireNonNull(next, "Transformer must not be null");
        return new PipeLine<>(input -> next.transform(steps.transform(input)));
    }

    public R execute(T input) {
        return steps.transform(input);
    }
}
