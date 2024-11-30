package org.qlik.deadlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DeadlockersolvedWithSemaphore {
    private static final List<String> resource1 = new ArrayList<>();
    private static final List<String> resource2 = new ArrayList();

    private static final Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("Thread 1: Trying to acquire semaphore");
                semaphore.acquire();
                System.out.println("Thread 1: Acquired semaphore");

                synchronized (resource1) {
                    System.out.println("Thread 1: Locked resource 1");
                    Thread.sleep(100);
                    synchronized (resource2) {
                        System.out.println("Thread 1: Locked resource 2");
                        Thread.sleep(100);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Thread 1: Released semaphore");
                semaphore.release();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("Thread 2: Trying to acquire semaphore");
                semaphore.acquire();
                System.out.println("Thread 2: Acquired semaphore");

                synchronized (resource2) {
                    System.out.println("Thread 2: Locked resource 2");
                    Thread.sleep(100);
                    synchronized (resource1) {
                        System.out.println("Thread 2: Locked resource 1");
                        Thread.sleep(100);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Thread 2: Released semaphore");
                semaphore.release(); // Release semaphore
            }
        });

        thread1.start();
        thread2.start();
    }
}
