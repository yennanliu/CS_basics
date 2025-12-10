package LeetCodeJava.HashTable;

// https://leetcode.com/problems/relocate-marbles/description/

import java.util.*;

/**
 * 2766. Relocate Marbles
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums representing the initial positions of some marbles. You are also given two 0-indexed integer arrays moveFrom and moveTo of equal length.
 *
 * Throughout moveFrom.length steps, you will change the positions of the marbles. On the ith step, you will move all marbles at position moveFrom[i] to position moveTo[i].
 *
 * After completing all the steps, return the sorted list of occupied positions.
 *
 * Notes:
 *
 * We call a position occupied if there is at least one marble in that position.
 * There may be multiple marbles in a single position.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,6,7,8], moveFrom = [1,7,2], moveTo = [2,9,5]
 * Output: [5,6,8,9]
 * Explanation: Initially, the marbles are at positions 1,6,7,8.
 * At the i = 0th step, we move the marbles at position 1 to position 2. Then, positions 2,6,7,8 are occupied.
 * At the i = 1st step, we move the marbles at position 7 to position 9. Then, positions 2,6,8,9 are occupied.
 * At the i = 2nd step, we move the marbles at position 2 to position 5. Then, positions 5,6,8,9 are occupied.
 * At the end, the final positions containing at least one marbles are [5,6,8,9].
 * Example 2:
 *
 * Input: nums = [1,1,3,3], moveFrom = [1,3], moveTo = [2,2]
 * Output: [2]
 * Explanation: Initially, the marbles are at positions [1,1,3,3].
 * At the i = 0th step, we move all the marbles at position 1 to position 2. Then, the marbles are at positions [2,2,3,3].
 * At the i = 1st step, we move all the marbles at position 3 to position 2. Then, the marbles are at positions [2,2,2,2].
 * Since 2 is the only occupied position, we return [2].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= moveFrom.length <= 105
 * moveFrom.length == moveTo.length
 * 1 <= nums[i], moveFrom[i], moveTo[i] <= 109
 * The test cases are generated such that there is at least a marble in moveFrom[i] at the moment we want to apply the ith move.
 *
 *
 */
public class RelocateMarbles {

    // V0
//    public List<Integer> relocateMarbles(int[] nums, int[] moveFrom, int[] moveTo) {
//
//    }

    // V0-1
    // IDEA: SET (gemini)
    /**
     *  NOTE !!!
     *
     *   the op:
     *
     *     you will change the positions of the marbles. On the ith step,
     *     you will move all marbles at position moveFrom[i] to position moveTo[i].
     *
     *
     *     -> so when move from i to j (moveFrom[i] -> moveTo[i]),
     *      ALL the marbles will be moved.
     *
     *
     *
     * -> so what we care if: tracking the `hole` over indexes,
     *    NOT the actual position (idx) of every single marble
     *
     */
    /**
     * Finds the final unique positions of the marbles after all move operations.
     * @param nums Initial positions of the marbles.
     * @param moveFrom Array of starting positions for moves.
     * @param moveTo Array of destination positions for moves.
     * @return A sorted list of the final unique positions.
     */
    public List<Integer> relocateMarbles_0_1(int[] nums, int[] moveFrom, int[] moveTo) {

        /**
         *  NOTE !!!
         *
         *  we use set to track the `unique` idx of marbles.
         *
         */
        // Use a HashSet to track the unique current positions of all marbles.
        Set<Integer> positions = new HashSet<>();

        // 1. Initialize the set with all unique starting positions.
        for (int position : nums) {
            positions.add(position);
        }

        /**
         *  NOTE !!!
         *
         * -> so what we care if: tracking the `hole` over indexes,
         *    NOT the actual position (idx) of every single marble
         *
         */
        // 2. Process all move operations.
        // The move is conceptual: all marbles at 'start' move to 'end'.
        for (int i = 0; i < moveFrom.length; i++) {
            int start = moveFrom[i];
            int end = moveTo[i];

            // Check if there are any marbles at the starting position to move.
            // This is O(1) due to the HashSet.
            if (positions.contains(start)) {

                // Remove the starting position.
                positions.remove(start);

                // Add the new ending position.
                // If 'end' already existed, the set operation does nothing, which is correct.
                positions.add(end);
            }
            // If 'start' doesn't contain a marble, the operation is skipped, which is correct.
        }

        // 3. Format and return the result.
        // The problem requires the result to be a sorted list.
        List<Integer> result = new ArrayList<>(positions);
        Collections.sort(result);

        return result;
    }

    // V0-2
    // IDEA: SET (gpt)
    public List<Integer> relocateMarbles_0_2(int[] nums, int[] moveFrom, int[] moveTo) {

        // Use a set because only unique positions matter
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }

        // Apply moves
        for (int i = 0; i < moveFrom.length; i++) {
            int start = moveFrom[i];
            int end = moveTo[i];
            if (set.contains(start)) {
                set.remove(start);
                set.add(end);
            }
        }

        // Convert to sorted list
        List<Integer> res = new ArrayList<>(set);
        Collections.sort(res);

        return res;
    }


    // V1
    // IDEA: HASHMAP
    // https://leetcode.com/problems/relocate-marbles/solutions/3737295/cjavapython-clean-code-using-map-by-adit-5oxa/
    public List<Integer> relocateMarbles_1(int[] nums, int[] moveFrom, int[] moveTo) {
        Map<Integer, Integer> map = new HashMap<>(); // Map to store the count of marbles

        // Count the marbles and store their count in the map
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        // Relocate marbles from moveFrom to moveTo
        for (int i = 0; i < moveFrom.length; i++) {
            int numMarbles = map.get(moveFrom[i]); // Get the count of marbles to be moved
            map.remove(moveFrom[i]); // Remove the marble from the original location
            map.put(moveTo[i], map.getOrDefault(moveTo[i], 0) + numMarbles); // Add the marbles to the new location
        }

        List<Integer> result = new ArrayList<>(); // Result list to store the relocated marbles
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            result.add(entry.getKey()); // Add the marble number to the result list
        }
        result.sort(null); // Sort the result list in ascending order

        return result; // Return the final result list
    }

    // V2
    // IDEA: SET
    // https://leetcode.com/problems/relocate-marbles/solutions/7218887/java-easy-100-solution-by-ytchouar-gowg/
    public List<Integer> relocateMarbles_2(final int[] nums, final int[] moveFrom, final int[] moveTo) {
        final int n = moveFrom.length;
        final Set<Integer> set = new HashSet<>();

        for (final int num : nums)
            set.add(num);

        for (int i = 0; i < n; ++i) {
            set.remove(moveFrom[i]);
            set.add(moveTo[i]);
        }

        final List<Integer> result = new ArrayList<>(set);

        Collections.sort(result);

        return result;
    }


    // V3
    // IDEA: SET
    // https://leetcode.com/problems/relocate-marbles/solutions/6848791/most-simple-java-solution-beginner-frien-8vsf/
    public List<Integer> relocateMarbles_3(int[] nums, int[] moveFrom, int[] moveTo) {
        Set<Integer> set=new HashSet<>();
        for(int i=0;i<nums.length;i++){
            set.add(nums[i]);
        }
        for(int i=0;i<moveFrom.length;i++){
            set.remove(moveFrom[i]);
            set.add(moveTo[i]);
        }
        List<Integer> result=new ArrayList<>(set);

        Collections.sort(result);
        return result;
    }



}
