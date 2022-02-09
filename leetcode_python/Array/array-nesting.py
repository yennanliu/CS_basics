"""

565. Array Nesting
Medium

You are given an integer array nums of length n where nums is a permutation of the numbers in the range [0, n - 1].

You should build a set s[k] = {nums[k], nums[nums[k]], nums[nums[nums[k]]], ... } subjected to the following rule:

The first element in s[k] starts with the selection of the element nums[k] of index = k.
The next element in s[k] should be nums[nums[k]], and then nums[nums[nums[k]]], and so on.
We stop adding right before a duplicate element occurs in s[k].
Return the longest length of a set s[k].

 

Example 1:

Input: nums = [5,4,0,3,1,6,2]
Output: 4
Explanation: 
nums[0] = 5, nums[1] = 4, nums[2] = 0, nums[3] = 3, nums[4] = 1, nums[5] = 6, nums[6] = 2.
One of the longest sets s[k]:
s[0] = {nums[0], nums[5], nums[6], nums[2]} = {5, 6, 2, 0}
Example 2:

Input: nums = [0,1,2]
Output: 1
 

Constraints:

1 <= nums.length <= 105
0 <= nums[i] < nums.length
All the values of nums are unique.

"""

# V0
# IDEA : UNION FIND
class Solution(object):
    def arrayNesting(self, nums):
        def search(idx):
            cnt = 0
            while nums[idx] >= 0:
                cnt += 1
                next = nums[idx]
                nums[idx] = -1
                idx = next
            return cnt
        ans = 0
        for x in range(len(nums)):
            if nums[x] >= 0:
                ans = max(ans, search(x))
        return ans

# V1
# IDEA : UNION FIND
# http://bookshadow.com/weblog/2017/05/28/leetcode-array-nesting/
class Solution(object):
    def arrayNesting(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        def search(idx):
            cnt = 0
            while nums[idx] >= 0:
                cnt += 1
                next = nums[idx]
                nums[idx] = -1
                idx = next
            return cnt
        ans = 0
        for x in range(len(nums)):
            if nums[x] >= 0:
                ans = max(ans, search(x))
        return ans

# V1'
# IDEA : UNION FIND
# https://blog.csdn.net/fuxuemingzhu/article/details/79460546
class Solution(object):
    def arrayNesting(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        visited = [False] * len(nums)
        ans = 0
        for i in range(len(nums)):
            road = 0
            while not visited[i]:
                road += 1
                # order of below 2 lines of code is unchangeable
                visited[i] = True
                i = nums[i]
            ans = max(ans, road)
        return ans

# V1''
# IDEA : UNION FIND
# https://leetcode.com/problems/array-nesting/discuss/209108/Python-solution
class Solution(object):
    def arrayNesting(self, nums):
        seen = [0]*len(nums)
        res = 0
        for i in range(len(nums)):
            if seen[i] != -1:
                j = i
                count = 0
                while seen[j] != -1:
                    count += 1
                    seen[j] = -1
                    j = nums[j]
                res = max(res, count)
        return res

# V1'''
# IDEA : optimzied (we don't need extra space for seen, just make nums[idx] as -1)
# https://leetcode.com/problems/array-nesting/discuss/209108/Python-solution
class Solution(object):
    def arrayNesting(self, nums):
        res = 0
        for i in range(len(nums)):
            if nums[i] != -1:
                j = i
                count = 0
                while nums[j] != -1:
                    count += 1
                    tmp = nums[j]
                    nums[j] = -1
                    j = tmp
                res = max(res, count)
        return res

# V1''''
# IDEA : UNION FIND
# https://leetcode.com/problems/array-nesting/discuss/1439139/AA-Python-solution
class Solution:
    def arrayNesting(self, nums):
        res = 0
        seen = set()
        for i in range(len(nums)):
            if nums[i] not in seen:
                tmp = self.build(nums, i)
                seen |= tmp
                res = max(res, len(tmp))
        return res     
        
    def build(self, nums, k):
        res = set()
        current = nums[k]
        while current not in res:
            res.add(current)
            current = nums[current]
        return res

# V1'''''
# https://leetcode.com/problems/array-nesting/discuss/314782/python
class Solution:
    def arrayNesting(self, nums):
        res = 0
        visited = [0] * len(nums)
        for i in range(len(nums)):
            cur, cur_res = i, 0
            while not visited[cur]:
                visited[cur] = 1
                cur_res += 1
                cur = nums[cur]
            res = max(res, cur_res)
        return res

# V1''''''
# IDEA : BRUTE FORCE (time out error)
# https://leetcode.com/problems/array-nesting/solution/
# JAVA
# public class Solution {
#     public int arrayNesting(int[] nums) {
#         int res = 0;
#         for (int i = 0; i < nums.length; i++) {
#             int start = nums[i], count = 0;
#             do {
#                 start = nums[start];
#                 count++;
#             }
#             while (start != nums[i]);
#             res = Math.max(res, count);
#
#         }
#         return res;
#     }
# }

# V1''''''
# IDEA : VISITED ARRAY
# JAVA
# https://leetcode.com/problems/array-nesting/solution/
# public class Solution {
#     public int arrayNesting(int[] nums) {
#         boolean[] visited = new boolean[nums.length];
#         int res = 0;
#         for (int i = 0; i < nums.length; i++) {
#             if (!visited[i]) {
#                 int start = nums[i], count = 0;
#                 do {
#                     start = nums[start];
#                     count++;
#                     visited[start] = true;
#                 }
#                 while (start != nums[i]);
#                 res = Math.max(res, count);
#             }
#         }
#         return res;
#     }
# }

# V1''''''''
# IDEA : VISITED ARRAY WITHOUT EXTRA SPACE
# https://leetcode.com/problems/array-nesting/solution/
# JAVA
# public class Solution {
#     public int arrayNesting(int[] nums) {
#         int res = 0;
#         for (int i = 0; i < nums.length; i++) {
#             if (nums[i] != Integer.MAX_VALUE) {
#                 int start = nums[i], count = 0;
#                 while (nums[start] != Integer.MAX_VALUE) {
#                     int temp = start;
#                     start = nums[start];
#                     count++;
#                     nums[temp] = Integer.MAX_VALUE;
#                 }
#                 res = Math.max(res, count);
#             }
#         }
#         return res;
#     }
# }

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def arrayNesting(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result = 0
        for num in nums:
            if num is not None:
                start, count = num, 0
                while nums[start] is not None:
                    temp = start
                    start = nums[start]
                    nums[temp] = None
                    count += 1
                result = max(result, count)
        return result