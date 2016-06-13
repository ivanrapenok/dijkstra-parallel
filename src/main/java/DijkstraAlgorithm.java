import java.util.Arrays;

import static java.lang.Math.min;
import static java.util.Arrays.fill;

/**
 * Dijkstra's algorithm is an algorithm for finding the shortest paths between nodes in a graph
 */
class DijkstraAlgorithm {

    private static int INF = Integer.MAX_VALUE / 2; // Infinity

    static int[][] dijkstra(int[][] graph, int start) {
        int vNum = graph.length;
        boolean[] used = new boolean [vNum]; // array of labels
        int[] dist = new int[vNum]; // dist[v] is shortest path between nodes start and v
        int[] prev = new int[vNum]; // array of previous vertex

        fill(dist, INF);
        fill(prev, -1);

        dist[start] = 0;

        while (true) {
            int v = -1;

            for (int nv = 0; nv < vNum; nv++) // iterate vertex
                if (!used[nv] && dist[nv] < INF && (v == -1 || dist[v] > dist[nv])) // choose the closest and not marked vertex
                    v = nv;
            if (v == -1) break; // the closest vertex not found
            used[v] = true; // marked the closest vertex
            for (int nv = 0; nv < vNum; nv++)
                if (!used[nv] && graph[v][nv] < INF) { // for all not marked vertex
                    prev[nv] = v;
                    dist[nv] = min(dist[nv], dist[v] + graph[v][nv]); // improve distance
                }
        }

        System.out.println(Arrays.toString(dist));
        System.out.println(Arrays.toString(prev));
        return null;
    }

    static int[][] createGraph() {
        return new int[][]{
                {0, 1, 4, INF, INF, INF, INF, INF},
                {1, 0, 2, INF, INF, INF, 4, 2},
                {4, 2, 0, 1, 3, INF, INF, INF},
                {INF, INF, 1, 0, 1, 3, 1, INF},
                {INF, INF, 3, 1, 0, 1, INF, INF},
                {INF, INF, INF, 3, 1, 0, 6, INF},
                {INF, 4, INF, 1, INF, 6, 0, 14},
                {INF, 2, INF, INF, INF, INF, 14, 0}
        };
    }
}
