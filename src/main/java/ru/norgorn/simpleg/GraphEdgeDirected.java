package ru.norgorn.simpleg;

public class GraphEdgeDirected<T extends Comparable> extends GraphEdge<T> {

    public GraphEdgeDirected(GraphVertex<T> from, GraphVertex<T> to, int weight) {
        super(from, to, weight);
    }
}
