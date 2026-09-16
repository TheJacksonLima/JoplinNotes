package org.jfl.Day05_EqualsAndHashCode;

import java.util.Objects;

public class Pair {
    private final String left;
    private final String right;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    public Pair(String left, String right) {
        this.left = left;
        this.right = right;
    }
}
