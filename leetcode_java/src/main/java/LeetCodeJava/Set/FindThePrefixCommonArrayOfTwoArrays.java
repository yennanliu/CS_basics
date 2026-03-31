package LeetCodeJava.Set;

// https://leetcode.com/problems/find-the-prefix-common-array-of-two-arrays/description/

import java.util.HashSet;
import java.util.Set;

/**
 *  2657. Find the Prefix Common Array of Two Arrays
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two 0-indexed integer permutations A and B of length n.
 *
 * A prefix common array of A and B is an array C such that C[i] is equal to the count of numbers that are present at or before the index i in both A and B.
 *
 * Return the prefix common array of A and B.
 *
 * A sequence of n integers is called a permutation if it contains all integers from 1 to n exactly once.
 *
 *
 *
 * Example 1:
 *
 * Input: A = [1,3,2,4], B = [3,1,2,4]
 * Output: [0,2,3,4]
 * Explanation: At i = 0: no number is common, so C[0] = 0.
 * At i = 1: 1 and 3 are common in A and B, so C[1] = 2.
 * At i = 2: 1, 2, and 3 are common in A and B, so C[2] = 3.
 * At i = 3: 1, 2, 3, and 4 are common in A and B, so C[3] = 4.
 * Example 2:
 *
 * Input: A = [2,3,1], B = [3,1,2]
 * Output: [0,1,3]
 * Explanation: At i = 0: no number is common, so C[0] = 0.
 * At i = 1: only 3 is common in A and B, so C[1] = 1.
 * At i = 2: 1, 2, and 3 are common in A and B, so C[2] = 3.
 *
 *
 * Constraints:
 *
 * 1 <= A.length == B.length == n <= 50
 * 1 <= A[i], B[i] <= n
 * It is guaranteed that A and B are both a permutation of n integers.
 *
 */
public class FindThePrefixCommonArrayOfTwoArrays {

    // V0
//    public int[] findThePrefixCommonArray(int[] A, int[] B) {
//
//    }

    // V0-1
    // IDEA: GREEDY (?) (gemini)
    public int[] findThePrefixCommonArray_0_1(int[] A, int[] B) {
        int n = A.length;
        int[] res = new int[n];

        // Since values are 1 to n, we track how many times each number appears
        int[] frequency = new int[n + 1];
        int commonCount = 0;

        for (int i = 0; i < n; i++) {
            // Process element from A
            frequency[A[i]]++;
            if (frequency[A[i]] == 2) {
                commonCount++;
            }

            // Process element from B
            frequency[B[i]]++;
            if (frequency[B[i]] == 2) {
                commonCount++;
            }

            // The current prefix common count
            res[i] = commonCount;
        }

        return res;
    }


    // V0-2
    // IDEA: GREEDY (?) (GPT)
    public int[] findThePrefixCommonArray_0_2(int[] A, int[] B) {
        int n = A.length;
        int[] res = new int[n];

        int[] count = new int[n + 1]; // since values are 1..n
        int common = 0;

        for (int i = 0; i < n; i++) {
            // process A[i]
            count[A[i]]++;
            if (count[A[i]] == 2) {
                common++;
            }

            // process B[i]
            count[B[i]]++;
            if (count[B[i]] == 2) {
                common++;
            }

            res[i] = common;
        }

        return res;
    }



    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/find-the-prefix-common-array-of-two-arrays/editorial/
    public int[] findThePrefixCommonArray_1_1(int[] A, int[] B) {
        int n = A.length;
        int[] prefixCommonArray = new int[n];

        // Loop through each index to calculate common elements for each prefix
        for (int currentIndex = 0; currentIndex < n; ++currentIndex) {
            int commonCount = 0;

            // Compare elements in A and B within the range of current prefix
            for (int aIndex = 0; aIndex <= currentIndex; ++aIndex) {
                for (int bIndex = 0; bIndex <= currentIndex; ++bIndex) {
                    // Check if elements match, and count if they do
                    if (A[aIndex] == B[bIndex]) {
                        ++commonCount;
                        break; // Prevent counting duplicates
                    }
                }
            }

            // Store the count of common elements for the current prefix
            prefixCommonArray[currentIndex] = commonCount;
        }

        // Return the final array with counts of common elements in each prefix
        return prefixCommonArray;
    }



    // V1-2
    // IDEA: HASHSET
    // https://leetcode.com/problems/find-the-prefix-common-array-of-two-arrays/editorial/
    public int[] findThePrefixCommonArray_1_2(int[] A, int[] B) {
        int n = A.length;
        int[] prefixCommonArray = new int[n];

        // Initialize sets to store elements from A and B
        Set<Integer> elementsInA = new HashSet<Integer>();
        Set<Integer> elementsInB = new HashSet<Integer>();

        // Iterate through the elements of both arrays
        for (int currentIndex = 0; currentIndex < n; ++currentIndex) {
            // Add current elements from A and B to respective sets
            elementsInA.add(A[currentIndex]);
            elementsInB.add(B[currentIndex]);

            int commonCount = 0;

            // Count common elements between the sets
            for (int element : elementsInA) {
                if (elementsInB.contains(element)) {
                    ++commonCount;
                }
            }

            // Store the count of common elements for the current prefix
            prefixCommonArray[currentIndex] = commonCount;
        }

        // Return the final array with counts of common elements in each prefix
        return prefixCommonArray;
    }



    // V1-3
    // IDEA: Single Pass with Frequency Array
    // https://leetcode.com/problems/find-the-prefix-common-array-of-two-arrays/editorial/
    public int[] findThePrefixCommonArray_1_3(int[] A, int[] B) {
        int n = A.length;
        int[] prefixCommonArray = new int[n];
        int[] frequency = new int[n + 1];
        int commonCount = 0;

        // Iterate through the elements of both arrays
        for (int currentIndex = 0; currentIndex < n; ++currentIndex) {
            // Increment frequency of current elements in A and B
            // Check if the element in A has appeared before (common in prefix)
            frequency[A[currentIndex]] += 1;
            if (frequency[A[currentIndex]] == 2)
                ++commonCount;

            // Check if the element in B has appeared before (common in prefix)
            frequency[B[currentIndex]] += 1;
            if (frequency[B[currentIndex]] == 2)
                ++commonCount;

            // Store the count of common elements for the current prefix
            prefixCommonArray[currentIndex] = commonCount;
        }

        // Return the final array with counts of common elements in each prefix
        return prefixCommonArray;
    }




    // V2



    // V3




}
