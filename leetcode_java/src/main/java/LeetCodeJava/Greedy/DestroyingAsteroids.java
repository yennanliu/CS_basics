package LeetCodeJava.Greedy;

// https://leetcode.com/problems/destroying-asteroids/description/

import java.util.Arrays;

/**
 *  2126. Destroying Asteroids
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer mass, which represents the original mass of a planet. You are further given an integer array asteroids, where asteroids[i] is the mass of the ith asteroid.
 *
 * You can arrange for the planet to collide with the asteroids in any arbitrary order. If the mass of the planet is greater than or equal to the mass of the asteroid, the asteroid is destroyed and the planet gains the mass of the asteroid. Otherwise, the planet is destroyed.
 *
 * Return true if all asteroids can be destroyed. Otherwise, return false.
 *
 *
 *
 * Example 1:
 *
 * Input: mass = 10, asteroids = [3,9,19,5,21]
 * Output: true
 * Explanation: One way to order the asteroids is [9,19,5,3,21]:
 * - The planet collides with the asteroid with a mass of 9. New planet mass: 10 + 9 = 19
 * - The planet collides with the asteroid with a mass of 19. New planet mass: 19 + 19 = 38
 * - The planet collides with the asteroid with a mass of 5. New planet mass: 38 + 5 = 43
 * - The planet collides with the asteroid with a mass of 3. New planet mass: 43 + 3 = 46
 * - The planet collides with the asteroid with a mass of 21. New planet mass: 46 + 21 = 67
 * All asteroids are destroyed.
 * Example 2:
 *
 * Input: mass = 5, asteroids = [4,9,23,4]
 * Output: false
 * Explanation:
 * The planet cannot ever gain enough mass to destroy the asteroid with a mass of 23.
 * After the planet destroys the other asteroids, it will have a mass of 5 + 4 + 9 + 4 = 22.
 * This is less than 23, so a collision would not destroy the last asteroid.
 *
 *
 * Constraints:
 *
 * 1 <= mass <= 105
 * 1 <= asteroids.length <= 105
 * 1 <= asteroids[i] <= 105
 *
 */
public class DestroyingAsteroids {

    // V0
//    public boolean asteroidsDestroyed(int mass, int[] asteroids) {
//
//    }


    // V1


    // V2
    // https://leetcode.com/problems/destroying-asteroids/solutions/7026917/beats-100-beginner-friendly-explanation-b3stp/
    public boolean asteroidsDestroyed_2(int mass, int[] asteroids) {
        long m = mass;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        // Find min and max asteroid sizes
        for (int a : asteroids) {
            min = Math.min(min, a);
            max = Math.max(max, a);
        }

        // Frequency array to store asteroid counts
        int[] count = new int[max + 1];

        // Try to absorb what we can immediately and store others
        for (int a : asteroids) {
            if (a <= m) {
                m += a; // Absorb
            } else {
                count[a]++; // Store for later
            }
        }

        // Try to absorb remaining asteroids from smallest to largest
        for (int i = min; i <= max; i++) {
            if (count[i] > 0) {
                if (m >= i) {
                    m += (long) i * count[i]; // Absorb all of this size
                } else {
                    return false; // Can't absorb, fail
                }
            }
        }

        return true; // Survived all asteroids
    }


    // V3
    // https://leetcode.com/problems/destroying-asteroids/solutions/7782608/test-us-when-to-use-long-instead-of-int-z24ve/
    // time : O(n log n), n = number of elemnts to loop through and sorting algo
    // space: O(1)
    public boolean asteroidsDestroyed_3(int mass, int[] asteroids) {
        // arr    = 3 9 19 5 21
        // sorted = 3 5 9 19 21

        // mass = 10 -> for large values mass should be a long type

        Arrays.sort(asteroids);

        long planet = mass;

        for (int asteroid : asteroids) {
            if (asteroid > planet)
                return false;
            planet += asteroid;
        }

        return true;
    }




}
