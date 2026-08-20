package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/find-the-k-th-character-in-string-game-ii/

/**
 *  3307. Find the K-th Character in String Game II
 *  Hard
 *
 *  Alice and Bob are playing a game. Initially, Alice has a string word = "a".
 *
 *  You are given a positive integer k. You are also given an integer array
 *  operations, where operations[i] represents the type of the ith operation.
 *
 *  Now Bob will ask Alice to perform all operations in sequence:
 *    If operations[i] == 0, append a copy of word to itself.
 *    If operations[i] == 1, generate a new string by changing each character in
 *    word to its next character in the English alphabet, and append it to the
 *    original word.
 *
 *  Return the value of the kth character in word after performing all operations.
 *
 *  Note that the character 'z' can be changed to 'a' in the operation.
 *
 *  Example 1:
 *    Input: k = 5, operations = [0,0,0]
 *    Output: "a"
 *    Explanation: word becomes "aa" -> "aaaa" -> "aaaaaaaa".
 *
 *  Example 2:
 *    Input: k = 10, operations = [0,1,0,1]
 *    Output: "b"
 *    Explanation: word becomes "aa" -> "aabb" -> "aabbaabb" ->
 *                 "aabbaabbbbccbbcc"; the 10th char is 'b'.
 *
 *  Constraints:
 *    1 <= k <= 10^14
 *    1 <= operations.length <= 100
 *    operations[i] is either 0 or 1.
 *    The input is generated such that word has at least k characters after all
 *    operations.
 */
public class FindTheKthCharacterInStringGameII {

    // V0
    // IDEA: WALK THE OPERATIONS BACKWARDS, FOLDING k INTO THE FIRST HALF
    //       every operation doubles the length, so the round that first covers
    //       index k is easy to find: grow the lengths until they reach k, then
    //       unwind.
    //       unwinding one operation: if k lies in the first half nothing happened
    //       to it; if it lies in the second half it is a copy of position
    //       k - half, shifted by one letter when the operation was of type 1.
    //       so the answer is 'a' plus the number of type-1 operations whose second
    //       half contained k, taken modulo 26.
    //       NOTE: k reaches 10^14, so k / lengths must be long - and the lengths
    //             are CAPPED (we stop as soon as they cover k) rather than built
    //             for all 100 operations, which would overflow any integer type.
    /**
     * time = O(len(operations))
     * space = O(len(operations))
     */
    public char kthCharacter(long k, int[] operations) {
        int m = operations.length;
        long[] lengths = new long[m];
        int used = 0;
        long size = 1L;
        for (int i = 0; i < m; i++) {
            size *= 2L;
            lengths[used++] = size;
            if (size >= k) {
                break;
            }
        }

        int shifts = 0;
        long pos = k - 1;                    // 0-indexed
        for (int i = used - 1; i >= 0; i--) {
            long half = lengths[i] / 2;
            if (pos >= half) {               // came from the appended copy
                pos -= half;
                if (operations[i] == 1) {
                    shifts++;
                }
            }
        }
        return (char) ('a' + (shifts % 26));
    }
}
