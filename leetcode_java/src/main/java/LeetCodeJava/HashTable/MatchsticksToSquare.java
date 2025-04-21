package LeetCodeJava.HashTable;

// https://leetcode.com/problems/matchsticks-to-square

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 473. Matchsticks to Square
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an integer array matchsticks where matchsticks[i] is the length of the ith matchstick. You want to use all the matchsticks to make one square. You should not break any stick, but you can link them up, and each matchstick must be used exactly one time.
 *
 * Return true if you can make this square and false otherwise.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matchsticks = [1,1,2,2,2]
 * Output: true
 * Explanation: You can form a square with length 2, one side of the square came two sticks with length 1.
 * Example 2:
 *
 * Input: matchsticks = [3,3,3,3,4]
 * Output: false
 * Explanation: You cannot find a way to form a square with all the matchsticks.
 *
 *
 * Constraints:
 *
 * 1 <= matchsticks.length <= 15
 * 1 <= matchsticks[i] <= 108
 *
 *
 */
public class MatchsticksToSquare {

    // V0
//    public boolean makesquare(int[] matchsticks) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=hUe0cUKV-YY
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0473-matchsticks-to-square.java
    boolean[] used;
    public boolean makesquare_1(int[] matchsticks) {
        used = new boolean[matchsticks.length];
        int total = 0;
        for (int n : matchsticks) {
            total += n;
        }
        //Check if total of all the sides is divisible by 4 or not
        if (total % 4 != 0) return false;
        int side = total / 4;

        return helper(matchsticks, side, 0, 0, 4);
    }

    boolean helper(int[] matchsticks, int targetSide, int currentSum, int index, int sides) {
        //if all the sides are matching the target side length then we found a solution
        if (sides == 0)
            return true;
        //Check if current side is equal to targetSide , that means we found another side
        if (currentSum == targetSide) {
            return helper(matchsticks, targetSide, 0, 0, sides - 1);
        }

        for (int i = index; i < matchsticks.length; i++) {
            //Only use matchsticks which are not used and which doesn't increase the current side more than target side
            if (!used[i] && currentSum + matchsticks[i] <= targetSide) {
                used[i] = true;
                boolean found = helper(matchsticks, targetSide, currentSum + matchsticks[i], i + 1, sides);
                if (found) {
                    return true;
                }
                used[i] = false;
            }
        }
        return false;
    }


    // V2-1
    // https://leetcode.com/problems/matchsticks-to-square/editorial/
    // IDEA: DFS
    public List<Integer> nums;
    public int[] sums;
    public int possibleSquareSide;

    // Depth First Search function.
    public boolean dfs(int index) {

        // If we have exhausted all our matchsticks, check if all sides of the square are of equal length
        if (index == this.nums.size()) {
            return sums[0] == sums[1] && sums[1] == sums[2] && sums[2] == sums[3];
        }

        // Get current matchstick.
        int element = this.nums.get(index);

        // Try adding it to each of the 4 sides (if possible)
        for(int i = 0; i < 4; i++) {
            if (this.sums[i] + element <= this.possibleSquareSide) {
                this.sums[i] += element;
                if (this.dfs(index + 1)) {
                    return true;
                }
                this.sums[i] -= element;
            }
        }

        return false;
    }

    public boolean makesquare_2_1(int[] nums) {
        // Empty matchsticks.
        if (nums == null || nums.length == 0) {
            return false;
        }

        // Find the perimeter of the square (if at all possible)
        int L = nums.length;
        int perimeter = 0;
        for(int i = 0; i < L; i++) {
            perimeter += nums[i];
        }

        this.possibleSquareSide =  perimeter / 4;
        if (this.possibleSquareSide * 4 != perimeter) {
            return false;
        }

        // Convert the array of primitive int to ArrayList (for sorting).
        this.nums = Arrays.stream(nums).boxed().collect(Collectors.toList());
        Collections.sort(this.nums, Collections.reverseOrder());
        return this.dfs(0);
    }


    // V2-2
    // https://leetcode.com/problems/matchsticks-to-square/editorial/
    // IDEA: DP
//
//    // The memoization cache to be used during recursion.
//    public HashMap<Pair<Integer, Integer>, Boolean> memo;
//
//    // Array containing our matchsticks.
//    public int[] nums;
//
//    // Possible side of our square depending on the total sum of all the matchsticks. 
//    public int possibleSquareSide;
//
//    // Main DP function.
//    public boolean recurse(Integer mask, Integer sidesDone) {
//        int total = 0;
//        int L = this.nums.length;
//
//        // The memo key for this recursion
//        Pair<Integer, Integer> memoKey = new Pair(mask, sidesDone);
//
//        // Find out the sum of matchsticks used till now.
//        for (int i = L - 1; i >= 0; i--) {
//            if ((mask & (1 << i)) == 0) {
//                total += this.nums[L - 1 - i];
//            }
//        }
//
//        // If the sum if divisible by our square's side, then we increment our number of complete sides formed variable.
//        if (total > 0 && total % this.possibleSquareSide == 0) {
//            sidesDone++;
//        }
//
//        // Base case.
//        if (sidesDone == 3) {
//            return true;
//        }
//
//        // Return precomputed results
//        if (this.memo.containsKey(memoKey)) {
//            return this.memo.get(memoKey);
//        }
//
//        boolean ans = false;
//        int c = total / this.possibleSquareSide;
//
//        // Remaining vlength in the current partially formed side.
//        int rem = this.possibleSquareSide * (c + 1) - total;
//
//        // Try out all remaining options (that are valid)
//        for (int i = L - 1; i >= 0; i--) {
//            if (this.nums[L - 1 - i] <= rem && (mask & (1 << i)) > 0) {
//                if (this.recurse(mask ^ (1 << i), sidesDone)) {
//                    ans = true;
//                    break;
//                }
//            }
//        }
//
//        // Cache the computed results.
//        this.memo.put(memoKey, ans);
//        return ans;
//    }
//
//    public boolean makesquare_2_2(int[] nums) {
//
//        // Empty matchsticks.
//        if (nums == null || nums.length == 0) {
//            return false;
//        }
//
//        // Find the perimeter of the square (if at all possible)
//        int L = nums.length;
//        int perimeter = 0;
//        for (int i = 0; i < L; i++) {
//            perimeter += nums[i];
//        }
//
//        int possibleSquareSide = perimeter / 4;
//        if (possibleSquareSide * 4 != perimeter) {
//            return false;
//        }
//
//        this.nums = nums;
//        this.possibleSquareSide = possibleSquareSide;
//        return this.recurse((1 << L) - 1, 0);
//    }



}
