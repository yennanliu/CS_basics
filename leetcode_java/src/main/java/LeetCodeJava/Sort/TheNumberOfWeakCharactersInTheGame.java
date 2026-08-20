package LeetCodeJava.Sort;

// https://leetcode.com/problems/the-number-of-weak-characters-in-the-game/

import java.util.Arrays;

/**
 *  1996. The Number of Weak Characters in the Game
 *  Medium
 *
 *  You are playing a game that contains multiple characters, and each of the
 *  characters has two main properties: attack and defense. You are given a 2D
 *  integer array properties where properties[i] = [attack_i, defense_i]
 *  represents the properties of the ith character in the game.
 *
 *  A character is said to be weak if any other character has both attack and
 *  defense levels strictly greater than this character's attack and defense
 *  levels. More formally, a character i is said to be weak if there exists
 *  another character j where attack_j > attack_i and defense_j > defense_i.
 *
 *  Return the number of weak characters.
 *
 *  Example 1:
 *    Input: properties = [[5,5],[6,3],[3,6]]
 *    Output: 0
 *
 *  Example 2:
 *    Input: properties = [[2,2],[3,3]]
 *    Output: 1
 *
 *  Example 3:
 *    Input: properties = [[1,5],[10,4],[4,3]]
 *    Output: 1
 *
 *  Constraints:
 *    2 <= properties.length <= 10^5
 *    properties[i].length == 2
 *    1 <= attack_i, defense_i <= 10^5
 */
public class TheNumberOfWeakCharactersInTheGame {

    // V0
    // IDEA: SORT BY (attack DESC, defense ASC) + RUNNING MAX DEFENSE
    //       scanning in that order, every character seen BEFORE the current one
    //       has attack >= mine; the tie-break (defense ASCENDING within equal
    //       attack) guarantees anyone with the SAME attack was seen with a
    //       defense <= mine, so they can never inflate the running max above my
    //       own defense.
    //       therefore: mx (max defense seen so far) > my defense <=> somebody
    //       strictly dominates me -> count me as weak.
    //       NOTE: the ascending defense tie-break is the whole trick; sorting
    //             defense descending would wrongly flag equal-attack characters.
    /**
     * time = O(n log n)
     * space = O(log n)   // sort recursion only
     */
    public int numberOfWeakCharacters(int[][] properties) {
        Arrays.sort(properties, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(b[0], a[0]);   // attack DESC
            }
            return Integer.compare(a[1], b[1]);       // defense ASC
        });

        int res = 0;
        int mx = 0;
        for (int[] p : properties) {
            if (p[1] < mx) {
                res++;
            } else {
                mx = p[1];
            }
        }
        return res;
    }
}
