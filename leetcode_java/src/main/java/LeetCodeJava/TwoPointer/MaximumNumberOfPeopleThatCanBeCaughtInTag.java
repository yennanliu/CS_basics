package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/maximum-number-of-people-that-can-be-caught-in-tag/description/
// https://leetcode.ca/2021-05-11-1989-Maximum-Number-of-People-That-Can-Be-Caught-in-Tag/

/**
 * 1989. Maximum Number of People That Can Be Caught in Tag
 * Medium
 *
 * You are playing a game of tag with your friends. In tag, people are divided into two teams: people who are "it", and people who are not "it". The people who are "it" want to catch as many people as possible who are not "it".
 *
 * You are given a 0-indexed integer array team containing only zeros (denoting people who are not "it") and ones (denoting people who are "it"), and an integer dist. A person who is "it" at index i can catch any one person whose index is in the range [i - dist, i + dist] (inclusive) and is not "it".
 *
 * Return the maximum number of people that the people who are "it" can catch.
 *
 *
 * Example 1:
 *
 * Input: team = [0,1,0,1,0], dist = 3
 * Output: 2
 * Explanation:
 * The person who is "it" at index 1 can catch people in the range [i-dist, i+dist] = [1-3, 1+3] = [-2, 4].
 * They can catch the person who is not "it" at index 2.
 * The person who is "it" at index 3 can catch people in the range [i-dist, i+dist] = [3-3, 3+3] = [0, 6].
 * They can catch the person who is not "it" at index 0.
 * The person who is not "it" at index 4 will not be caught because the people at indices 1 and 3 are already catching one person.
 *
 * Example 2:
 *
 * Input: team = [1], dist = 1
 * Output: 0
 * Explanation:
 * There are no people who are not "it" to catch.
 *
 * Example 3:
 *
 * Input: team = [0], dist = 1
 * Output: 0
 * Explanation:
 * There are no people who are "it" to catch people.
 *
 *
 * Constraints:
 *
 * 1 <= team.length <= 10^5
 * 0 <= team[i] <= 1
 * 1 <= dist <= team.length
 *
 */
public class MaximumNumberOfPeopleThatCanBeCaughtInTag {

    // V0
    // IDEA: GREEDY TWO POINTERS (match each catcher to the leftmost free target)
    //       sweep i over the catchers (team[i] == 1) left to right, keeping j on
    //       the leftmost not-yet-caught person who is "it" (team[j] == 0).
    //       for the current catcher, skip past any 1 and any 0 that is too far
    //       behind (i - j > dist); if the next 0 is within reach, catch it.
    //       greedy is safe: a later catcher's reach starts further right, so
    //       taking the leftmost reachable target never blocks it.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int catchMaximumAmountofPeople(int[] team, int dist) {
        int n = team.length;
        int caught = 0;
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (team[i] != 1) {
                continue;
            }
            // skip catchers and targets that are now out of reach behind i
            while (j < n && (team[j] == 1 || i - j > dist)) {
                j++;
            }
            // j is the leftmost free target not too far behind; check the front too
            if (j < n && j - i <= dist) {
                caught++;
                j++;
            }
        }
        return caught;
    }
}
