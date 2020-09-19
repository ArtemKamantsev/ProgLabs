package lab1;

import java.util.Scanner;

public class Dispatcher {
    private static final String namePrefix = "Thread-";

    public static void main(String[] args) throws InterruptedException {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        int limit = s.nextInt();

        Thread threads[] = new Thread[n];
        // part 1
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(new PrintNumberRunnable(
                    System.out,
                    (idx) -> idx < limit),
                    namePrefix + i
            );
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }
        System.out.println("First part finished successfully");

        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(new PrintNumberRunnable(
                    System.out,
                    (idx) -> !Thread.currentThread().isInterrupted()),
                    namePrefix + i
            );
            threads[i].start();
        }

        Thread.sleep(10);
        for (Thread t : threads) {
            System.out.printf("Interrupting %s\n", t.getName());
            t.interrupt();
            t.join();
        }
        System.out.println("Second part finished successfully");
    }
}