package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/

import java.util.Arrays;
import java.util.HashMap;

public class TwoSumIIArrayInputIsSorted {

    // V0
    // IDEA : 2 POINTERS // NOTE !!! we use 2 pointers instead of binary search
    class Solution {
        public int[] twoSum(int[] numbers, int target) {

            if (numbers.length == 0 || numbers.equals(null)) {
                return null;
            }

            int left = 0;
            int right = numbers.length - 1;
            int[] ans = new int[2];
            //System.out.println("numbers = " + Arrays.toString(numbers));
            while (right >= left) {
                int cur = numbers[left] + numbers[right];
                if (cur == target) {
                    ans[0] = left + 1;
                    ans[1] = right + 1;
                    return ans;
                } else if (cur > target) {
                    //right = mid;
                    right -= 1;
                } else {
                    //left = mid + 1;
                    left += 1;
                }
            }

            ans[0] = -1;
            ans[1] = -1;
            return ans;
        }
    }

}
