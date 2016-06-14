import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by dagon on 6/14/2016.
 */
public class State {
    private volatile int global = 0;
    private volatile int threadsCount;
    private volatile boolean[] localComplete;

    //private volatile int[] newVertex;
    //private volatile int[] nDist;

    private volatile AtomicIntegerArray newVertex;
    private volatile AtomicIntegerArray newDist;

    public synchronized void setNewVertex(int i, int val) {
        //this.newVertex[i] = val;
        newVertex.set(i, val);
    }

    public synchronized void setnDist(int i, int val) {
        //this.nDist[i] = val;
        newDist.set(i, val);
    }

    public synchronized int getNewVertex(int i) {
        //return newVertex[i];
        return newVertex.get(i);
    }

    public synchronized int getnDist(int i) {
        //return nDist[i];
        return newDist.get(i);
    }

    public State(int threadsCount) {
        this.threadsCount = threadsCount;
        localComplete = new boolean[threadsCount];

        newVertex = new AtomicIntegerArray(threadsCount);
        newDist = new AtomicIntegerArray(threadsCount);
        //newVertex = new int[threadsCount];
        //nDist = new int[threadsCount];
    }

    public synchronized int getGlobal() {
        return global;
    }

    public synchronized void localCompleted(int i) {
        global++;
        localComplete[i] = true;
        //System.out.println("Wax Off! " + Arrays.toString(localComplete));
        notifyAll();
    }
    public synchronized void globalCompleted() {
        //System.out.println("GLOBALCOMPLETED " + getNewVertex(0) + " dist " + getnDist(0));
        global = 0;
        for (int i = 0; i < threadsCount; i++) {
            localComplete[i] = false;
        }
        notifyAll();
    }
    public synchronized void waitForLocal(int i) throws InterruptedException {
        while(localComplete[i])
            wait();
    }

    public synchronized void waitForGlobal() throws InterruptedException {
        while(global < threadsCount)
            wait();
    }
}
