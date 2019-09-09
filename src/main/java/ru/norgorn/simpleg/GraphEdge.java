package ru.norgorn.simpleg;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
@ToString
public abstract class GraphEdge<T extends Comparable> {

    private final GraphVertex<T> from;
    private final GraphVertex<T> to;
    private final int weight;

    public GraphEdge(@NonNull GraphVertex<T> from, @NonNull GraphVertex<T> to, int weight) {
        this.from = from;
        this.to = to;
        checkArgument(weight >= 0, "Unsupported negative weight %s", weight);
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFrom(), getTo());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        GraphEdge otherEdge = (GraphEdge) o;
        return Objects.equal(getFrom(), otherEdge.getFrom())
                && Objects.equal(getTo(), otherEdge.getTo());
    }

    public GraphVertex<T> otherThan(GraphVertex<T> currentVertex) {
        return from.equals(currentVertex) ? to : from;
    }
}
