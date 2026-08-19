package LeetCodeJava.HashTable;

// https://leetcode.com/problems/distribute-candies/

import java.util.HashSet;
import java.util.Set;

/**
 *  575. Distribute Candies
 *  Easy
 *
 *  Alice has n candies, where the ith candy is of type candyType[i].
 *  Alice noticed that she started to gain weight, so she visited a doctor.
 *
 *  The doctor advised Alice to only eat n / 2 of the candies she has (n is always even).
 *  Alice likes her candies very much, and she wants to eat the maximum number of
 *  different types of candies while still following the doctor's advice.
 *
 *  Return the maximum number of different types of candies she can eat if she only
 *  eats n / 2 of them.
 *
 *  Example 1:
 *  Input: candyType = [1,1,2,2,3,3]
 *  Output: 3
 *
 *  Example 2:
 *  Input: candyType = [1,1,2,3]
 *  Output: 2
 *
 *  Example 3:
 *  Input: candyType = [6,6,6,6]
 *  Output: 1
 *
 *  Constraints:
 *  n == candyType.length, 2 <= n <= 10^4, n is even
 *  -10^5 <= candyType[i] <= 10^5
 */
public class DistributeCandies {

    // V0
    // IDEA: answer = min(# distinct types, n / 2)
    /**
     * time = O(n)
     * space = O(n)
     */
    public int distributeCandies(int[] candyType) {
        Set<Integer> types = new HashSet<>();
        for (int c : candyType) {
            types.add(c);
        }
        return Math.min(types.size(), candyType.length / 2);
    }
}
