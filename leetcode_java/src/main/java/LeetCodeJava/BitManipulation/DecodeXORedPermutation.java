package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/decode-xored-permutation/

/**
 *  1734. Decode XORed Permutation
 *  Medium
 *
 *  There is an integer array perm that is a permutation of the first n positive
 *  integers, where n is always odd.
 *
 *  It was encoded into another integer array encoded of length n - 1, such that
 *  encoded[i] = perm[i] XOR perm[i + 1]. For example, if perm = [1,3,2], then
 *  encoded = [2,1].
 *
 *  Given the encoded array, return the original array perm. It is guaranteed
 *  that the answer exists and is unique.
 *
 *  Example 1:
 *    Input: encoded = [3,1]
 *    Output: [1,2,3]
 *    Explanation: perm = [1,2,3] -> encoded = [1^2, 2^3] = [3,1]
 *
 *  Example 2:
 *    Input: encoded = [6,5,4,6]
 *    Output: [2,4,1,5,3]
 *
 *  Constraints:
 *    3 <= n < 10^5
 *    n is odd.
 *    encoded.length == n - 1
 */
public class DecodeXORedPermutation {

    // V0
    // IDEA: RECOVER perm[0] FROM PARITY, THEN UNROLL THE CHAIN
    //       unlike LC 1720 perm[0] is not given, so exploit that perm is a
    //       permutation of 1..n with n ODD:
    //         total = 1 ^ 2 ^ ... ^ n = perm[0] ^ perm[1] ^ ... ^ perm[n-1]
    //       now pair the elements up with the ODD-INDEXED encoded entries:
    //         encoded[1] = perm[1]^perm[2], encoded[3] = perm[3]^perm[4], ...
    //       there are (n-1)/2 of them and together they cover perm[1..n-1]
    //       exactly once. call their XOR `odd`; everything cancels except:
    //         perm[0] = total ^ odd
    //       then perm[i+1] = perm[i] ^ encoded[i] walks the rest.
    //       NOTE: n odd is essential - it is what leaves exactly one unpaired
    //             element (perm[0]) after chaining the pairs.
    /**
     * time = O(N)
     * space = O(N)   // the output array
     */
    public int[] decode(int[] encoded) {
        int n = encoded.length + 1;

        int total = 0;
        for (int v = 1; v <= n; v++) {
            total ^= v;
        }

        int odd = 0;
        for (int i = 1; i < n - 1; i += 2) {
            odd ^= encoded[i];
        }

        int[] res = new int[n];
        res[0] = total ^ odd;
        for (int i = 0; i < encoded.length; i++) {
            res[i + 1] = res[i] ^ encoded[i];
        }
        return res;
    }
}
