import static java.lang.Math.min;

/**
 * Created by dagon on 6/13/2016.
 */
public class DijkstraThread implements Runnable{
    private int first, last, num, countUsed; // boundary of subgroup
    private DijkstraAlgorithm dA;
    private Car car;

    public DijkstraThread(int num, int first, int last, DijkstraAlgorithm dijkstraAlgorithm, Car car) {
        this.num = num;
        this.first = first;
        this.last = last;
        this.dA = dijkstraAlgorithm;
        this.countUsed = last - first;
        this.car = car;
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
                    car.setNewVertex(num, v);
                    car.setnDist(num, DijkstraAlgorithm.INF);
                } else {
                    //System.out.println("NOWNOW " + num + " " + v);
                    car.setNewVertex(num, v);
                    car.setnDist(num, dA.dist[v]);
                }
                //System.out.println(num + " A " + num + "-" + car.getNewVertex(num) + " " + car.getnDist(num));
                car.localCompleted(num);
                car.waitForLocal(num);
                //System.out.println(num + " BBBL 0-" + car.getNewVertex(num) + " dist " + car.getnDist(num));

                //if (dA.newVertex[0] != -1) {
                    if (v == car.getNewVertex(num)) {
                        dA.used[v] = true; // marked the closest vertex
                        countUsed--;
                        //System.out.println(num + " v == new");
                    }
                    v = car.getNewVertex(num);

                //System.out.println("XXX " + v);
                    for (int nv = first; nv < last; nv++)
                        if (!dA.used[nv] && dA.graph[v][nv] < DijkstraAlgorithm.INF) { // for all not marked vertex
                            dA.prev[nv] = v;
                            dA.dist[nv] = min(dA.dist[nv], car.getnDist(num) + dA.graph[v][nv]); // improve distance
                        }

                //}
            }
            while(true) {
                car.setNewVertex(num, DijkstraAlgorithm.INF);
                car.setnDist(num, DijkstraAlgorithm.INF);
                car.localCompleted(num);
                car.waitForLocal(num);
            }
        } catch(InterruptedException e) {

        }

    }
}
