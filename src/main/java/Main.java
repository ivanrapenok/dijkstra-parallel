/**
 * Created by ivanchic on 6/12/2016.
 */
public class Main {
    public static void main(String[] args) {
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(0, 3);

        long start = System.currentTimeMillis();
        int[][] res = dijkstraAlgorithm.dijkstra();
        long finish = System.currentTimeMillis();
        System.out.println("time for sequential algorithm: " + (finish - start) + "\n");

        start = System.currentTimeMillis();
        res = dijkstraAlgorithm.dijkstraParallel();
        finish = System.currentTimeMillis();
        System.out.println("time for parallel algorithm: " + (finish - start) + "\n");

    }
}
