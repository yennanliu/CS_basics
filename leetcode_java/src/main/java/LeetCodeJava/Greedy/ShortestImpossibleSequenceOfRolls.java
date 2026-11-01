package LeetCodeJava.Greedy;

// https://leetcode.com/problems/shortest-impossible-sequence-of-rolls/description/

/**
 * 2350. Shortest Impossible Sequence of Rolls
 * Hard
 *
 * You are given an integer array rolls of length n and an integer k. You roll a k sided dice numbered from 1 to k, n times, where the result of the ith roll is rolls[i].
 *
 * Return the length of the shortest sequence of rolls so that there's no such subsequence in rolls.
 *
 * A sequence of rolls of length len is the result of rolling a k sided dice len times.
 *
 *
 * Example 1:
 *
 * Input: rolls = [4,2,1,2,3,3,2,4,1], k = 4
 * Output: 3
 * Explanation: Every sequence of rolls of length 1, [1], [2], [3], [4], can be taken from rolls.
 * Every sequence of rolls of length 2, [1, 1], [1, 2], ..., [4, 4], can be taken from rolls.
 * The sequence [1, 4, 2] cannot be taken from rolls, so we return 3.
 * Note that there are other sequences that cannot be taken from rolls.
 *
 * Example 2:
 *
 * Input: rolls = [1,1,2,2], k = 2
 * Output: 2
 * Explanation: Every sequence of rolls of length 1, [1], [2], can be taken from rolls.
 * The sequence [2, 1] cannot be taken from rolls, so we return 2.
 * Note that there are other sequences that cannot be taken from rolls but [2, 1] is the shortest.
 *
 * Example 3:
 *
 * Input: rolls = [1,1,3,2,2,2,3,3], k = 4
 * Output: 1
 * Explanation: The sequence [4] cannot be taken from rolls, so we return 1.
 * Note that there are other sequences that cannot be taken from rolls but [4] is the shortest.
 *
 *
 * Constraints:
 *
 * n == rolls.length
 * 1 <= n <= 10^5
 * 1 <= rolls[i] <= k <= 10^5
 *
 */
public class ShortestImpossibleSequenceOfRolls {

    // V0
    // IDEA: GREEDY SEGMENT SPLIT (count complete "all k faces" blocks)
    //       scan left to right and close a segment each time it has seen all k faces.
    //       with m complete segments, every sequence of length m is a subsequence
    //       (match the i-th face inside the i-th segment), but the leftover tail is
    //       missing some face f, so a sequence of length m + 1 ending in f is not.
    //       -> answer = m + 1.
    /**
     * time = O(N)
     * space = O(K)
     */
    public int shortestSequence(int[] rolls, int k) {
        int answer = 1;
        // seenInSegment[face] == answer means the face was seen in the current segment;
        // bumping answer starts a new segment without clearing the array
        int[] seenInSegment = new int[k + 1];
        int distinctInSegment = 0;

        for (int face : rolls) {
            if (seenInSegment[face] != answer) {
                seenInSegment[face] = answer;
                distinctInSegment++;
            }
            // all k faces seen: this segment is complete, start the next one
            if (distinctInSegment == k) {
                answer++;
                distinctInSegment = 0;
            }
        }
        return answer;
    }
}
