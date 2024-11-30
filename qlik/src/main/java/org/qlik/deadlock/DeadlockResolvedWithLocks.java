package org.qlik.deadlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockResolvedWithLocks {
    private static final List<String> resource1 = new ArrayList<>();
    private static final List<String> resource2 = new ArrayList<>();
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            boolean locksAcquired = false;
            while (!locksAcquired) {
                if (lock1.tryLock()) {
                    if (lock2.tryLock()) {
                        try {
                            System.out.println("Thread 1: Locked resource 1 and resource 2");
                            resource1.add("Thread 1 Data");
                            resource2.add("Thread 1 Data");
                            System.out.println("Thread 1: Modified resource 1 and resource 2");
                            break;
                        } finally {
                            lock2.unlock();
                            lock1.unlock();
                            System.out.println("Thread 1: Released locks on resource 1 and resource 2");
                        }
                    } else {
                        lock1.unlock();
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            boolean locksAcquired = false;
            while (!locksAcquired) {                // Try to acquire both locks
                if (lock2.tryLock()) {
                    if (lock1.tryLock()) {
                        try {
                            System.out.println("Thread 2: Locked resource 2 and resource 1");
                            // Work with both resources
                            resource2.add("Thread 2 Data");
                            resource1.add("Thread 2 Data");
                            System.out.println("Thread 2: Modified resource 2 and resource 1");
                            locksAcquired = true;
                        } finally {
                            lock1.unlock();
                            lock2.unlock();
                            System.out.println("Thread 2: Released locks on resource 2 and resource 1");
                        }
                    } else {
                        lock2.unlock();
                    }
                }
                // Avoid busy waiting
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            boolean locksAcquired = false;
            while (!locksAcquired) {                // Try to acquire both locks
                if (lock2.tryLock()) {

                    try {
                        System.out.println("Thread 3: Locked resource 2");
                        resource2.add("Thread 3 Data");
                        System.out.println("Thread 3: Modified resource 2");
                        locksAcquired = true;
                    } finally {
                        lock2.unlock();
                        System.out.println("Thread 3: Released locks on resource 2");
                    }
                } else {
                    lock2.unlock();
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread thread4 = new Thread(() -> {
            boolean locksAcquired = false;
            while (!locksAcquired) {
                if (lock1.tryLock()) {
                    try {
                        System.out.println("Thread 4: Locked resource 1");
                        resource1.add("Thread 4 Data");
                        System.out.println("Thread 4 Modified 1");
                        locksAcquired = true;
                    } finally {
                        lock1.unlock();
                        System.out.println("Thread 4: Released locks on resource 1");
                    }
                } else {
                    lock1.unlock();
                }
            }
            // Avoid busy waiting
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final state of resource 1: " + resource1);
        System.out.println("Final state of resource 2: " + resource2);
    }
}