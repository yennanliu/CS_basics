package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/longest-increasing-subsequence/description/
/**
 * 300. Longest Increasing Subsequence
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, return the length of the longest strictly increasing
 * subsequence
 * .
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
 * Example 2:
 *
 * Input: nums = [0,1,0,3,2,3]
 * Output: 4
 * Example 3:
 *
 * Input: nums = [7,7,7,7,7,7,7]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2500
 * -104 <= nums[i] <= 104
 *
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongestIncreasingSubsequence {

    // V0
    // IDEA : DP
    // TODO : check & implement again
    public int lengthOfLIS(int[] nums) {

        if(nums == null || nums.length == 0){
            return 0;
        }

        int n = nums.length;
        // init dp
        int[] dp = new int[nums.length];
        /**
         *
         * NOTE !!!
         *
         * Arrays.fill(dp, 1);
         * -> sets every element in the array dp to the value 1.
         *
         */
        Arrays.fill(dp,1);
        // set res as final result (Longest Increasing Subsequence length) , keep updating it
        int res = 1;
        /**
         * NOTE !!!
         *
         *  main dp logic : dp[i]=Math.max(dp[i],dp[j]+1);
         *
         *  -> we still use "brute force", double looping over i, j
         *  However, we use dp to "memorize" the result already calculated before
         *
         *
         *  i start from 1, while j start from 0
         */
        for(int i=1; i<n; i++){
            for(int j=0; j<i; j++){
                /**
                 * NOTE !!!
                 *  `nums[i] > nums[j]` condition
                 *  -> ONLY if `right element is bigger than left element`, new length is calculated and DP array is updated
                 */
                if(nums[i] > nums[j]){
                    dp[i] = Math.max(dp[i],dp[j]+1);
                    res = Math.max(res, dp[i]);
                }
            }
        }
        return res;
    }

    // V0-1
    // IDEA: BRUTE FORCE + BINARY SEARCH (GPT)
    /**
     *  Explanation:
     *
     *  - Tails List: This list will store the smallest possible tail value for
     *          increasing subsequences of different lengths.
     *
     *  - Binary Search: For each element in nums, we use binary search to find the
     *                   correct position in the tails list where the element should go.
     *
     *  - Update or Append: If the element is larger than all elements in tails,
     *                      we append it. Otherwise, we replace the element in the list at the
     *                      found position to maintain the smallest possible tail value for subsequences.
     *
     *  - Final Result: The length of the tails list at the end is the length of the longest
     *                  increasing subsequence.
     *
     * Time Complexity:
     *  Binary Search: For each element in the array, we perform binary search on the tails list,
     *  which takes O(log n) time.
     *
     * Overall Complexity:
     *   Since we perform this for all n elements in nums, the total time complexity is O(n log n).
     *   This is a much more efficient solution than the classic dynamic programming approach
     *   with O(n^2) time complexity.
     *
     *
     */
    public int lengthOfLIS_0_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            int left = 0, right = tails.size();

            // Use binary search to find the right position for num
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // If left is equal to the size of tails, it means the current number
            // is larger than all the numbers in tails
            if (left == tails.size()) {
                tails.add(num);
            } else {
                tails.set(left, num);
            }
        }

        return tails.size();
    }

    // V1-1
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA: RECURSION
    public int lengthOfLIS_1_1(int[] nums) {
        return dfs(nums, 0, -1);
    }

    private int dfs(int[] nums, int i, int j) {
        if (i == nums.length) {
            return 0;
        }

        int LIS = dfs(nums, i + 1, j); // not include

        if (j == -1 || nums[j] < nums[i]) {
            LIS = Math.max(LIS, 1 + dfs(nums, i + 1, i)); // include
        }

        return LIS;
    }

    // V1-2
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA: Dynamic Programming (Top-Down)
    private int[][] memo;

    private int dfs(int i, int j, int[] nums) {
        if (i == nums.length) {
            return 0;
        }
        if (memo[i][j + 1] != -1) {
            return memo[i][j + 1];
        }

        int LIS = dfs(i + 1, j, nums);

        if (j == -1 || nums[j] < nums[i]) {
            LIS = Math.max(LIS, 1 + dfs(i + 1, i, nums));
        }

        memo[i][j + 1] = LIS;
        return LIS;
    }

    public int lengthOfLIS_1_2(int[] nums) {
        int n = nums.length;
        memo = new int[n][n + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return dfs(0, -1, nums);
    }

    // V1-3
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA: Dynamic Programming (Bottom-Up)
    public int lengthOfLIS_1_3(int[] nums) {
        int[] LIS = new int[nums.length];
        Arrays.fill(LIS, 1);

        for (int i = nums.length - 1; i >= 0; i--) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] < nums[j]) {
                    LIS[i] = Math.max(LIS[i], 1 + LIS[j]);
                }
            }
        }
        return Arrays.stream(LIS).max().getAsInt();
    }

    // V1-4
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA:  Segment Tree
    public class SegmentTree {
        int n;
        int[] tree;

        public SegmentTree(int N) {
            n = N;
            while ((n & (n - 1)) != 0) {
                n++;
            }
            tree = new int[2 * n];
        }

        void update(int i, int val) {
            tree[n + i] = val;
            int j = (n + i) >> 1;
            while (j >= 1) {
                tree[j] = Math.max(tree[j << 1], tree[j << 1 | 1]);
                j >>= 1;
            }
        }

        int query(int l, int r) {
            if (l > r) {
                return 0;
            }
            int res = Integer.MIN_VALUE;
            l += n;
            r += n + 1;
            while (l < r) {
                if ((l & 1) != 0) {
                    res = Math.max(res, tree[l]);
                    l++;
                }
                if ((r & 1) != 0) {
                    r--;
                    res = Math.max(res, tree[r]);
                }
                l >>= 1;
                r >>= 1;
            }
            return res;
        }
    }

    public int lengthOfLIS_1_4(int[] nums) {
        int[] sortedArr = Arrays.stream(nums).distinct().sorted().toArray();
        int[] order = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            order[i] = Arrays.binarySearch(sortedArr, nums[i]);
        }
        int n = sortedArr.length;
        SegmentTree segTree = new SegmentTree(n);

        int LIS = 0;
        for (int num : order) {
            int curLIS = segTree.query(0, num - 1) + 1;
            segTree.update(num, curLIS);
            LIS = Math.max(LIS, curLIS);
        }
        return LIS;
    }


    // V1-5
    // https://neetcode.io/problems/longest-increasing-subsequence
    // IDEA: Binary Search
    public int lengthOfLIS_1_5(int[] nums) {
        List<Integer> dp = new ArrayList<>();
        dp.add(nums[0]);

        int LIS = 1;
        for (int i = 1; i < nums.length; i++) {
            if (dp.get(dp.size() - 1) < nums[i]) {
                dp.add(nums[i]);
                LIS++;
                continue;
            }

            int idx = Collections.binarySearch(dp, nums[i]);
            if (idx < 0) idx = -idx - 1;
            dp.set(idx, nums[i]);
        }

        return LIS;
    }


    // V2
    // IDEA : DP
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4509493/300/
    public int lengthOfLIS_2(int[] nums) {
        if(nums == null || nums.length == 0)return 0;
        int n=nums.length;
        int[] dp=new int[n];
        Arrays.fill(dp,1);
        for(int i=1;i<n;i++){
            for(int j=0;j<i;j++){
                if(nums[i]>nums[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }
            }
        }
        int maxi=1;
        for(int len : dp){
            maxi=Math.max(maxi,len);
        }
        return maxi;
    }

    // V3
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4509303/beats-100-binary-search-explained-with-video-c-java-python-js/
    public int lengthOfLIS_3(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int i = 0, j = size;
            while (i != j) {
                int m = (i + j) / 2;
                if (tails[m] < x)
                    i = m + 1;
                else
                    j = m;
            }
            tails[i] = x;
            if (i == size) ++size;
        }
        return size;
    }

    // V4
    // IDEA : DP
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4510776/java-solution-explained-in-hindi/
    public int lengthOfLIS_4(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        int maxLength = 0;
        for (int len : dp) {
            maxLength = Math.max(maxLength, len);
        }
        return maxLength;
    }

}
