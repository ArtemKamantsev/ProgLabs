package lab2;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Dispatcher {
    private static final int SIZE = 10;
    private static final int WORKERS_NUMBER = 2;
    public static int[] data = new int[SIZE];
    public static int iterationShift = 0;

    static {
        Random r = new Random();
        for (int i = 0; i < SIZE; i++) {
            data[i] = r.nextInt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.printf("Generated array: %s\n", Arrays.toString(data));
        Thread[] workers = new Thread[WORKERS_NUMBER];
        CyclicBarrier barrier = new CyclicBarrier(WORKERS_NUMBER, () -> {
            if (isSorted(data)) {
                for (Thread t : workers) {
                    t.interrupt();
                }
            } else {
                if (iterationShift == 0) {
                    iterationShift = 1;
                } else {
                    iterationShift = 0;
                }
            }
        });
        for (int i = 0; i < workers.length; i++) {
            final int id = i;
            workers[i] = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    for (int j = iterationShift + id * 2; j < data.length - 1; j += WORKERS_NUMBER * 2) {
                        if (data[j] > data[j + 1]) {
                            data[j] = data[j] ^ data[j + 1];
                            data[j + 1] = data[j] ^ data[j + 1];
                            data[j] = data[j] ^ data[j + 1];
                        }
                    }

                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
            workers[i].start();
        }

        for (Thread t : workers) {
            t.join();
        }

        System.out.printf("Sorted array: %s\n", Arrays.toString(data));
    }

    static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }

        return true;
    }
}
