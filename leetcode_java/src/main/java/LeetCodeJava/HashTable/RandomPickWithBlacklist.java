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
    // IDEA: Virtual Mapping (GEMINI)
    class Solution {

        Map<Integer, Integer> map;
        Set<Integer> blackSet;
        int boundary;
        Random random;

        public Solution(int n, int[] blacklist) {

            map = new HashMap<>();
            blackSet = new HashSet<>();

            for (int b : blacklist) {
                blackSet.add(b);
            }

            /** NOTE !!! CORE
             *
             *
             *  boundary = n - blacklist.length;
             *
             *  -> so we know the `size` of `remaining`
             *     val that NOT in black list
             *     (we'll do `remapping` later)
             *
             */
            // valid random range: [0, boundary)
            boundary = n - blacklist.length;

            // start from the end
            int last = n - 1;

            /** NOTE !!! Remapping
             *
             *
             *  1. go through black list
             *
             *  2. if the `black list val` < boundary
             *      -> NEED to REMAP
             *      -> use `while` loop
             *         -> check if `last` is in blacklist
             *            (if yes, we CAN NOT use it as remapping val)
             *         -> REMAPPING: { black_list_val : last }
             *         -> DON'T forget to update last (last -= 1)
             *
             */
            // only remap blacklisted numbers < boundary
            for (int b : blacklist) {

                if (b < boundary) {

                    // find next valid number from the tail
                    while (blackSet.contains(last)) {
                        last--;
                    }

                    map.put(b, last);
                    last--;
                }
            }

            random = new Random();
        }

        public int pick() {

            int x = random.nextInt(boundary);

            // if x is blacklisted, use mapped value
            return map.getOrDefault(x, x);
        }
    }


    // V0-0-1
    // IDEA: Virtual Mapping (GEMINI)
    /**

     * The goal is to use **Virtual Mapping**:
     *
     * 1. **Define the Safe Range**:
     *
     *       If there are $M$ blacklisted numbers,
     *       there are $N - M$ valid numbers.
     *       We only ever pick a random index
     *       in the range $[0, N - M)$.
     *
     *
     * 2. **The Re-map**:
     *
     *      If the random index we pick is a blacklisted number,
     *      we "redirect" it to a valid number located in the "back"
     *      of the array (the range $[N - M, N)$).
     *
     *
     */
    /**  CORE IDEA & DRY RUN
     *
     *  Suppose:
     *
     * ```
     * n = 10
     * blacklist = [2,3,5,8]
     * ```
     *
     * ---
     *
     * # STEP 1: Compute bound
     *
     * ```
     * bound = n - blacklist.length
     *       = 10 - 4
     *       = 6
     * ```
     *
     * So random only generates:
     *
     * ```
     * [0 ... 5]
     * ```
     *
     * Visualization:
     *
     * ```
     * RANDOM RANGE
     * |----|----|----|----|----|----|
     *   0    1    2    3    4    5
     *             X    X         X
     * ```
     *
     * Blacklisted inside random range:
     *
     * * 2
     * * 3
     * * 5
     *
     * These are BAD.
     *
     * ---
     *
     * # STEP 2: Find replacement values
     *
     * Remaining numbers:
     *
     * ```
     * [6 ... 9]
     * ```
     *
     * Visualization:
     *
     * ```
     * OUTSIDE RANDOM RANGE
     * |----|----|----|----|
     *   6    7    8    9
     *             X
     * ```
     *
     * 8 is blacklisted.
     *
     * Valid replacement numbers:
     *
     * ```
     * 6, 7, 9
     * ```
     *
     * ---
     *
     * # STEP 3: Start mapping
     *
     * Initial:
     *
     * ```
     * last = 9
     * ```
     *
     * ---
     *
     * # ITERATION 1
     *
     * ```
     * b = 2
     * ```
     *
     * Need to remap 2.
     *
     * ---
     *
     * Current structure:
     *
     * ```
     * RANDOM RANGE
     * |----|----|----|----|----|----|
     *   0    1    2    3    4    5
     *             X    X         X
     *
     *
     * OUTSIDE RANGE
     * |----|----|----|----|
     *   6    7    8    9
     *             X         ^
     *                       last
     * ```
     *
     * Check:
     *
     * ```
     * while (blackSet.contains(last))
     * ```
     *
     * Is 9 blacklisted?
     *
     * ```
     * NO
     * ```
     *
     * So map:
     *
     * ```
     * 2 -> 9
     * ```
     *
     * Visualization:
     *
     * ```
     * |----|----|----|----|----|----|
     *   0    1    2    3    4    5
     *             |
     *             |
     *             v
     *             9
     * ```
     *
     * Then:
     *
     * ```
     * last--
     * ```
     *
     * Now:
     *
     * ```
     * last = 8
     * ```
     *
     * ---
     *
     * # ITERATION 2
     *
     * ```
     * b = 3
     * ```
     *
     * Need remapping.
     *
     * Current:
     *
     * ```
     * |----|----|----|----|
     *   6    7    8    9
     *             ^
     *            last
     * ```
     *
     * But:
     *
     * ```
     * 8 is blacklisted
     * ```
     *
     * So while loop runs:
     *
     * ```
     * last--
     * ```
     *
     * Now:
     *
     * ```
     * last = 7
     * ```
     *
     * Visualization:
     *
     * ```
     * |----|----|----|----|
     *   6    7    8    9
     *        ^    X
     *       last
     * ```
     *
     * 7 is valid.
     *
     * So:
     *
     * ```
     * 3 -> 7
     * ```
     *
     * Visualization:
     *
     * ```
     * |----|----|----|----|----|----|
     *   0    1    2    3    4    5
     *                  |
     *                  |
     *                  v
     *                  7
     * ```
     *
     * Then:
     *
     * ```
     * last = 6
     * ```
     *
     * ---
     *
     * # ITERATION 3
     *
     * ```
     * b = 5
     * ```
     *
     * Current:
     *
     * ```
     * |----|----|----|----|
     *   6    7    8    9
     *   ^
     *  last
     * ```
     *
     * 6 is valid.
     *
     * So:
     *
     * ```
     * 5 -> 6
     * ```
     *
     * Visualization:
     *
     * ```
     * |----|----|----|----|----|----|
     *   0    1    2    3    4    5
     *                                 |
     *                                 |
     *                                 v
     *                                 6
     * ```
     *
     * ---
     *
     * # FINAL MAPPING
     *
     * ```
     * 2 -> 9
     * 3 -> 7
     * 5 -> 6
     * ```
     *
     * ---
     *
     * # FINAL RANDOM BEHAVIOR
     *
     * Random only generates:
     *
     * ```
     * [0..5]
     * ```
     *
     * Visualization:
     *
     * ```
     * |----|----|----|----|----|----|
     *   0    1    2    3    4    5
     *             |    |         |
     *             v    v         v
     *             9    7         6
     * ```
     *
     * So actual returned values become:
     *
     * ```
     * 0, 1, 9, 7, 4, 6
     * ```
     *
     * Which are exactly ALL valid numbers:
     *
     * ```
     * 0,1,4,6,7,9
     * ```
     *
     * uniformly distributed.
     *
     *
     *
     */
    class Solution_0_0_1 {
        // Maps blacklisted numbers in the [0, bound) range to valid numbers in [bound, n)
        private Map<Integer, Integer> mapping;
        private Random random;
        private int bound;

        public Solution_0_0_1(int n, int[] blacklist) {
            this.random = new Random();
            this.mapping = new HashMap<>();
            this.bound = n - blacklist.length;

            // 1. Identify all blacklisted numbers for O(1) lookup
            Set<Integer> blackSet = new HashSet<>();
            for (int b : blacklist) {
                blackSet.add(b);
            }

            /** NOTE !!!   CORE IDEA
             *
             *  RE-MAP `blacklisted numbers`
             *
             *  ->
             *
             *   1. ONLY need to map blacklisted val in [0, bound) range
             *
             *   2. will map above to a valid val in `[bound, n)` range
             *
             *
             *  ->
             *
             *  Convert blacklisted numbers INSIDE the random range
             *  into valid numbers OUTSIDE the random range.
             *
             */
            // 2. We only need to `remap` blacklisted numbers that fall within [0, bound)
            // We will map them to valid numbers in the `[bound, n)` range.
            int last = n - 1;
            for (int b : blacklist) {
                if (b < bound) {
                    // Find a 'last' index that is `NOT` in the blacklist
                    // to use as a `destination`
                    while (blackSet.contains(last)) {
                        last--;
                    }
                    mapping.put(b, last);
                    last--;
                }
            }

        }

        public int pick() {
            // 3. Generate a random index within the safe bound
            int idx = random.nextInt(bound);

            /** NOTE !!!
             *
             *  how we return val on `blacklisted`, and `non-blacklisted` cases
             *
             *   -> 1. if is a `blacklisted number` -> return mapped valid val
             *   -> 2. if NOT a `blacklisted`  -> return directly (index itself)
             */
            // 4. If the index is a blacklisted number, return its mapped valid value
            // Otherwise, return the index itself
            return mapping.getOrDefault(idx, idx);
        }

    }



    // V0-1
    // IDEA: BRUTE FORCE (TLE) (GPT)
    class Solution_0_1 {

        // allowed numbers
        List<Integer> list;

        Random random;

        public Solution_0_1(int n, int[] blacklist) {

            Set<Integer> set = new HashSet<>();

            for (int x : blacklist) {
                set.add(x);
            }

            list = new ArrayList<>();

            // store only valid numbers
            for (int i = 0; i < n; i++) {

                if (!set.contains(i)) {
                    list.add(i);
                }
            }

            random = new Random();
        }

        public int pick() {

            // random index
            int idx = random.nextInt(list.size());

            return list.get(idx);
        }
    }


    // V0-2
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
    class Solution_0_2 {

        private Map<Integer, Integer> map;
        private int bound;
        private Random random;

        public Solution_0_2(int n, int[] blacklist) {
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



    // V0-3
    // IDEA: (gemini)
    class Solution_0_3 {
        private Map<Integer, Integer> mapping;
        private Random random;
        private int safeRange;

        public Solution_0_3(int n, int[] blacklist) {
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
