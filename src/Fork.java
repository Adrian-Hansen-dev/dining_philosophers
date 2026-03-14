import java.util.concurrent.locks.ReentrantLock;

public class Fork {
    private final int index;
    private final ReentrantLock lock = new ReentrantLock(true); // fair lock to prevent starvation

    public Fork(int index) {
        this.index = index;
    }

    public void take(int philosopherIndex) {
        lock.lock();
        System.out.println("Philosopher " + philosopherIndex + " took fork " + index);
    }

    public void putBack(int philosopherIndex) {
        lock.unlock();
        System.out.println("Philosopher " + philosopherIndex + " put back fork " + index);
    }
}
