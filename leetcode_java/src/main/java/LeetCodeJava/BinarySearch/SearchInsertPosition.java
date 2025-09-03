package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-insert-position/description/
/**
 * 35. Search Insert Position
 * Solved
 * Easy
 * Topics
 * Companies
 * Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order.
 *
 * You must write an algorithm with O(log n) runtime complexity.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5,6], target = 5
 * Output: 2
 * Example 2:
 *
 * Input: nums = [1,3,5,6], target = 2
 * Output: 1
 * Example 3:
 *
 * Input: nums = [1,3,5,6], target = 7
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -104 <= nums[i] <= 104
 * nums contains distinct values sorted in ascending order.
 * -104 <= target <= 104
 *
 *
 */
public class SearchInsertPosition {

    // V0
    // IDEA: BINARY SEARCH ( r >= l)
    public int searchInsert(int[] nums, int target) {
        // edge
        if (nums == null) {
            return 0; // ??
        }
        if (nums.length == 1) {
            if (target > nums[0]) {
                return 1;
            }
            return 0;
        }
        if (target < nums[0]) {
            return 0;
        }
        if (target > nums[nums.length - 1]) {
            return nums.length;
        }

        // binary search
        int l = 0;
        int r = nums.length - 1;
        // r >= l
        while (r >= l) {
            int mid = (r + l) / 2;
            int val = nums[mid];
           //System.out.println(">>> l = " + l + ", r = " + r + ", mid = " + mid);
            /**
             *  CASE 1)  val == target
             */
            if (val == target) {
                return mid;
            }
            /**
             *  CASE 2)  target NOT in nums AND is in `mid + 1 ` index
             */
            // if target NOT in nums
            // and is in `mid + 1 ` index
            else if (mid + 1 <= nums.length - 1 && target < nums[mid + 1] && target > nums[mid]) {
                return mid + 1;
            }
            /**
             *  CASE 3)  target NOT in nums AND is in `mid - 1 ` index
             */
            // if target NOT in nums
            // and is in `mid - 1 ` index
            else if (mid - 1 >= 0 && target < nums[mid] && target > nums[mid - 1]) {
                return mid;
            }
            /**
             *  CASE 3)  val is TOO small
             */
            else if (val < target) {
                l = mid + 1;
            }
            /**
             *  CASE 4)  val is TOO big
             */
            else {
                r = mid - 1;
            }
        }

        return -1; // should NOT visit here
    }

    // V0-1
    // IDEA: BINARY SEARCH ( r >= l) (GPT)
    /**
     *  IDEA
     *
     *  🟡 How standard binary search behaves
     *
     * Let’s take an example:
     *
     * nums = [1, 3, 5, 6], target = 4.
     * 	1.	Start
     *    l=0, r=3
     *    mid=1, nums[1]=3
     *    target=4 > 3 → l = mid+1 = 2
     *
     * 	2.	Second step
     *    l=2, r=3
     *    mid=2, nums[2]=5
     *    target=4 < 5 → r = mid-1 = 1
     *
     * 	3.	Loop ends
     *    l=2, r=1.
     *
     *  -> Notice: l points exactly to where 4 should go (between 3 and 5).
     *
     *  -> So, we just return l = 2.
     *
     * ✅ Correct without needing special “between mid and mid+1” conditions.
     *
     */
    public int searchInsert_0_1(int[] nums, int target) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int l = 0;
        int r = nums.length - 1;

        while (l <= r) {
            int mid = l + (r - l) / 2; // avoid overflow
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }

        /**
         *  NOTE !!!!
         */
        // if not found, l is the correct insert position
        /**
         * 🟣 Why is this guaranteed?
         * 	  - Binary search shrinks the search range until l > r.
         * 	  - By construction:
         * 	      - nums[0..l-1] < target
         * 	      - nums[l..end] >= target
         * 	  - So when the loop ends, l is either:
         * 	      - The index where the target was found, or
         * 	      - The index where it should be inserted.
         */
        return l;
    }

    // V1

    // V2
    // https://leetcode.com/problems/search-insert-position/solutions/5361984/video-return-middle-or-left-pointer-by-n-dj1y/
    public int searchInsert_2(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    // V3
    // https://leetcode.com/problems/search-insert-position/solutions/6592447/runtime-0-ms-beats-10000-by-deepakkr_567-x8es/
    public int searchInsert_3(int[] nums, int target) {
        int st = 0;
        int end = nums.length - 1;
        boolean find = false;

        while (st <= end) {
            int mid = (st + end) / 2;

            if (nums[mid] == target) {
                find = true;
                return mid;
            } else if (nums[mid] < target)
                st = mid + 1;
            else
                end = mid - 1;
        }
        if (find == false) {
            for (int i = nums.length - 1; i >= 0; i--) {
                if (target > nums[i])
                    return i + 1;
                else if (i == 0) {
                    return i;
                }
            }
        }

        return -1;//Unreachable statement
    }

}
