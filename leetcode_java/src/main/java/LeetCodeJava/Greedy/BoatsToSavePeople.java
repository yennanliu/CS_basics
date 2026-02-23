package LeetCodeJava.Greedy;

// https://leetcode.com/problems/boats-to-save-people/description/

import java.util.Arrays;
import java.util.Comparator;

/**
 * 881. Boats to Save People
 * Medium
 * Topics
 * Companies
 * You are given an array people where people[i] is the weight of the ith person, and an infinite number of boats where each boat can carry a maximum weight of limit. Each boat carries at most two people at the same time, provided the sum of the weight of those people is at most limit.
 *
 * Return the minimum number of boats to carry every given person.
 *
 *
 *
 * Example 1:
 *
 * Input: people = [1,2], limit = 3
 * Output: 1
 * Explanation: 1 boat (1, 2)
 * Example 2:
 *
 * Input: people = [3,2,2,1], limit = 3
 * Output: 3
 * Explanation: 3 boats (1, 2), (2) and (3)
 * Example 3:
 *
 * Input: people = [3,5,3,4], limit = 5
 * Output: 4
 * Explanation: 4 boats (3), (3), (4), (5)
 *
 *
 * Constraints:
 *
 * 1 <= people.length <= 5 * 104
 * 1 <= people[i] <= limit <= 3 * 104
 *
 *
 */
public class BoatsToSavePeople {

    // V0
    // IDEA: SORT + 2 POINTERS
    // https://youtu.be/XbaxWuHIWUs?si=xUbPJSoZXiLNk6LZ
    public int numRescueBoats(int[] people, int limit) {
        // edge
        if (people == null || people.length == 0) {
            return 0;
        }
        if (people.length == 1) {
            return 1;
        }

        // sorting (small -> big)
        Integer[] people2 = new Integer[people.length];
        for (int i = 0; i < people.length; i++) {
            people2[i] = people[i];
        }
        Arrays.sort(people2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        int cnt = 0;
        // 2 pointers
        int l = 0;
        int r = people2.length - 1;
        while (r >= l) {
            int remaining = limit - people2[r];
            cnt += 1;
            r -= 1;
            /**
             *  NOTE !!!
             *
             *   below:
             *
             *   1) if `remaining` still has capacity for `left` person, we get him/her
             *   2) `r >= l` is for edge case handling, to avoid pointers move overhead
             */
            if (remaining >= people2[l] && r >= l) {
                l += 1;
            }
        }

        return cnt;
    }

    // V0-0-1
    // IDEA: SORT + 2 POINTERS (gemini)
    /**  IDEA:
     *
     * In **LC 881**, there is a very specific constraint: **Each boat can carry at most two people at the same time.**
     *
     * ---
     *
     * ### 🔍 Why the Two-Pointer logic works
     *
     * Imagine `people = [1, 2, 4, 5]` and `limit = 6`.
     *
     * 1. **Sort**: `[1, 2, 4, 5]`
     * 2. **Step 1**: `left(1)` + `right(5)` = 6. (Valid!)
     * * Both 1 and 5 get on the boat.
     * * `left` moves to 2, `right` moves to 4. `boats = 1`.
     *
     *
     * 3. **Step 2**: `left(2)` + `right(4)` = 6. (Valid!)
     * * Both 2 and 4 get on the boat.
     * * `left` moves to index 2, `right` moves to index 1. Loop ends. `boats = 2`.
     *
     *
     *
     * **If you had paired the lightest people first (1+2=3):**
     *
     * 1. 1 and 2 take a boat.
     * 2. 4 is too heavy to fit with 5 (4+5=9 > 6), so 4 takes a boat alone.
     * 3. 5 takes a boat alone.
     * 4. **Total: 3 boats.** (Worse!)
     *
     * ### 📊 Summary
     *
     * * **Sort** the array.
     * * **Heaviest person** is the "bottleneck." They are the most likely to need a boat alone.
     * * If they can "carry" the **lightest person** with them without hitting the limit, do it!
     * * If not, the heavy person must go alone, and the light person waits for someone slightly lighter.
     *
     *
     */
    public int numRescueBoats_0_0_1(int[] people, int limit) {
        // 1. Sort the people by weight
        Arrays.sort(people);

        /** NOTE !!!
         *
         *  2 pointers
         */
        int left = 0; // Lightest person
        int right = people.length - 1; // Heaviest person
        int boats = 0;

        while (left <= right) {
            // Every time we loop, a boat will be used
            boats++;

            // 2. Can the lightest person fit with the heaviest person?
            if (people[left] + people[right] <= limit) {
                // If they fit, they both take the boat
                left++;
            }

            // 3. The heaviest person always takes a boat
            // (either alone or with the lightest person)
            right--;
        }

        return boats;
    }


    // V0-1
    // IDEA: 2 POINTERS (gpt)
    public int numRescueBoats_0_1(int[] people, int limit) {
        if (people == null || people.length == 0) {
            return 0;
        }

        Arrays.sort(people);
        int left = 0;
        int right = people.length - 1;
        int res = 0;

        while (left <= right) {
      /**
       *  NOTE !!!!
       *
       *  1) If people[left] + people[right] <= limit, move both pointers inward.
       * 	 Otherwise, just move right inward.
       *
       * 	 -> so that's why in code below, we move `right` idx anyway
       * 	    e.g. whether `people[left] + people[right] <= limit` condition is met, we move `right` idx
       *
       *
       *
       *  2) from problem description, `1 <= people[i] <= limit <= 3 * 104`
       *    -> means people weight (e.g. people[i]) MUST <= limit
       *    -> so, we are sure that at least the boat can take the heaviest person during the process
       *
       */
      if (people[left] + people[right] <= limit) {
                left++; // Can fit both
            }
            right--; // The heaviest person goes anyway
            res++; // One boat used
        }

        return res;
    }

    // V1-1
    // IDEA: 2 POINTERS (neetcode)
    // https://youtu.be/XbaxWuHIWUs?si=xUbPJSoZXiLNk6LZ
    // https://github.com/ensonfun/leetcode-neetcode/blob/main/c%2F881-Boats-To-Save-People.c
    public int numRescueBoats_1_1(int[] people, int limit) {
        Arrays.sort(people);
        int res = 0;
        int l = 0, r = people.length - 1;

        while (l <= r) {
            int remain = limit - people[r];
            r--;
            res++;

            if (l <= r && remain >= people[l]) {
                l++;
            }
        }

        return res;
    }


    // V2
    // https://leetcode.com/problems/boats-to-save-people/solutions/1877945/javac-a-very-easy-explanation-trust-me-b-pfrk/
    public int numRescueBoats_2(int[] people, int limit) {
        int boatCount = 0;
        Arrays.sort(people);

        int left = 0;
        int right = people.length - 1;

        while (left <= right) {
            int sum = people[left] + people[right];
            if (sum <= limit) {
                boatCount++;
                left++;
                right--;
            } else {
                boatCount++;
                right--;
            }
        }
        return boatCount;
    }

    // V3
    // https://leetcode.com/problems/boats-to-save-people/solutions/5109019/beats-9897-of-users-with-java-simple-eas-6zgk/
    public int numRescueBoats_3(int[] people, int limit) {
        int boats = 0;
        Arrays.sort(people);
        int i = 0, j = people.length - 1;
        while (i <= j) {
            if ((people[j] + people[i]) <= limit) {
                i++;
            }
            j--;
            boats++;
        }
        return boats;
    }

    // V4
    public int numRescueBoats_4(int[] people, int limit) {
        Arrays.sort(people);
        int ans = 0;
        for (int hi = people.length - 1, lo = 0; hi >= lo; --hi, ++ans) { // high end always moves
            if (people[lo] + people[hi] <= limit) { // low end moves only if it can fit in a boat with high end.
                ++lo;
            }
        }
        return ans;
    }

    // V5




}
