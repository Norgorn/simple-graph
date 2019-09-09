package ru.norgorn.simpleg;

public class GraphEdgeUndirected<T extends Comparable> extends GraphEdge<T> {


    public GraphEdgeUndirected(GraphVertex<T> from, GraphVertex<T> to, int weight) {
        super(minor(from, to), major(from, to), weight);
    }

    private static <T extends Comparable> GraphVertex<T> minor(GraphVertex<T> from, GraphVertex<T> to) {
        int compare = from.compareTo(to);
        if (compare > 0) {
            return to;
        } else { // 'from' if ==0
            return from;
        }
    }

    private static <T extends Comparable> GraphVertex<T> major(GraphVertex<T> from, GraphVertex<T> to) {
        int compare = from.compareTo(to);
        if (compare > 0) {
            return from;
        } else { // 'to' if ==0
            return to;
        }
    }
}
