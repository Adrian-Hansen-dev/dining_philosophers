import java.util.Random;

public class Philosopher implements Runnable {
    private final int index;
    private final Fork leftFork;
    private final Fork rightFork;
    private final int maxThinkingTime;
    private final int maxEatingTime;
    // volatile ensures the updated value is visible across threads immediately
    private volatile boolean running = true;
    private final Random random = new Random();

    public Philosopher(int index, Fork leftFork, Fork rightFork, int maxThinkingTime, int maxEatingTime) {
        this.index = index;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.maxThinkingTime = maxThinkingTime;
        this.maxEatingTime = maxEatingTime;
    }

    // called by main thread to signal cooperative shutdown
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        // only checks running at the top — if deadlocked mid-loop, this is never reached
        while (running) {
            think();
            leftFork.take(index);   // NAIVE: always takes left first — can cause deadlock
            rightFork.take(index);  // blocks here if right fork is held by neighbour
            eat();
            leftFork.putBack(index);
            rightFork.putBack(index);
        }
        System.out.println("Philosopher " + index + " has left the table.");
    }

    private void think() {
        try {
            // +1 so that maxThinkingTime itself is included in the range [0, maxThinkingTime]
            int t = random.nextInt(maxThinkingTime + 1);
            System.out.println("Philosopher " + index + " is thinking for " + t + " ms");
            Thread.sleep(t);
            System.out.println("Philosopher " + index + " finished thinking");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void eat() {
        try {
            int t = random.nextInt(maxEatingTime + 1);
            System.out.println("Philosopher " + index + " is eating for " + t + " ms");
            Thread.sleep(t);
            System.out.println("Philosopher " + index + " is done eating");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
