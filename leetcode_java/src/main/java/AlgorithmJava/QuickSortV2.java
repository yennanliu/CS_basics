package AlgorithmJava;

import java.util.*;

/**
 *  Quick Sort implementation V2 (gpt)
 */
public class QuickSortV2 {
    public List<Integer> quickSort(Integer[] input) {
        // Edge case
        if (input == null || input.length == 0) {
            return new ArrayList<>();
        }

        // Base case for recursion
        if (input.length == 1) {
            return new ArrayList<>(Collections.singletonList(input[0]));
        }

        // Choosing the pivot
        int mid = input.length / 2;
        int pivot = input[mid];

        // Partition lists
        List<Integer> left = new ArrayList<>();
        List<Integer> equal = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for (int num : input) {
            if (num < pivot) {
                left.add(num);
            } else if (num == pivot) {
                equal.add(num);
            } else {
                right.add(num);
            }
        }

        // Recursively sort left and right partitions
        List<Integer> sortedLeft = quickSort(left.toArray(new Integer[0]));
        List<Integer> sortedRight = quickSort(right.toArray(new Integer[0]));

        // Combine sorted parts
        List<Integer> result = new ArrayList<>();
        result.addAll(sortedLeft);
        result.addAll(equal);
        result.addAll(sortedRight);

        return result;
    }

    public static void main(String[] args) {
        QuickSortV2 sorter = new QuickSortV2();


        // test 1
        Integer[] input = {3, 6, 8, 10, 1, 2, 1};
        System.out.println(sorter.quickSort(input));


        // test 2
        Integer[] input2 = {5,4,3,2,1,0,-1,-2};
        System.out.println(sorter.quickSort(input2));
    }
}