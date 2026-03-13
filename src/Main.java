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
        System.out.println("Max eating time: " + eatingTime + " ms");

        // TODO: Create forks
        // TODO: Create and start philosopher threads
        // TODO: Wait for keyboard input to shutdown

        System.out.println("\nPress ENTER to stop the dinner...");
        scanner.nextLine(); // consume leftover newline
        scanner.nextLine(); // wait for actual ENTER

        System.out.println("Shutting down...");
        // TODO: Signal philosophers to stop and join threads

        scanner.close();
        System.out.println("Done.");
    }
}
