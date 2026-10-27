# Lab 03: JUnit Intro

JUnit 5 tests for `edu.psu.se411.model.Stack` are in
`src/test/java/edu/psu/se411/model/StackTest.java`.

## Run

With JDK 8 or newer and Maven installed, run from this folder:

```sh
mvn clean test
```

The Maven build includes the JUnit Jupiter engine and Surefire runner so the
tests execute, and fails if no tests are discovered.

## Required exercises

- `push_push_pop`: push `Z`, then `A`; popping returns `A`.
- `pop_empty_stack`: popping an empty stack throws `NoSuchElementException`.
  The expected message matches the supplied implementation:
  `Stack is empty, cannot pop` (the handout uses different wording).
- `elements_popped_in_reverse_order`: pushing `A`, `B`, `C` returns `C`, `B`, `A`.

The other tests cover single elements, nulls, empty strings, stack reuse,
underflow after a pop, constructor capacities, and interleaved operations.

## Validation

All 12 tests passed with no failures, errors, or skipped tests using JDK 17
and Maven 3.9.9.

For the deliberate failure exercise, temporarily changing the first test to
`assertEquals("Z", stringStack.pop())` produced the expected failure:
`expected: <Z> but was: <A>`. The assertion was restored to `A`, and a clean
run passed all 12 tests again. The submitted tests contain the correct assertion.
