/**
 * Created by ivanchic on 6/12/2016.
 */
public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        GraphGenerator generator = new GraphGenerator(18000, 10000, 40);
        int[][] graph = generator.generateGraph();
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(0, 2, graph);
        long finish = System.currentTimeMillis();
        System.out.println("Create graph: " + (finish - start) + "\n");

        start = System.currentTimeMillis();
        int[][] res = dijkstraAlgorithm.dijkstra();
        finish = System.currentTimeMillis();
        System.out.println("time for sequential algorithm: " + (finish - start) + "\n");

        for (int i = 1; i < 8; i++) {
            start = System.currentTimeMillis();
            res = dijkstraAlgorithm.dijkstraParallel(i);
            finish = System.currentTimeMillis();
            System.out.println("time for parallel algorithm " + i + "threads: " + (finish - start) + "\n");
        }
    }
}
