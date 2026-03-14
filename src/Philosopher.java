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
    // only written by this philosopher's own thread,  no synchronisation needed
    private long totalEatingTime = 0;

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

 // NAIVE implementation that can lead to deadlock if all philosophers pick up their left fork and wait for the right one
//    @Override
//    public void run() {
//        while (running) {
//            think();
//            leftFork.take(index);
//            rightFork.take(index);
//            eat();
//            leftFork.putBack(index);
//            rightFork.putBack(index);
//        }
//        System.out.println("Philosopher " + index + " has left the table.");
//    }

    @Override
    public void run() {
        try {
            while (running) {
                think();
                Fork first = (index % 2 != 0) ? leftFork : rightFork;
                Fork second = (index % 2 != 0) ? rightFork : leftFork;

                first.take(index);
                try {
                    second.take(index);
                    try {
                        eat();
                    } finally {
                        second.putBack(index);
                    }
                } finally {
                    first.putBack(index);
                }
            }
        } catch (Exception e) {
            System.out.println("Philosopher " + index + " encountered an error: " + e.getMessage());
        } finally {
            System.out.println("Philosopher " + index + " cleanup and leaving table.");
        }
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
            totalEatingTime += t;
            System.out.println("Philosopher " + index + " is done eating");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public long getTotalEatingTime() {
        return totalEatingTime;
    }
}
