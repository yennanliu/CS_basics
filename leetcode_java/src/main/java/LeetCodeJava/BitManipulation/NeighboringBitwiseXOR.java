package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/neighboring-bitwise-xor/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 2683. Neighboring Bitwise XOR
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * A 0-indexed array derived with length n is derived by computing the bitwise XOR (⊕) of adjacent values in a binary array original of length n.
 *
 * Specifically, for each index i in the range [0, n - 1]:
 *
 * If i = n - 1, then derived[i] = original[i] ⊕ original[0].
 * Otherwise, derived[i] = original[i] ⊕ original[i + 1].
 * Given an array derived, your task is to determine whether there exists a valid binary array original that could have formed derived.
 *
 * Return true if such an array exists or false otherwise.
 *
 * A binary array is an array containing only 0's and 1's
 *
 *
 * Example 1:
 *
 * Input: derived = [1,1,0]
 * Output: true
 * Explanation: A valid original array that gives derived is [0,1,0].
 * derived[0] = original[0] ⊕ original[1] = 0 ⊕ 1 = 1
 * derived[1] = original[1] ⊕ original[2] = 1 ⊕ 0 = 1
 * derived[2] = original[2] ⊕ original[0] = 0 ⊕ 0 = 0
 * Example 2:
 *
 * Input: derived = [1,1]
 * Output: true
 * Explanation: A valid original array that gives derived is [0,1].
 * derived[0] = original[0] ⊕ original[1] = 1
 * derived[1] = original[1] ⊕ original[0] = 1
 * Example 3:
 *
 * Input: derived = [1,0]
 * Output: false
 * Explanation: There is no valid original array that gives derived.
 *
 *
 * Constraints:
 *
 * n == derived.length
 * 1 <= n <= 105
 * The values in derived are either 0's or 1's
 *
 */
public class NeighboringBitwiseXOR {

    // V0
//    public boolean doesValidArrayExist(int[] derived) {
//
//    }

    // V0-0-1
    // IDEA: BIT OP + BRUTE FORCE (fixed by gemini)
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/63789835
    /**
     * Checks if a valid original array 'a' exists such that
     * derived[i] = a[i] XOR a[(i + 1) % n] is true for all i.
     * * NOTE: This code directly translates the logic structure seen in the screenshot.
     * The efficient solution only requires checking if the XOR sum of 'derived' is zero.
     * * @param derived The derived array.
     * @return true if a valid original array exists, false otherwise.
     */
    public boolean doesValidArrayExist_0_0_1(int[] derived) {

        if (derived == null || derived.length == 0) {
            // An empty derived array corresponds to an empty original array, which is valid.
            return true;
        }

        // The outer loop checks the two possible starting values for the first element, a[0]: 0 and 1.
        // i represents the assumed value of a[0]
        for (int i = 0; i < 2; i++) {

            // 'now' tracks the XOR sum, starting with the assumed a[0]
            int now = i;

            // Inner loop calculates the XOR sum of derived[0] through derived[derived.length - 2]
            // starting from the initial guess 'i'.
            // The loop runs for j from 0 up to (derived.length - 2).
            for (int j = 0; j + 1 < derived.length; j++) {
                // In a valid construction, a[j+1] = a[j] ^ derived[j].
                // This line accumulates the XOR sum: i ^ derived[0] ^ derived[1] ^ ...
                now = now ^ derived[j];
            }

            // After the inner loop, 'now' is equal to (a[0] ^ derived[0] ^ ... ^ derived[n-2]).
            // This entire term must equal a[n-1] for a valid non-circular chain.
            //
            // The check is for the circular condition: a[n-1] ^ a[0] = derived[n-1].
            // Substitute: (now ^ i) should equal derived[derived.length - 1].

            int lastDerivedElement = derived[derived.length - 1];

            // The expression (now ^ i) is effectively:
            // (a[0] ^ derived[0] ^ ... ^ derived[n-2]) ^ a[0]
            // which simplifies to: (derived[0] ^ ... ^ derived[n-2])

            if ((now ^ i) == lastDerivedElement) {
                // This is mathematically equivalent to checking if the XOR sum of ALL derived elements is zero.
                return true;
            }
        }

        // If the property doesn't hold for starting a[0]=0 nor a[0]=1, no valid array exists.
        return false;
    }

    // V0-1
    // IDEA: BIT OP (gpt)
    public boolean doesValidArrayExist_0_1(int[] derived) {
        int xor = 0;
        for (int val : derived) {
            xor ^= val;
        }
        return xor == 0;
    }

    // V0-2
    // IDEA: BIT OP (fixed by gemini)
    /**
     * Fix for LC 2683: Does Valid Array Exist?
     *
     * The array 'derived' is defined by derived[i] = original[i] ^ original[(i + 1) % n].
     * The total XOR sum of the derived array is:
     * (o[0]^o[1]) ^ (o[1]^o[2]) ^ ... ^ (o[n-1]^o[0])
     * Since every element in 'original' appears twice, the sum must be 0 (A ^ A = 0).
     * Therefore, a valid 'original' array exists IF AND ONLY IF the XOR sum of 'derived' is 0.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public boolean doesValidArrayExist_0_2(int[] derived) {
        // Edge case: If derived is null or empty, we assume a valid (empty) array exists.
        if (derived == null || derived.length == 0) {
            return true;
        }

        int xorSum = 0;

        /**
         * 🔹 Why This Works
         * 	 1.	Let original[0] = 0 (or 1, doesn’t matter).
         * 	 2 .Reconstruct other elements:
         *
         * 	    original[1] = original[0] ^ derived[0]
         *      original[2] = original[1] ^ derived[1]
         *      ...
         *      original[n-1] = original[n-2] ^ derived[n-2]
         *
         * 	 3.	The last element must satisfy original[n-1] ^ original[0] == derived[n-1].
         * 	 4.	This reduces to XOR of all derived = 0.
         *
         *   -> so, No need to mutate arrays, sort, or compare.
         */
        // Calculate the XOR sum of all elements in the derived array.
        for (int value : derived) {
            xorSum ^= value;
        }

        // The condition for existence is that the total XOR sum must be 0.
        return xorSum == 0;
    }

    // V0-3
    // IDEA: BIT OP (gemini)
    /**
     * Checks if a valid original array 'a' exists such that
     * derived[i] = a[i] XOR a[(i + 1) % n] is true for all i.
     * * @param derived The derived array.
     * @return true if a valid original array exists, false otherwise.
     */
    public boolean doesValidArrayExist_0_3(int[] derived) {
        // Core Principle: For a valid circular array reconstruction (a[i] ^ a[i+1] = derived[i]),
        // the XOR sum of all elements in the derived array must be zero.
        // This is because (a[0]^a[1]) ^ (a[1]^a[2]) ^ ... ^ (a[n-1]^a[0]) results in 0,
        // as every element a[i] appears twice.

        int xorSum = 0;

        // Calculate the XOR sum of all elements in the derived array
        for (int element : derived) {
            xorSum ^= element;
        }

        // If the total XOR sum is 0, a valid original array exists.
        // If the XOR sum is not 0, it's impossible to satisfy the circular XOR relationship.
        return xorSum == 0;
    }

    // V1-1
    // IDEA: Simulation
    // https://leetcode.com/problems/neighboring-bitwise-xor/editorial/
    public boolean doesValidArrayExist_1_1(int[] derived) {
        // Create an original array initialized with 0.
        int[] original = new int[derived.length + 1];
        original[0] = 0;
        for (int i = 0; i < derived.length; i++) {
            original[i + 1] = derived[i] ^ original[i];
        }
        // Store the validation results in checkForZero and checkForOne respectively.
        boolean checkForZero = (original[0] == original[original.length - 1]);

        original[0] = 1;
        for (int i = 0; i < derived.length; i++) {
            original[i + 1] = derived[i] ^ original[i];
        }
        boolean checkForOne = (original[0] == original[original.length - 1]);

        return checkForZero || checkForOne;
    }

    // V1-2
    // IDEA: Optimized Simulation
    // https://leetcode.com/problems/neighboring-bitwise-xor/editorial/
    public boolean doesValidArrayExist_1_2(int[] derived) {
        // Initialize the original array with 0 as the first element.
        List<Integer> original = new ArrayList<>();
        original.add(0);

        // Generate the original array based on derived and original[0] = 0.
        for (int i = 0; i < derived.length; i++) {
            original.add(derived[i] ^ original.get(i));
        }

        // Check if the array is valid by comparing the first and last elements.
        return original.get(0).equals(original.get(original.size() - 1));
    }

    // V1-3
    // IDEA: Cumulative XOR
    // https://leetcode.com/problems/neighboring-bitwise-xor/editorial/
    public boolean doesValidArrayExist_1_3(int[] derived) {
        int XOR = 0;
        for (int element : derived) {
            XOR = XOR ^ element;
        }
        return XOR == 0;
    }

    // V1-4
    // IDEA: Sum Parity
    // https://leetcode.com/problems/neighboring-bitwise-xor/editorial/
    public boolean doesValidArrayExist_1_4(int[] derived) {
        int sum = 0;
        for (int num : derived) {
            sum += num;
        }
        return sum % 2 == 0;
    }


    // V2


}
