package ru.norgorn.simpleg;


import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphMainTestUndirected extends GraphMainTestBase {

    @Override
    @Before
    public void setUp() {
        super.setUp();
        sut = new GraphMain<>(false);
    }

    @Test
    public void addEdge() {
        GraphVertex<Integer> v1 = sut.addVertex(1);
        GraphVertex<Integer> v2 = sut.addVertex(2);
        GraphEdgeUndirected<Integer> expectedEdge = new GraphEdgeUndirected<>(v1, v2, 1);

        GraphEdge<Integer> actualEdge = sut.addEdge(v1, v2);

        assertEquals(expectedEdge, actualEdge);
        assertEquals(ImmutableSet.of(expectedEdge), v1.getEdges());
        assertEquals(ImmutableSet.of(expectedEdge), v2.getEdges());
    }
}
