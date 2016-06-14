import static java.lang.Math.min;

/**
 * Created by dagon on 6/13/2016.
 */
public class DijkstraThread implements Runnable{
    private int first, last, num, countUsed; // boundary of subgroup
    private DijkstraAlgorithm dA;
    private State state;

    public DijkstraThread(int num, int first, int last, DijkstraAlgorithm dijkstraAlgorithm, State state) {
        this.num = num;
        this.first = first;
        this.last = last;
        this.dA = dijkstraAlgorithm;
        this.countUsed = last - first;
        this.state = state;
    }

    public void run() {
        //System.out.println(num + " Start ");
        try {
            while (true) {
                int v = -1;
                for (int nv = first; nv < last; nv++) // iterate vertex
                    if (!dA.used[nv] && dA.dist[nv] < DijkstraAlgorithm.INF && (v == -1 || dA.dist[v] > dA.dist[nv])) // choose the closest and not marked vertex
                        v = nv;

                if (v == -1) {
                    if (countUsed == 0)
                        break; // the closest vertex not found
                    state.setNewVertex(num, v);
                    state.setnDist(num, DijkstraAlgorithm.INF);
                } else {
                    //System.out.println("NOWNOW " + num + " " + v);
                    state.setNewVertex(num, v);
                    state.setnDist(num, dA.dist[v]);
                }
                //System.out.println(num + " A " + num + "-" + state.getNewVertex(num) + " " + state.getnDist(num));
                state.localCompleted(num);
                state.waitForLocal(num);
                //System.out.println(num + " BBBL 0-" + state.getNewVertex(num) + " dist " + state.getnDist(num));

                //if (dA.newVertex[0] != -1) {
                    if (v == state.getNewVertex(num)) {
                        dA.used[v] = true; // marked the closest vertex
                        countUsed--;
                        //System.out.println(num + " v == new");
                    }
                    v = state.getNewVertex(num);

                //System.out.println("XXX " + v);
                    for (int nv = first; nv < last; nv++)
                        if (!dA.used[nv] && dA.graph[v][nv] < DijkstraAlgorithm.INF) { // for all not marked vertex
                            dA.prev[nv] = v;
                            dA.dist[nv] = min(dA.dist[nv], state.getnDist(num) + dA.graph[v][nv]); // improve distance
                        }

                //}
            }
            while(true) {
                state.setNewVertex(num, DijkstraAlgorithm.INF);
                state.setnDist(num, DijkstraAlgorithm.INF);
                state.localCompleted(num);
                state.waitForLocal(num);
            }
        } catch(InterruptedException e) {

        }

    }
}
