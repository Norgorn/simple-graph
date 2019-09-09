package ru.norgorn.simpleg;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


@Value
@ToString(of = {"value"})
public class GraphVertex<T extends Comparable> implements Comparable<GraphVertex<T>> {

    public static <T extends Comparable> GraphVertex<T> vertex(T value) {
        return new GraphVertex<>(value);
    }

    @NonNull
    private final T value;

    private final Set<GraphEdge<T>> edges = ConcurrentHashMap.newKeySet();

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(GraphVertex<T> other) {
        return value.compareTo(other.getValue());
    }

    /** @noinspection unchecked */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        GraphVertex<?> that = (GraphVertex<?>) o;
        return value.compareTo(that.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public void addEdge(GraphEdge<T> newEdge) {
        edges.add(newEdge);
    }

    public Stream<GraphEdge<T>> edges() {
        return edges.stream();
    }
}
