import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.min;
import static java.util.Arrays.fill;

/**
 * Dijkstra's algorithm is an algorithm for finding the shortest paths between nodes in a graph
 */
class DijkstraAlgorithm {

    public static int INF = Integer.MAX_VALUE / 2; // Infinity

    public int[][] graph;   // the analyzed graph
    public int start, threadsCount, vNum; // source vertex, count of threads, count of vertex

    public boolean[] used; // array of labels
    public volatile int[] dist; // dist[v] is shortest path between nodes start and v
    public int[] prev; // array of previous vertex
    //public volatile int[] nDist; // dist[v] is local shortest path between nodes start and v
    //public volatile int[] newVertex;

    public DijkstraAlgorithm(int start, int threadsCount) {
        this.graph = this.createGraph();
        this.start = start;
        this.threadsCount = threadsCount;
        vNum = this.graph.length;
        used = new boolean [vNum];
        dist = new int[vNum];
        prev = new int[vNum];
    }

    public DijkstraAlgorithm(int start, int threadsCount, int[][] graph) {
        this.graph = graph;
        //this.graph = this.createGraph();
        this.start = start;
        this.threadsCount = threadsCount;
        vNum = this.graph.length;
        used = new boolean [vNum];
        dist = new int[vNum];
        prev = new int[vNum];
    }

    int[][] dijkstraParallel() {
        State state = new State(threadsCount);

        fill(dist, INF);
        fill(prev, -1);
        fill(used, false);

        dist[start] = 0;

        //nDist = new int[threadsCount];
        //newVertex = new int[threadsCount];

        ExecutorService exec = Executors.newCachedThreadPool();
        int vertexInSubgroup = (vNum + (threadsCount - 1))/threadsCount;
        for (int i = 0; i < threadsCount; i++) {
            int first = i * vertexInSubgroup;
            int last = (first + vertexInSubgroup > vNum) ? vNum : first + vertexInSubgroup;
            //System.out.println(i + " f=" + first + " l=" + last);
            exec.execute(new DijkstraThread(i, first, last, this, state));
        }

        try {
            state.waitForGlobal();
            while(true) {
                //System.out.println("MAIN " + Arrays.toString(nDist));
                //System.out.println("MAIN " + Arrays.toString(newVertex));
                for (int i = threadsCount - 1; i > 0; i--) {
                    if (state.getnDist(i) < state.getnDist(i - 1)) {
                        state.setnDist(i - 1, state.getnDist(i));
                        state.setNewVertex(i - 1, state.getNewVertex(i));
                    }
                }
                for (int i = 0; i < threadsCount; i++) {
                        state.setnDist(i, state.getnDist(0));
                        state.setNewVertex(i, state.getNewVertex(0));
                }
                //System.out.println("MAIN " + Arrays.toString(nDist));
                //System.out.println("MAIN " + Arrays.toString(newVertex));
                //System.out.println("MAIN 0-" + state.getNewVertex(0) + " dist " + state.getnDist(0) + " " + Arrays.toString(used));
                if (state.getNewVertex(0) == INF) break;
                state.globalCompleted();
                state.waitForGlobal();
            }
        } catch (InterruptedException e) {

        }
        exec.shutdownNow();
        System.out.println(Arrays.toString(dist));
        System.out.println(Arrays.toString(prev));
        return null;
    }

    int[][] dijkstra() {
        fill(dist, INF);
        fill(prev, -1);
        fill(used, false);

        dist[start] = 0;

        while (true) {
            int v = -1;

            for (int nv = 0; nv < vNum; nv++) // iterate vertex
                if (!used[nv] && dist[nv] < INF && (v == -1 || dist[v] > dist[nv])) // choose the closest and not marked vertex
                    v = nv;
            //System.out.println(" run vertex " + v);
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

    int[][] createGraph() {
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
