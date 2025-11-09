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

    // V0-1
    // TODO: validate below
//    public boolean doesValidArrayExist(int[] derived) {
//        // edge
//        if(derived == null || derived.length == 0){
//            return true;
//        }
//
//        //  int result = a ^ b; // Binary: 0110 (which is 6 in decimal)
//
//        // cache as original
//        int[] original = derived;
//
//        // ??? op
//        int n = derived.length;
//        for(int i = 0; i < n; i++){
//            // check if `can form as original`
//            if(n > 0){
//                if(isSameBySort(original, derived)){
//                    return true;
//                }
//            }
//            // case 1) i == n - 1
//            if(i == n - 1){
//                derived[i] = (original[i] ^ original[0]); // ???
//            }
//            // case 2) otherwise
//            else{
//                derived[i] = (original[i] ^ original[i + 1]); // ???
//            }
//        }
//
//        return false;
//    }
//
//    private boolean isSameBySort(int[] original, int[] cur){
//        // sort ??
//        Arrays.sort(original);
//        Arrays.sort(cur);
//
//        //????
//        if(original.length != cur.length){
//            return false;
//        }
//        for(int i = 0; i < original.length; i++){
//            if(original[i] != cur[i]){
//                return false;
//            }
//        }
//
//        // ???
//        // return original.equals(cur);
//        return true;
//    }


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
