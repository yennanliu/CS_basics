package LeetCodeJava.HashTable;

// https://leetcode.com/problems/implement-rand10-using-rand7/

import java.util.Random;

/**
 *  470. Implement Rand10() Using Rand7()
 *  Medium
 *
 *  Given the API rand7() that generates a uniform random integer in the range [1, 7],
 *  write a function rand10() that generates a uniform random integer in the range [1, 10].
 *  You can only call the API rand7(), and you shouldn't call any other API.
 *
 *  Each test case will have one internal argument n, the number of times that your
 *  implemented function rand10() will be called while testing.
 *
 *  Example 1:
 *  Input: n = 1
 *  Output: [2]
 *
 *  Example 2:
 *  Input: n = 3
 *  Output: [3,8,10]
 *
 *  Constraints:
 *  1 <= n <= 10^5
 *
 *  NOTE: on LeetCode the solution class extends SolBase which supplies rand7().
 *        Here rand7() is provided locally so the file is self-contained / compilable.
 */
public class ImplementRand10UsingRand7 {

    private final Random random = new Random();

    /** the LeetCode-provided API: a uniform random integer in [1, 7] */
    public int rand7() {
        return random.nextInt(7) + 1;
    }

    // V0
    // IDEA: REJECTION SAMPLING
    //       rand7 x rand7 -> uniform 0..48 (rand49); reject >= 40 so 0..39 is uniform,
    //       then `% 10 + 1` maps evenly to 1..10 (each value hit exactly 4 times)
    /**
     * time = O(1)      // expected; each round succeeds with prob 40/49
     * space = O(1)
     */
    public int rand10() {
        while (true) {
            int num = 7 * (rand7() - 1) + (rand7() - 1); // 0 .. 48, uniform
            if (num < 40) {
                return num % 10 + 1;
            }
        }
    }
}
