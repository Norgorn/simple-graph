package ru.norgorn.simpleg;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
public class GraphMain<T extends Comparable> {

    private final Set<GraphEdge<T>> edges = ConcurrentHashMap.newKeySet();
    private final Set<GraphVertex<T>> vertices = ConcurrentHashMap.newKeySet();

    private final boolean isDirected;

    public GraphVertex<T> addVertex(T value) {
        GraphVertex<T> vertex = new GraphVertex<>(value);
        checkArgument(vertices.add(vertex), "Already contains vertex with value %s", value);
        return vertex;
    }

    public GraphEdge<T> addEdge(GraphVertex<T> from, GraphVertex<T> to) {
        return addEdge(from, to, 1);
    }

    public GraphEdge<T> addEdge(GraphVertex<T> from, GraphVertex<T> to, int weight) {
        GraphEdge<T> newEdge = isDirected ? new GraphEdgeDirected<>(from, to, weight)
                : new GraphEdgeUndirected<>(from, to, weight);

        checkArgument(edges.add(newEdge), "Already contains edge between %s AND %s", from, to);
        from.addEdge(newEdge);
        if (!isDirected) {
            to.addEdge(newEdge);
        }
        return newEdge;
    }

    public Optional<List<GraphVertex<T>>> findPath(GraphVertex<T> from, GraphVertex<T> to) {

        Map<GraphVertex<T>, Integer> unvisited = vertices.stream()
                .collect(Collectors.toMap(v -> v, v -> Integer.MAX_VALUE));
        NavigableMap<Integer, Set<GraphVertex<T>>> unvisitedWeights = new TreeMap<>();
        Map<GraphVertex<T>, GraphVertex<T>> parents = new HashMap<>();
        unvisited.put(from, 0);
        updateVertexWeight(unvisitedWeights, from, 0, 0);

        GraphVertex<T> currentVertex;
        int currentWeight;
        while (!unvisitedWeights.isEmpty()) {
            currentWeight = unvisitedWeights.firstKey();
            Set<GraphVertex<T>> row = unvisitedWeights.get(currentWeight);
            currentVertex = row.iterator().next();
            unvisited.remove(currentVertex);
            row.remove(currentVertex);
            if (row.isEmpty()) {
                unvisitedWeights.remove(currentWeight);
            }

            updateWeights(unvisited, unvisitedWeights, parents, currentVertex, currentWeight);
            if (currentVertex.equals(to)) {
                return Optional.of(constructPath(parents, to));
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("ConstantConditions")
    private void updateWeights(Map<GraphVertex<T>, Integer> unvisited,
                               NavigableMap<Integer, Set<GraphVertex<T>>> unvisitedWeights,
                               Map<GraphVertex<T>, GraphVertex<T>> parents,
                               GraphVertex<T> currentVertex, int currentWeight) {
        currentVertex.edges()
                .forEach(edge -> {
                    GraphVertex<T> vertex = edge.otherThan(currentVertex);
                    int newWeight = currentWeight + edge.getWeight();
                    unvisited.computeIfPresent(vertex, (k, weight) -> {
                        if (weight > newWeight) {
                            parents.put(vertex, currentVertex);
                            updateVertexWeight(unvisitedWeights, vertex, newWeight, weight);
                            return newWeight;
                        } else {
                            return weight;
                        }
                    });
                });
    }

    private List<GraphVertex<T>> constructPath(Map<GraphVertex<T>, GraphVertex<T>> parents, GraphVertex<T> source) {
        List<GraphVertex<T>> path = new ArrayList<>();
        path.add(source);
        GraphVertex<T> prev = source;
        GraphVertex<T> next;
        while ((next = parents.get(prev)) != null) {
            path.add(next);
            prev = next;
        }
        return Lists.reverse(path);
    }

    private void updateVertexWeight(NavigableMap<Integer, Set<GraphVertex<T>>> unvisitedWeights, GraphVertex<T> vertex,
                                    int newWeight, Integer weight) {
        unvisitedWeights.computeIfPresent(weight, (k, row) -> {
            row.remove(vertex);
            if (row.isEmpty()) {
                return null;
            }
            return row;
        });
        unvisitedWeights.compute(newWeight, (k1, row) -> {
            if (row == null) {
                row = new HashSet<>();
            }
            row.add(vertex);
            return row;
        });
    }
}
