package LeetCodeJava.Array;

// https://leetcode.com/problems/duplicate-zeros/description/
/**
 *  1089. Duplicate Zeros
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a fixed-length integer array arr, duplicate each occurrence of zero, shifting the remaining elements to the right.
 *
 * Note that elements beyond the length of the original array are not written. Do the above modifications to the input array in place and do not return anything.
 *
 *
 *
 * Example 1:
 *
 * Input: arr = [1,0,2,3,0,4,5,0]
 * Output: [1,0,0,2,3,0,0,4]
 * Explanation: After calling your function, the input array is modified to: [1,0,0,2,3,0,0,4]
 * Example 2:
 *
 * Input: arr = [1,2,3]
 * Output: [1,2,3]
 * Explanation: After calling your function, the input array is modified to: [1,2,3]
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 104
 * 0 <= arr[i] <= 9
 */
public class DuplicateZeros {

    // V0
//    public void duplicateZeros(int[] arr) {
//
//    }

    // V0-1
    // IDEA: 2 PASS (gemini)
    public void duplicateZeros_0_1(int[] arr) {
        int n = arr.length;
        int zeros = 0;

        // 1. First pass: Count how many zeros we can actually duplicate
        // within the bounds of the original array length.
        int length = n + countZeros(arr); // Theoretical length

        // 2. Second pass: Move elements from back to front
        // We use two pointers: 'i' for the original elements, 'j' for the new positions
        for (int i = n - 1, j = n + countZeros(arr) - 1; i >= 0; i--, j--) {
            if (arr[i] == 0) {
                // If it's a zero, we write it twice (if within bounds)
                if (j < n)
                    arr[j] = 0;
                j--; // Decrement j for the "duplicated" zero
                if (j < n)
                    arr[j] = 0;
            } else {
                // If it's not a zero, just copy it (if within bounds)
                if (j < n)
                    arr[j] = arr[i];
            }
        }
    }

    // Helper to count zeros
    private int countZeros(int[] arr) {
        int count = 0;
        for (int x : arr)
            if (x == 0)
                count++;
        return count;
    }

    // V0-2
    // IDEA: (gpt)
    public void duplicateZeros_0_2(int[] arr) {
        int n = arr.length;
        int zeros = 0;

        // Count zeros that can be duplicated
        for (int i = 0; i < n; i++) {
            if (arr[i] == 0) {
                zeros++;
            }
        }

        int i = n - 1;
        int j = n + zeros - 1;

        // Work backwards
        while (i >= 0) {
            if (j < n) {
                arr[j] = arr[i];
            }

            if (arr[i] == 0) {
                j--;
                if (j < n) {
                    arr[j] = 0;
                }
            }

            i--;
            j--;
        }
    }



    // V1
    // IDEA: TWO PASS
    // https://leetcode.com/problems/duplicate-zeros/editorial/
    public void duplicateZeros_1(int[] arr) {
        int possibleDups = 0;
        int length_ = arr.length - 1;

        // Find the number of zeros to be duplicated
        // Stopping when left points beyond the last element in the original array
        // which would be part of the modified array
        for (int left = 0; left <= length_ - possibleDups; left++) {

            // Count the zeros
            if (arr[left] == 0) {

                // Edge case: This zero can't be duplicated. We have no more space,
                // as left is pointing to the last element which could be included
                if (left == length_ - possibleDups) {
                    // For this zero we just copy it without duplication.
                    arr[length_] = 0;
                    length_ -= 1;
                    break;
                }
                possibleDups++;
            }
        }

        // Start backwards from the last element which would be part of new array.
        int last = length_ - possibleDups;

        // Copy zero twice, and non zero once.
        for (int i = last; i >= 0; i--) {
            if (arr[i] == 0) {
                arr[i + possibleDups] = 0;
                possibleDups--;
                arr[i + possibleDups] = 0;
            } else {
                arr[i + possibleDups] = arr[i];
            }
        }
    }


    // V2-1
    // https://leetcode.com/problems/duplicate-zeros/solutions/4094396/two-simple-java-solutions-by-ahmedna126-tclh/
    public void duplicateZeros_2_1(int[] arr) {
        int length = arr.length;
        int zeros = 0;

        for (int i = 0; i < length; i++) {
            if (arr[i] == 0) {
                zeros++;
            }
        }

        int lastIndex = length - 1;
        int newIndex = length - 1 + zeros;

        while (lastIndex >= 0) {
            if (newIndex < length) {
                arr[newIndex] = arr[lastIndex];
            }

            if (arr[lastIndex] == 0) {
                newIndex--;

                if (newIndex < length) {
                    arr[newIndex] = 0;
                }
            }

            lastIndex--;
            newIndex--;
        }
    }

    // V2-2
    // https://leetcode.com/problems/duplicate-zeros/solutions/4094396/two-simple-java-solutions-by-ahmedna126-tclh/
    public void duplicateZeros_2_2(int[] arr) {
        int length = arr.length;

        for (int i = 0; i < length; i++) {
            if (arr[i] == 0 && i + 1 < length) {
                for (int j = length - 1; j > i + 1; j--) {
                    arr[j] = arr[j - 1];
                }
                arr[i + 1] = 0;
                i++;
            }
        }
    }


    // V3


    
}
