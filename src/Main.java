import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of philosophers: ");
        int n = scanner.nextInt();

        System.out.print("Enter maximal thinking time (ms): ");
        int thinkingTime = scanner.nextInt();

        System.out.print("Enter maximal eating time (ms): ");
        int eatingTime = scanner.nextInt();

        if (n < 2) {
            System.out.println("Need at least 2 philosophers.");
            scanner.close();
            return;
        }
        if (thinkingTime < 0 || eatingTime < 0) {
            System.out.println("Times must be non-negative.");
            scanner.close();
            return;
        }

        System.out.println("\nStarting dinner with " + n + " philosophers");
        System.out.println("Max thinking time: " + thinkingTime + " ms");
        System.out.println("Max eating time:   " + eatingTime + " ms\n");

        // Create forks: f0 is between p(n-1) and p0, fi is between p(i-1) and pi
        Fork[] forks = new Fork[n];
        for (int i = 0; i < n; i++) {
            forks[i] = new Fork(i);
        }

        // Create philosophers: pi takes fork[i] (left) and fork[(i+1) % n] (right)
        Philosopher[] philosophers = new Philosopher[n];
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            Fork left  = forks[i];
            Fork right = forks[(i + 1) % n];
            philosophers[i] = new Philosopher(i, left, right, thinkingTime, eatingTime);
            threads[i] = new Thread(philosophers[i], "Philosopher-" + i);
        }

        for (Thread t : threads) {
            t.start();
        }

        long startTime = System.currentTimeMillis();
        System.out.println("Press ENTER to stop the dinner...");
        scanner.nextLine(); // consume leftover newline after nextInt
        scanner.nextLine(); // wait for actual ENTER

        System.out.println("Shutting down...");
        for (Philosopher p : philosophers) {
            p.stop();
        }
        // wake up any philosophers that might be sleeping (thinking or eating) so they can check the running flag and exit
        for (Thread t : threads) {
            t.interrupt();
        }
        // wait for all philosopher threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long wallClockTime = System.currentTimeMillis() - startTime;
        long totalEatingTime = 0;
        for (Philosopher p : philosophers) {
            totalEatingTime += p.getTotalEatingTime();
        }

        scanner.close();
        System.out.println("All philosophers have left. Dinner is over.");
        System.out.println("\n--- Dinner Statistics ---");
        System.out.println("Wall-clock time:      " + wallClockTime + " ms");
        System.out.println("Total eating time:    " + totalEatingTime + " ms");
        System.out.println("Parallelism factor:   " + String.format("%.2f", (double) totalEatingTime / wallClockTime) + "x");
    }
}
