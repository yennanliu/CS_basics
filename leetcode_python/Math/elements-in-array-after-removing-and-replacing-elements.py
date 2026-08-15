"""

2113. Elements in Array After Removing and Replacing Elements
Medium
🔒 (premium)

You are given a 0-indexed integer array nums. Initially on minute 0, the array is unchanged. Every minute, the leftmost element in nums is removed until no elements remain. Then, every minute, one element is appended to the end of nums, in the order they were removed in, until the original array is restored. This process repeats indefinitely.

For example, the array [0,1,2] would change as follows: [0,1,2] -> [1,2] -> [2] -> [] -> [0] -> [0,1] -> [0,1,2] -> [1,2] -> [2] -> [] -> [0] -> [0,1] -> [0,1,2] -> ...

You are also given a 2D integer array queries of size n where queries[j] = [timej, indexj]. The answer to the jth query is:

nums[indexj] if indexj < nums.length at minute timej
-1 if indexj >= nums.length at minute timej

Return an integer array ans of size n where ans[j] is the answer to the jth query.


Example 1:

Input: nums = [0,1,2], queries = [[0,2],[2,0],[3,2],[5,0]]
Output: [2,2,-1,0]
Explanation:
Minute 0: [0,1,2] - All elements are in the nums.
Minute 1: [1,2]   - The leftmost element, 0, is removed.
Minute 2: [2]     - The leftmost element, 1, is removed.
Minute 3: []      - The leftmost element, 2, is removed.
Minute 4: [0]     - 0 is added to the end of nums.
Minute 5: [0,1]   - 1 is added to the end of nums.

At minute 0, nums[2] is 2.
At minute 2, nums[0] is 2.
At minute 3, nums[2] does not exist.
At minute 5, nums[0] is 0.

Example 2:

Input: nums = [2], queries = [[0,0],[1,0],[2,0]]
Output: [2,-1,2]
Explanation:
Minute 0: [2] - All elements are in the nums.
Minute 1: []  - The leftmost element, 2, is removed.
Minute 2: [2] - 2 is added to the end of nums.


Constraints:

1 <= nums.length <= 100
0 <= nums[i] <= 100
n == queries.length
1 <= n <= 10^5
queries[j].length == 2
0 <= timej <= 10^5
0 <= indexj < nums.length

"""

# V0
# IDEA : THE STATE IS PERIODIC WITH PERIOD 2n — ANSWER EACH QUERY IN O(1)
#
#   n minutes to empty the array, n more to refill it, so minute t behaves
#   exactly like minute  t % (2n).  let  m = t % (2n) :
#
#       m <  n  ->  SHRINKING phase, the array is nums[m:]
#                   so length = n - m and position i holds nums[m + i]
#       m >= n  ->  GROWING phase with r = m - n elements back,
#                   the array is nums[:r]
#                   so length = r and position i holds nums[i]
#
#   in both phases an index at or past the current length answers -1.
#
#   NOTE : no simulation is needed — 10^5 queries each cost O(1).
#
# time = O(n + q), space = O(q)
class Solution(object):
    def elementInNums(self, nums, queries):
        n = len(nums)
        res = []
        for t, i in queries:
            m = t % (2 * n)
            if m < n:                       # shrinking : nums[m:]
                res.append(nums[m + i] if i < n - m else -1)
            else:                           # growing : nums[:m-n]
                res.append(nums[i] if i < m - n else -1)
        return res
