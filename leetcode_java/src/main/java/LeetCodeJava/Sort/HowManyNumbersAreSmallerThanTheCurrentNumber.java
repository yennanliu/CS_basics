package LeetCodeJava.Sort;

// https://leetcode.com/problems/how-many-numbers-are-smaller-than-the-current-number/

/**
 *  1365. How Many Numbers Are Smaller Than the Current Number
 *  Easy
 *
 *  Given the array nums, for each nums[i] find out how many numbers in the array
 *  are smaller than it. That is, for each nums[i] you have to count the number of
 *  valid j's such that j != i and nums[j] < nums[i].
 *
 *  Return the answer in an array.
 *
 *  Example 1:
 *    Input: nums = [8,1,2,2,3]
 *    Output: [4,0,1,1,3]
 *    Explanation:
 *      For nums[0]=8 there exist four smaller numbers than it (1, 2, 2 and 3).
 *      For nums[1]=1 does not exist any smaller number than it.
 *      For nums[2]=2 there exist one smaller number than it (1).
 *      For nums[3]=2 there exist one smaller number than it (1).
 *      For nums[4]=3 there exist three smaller numbers than it (1, 2 and 2).
 *
 *  Example 2:
 *    Input: nums = [6,5,4,8]
 *    Output: [2,1,0,3]
 *
 *  Example 3:
 *    Input: nums = [7,7,7,7]
 *    Output: [0,0,0,0]
 *
 *  Constraints:
 *    2 <= nums.length <= 500
 *    0 <= nums[i] <= 100
 */
public class HowManyNumbersAreSmallerThanTheCurrentNumber {

    // V0
    // IDEA: COUNTING SORT + PREFIX SUM
    //       values are bounded by 0 <= nums[i] <= 100, so bucket count them and
    //       prefix-sum: prefix[x] = number of elements STRICTLY smaller than x.
    /**
     * time = O(N + M)     // M = 101 distinct values
     * space = O(M)
     */
    public int[] smallerNumbersThanCurrent(int[] nums) {
        // cnt[v] = how many elements equal v
        int[] cnt = new int[101];
        for (int x : nums) {
            cnt[x]++;
        }

        // smaller[v] = how many elements are < v
        int[] smaller = new int[102];
        for (int v = 1; v <= 101; v++) {
            smaller[v] = smaller[v - 1] + cnt[v - 1];
        }

        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = smaller[nums[i]];
        }
        return res;
    }
}
