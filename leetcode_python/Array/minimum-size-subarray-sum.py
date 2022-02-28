"""

209. Minimum Size Subarray Sum
Medium

Given an array of positive integers nums and a positive integer target, return the minimal length of a contiguous subarray [numsl, numsl+1, ..., numsr-1, numsr] of which the sum is greater than or equal to target. If there is no such subarray, return 0 instead.

 

Example 1:

Input: target = 7, nums = [2,3,1,2,4,3]
Output: 2
Explanation: The subarray [4,3] has the minimal length under the problem constraint.
Example 2:

Input: target = 4, nums = [1,4,4]
Output: 1
Example 3:

Input: target = 11, nums = [1,1,1,1,1,1,1,1]
Output: 0
 

Constraints:

1 <= target <= 109
1 <= nums.length <= 105
1 <= nums[i] <= 105
 

Follow up: If you have figured out the O(n) solution, try coding another solution of which the time complexity is O(n log(n)).

"""

# V0
# IDEA : SLIDING WINDOW : start, end
class Solution:
    def minSubArrayLen(self, s, nums):
        if nums is None or len(nums) == 0:
            return 0

        n = len(nums)
        minLength = n + 1
        sum = 0
        j = 0
        for i in range(n):
            ### NOTE the while loop condition (j < n and sum < s)
            while j < n and sum < s:
                sum += nums[j]
                # e.g. move right pointer to "1 right idx"
                j += 1
            # NOTE : we need to check if sum >= s here
            if sum >= s:
                minLength = min(minLength, j - i)

            ### NOTE : we need to get min length of sub array
            #          so once it meats the condition (sum >= s)
            #          we should update the minLength (minLength = min(minLength, j - i))
            #          and move to next i and roll back _sum (_sum -= nums[i])
            #
            # e.g. move left pointer to "1 right idx"
            sum -= nums[i]
            
        ### NOTE : if minLength == n + 1, means there is no such subarray, so return 0 instead
        if minLength == n + 1:
            return 0         
        return minLength

# V0'
# IDEA : SLIDING WINDOW : start, end
class Solution:
    def minSubArrayLen(self, s, nums):
        size = len(nums)
        start, end, _sum = 0, 0, 0
        _ans = size + 1
        while True:
            if _sum < s:
                if end >= size:
                    break
                _sum += nums[end]
                end += 1
            else:
                # NOTE here
                if start > end:
                    break
                _ans = min(end - start, _ans)
                _sum -= nums[start]
                start += 1
        ### NOTE : if ans <= size, then return ans
        #          else return 0
        if _ans <= size:
            return _ans
        return 0

# V0' : NEED TO FIX
# class Solution:
#     def minSubArrayLen(self, nums, s):
#         if nums is None or len(nums) == 0:
#             return -1
#
#         n = len(nums)
#         minLength = n + 1
#         sum = 0
#         j = 0
#         for i in range(n):
#             while j < n and sum < s:
#                 sum += nums[j]
#                 j += 1
#             if sum >= s:
#                 minLength = min(minLength, j - i)
#
#             sum -= nums[i]
#            
#         if minLength == n + 1:
#             return -1
#           
#         return minLength

# V1
# IDEA : MOVING WINDOW (START, END AS WINDOW INDEX)
# https://www.jiuzhang.com/solution/minimum-size-subarray-sum/#tag-highlight-lang-python
class Solution:
    def minSubArrayLen(self, s, nums):
        if nums is None or len(nums) == 0:
            return 0

        n = len(nums)
        minLength = n + 1
        sum = 0
        j = 0
        for i in range(n):
            while j < n and sum < s:
                sum += nums[j]
                j += 1
            if sum >= s:
                minLength = min(minLength, j - i)

            sum -= nums[i]
            
        if minLength == n + 1:
            return 0         
        return minLength

# V1 ''
# http://bookshadow.com/weblog/2015/05/12/leetcode-minimum-size-subarray-sum/
# IDEA : MOVING WINDOW (START, END AS WINDOW INDEX)
class Solution:
    # @param {integer} s
    # @param {integer[]} nums
    # @return {integer}
    def minSubArrayLen(self, s, nums):
        size = len(nums)
        start, end, sum = 0, 0, 0
        bestAns = size + 1
        while True:
            if sum < s:
                if end >= size:
                    break
                sum += nums[end]
                end += 1
            else:
                if start > end:
                    break
                bestAns = min(end - start, bestAns)
                sum -= nums[start]
                start += 1
        ### NOTE : [0, bestAns][bestAns <= size] is equal to below code
        #return [0, bestAns][bestAns <= size]
        if bestAns <= size:
            return bestAns
        return 0

# V1' 
# http://bookshadow.com/weblog/2015/05/12/leetcode-minimum-size-subarray-sum/
# IDEA : BINARY SEARCH 
class Solution:
    def minSubArrayLen(self, s, nums):
        size = len(nums)
        left, right = 0, size
        bestAns = 0
        while left <= right:
            mid = (left + right) / 2
            if self.solve(mid, s, nums):
                bestAns = mid
                right = mid - 1
            else:
                left = mid + 1
        return bestAns

    def solve(self, l, s, nums):
        sums = 0
        for x in range(len(nums)):
            sums += nums[x]
            if x >= l:
                sums -= nums[x - l]
            if sums >= s:
                return True
        return False

# V1'''
# A better brute force [Accepted]
# https://leetcode.com/problems/minimum-size-subarray-sum/solution/
# C++
# int minSubArrayLen(int s, vector<int>& nums)
# {
#     int n = nums.size();
#     if (n == 0)
#         return 0;
#     int ans = INT_MAX;
#     vector<int> sums(n);
#     sums[0] = nums[0];
#     for (int i = 1; i < n; i++)
#         sums[i] = sums[i - 1] + nums[i];
#     for (int i = 0; i < n; i++) {
#         for (int j = i; j < n; j++) {
#             int sum = sums[j] - sums[i] + nums[i];
#             if (sum >= s) {
#                 ans = min(ans, (j - i + 1));
#                 break; //Found the smallest subarray with sum>=s starting with index i, hence move to next index
#             }
#         }
#     }
#     return (ans != INT_MAX) ? ans : 0;
# }

# V1'''''
# Approach #3 Using Binary search
# https://leetcode.com/problems/minimum-size-subarray-sum/solution/
# C++
# int minSubArrayLen(int s, vector<int>& nums)
# {
#     int n = nums.size();
#     if (n == 0)
#         return 0;
#     int ans = INT_MAX;
#     vector<int> sums(n + 1, 0); //size = n+1 for easier calculations
#     //sums[0]=0 : Meaning that it is the sum of first 0 elements
#     //sums[1]=A[0] : Sum of first 1 elements
#     //ans so on...
#     for (int i = 1; i <= n; i++)
#         sums[i] = sums[i - 1] + nums[i - 1];
#     for (int i = 1; i <= n; i++) {
#         int to_find = s + sums[i - 1];
#         auto bound = lower_bound(sums.begin(), sums.end(), to_find);
#         if (bound != sums.end()) {
#             ans = min(ans, static_cast<int>(bound - (sums.begin() + i - 1)));
#         }
#     }
#     return (ans != INT_MAX) ? ans : 0;
# }

# V1'''''
# Approach #4 Using 2 pointers [Accepted]
# https://leetcode.com/problems/minimum-size-subarray-sum/solution/
# C++
# int minSubArrayLen(int s, vector<int>& nums)
# {
#     int n = nums.size();
#     int ans = INT_MAX;
#     int left = 0;
#     int sum = 0;
#     for (int i = 0; i < n; i++) {
#         sum += nums[i];
#         while (sum >= s) {
#             ans = min(ans, i + 1 - left);
#             sum -= nums[left++];
#         }
#     }
#     return (ans != INT_MAX) ? ans : 0;
# }

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param {integer} s
    # @param {integer[]} nums
    # @return {integer}
    def minSubArrayLen(self, s, nums):
        start = 0
        sum = 0
        min_size = float("inf")
        for i in range(len(nums)):
            sum += nums[i]
            while sum >= s:
                min_size = min(min_size, i - start + 1)
                sum -= nums[start]
                start += 1
        return min_size if min_size != float("inf") else 0

# Time:  O(nlogn)
# Space: O(n)
# Binary search solution.
class Solution2(object):
    # @param {integer} s
    # @param {integer[]} nums
    # @return {integer}
    def minSubArrayLen(self, s, nums):
        min_size = float("inf")
        sum_from_start = [n for n in nums]
        for i in range(len(sum_from_start) - 1):
            sum_from_start[i + 1] += sum_from_start[i]
        for i in range(len(sum_from_start)):
            end = self.binarySearch(lambda x, y: x <= y, sum_from_start, \
                                    i, len(sum_from_start), \
                                    sum_from_start[i] - nums[i] + s)
            if end < len(sum_from_start):
                min_size = min(min_size, end - i + 1)

        return min_size if min_size != float("inf") else 0

    def binarySearch(self, compare, A, start, end, target):
        while start < end:
            mid = start + (end - start) / 2
            if compare(target, A[mid]):
                end = mid
            else:
                start = mid + 1
        return start