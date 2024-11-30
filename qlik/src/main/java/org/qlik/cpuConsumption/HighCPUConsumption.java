package org.qlik.cpuConsumption;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class HighCPUConsumption {

    public static void main(String[] args) {

        Thread cpuIntensiveTask = new Thread(() -> {
            while (true) { //keeps the cpu in a busy wait state
            }
        });

        Thread anotherTask = new Thread(() -> {
            while (true) { //adds a heavy load computation to spike cpu usage
                Math.pow(2, Math.random() * 1000);
            }
        });

        cpuIntensiveTask.start();
        anotherTask.start();

        int size = 10000; // Reduce size for testing; increase for higher CPU usage

        // Step 1: Create a 3D ArrayList
        List<List<List<Integer>>> threeDList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            List<List<Integer>> twoDList = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                List<Integer> oneDList = new ArrayList<>(size);
                for (int k = 0; k < size; k++) {
                    oneDList.add((int) (Math.random() * 100)); // Populate with random integers
                }
                twoDList.add(oneDList);
            }
            threeDList.add(twoDList);
        }

        System.out.println("3D ArrayList created. Starting inefficient sorting...");

        // Step 2: Inefficiently sort each 1D array
        for (List<List<Integer>> twoDList : threeDList) {
            for (List<Integer> oneDList : twoDList) {
                bubbleSort(oneDList); // Use Bubble Sort on the 1D list
            }
        }

        // Step 3: Inefficiently sort each 2D array based on the sum of their 1D arrays
        for (List<List<Integer>> twoDList : threeDList) {
            inefficient2DSort(twoDList);
        }

        // Step 4: Inefficiently sort the 3D array based on the cumulative sum of their 2D arrays
        inefficient3DSort(threeDList);

        System.out.println("Inefficient sorting complete.");
    }

    // Bubble Sort for 1D list
    private static void bubbleSort(List<Integer> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    // Swap
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    // Inefficient sort for 2D array based on the sum of their 1D arrays
    private static void inefficient2DSort(List<List<Integer>> twoDList) {
        int n = twoDList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sumOf1DList(twoDList.get(j)) > sumOf1DList(twoDList.get(j + 1))) {
                    // Swap
                    List<Integer> temp = twoDList.get(j);
                    twoDList.set(j, twoDList.get(j + 1));
                    twoDList.set(j + 1, temp);
                }
            }
        }
    }

    // Inefficient sort for 3D array based on the cumulative sum of their 2D arrays
    private static void inefficient3DSort(List<List<List<Integer>>> threeDList) {
        int n = threeDList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sumOf2DList(threeDList.get(j)) > sumOf2DList(threeDList.get(j + 1))) {
                    // Swap
                    List<List<Integer>> temp = threeDList.get(j);
                    threeDList.set(j, threeDList.get(j + 1));
                    threeDList.set(j + 1, temp);
                }
            }
        }
    }

    // Helper method to compute the sum of a 1D list
    private static int sumOf1DList(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    // Helper method to compute the sum of all 1D arrays in a 2D list
    private static int sumOf2DList(List<List<Integer>> list) {
        return list.stream().mapToInt(HighCPUConsumption::sumOf1DList).sum();
    }
}


