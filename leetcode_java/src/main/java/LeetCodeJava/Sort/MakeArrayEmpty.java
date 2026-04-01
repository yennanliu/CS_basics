package LeetCodeJava.Sort;

// https://leetcode.com/problems/make-array-empty/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 Code
 Testcase
 Testcase
 Test Result
 2659. Make Array Empty
 Solved
 Hard
 Topics
 premium lock icon
 Companies
 Hint
 You are given an integer array nums containing distinct numbers, and you can perform the following operations until the array is empty:

 If the first element has the smallest value, remove it
 Otherwise, put the first element at the end of the array.
 Return an integer denoting the number of operations it takes to make nums empty.



 Example 1:

 Input: nums = [3,4,-1]
 Output: 5
 Operation	Array
 1	[4, -1, 3]
 2	[-1, 3, 4]
 3	[3, 4]
 4	[4]
 5	[]
 Example 2:

 Input: nums = [1,2,4,3]
 Output: 5
 Operation	Array
 1	[2, 4, 3]
 2	[4, 3]
 3	[3, 4]
 4	[4]
 5	[]
 Example 3:

 Input: nums = [1,2,3]
 Output: 3
 Operation	Array
 1	[2, 3]
 2	[3]
 3	[]


 Constraints:

 1 <= nums.length <= 105
 -109 <= nums[i] <= 109
 All values in nums are distinct.
 *
 *
 */
public class MakeArrayEmpty {

    // V0
//    public long countOperationsToEmptyArray(int[] nums) {
//
//    }

    // V1
    // https://leetcode.com/problems/make-array-empty/solutions/3466800/javacpython-easy-sort-solution-by-lee215-7f0t/
    public long countOperationsToEmptyArray_1(int[] A) {
        Map<Integer, Integer> pos = new HashMap<>();
        long n = A.length, res = n;
        for (int i = 0; i < n; ++i)
            pos.put(A[i], i);
        Arrays.sort(A);
        for (int i = 1; i < n; ++i)
            if (pos.get(A[i]) < pos.get(A[i - 1]))
                res += n - i;
        return res;
    }


    // V2
    // https://leetcode.com/problems/make-array-empty/solutions/3469773/day-395-easy-100-0ms-pythonjavac-explain-p9r0/
    public long countOperationsToEmptyArray_2(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int n = nums.length;
        for (int i = 0; i < n; i++)
            map.put(nums[i], i);
        Arrays.sort(nums);
        long ans = 1;
        for (int i = n - 2; i >= 0; i--) {
            ans += (map.get(nums[i]) > map.get(nums[i + 1])) ? n - i : 1;
        }
        return ans;
    }



    // V3
    // https://leetcode.com/problems/make-array-empty/solutions/3466953/simple-java-solution-by-siddhant_1602-u27x/
    public long countOperationsToEmptyArray_3(int[] nums) {
        Map<Integer, Integer> nm = new HashMap<>();
        int n = nums.length, val = 0;
        long ans = n;
        for (int i = 0; i < n; i++) {
            nm.put(nums[i], i);
        }
        Arrays.sort(nums);
        for (int i = 0; i < n; i++) {
            if (nm.get(nums[i]) < val) {
                ans += n - i;
            }
            val = nm.get(nums[i]);
        }
        return ans;
    }





}
