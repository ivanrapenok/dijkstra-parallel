/**
 * Created by dagon on 6/14/2016.
 */
public class State {
    private int localSection = 0;
    //public int threadsCount;

 /*   State(int threadsCount) {
        this.threadsCount = threadsCount;
    }
*/
    public synchronized void incrementLocalSection() {
        localSection++;
        notifyAll();
    }

/*    public synchronized void decrementThreadsCount() {
        //threadsCount--;
        notifyAll();
    }*/

    public void localSectionOn() {
        localSection = 0;
        notifyAll();
    }

    public synchronized void waitLocalSection() throws InterruptedException {
        while (localSection != 0)
            wait();
    }

    public synchronized void waitGlobalSection() throws InterruptedException {
        while (localSection < 4)
            wait();
    }

  /*  public synchronized int getLocalSection() {
        return localSection;
    }

    public synchronized void setLocalSection(int localSection) {
        this.localSection = localSection;
    }*/
}
