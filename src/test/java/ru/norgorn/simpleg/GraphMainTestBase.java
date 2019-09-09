package ru.norgorn.simpleg;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("ConstantConditions")
public class GraphMainTestBase {

    protected GraphMain<Integer> sut;

    protected Map<Integer, GraphVertex<Integer>> vertices;

    @Before
    public void setUp() {
        vertices = new HashMap<>();
    }

    @Test
    public void addVertex_whenNew_thenAdd() {
        sut.addVertex(1);
    }

    @Test
    public void addVertex_whenExists_thenException() {
        sut.addVertex(1);
        Assertions.assertThatThrownBy(() -> sut.addVertex(1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessageContaining("Already contains vertex with value 1");
    }

    @Test
    public void findPath_wikiExample() {
        buildWikiGraph();
        List<GraphVertex<Integer>> expectedPath = asList(getVertex(1), getVertex(3), getVertex(6), getVertex(5));

        List<GraphVertex<Integer>> actualPath = sut.findPath(getVertex(1), getVertex(5)).get();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void findPath_whenCyclicAndOutsideOfCycle() {
        buildCyclicGraph();
        List<GraphVertex<Integer>> expectedPath = asList(getVertex(2), getVertex(3), getVertex(4));

        List<GraphVertex<Integer>> actualPath = sut.findPath(getVertex(2), getVertex(4)).get();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void findPath_whenCyclicAndInsideCycle() {
        buildCyclicGraph();
        List<GraphVertex<Integer>> expectedPath = asList(getVertex(1), getVertex(2), getVertex(3), getVertex(5));

        List<GraphVertex<Integer>> actualPath = sut.findPath(getVertex(1), getVertex(5)).get();

        assertEquals(expectedPath, actualPath);
    }

    protected void buildWikiGraph() {
        // First graph from https://en.wikipedia.org/wiki/Dijkstra's_algorithm
        GraphVertex<Integer> v1 = addVertex(1);
        GraphVertex<Integer> v2 = addVertex(2);
        GraphVertex<Integer> v3 = addVertex(3);
        GraphVertex<Integer> v4 = addVertex(4);
        GraphVertex<Integer> v5 = addVertex(5);
        GraphVertex<Integer> v6 = addVertex(6);

        sut.addEdge(v1, v2, 7);
        sut.addEdge(v1, v3, 9);
        sut.addEdge(v1, v6, 14);

        sut.addEdge(v2, v3, 10);
        sut.addEdge(v2, v4, 15);

        sut.addEdge(v3, v4, 11);
        sut.addEdge(v3, v6, 2);

        sut.addEdge(v4, v5, 6);

        sut.addEdge(v6, v5, 9);
    }

    protected void buildCyclicGraph() {
        /*
          1 --- 2 ---- 3 --- 4
                |       \
                |        \
                6---------5
         */
        GraphVertex<Integer> v1 = addVertex(1);
        GraphVertex<Integer> v2 = addVertex(2);
        GraphVertex<Integer> v3 = addVertex(3);
        GraphVertex<Integer> v4 = addVertex(4);
        GraphVertex<Integer> v5 = addVertex(5);
        GraphVertex<Integer> v6 = addVertex(6);

        sut.addEdge(v1, v2);
        sut.addEdge(v2, v3);
        sut.addEdge(v3, v4);
        sut.addEdge(v3, v5);
        sut.addEdge(v5, v6);
        sut.addEdge(v6, v2);
    }

    protected GraphVertex<Integer> getVertex(int i) {
        return checkNotNull(vertices.get(i), "Vertex %s", i);
    }

    protected GraphVertex<Integer> addVertex(int i) {
        GraphVertex<Integer> v = sut.addVertex(i);
        vertices.put(i, v);
        return v;
    }
}