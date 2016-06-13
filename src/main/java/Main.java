/**
 * Created by ivanchic on 6/12/2016.
 */
public class Main {
    public static void main(String[] args) {
        int[][] graph = DijkstraAlgorithm.createGraph();
        int[][] res = DijkstraAlgorithm.dijkstra(graph, 0);
    }
}
