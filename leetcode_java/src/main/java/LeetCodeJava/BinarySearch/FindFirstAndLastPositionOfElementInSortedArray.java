package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/description/
/**
 * 34. Find First and Last Position of Element in Sorted Array
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.
 *
 * If target is not found in the array, return [-1, -1].
 *
 * You must write an algorithm with O(log n) runtime complexity.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [5,7,7,8,8,10], target = 8
 * Output: [3,4]
 * Example 2:
 *
 * Input: nums = [5,7,7,8,8,10], target = 6
 * Output: [-1,-1]
 * Example 3:
 *
 * Input: nums = [], target = 0
 * Output: [-1,-1]
 *
 *
 * Constraints:
 *
 * 0 <= nums.length <= 105
 * -109 <= nums[i] <= 109
 * nums is a non-decreasing array.
 * -109 <= target <= 109
 *
 */
public class FindFirstAndLastPositionOfElementInSortedArray {

    // V0
    // IDEA: BINARY SEARCH (fixed by gpt)
    public int[] searchRange(int[] nums, int target) {
        int[] res = new int[] { -1, -1 };
        if (nums == null || nums.length == 0)
            return res;

        int left = getLeftBound(nums, target);
        int right = getRightBound(nums, target);

        return new int[] { left, right };
    }

    private int getLeftBound(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        /**
         *  NOTE !!!
         *
         *   `r > l` binary search pattern
         */
        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] < target) {
                l = mid + 1; // move right
            } else {
                r = mid; // shrink left side
            }
        }
        return nums[l] == target ? l : -1;
    }

    private int getRightBound(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        /**
         *  NOTE !!!
         *
         *   `r > l` binary search pattern
         */
        while (l < r) {
            int mid = (l + r + 1) / 2; // bias mid to the right
            if (nums[mid] > target) {
                r = mid - 1; // shrink right side
            } else {
                l = mid; // move right
            }
        }
        return nums[r] == target ? r : -1;
    }

    // V0-0-1
    // IDEA: BINARY SEARCH + `left, right` idx extension (fixed by gpt)
    public int[] searchRange_0_0_1(int[] nums, int target) {
        int[] res = new int[] { -1, -1 };

        if (nums == null || nums.length == 0) {
            return res;
        }
        if (nums.length == 1) {
            if (nums[0] == target) {
                return new int[] { 0, 0 };
            }
            return res;
        }

        int l = 0;
        int r = nums.length - 1;

        while (r >= l) {
            int mid = (l + r) / 2;
            if (nums[mid] == target) {
                /** NOTE !!! below */
                return getRange(nums, mid, target);
            } else if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }

        return res;
    }

    private int[] getRange(int[] nums, int idx, int target) {
        int l = idx;
        int r = idx;

        /**
         *  NOTE !!!!
         *
         *   we compare `next` idx, so we're sure that when the `next` idx still fit target
         *   when we move
         *
         *   -> check `l-1` idx
         */
        while (l - 1 >= 0 && nums[l - 1] == target) {
            l--;
        }
        /**
         *  NOTE !!!!
         *
         *   we compare `next` idx, so we're sure that when the `next` idx still fit target
         *   when we move
         *
         *   -> check `r+1` idx
         */
        while (r + 1 < nums.length && nums[r + 1] == target) {
            r++;
        }

        return new int[] { l, r };
    }

    // V0-1
    // IDEA: BINARY SEARCH (fixed by gpt)
    public int[] searchRange_0_1(int[] nums, int target) {
        int[] res = new int[] { -1, -1 };
        if (nums == null || nums.length == 0) {
            return res;
        }

        // find left bound
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        if (nums[l] != target)
            return res;
        res[0] = l;

        // find right bound
        r = nums.length - 1;
        while (l < r) {
            int mid = l + (r - l) / 2 + 1; // bias to the right
            if (nums[mid] > target) {
                r = mid - 1;
            } else {
                l = mid;
            }
        }
        res[1] = r;

        return res;
    }

    // V0-2
    // IDEA: BINARY SEARCH (fixed by gpt)
    public int[] searchRange_0_2(int[] nums, int target) {
        int[] res = new int[]{-1, -1}; // Default result

        if (nums == null || nums.length == 0) {
            return res;
        }

        // Find the first occurrence of target
        int left = findBound(nums, target, true);
        if (left == -1) {
            return res; // Target not found
        }

        // Find the last occurrence of target
        int right = findBound(nums, target, false);

        return new int[]{left, right};
    }

    private int findBound(int[] nums, int target, boolean isFirst) {
        int l = 0, r = nums.length - 1;
        int bound = -1;

        while (l <= r) {
            int mid = l + (r - l) / 2;

            if (nums[mid] == target) {
                bound = mid;
                if (isFirst) {
                    r = mid - 1; // Keep searching left
                } else {
                    l = mid + 1; // Keep searching right
                }
            } else if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }

        return bound;
    }


    // V2
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/solutions/14734/easy-java-o-logn-solution/
    public int[] searchRange_2(int[] nums, int target) {
        int[] result = new int[2];

        // NOTE !!! use `findFirst` for 1st element
        result[0] = findFirst(nums, target);

        // NOTE !!! use `findLast` for 2nd element
        result[1] = findLast(nums, target);
        return result;
    }

    private int findFirst(int[] nums, int target) {
        int idx = -1;
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;

            /** NOTE !!!
             *
             * 1) nums[mid] >= target (find right boundary)
             * 2) we put equals condition below  (nums[mid] == target)
             */
            if (nums[mid] >= target) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
            if (nums[mid] == target)
                idx = mid;
        }
        return idx;
    }

    private int findLast(int[] nums, int target) {
        int idx = -1;
        int start = 0;
        int end = nums.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            /** NOTE !!!
             *
             * 1) nums[mid] <= target (find left boundary)
             * 2) we put equals condition below  (nums[mid] == target)
             */
            if (nums[mid] <= target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
            if (nums[mid] == target)
                idx = mid;
        }
        return idx;
    }

    // V3
    // IDEA : Binary Tree
    // https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/solutions/14701/a-very-simple-java-solution-with-only-one-binary-search-algorithm/
    public int[] searchRange_3(int[] A, int target) {
        int start = firstGreaterEqual(A, target);
        if (start == A.length || A[start] != target) {
            return new int[]{-1, -1};
        }
        return new int[]{start, firstGreaterEqual(A, target + 1) - 1};
    }

    //find the first number that is greater than or equal to target.
    //could return A.length if target is greater than A[A.length-1].
    //actually this is the same as lower_bound in C++ STL.
    private static int firstGreaterEqual(int[] A, int target) {
        int low = 0, high = A.length;
        while (low < high) {
            int mid = low + ((high - low) >> 1);
            //low <= mid < high
            if (A[mid] < target) {
                low = mid + 1;
            } else {
                //should not be mid-1 when A[mid]==target.
                //could be mid even if A[mid]>target because mid<high.
                high = mid;
            }
        }
        return low;
    }

    // V4
    // IDEA : binary Search
    // https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/solutions/5378191/video-binary-search-solution/
    public int[] searchRange_4(int[] nums, int target) {
        int[] result = { -1, -1 };
        int left = binarySearch(nums, target, true);
        int right = binarySearch(nums, target, false);
        result[0] = left;
        result[1] = right;
        return result;
    }

    private int binarySearch(int[] nums, int target, boolean isSearchingLeft) {
        int left = 0;
        int right = nums.length - 1;
        int idx = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                idx = mid;
                if (isSearchingLeft) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        return idx;
    }

    // V5
    // IDEA : binary Search (gpt)
    public int[] searchRange_5(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }

        int start = findBoundary(nums, target, true);  // Find the left boundary
        if (start == -1) {
            return new int[]{-1, -1}; // Target not found
        }

        int end = findBoundary(nums, target, false); // Find the right boundary

        return new int[]{start, end};
    }

    private int findBoundary(int[] nums, int target, boolean findStart) {
        int left = 0;
        int right = nums.length - 1;
        int boundary = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                boundary = mid;
                if (findStart) {
                    right = mid - 1; // Narrow down to the left side
                } else {
                    left = mid + 1; // Narrow down to the right side
                }
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return boundary;
    }
    

}
