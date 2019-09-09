package ru.norgorn.simpleg.util;


import org.apache.commons.lang3.tuple.Pair;
import ru.norgorn.simpleg.GraphEdge;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

public class EdgesBag<T extends Comparable> {

    private final PriorityQueue<Pair<Integer, GraphEdge<T>>> edges;

    public EdgesBag() {
        edges = new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));
    }

    public void add(int priority, GraphEdge<T> edge) {
        edges.add(Pair.of(priority, edge));
    }

    public Optional<Pair<Integer, GraphEdge<T>>> poll() {
        return Optional.ofNullable(edges.poll());
    }
}
