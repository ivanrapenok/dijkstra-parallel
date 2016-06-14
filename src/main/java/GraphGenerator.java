import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dagon on 6/14/2016.
 */
public class GraphGenerator {
    private int vertexCount;
    private int linkCount;
    private int maxWeight;

    private int[][] graph;

    public GraphGenerator(int vertexCount, int linkCount, int maxWeight) {
        this.vertexCount = vertexCount;
        this.linkCount = linkCount;
        this.maxWeight = maxWeight;
        graph = new int[vertexCount][vertexCount];
    }

    private void fill() {
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                graph[i][j] = DijkstraAlgorithm.INF;
                if (i == j) graph[i][j] = 0;
            }
        }
    }

    private int randomNumber(int boundary1, int boundary2) {
        Random randForRand = new Random(System.currentTimeMillis() + 17);
        Random rand = new Random(System.currentTimeMillis() + randForRand.nextLong());
        if (boundary1 < boundary2)
            return rand.nextInt(boundary2 - boundary1) + boundary1;
        else
            return rand.nextInt(boundary1 - boundary2) + boundary2;
    }

    private void connectAllVertices() {
        ArrayList<Integer> relatedVertices = new ArrayList<Integer>();
        ArrayList<Integer> unrelatedVertices = new ArrayList<Integer>();

        for (int i = 0; i < vertexCount; i++) {
            unrelatedVertices.add(i);
        }

        Integer a = unrelatedVertices.get(randomNumber(0, unrelatedVertices.size() - 1));
        unrelatedVertices.remove(a);
        Integer b = unrelatedVertices.get(randomNumber(0, unrelatedVertices.size() - 1));
        unrelatedVertices.remove(b);
        relatedVertices.add(a);
        relatedVertices.add(b);
        int weight = randomNumber(1, maxWeight);
        graph[a][b] = weight;
        graph[b][a] = weight;

        for (Integer vertex : unrelatedVertices) {
            b = relatedVertices.get(randomNumber(0, relatedVertices.size() - 1));
            //Random r = new Random(System.currentTimeMillis());
            //weight = r.nextInt(maxWeight) + 1;
            weight = randomNumber(1, maxWeight);
            graph[vertex][b] = weight;
            graph[b][vertex] = weight;
            relatedVertices.add(b);
        }
        unrelatedVertices.clear();
    }

    private void createAdditionLinks() {
        for (int i = 0; i < vertexCount; i++) {
            for (int j = randomNumber(linkCount/6, linkCount/5); j < randomNumber(linkCount - linkCount/6, linkCount); j++) {
                int randVertex = randomNumber(0, vertexCount - 2);
                randVertex = (randVertex != i) ? randVertex : ((i < vertexCount - 2) ? randVertex + 1 : randVertex - 1);
                int weight = randomNumber(1, maxWeight);
                graph[i][randVertex] = weight;
                graph[randVertex][i] = weight;
            }
        }
    }

    public int[][] generateGraph() {
        fill();
        connectAllVertices();
        createAdditionLinks();
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                System.out.print(graph[i][j] + "  ");
            }
            System.out.println();
        }
        return graph;
    }
}
