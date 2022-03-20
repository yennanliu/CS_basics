"""

2009. Minimum Number of Operations to Make Array Continuous

# https://leetcode-cn.com/problems/minimum-number-of-operations-to-make-array-continuous/

Hard

You are given an integer array nums. In one operation, you can replace any element in nums with any integer.

nums is considered continuous if both of the following conditions are fulfilled:

All elements in nums are unique.
The difference between the maximum element and the minimum element in nums equals nums.length - 1.
For example, nums = [4, 2, 5, 3] is continuous, but nums = [1, 2, 3, 5, 6] is not continuous.

Return the minimum number of operations to make nums continuous.

 

Example 1:

Input: nums = [4,2,5,3]
Output: 0
Explanation: nums is already continuous.
Example 2:

Input: nums = [1,2,3,5,6]
Output: 1
Explanation: One possible solution is to change the last element to 4.
The resulting array is [1,2,3,5,4], which is continuous.
Example 3:

Input: nums = [1,10,100,1000]
Output: 3
Explanation: One possible solution is to:
- Change the second element to 2.
- Change the third element to 3.
- Change the fourth element to 4.
The resulting array is [1,2,3,4], which is continuous.
 

Constraints:

1 <= nums.length <= 105
1 <= nums[i] <= 109

"""

# V0

# V1
# IDEA : deque
# https://leetcode.com/problems/minimum-number-of-operations-to-make-array-continuous/discuss/1481315/Python-deque-linear
class Solution:
    def minOperations(self, nums: List[int]) -> int:      
        uniq = list(set(nums))
        q=deque()
        n=len(nums)
        ans=n-1
        uniq.sort()
        for x in uniq:
            while q and x-q[0]>=n:
                q.popleft()
            q.append(x)
            ans=min(ans, n-len(q))
        return ans

# V1'
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/minimum-number-of-operations-to-make-array-continuous/discuss/1470900/Python-Explanation-with-Pictures-Binary-Search
class Solution(object):
    def minOperations(self, A: List[int]) -> int:
            A.sort()
            n = len(A)
            ans = n - 1
            uniq, cur = [1], 1

            for i in range(1, n):
                if A[i] != A[i - 1]: 
                    cur += 1
                uniq.append(cur)

            for i in range(n):
                a = A[i]
                idx = bisect.bisect_right(A, a + n - 1)
                cur_uniq = max(0, uniq[idx - 1] - uniq[i]) + 1
                ans = min(ans, n - cur_uniq)

            return ans

# V1''
# https://zxi.mytechroad.com/blog/sliding-window/leetcode-2009-minimum-number-of-operations-to-make-array-continuous/
# C++
# class Solution {
# public:
#   int minOperations(vector<int>& A) {
#     const int n = A.size();
#     sort(begin(A), end(A));
#     A.erase(unique(begin(A), end(A)), end(A));    
#     int ans = INT_MAX;
#     for (int i = 0, j = 0, m = A.size(); i < m; ++i) {
#       while (j < m && A[j] < A[i] + n) ++j;
#       ans = min(ans, n - (j - i));
#     }
#     return ans;
#   }
# };

# V1'''
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/minimum-number-of-operations-to-make-array-continuous/discuss/1470853/Python-Binary-Search-Clean-and-Concise
class Solution:
    def minOperations(self, nums: List[int]) -> int:
        n = len(nums)
        nums = sorted(set(nums))  # Make `nums` as unique numbers and sort `nums`.

        ans = n
        for i, start in enumerate(nums):
            end = start + n - 1  # We expect elements of continuous array must in range [start..end]
            idx = bisect_right(nums, end)  # Find right insert position
            uniqueLen = idx - i
            ans = min(ans, n - uniqueLen)
        return ans

# V1''''
# IDEA : SLIDING WINDOW
# https://leetcode.com/problems/minimum-number-of-operations-to-make-array-continuous/discuss/1690374/Pythonorslide-window
class Solution(object):
    def minOperations(self, nums):
        """
        :type nums: List[int]
        :rtype: int

        1, 2, 3, 5, 6
        
         1. sort
         2. get max continuous subarray, m
         3. n - m
        """
        n = len(nums)
        uniq_nums = list(set(nums))
        uniq_nums.sort()

        start = 0
        max_continusous = 0
        for end in range(len(uniq_nums)):
            while end - max_continusous >= 0 and uniq_nums[end] - uniq_nums[max(0, end - max_continusous)] <= n - 1:
                max_continusous += 1
        return n - max_continusous

# V1'''''
class Solution:
	def minOperations(self, nums: List[int]) -> int:
		# use binary search using bisect_right
		# Time O(nlogn), Space O(n)
		n = len(nums)
		nums = sorted(set(nums))
		result = sys.maxsize

		for i, start in enumerate(nums):
			end = start+n-1
			# get the next insertion point for end to maintain sorted order
			idx = bisect_right(nums, end)

			# (idx-i) will be the nums that do not need to change 
			result = min(result, n-(idx-i))

		return result

# V2