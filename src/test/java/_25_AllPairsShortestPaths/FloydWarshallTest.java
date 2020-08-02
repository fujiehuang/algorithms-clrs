package _25_AllPairsShortestPaths;

import _22_ElementaryGraphAlgorithms.Graph;
import org.junit.jupiter.api.Test;

import static _22_ElementaryGraphAlgorithms.Graph.Vertex;
import static _25_AllPairsShortestPaths.Util.VertexMatrix;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FloydWarshallTest
{
    @Test
    void returnCorrectOutput() {
        Graph<Integer> graph = new Graph<>(true);
        Integer[] V = {1, 2, 3, 4, 5};
        Integer[][] E = {
                {1, 2}, {1, 3}, {1, 5}, {2, 4}, {2, 5},
                {3, 2}, {4, 1}, {4, 3}, {5, 4}
        };
        Integer[] W = {3, 8, -4, 1, 7, 4, 2, -5, 6};

        for (Integer i : V)
            graph.addVertex(i);
        for (int i = 0; i < E.length; i++)
            graph.addEdge(E[i][0], E[i][1], W[i]);

        VertexMatrix<Integer, Integer> distances = FloydWarshall.distances(graph);
        Integer[][] expectedDistances = {
                {0, 1, -3, 2, -4},
                {3, 0, -4, 1, -1},
                {7, 4, 0, 5, 3},
                {2, -1, -5, 0, -2},
                {8, 5, 1, 6, 0}
        };

        for (Vertex<Integer> v : distances.getVertices()) {
            for (Vertex<Integer> u : distances.getVertices()) {
                assertEquals(expectedDistances[v.getKey() - 1][u.getKey() - 1], distances.get(v, u));
            }
        }

        VertexMatrix<Integer, Vertex<Integer>> predecessorsMatrix = FloydWarshall.predecessorsMatrix(graph);
        Integer[][] expectedPredecessors = {
                {null, 3, 4, 5, 1},
                {4, null, 4, 2, 1},
                {4, 3, null, 2, 1},
                {4, 3, 4, null, 1},
                {4, 3, 4, 5, null}
        };

        for (Vertex<Integer> v : predecessorsMatrix.getVertices()) {
            for (Vertex<Integer> u : predecessorsMatrix.getVertices()) {
                if (expectedPredecessors[v.getKey() - 1][u.getKey() - 1] == null) {
                    assertNull(predecessorsMatrix.get(v, u));
                } else {
                    assertEquals(expectedPredecessors[v.getKey() - 1][u.getKey() - 1], predecessorsMatrix.get(v, u).getKey());
                }
            }
        }
    }
}