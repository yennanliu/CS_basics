package LeetCodeJava.HashTable;

// https://leetcode.com/problems/random-pick-with-blacklist/description/

import java.util.*;

/**
 *  710. Random Pick with Blacklist
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given an integer n and an array of unique integers blacklist. Design an algorithm to pick a random integer in the range [0, n - 1] that is not in blacklist. Any integer that is in the mentioned range and not in blacklist should be equally likely to be returned.
 *
 * Optimize your algorithm such that it minimizes the number of calls to the built-in random function of your language.
 *
 * Implement the Solution class:
 *
 * Solution(int n, int[] blacklist) Initializes the object with the integer n and the blacklisted integers blacklist.
 * int pick() Returns a random integer in the range [0, n - 1] and not in blacklist.
 *
 *
 * Example 1:
 *
 * Input
 * ["Solution", "pick", "pick", "pick", "pick", "pick", "pick", "pick"]
 * [[7, [2, 3, 5]], [], [], [], [], [], [], []]
 * Output
 * [null, 0, 4, 1, 6, 1, 0, 4]
 *
 * Explanation
 * Solution solution = new Solution(7, [2, 3, 5]);
 * solution.pick(); // return 0, any integer from [0,1,4,6] should be ok. Note that for every call of pick,
 *                  // 0, 1, 4, and 6 must be equally likely to be returned (i.e., with probability 1/4).
 * solution.pick(); // return 4
 * solution.pick(); // return 1
 * solution.pick(); // return 6
 * solution.pick(); // return 1
 * solution.pick(); // return 0
 * solution.pick(); // return 4
 *
 *
 * Constraints:
 *
 * 1 <= n <= 109
 * 0 <= blacklist.length <= min(105, n - 1)
 * 0 <= blacklist[i] < n
 * All the values of blacklist are unique.
 * At most 2 * 104 calls will be made to pick.
 *
 */
public class RandomPickWithBlacklist {

    // V0
//    class Solution {
//
//        public Solution(int n, int[] blacklist) {
//
//        }
//
//        public int pick() {
//
//        }
//    }


    // V0-1
    // IDEA: MATH + BRUTE FORCE (TLE)
//    class Solution {
//        // ???
//        HashSet<Integer> set;
//        int n;
//        int[] blacklist;
//        Random random;
//
//        public Solution(int n, int[] blacklist) {
//            this.set = new HashSet<>();
//            this.n = n;
//            this.blacklist = blacklist;
//            for(int x: blacklist){
//                this.set.add(x);
//            }
//            // ???
//            this.random = new Random();
//        }
//
//        public int pick() {
//            // edge
//            if(this.n <= 1){
//                return this.n; // :::
//            }
//            //int val = -1;
//            int val = this.random.nextInt(n); // ???
//            while (this.set.contains(val)){
//                val = this.random.nextInt(n);
//            }
//            return val; // ??
//        }
//
//    }



    // V0-1
    // IDEA: (fixed by gpt)
    /**  Core idea:
     *
     * We must:
     *
     * Only sample from valid range [0, bound)
     * Remap blacklisted numbers in that range
     *
     *
     * -----
     *
     * ## ✅ Correct approach (standard solution)
     *
     * We must:
     *
     * * Only sample from valid range `[0, bound)`
     * * Remap blacklisted numbers in that range
     *
     * ---
     *
     * ## 🧠 Key intuition
     *
     * We split numbers:
     *
     * ### Valid pool:
     *
     * ```
     * [0, bound)
     * ```
     *
     * ### Blacklisted inside that range:
     *
     * → must be replaced with valid numbers from
     * `[bound, n)`
     *
     * So we guarantee:
     *
     * * uniform sampling
     * * O(1) pick
     * * no rejection loop
     *
     * ---
     *
     * ## ⏱ Complexity
     *
     * * Constructor: O(B)
     * * pick(): O(1)
     *
     */
    class Solution_0_1 {

        private Map<Integer, Integer> map;
        private int bound;
        private Random random;

        public Solution_0_1(int n, int[] blacklist) {
            map = new HashMap<>();
            random = new Random();

            Set<Integer> black = new HashSet<>();
            for (int b : blacklist) {
                black.add(b);
            }

            bound = n - blacklist.length;

            int last = n - 1;

            for (int b : blacklist) {
                if (b < bound) {
                    while (black.contains(last)) {
                        last--;
                    }
                    map.put(b, last);
                    last--;
                }
            }
        }

        public int pick() {
            int x = random.nextInt(bound);
            return map.getOrDefault(x, x);
        }
    }



    // V0-2
    // IDEA: (gemini)
    class Solution_0_2 {
        private Map<Integer, Integer> mapping;
        private Random random;
        private int safeRange;

        public Solution_0_2(int n, int[] blacklist) {
            this.random = new Random();
            this.mapping = new HashMap<>();
            this.safeRange = n - blacklist.length;

            // 1. Put all blacklisted numbers into a set for O(1) lookup
            Set<Integer> blackSet = new HashSet<>();
            for (int b : blacklist) {
                blackSet.add(b);
            }

            /** Virtual Remapping strategy */
            // 2. We only need to `remap` blacklisted numbers that
            //    fall within the [0, safeRange)
            // We will map them to valid numbers in the [safeRange, n) zone.
            int last = n - 1;
            for (int b : blacklist) {
                if (b < safeRange) {
                    // Find a 'last' index that is NOT in the blacklist
                    while (blackSet.contains(last)) {
                        last--;
                    }
                    mapping.put(b, last);
                    last--;
                }
            }
        }

        public int pick() {
            // 3. Generate a random index within the guaranteed safe count
            int idx = random.nextInt(safeRange);

            // 4. If the index is a blacklisted number, return its mapped valid value
            // Otherwise, return the index itself
            return mapping.getOrDefault(idx, idx);
        }
    }




    // V1



    // V2
    // https://leetcode.com/problems/random-pick-with-blacklist/solutions/6459117/simple-o1-design-with-detailed-explanati-g9yk/
//    List<int[]> dividedRange;
//    int numberOfDivisions = 0;
//    Random rand;
//
//    public Solution_1_1(int n, int[] arr) {
//        Arrays.sort(arr);
//        rand = new Random();
//        dividedRange = new ArrayList<>();
//        int start = 0, end = 0;
//        for (int i = 0; i < arr.length; i++) {
//            end = arr[i] - 1;
//            if (start > end) {
//                start = arr[i] + 1;
//                continue;
//            }
//            dividedRange.add(new int[] { start, end });
//            start = arr[i] + 1;
//        }
//        if (start < n) {
//            dividedRange.add(new int[] { start, n - 1 });
//        }
//        numberOfDivisions = dividedRange.size();
//    }
//
//    public int pick() {
//        int pickedDivision = rand.nextInt(numberOfDivisions);
//        int start = dividedRange.get(pickedDivision)[0];
//        int end = dividedRange.get(pickedDivision)[1];
//        return rand.nextInt(end - start + 1) + start;
//    }




    // V3
    // https://leetcode.com/problems/random-pick-with-blacklist/solutions/4296646/java-39ms-beats-99-randomly-pick-up-numb-uln4/



    // V4
    // https://leetcode.com/problems/random-pick-with-blacklist/solutions/1992074/java-solution-using-weighted-intervals-b-lryq/




}
