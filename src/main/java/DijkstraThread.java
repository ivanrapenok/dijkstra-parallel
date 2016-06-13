import static java.lang.Math.min;

/**
 * Created by dagon on 6/13/2016.
 */
public class DijkstraThread implements Runnable{
    private int first, last; // boundary of subgroup
    DijkstraAlgorithm dA;

    public DijkstraThread(int first, int last, DijkstraAlgorithm dijkstraAlgorithm) {
        this.first = first;
        this.last = last;
        this.dA = dijkstraAlgorithm;
    }

    public void run() {
        try {
            while (true) {
                int v = -1;
                for (int nv = first; nv < last; nv++) // iterate vertex
                    if (!dA.used[nv] && dA.dist[nv] < DijkstraAlgorithm.INF && (v == -1 || dA.dist[v] > dA.dist[nv])) // choose the closest and not marked vertex
                        v = nv;
                if (v == -1) break; // the closest vertex not found

                dA.newVertex[v] = v;
                dA.nDist[v] = dA.dist[v];
                dA.incrementLocalSection();
                dA.waitLocalSection();

                if (v == dA.newVertex[v]) {
                    dA.used[v] = true; // marked the closest vertex
                } else {
                    v = dA.newVertex[v];
                }
                for (int nv = first; nv < last; nv++)
                    if (!dA.used[nv] && dA.graph[v][nv] < DijkstraAlgorithm.INF) { // for all not marked vertex
                        dA.prev[nv] = v;
                        dA.dist[nv] = min(dA.dist[nv], dA.dist[v] + dA.graph[v][nv]); // improve distance
                    }
            }
        } catch(InterruptedException e) {
            System.out.println(e);
        }
        dA.decrementLocalSection();
    }
}
