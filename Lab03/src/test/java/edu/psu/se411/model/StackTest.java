package edu.psu.se411.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class StackTest {

    @Test
    public void push_push_pop() {
        Stack<String> stringStack = new Stack<>();

        stringStack.push("Z");
        stringStack.push("A");

        assertEquals("A", stringStack.pop());
    }

    @Test
    public void pop_empty_stack() {
        Stack<String> stringStack = new Stack<>();

        NoSuchElementException thrown =
            assertThrows(
                NoSuchElementException.class,
                () -> stringStack.pop(),
                "Expected pop from empty Stack to throw, but it didn't"
            );

        assertEquals("Stack is empty, cannot pop", thrown.getMessage());
    }

    @Test
    public void elements_popped_in_reverse_order() {
        Stack<String> stringStack = new Stack<>();

        stringStack.push("A");
        stringStack.push("B");
        stringStack.push("C");

        assertEquals("C", stringStack.pop());
        assertEquals("B", stringStack.pop());
        assertEquals("A", stringStack.pop());
    }

    @Test
    public void push_and_pop_single_element() {
        Stack<String> stack = new Stack<>();

        stack.push("Hello");

        assertEquals("Hello", stack.pop());
    }

    @Test
    public void push_null_value() {
        Stack<String> stack = new Stack<>();

        stack.push(null);

        assertNull(stack.pop());
    }

    @Test
    public void push_empty_string() {
        Stack<String> stack = new Stack<>();

        stack.push("");

        assertEquals("", stack.pop());
    }

    @Test
    public void stack_can_be_reused_after_becoming_empty() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        assertEquals("A", stack.pop());

        stack.push("B");
        assertEquals("B", stack.pop());
    }

    @Test
    public void pop_twice_after_one_push_throws_exception() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        assertEquals("A", stack.pop());

        NoSuchElementException thrown =
            assertThrows(NoSuchElementException.class, () -> stack.pop());

        assertEquals("Stack is empty, cannot pop", thrown.getMessage());
    }

    @Test
    public void constructor_with_positive_capacity_works() {
        Stack<Integer> stack = new Stack<>(5);

        stack.push(10);
        stack.push(20);

        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
    }

    @Test
    public void constructor_with_zero_capacity_works() {
        Stack<Integer> stack = new Stack<>(0);

        stack.push(1);

        assertEquals(1, stack.pop());
    }

    @Test
    public void constructor_with_negative_capacity_works() {
        Stack<Integer> stack = new Stack<>(-5);

        stack.push(1);

        assertEquals(1, stack.pop());
    }

    @Test
    public void multiple_push_pop_operations_preserve_state() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        stack.push("B");

        assertEquals("B", stack.pop());

        stack.push("C");

        assertEquals("C", stack.pop());
        assertEquals("A", stack.pop());
    }
}