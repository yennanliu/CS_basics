"""

560. Subarray Sum Equals K
Medium

10533

343

Add to List

Share
Given an array of integers nums and an integer k, return the total number of continuous subarrays whose sum equals to k.

 

Example 1:

Input: nums = [1,1,1], k = 2
Output: 2
Example 2:

Input: nums = [1,2,3], k = 3
Output: 2
 

Constraints:

1 <= nums.length <= 2 * 104
-1000 <= nums[i] <= 1000
-107 <= k <= 107

"""

# V0
# IDEA : HASH TABLE + sub array sum
# IDEA : https://blog.csdn.net/fuxuemingzhu/article/details/82767119
class Solution(object):
    def subarraySum(self, nums, k):
        n = len(nums)
        d = collections.defaultdict(int)
        d[0] = 1
        sum = 0
        res = 0
        for i in range(n):
            sum += nums[i]
            """
            NOTE this !!!
              if sum - k in d
              -> sum - (sum - k) == k      (exclude "sum - k" term from aggregated sum)
              -> so the sum equal to k
            """
            if sum - k in d:
                res += d[sum - k]
            d[sum] += 1
        return res

# V0'
# IDEA : HASH TABLE + sub array sum
class Solution:
    def subarraySum(self, nums, k):
        # write your code here
        for i in range(1, len(nums)):
            nums[i] += nums[i - 1]
        print ("nums = " + str(nums))
        d = {0:1}
        ans = 0
        for i in range(len(nums)):
            # check sub array equals k
            if(d.get(nums[i] - k) != None):
                ans += d[nums[i] - k]
            # update dict
            if nums[i] not in d:
                d[nums[i]] = 1
            else:
                d[nums[i]] += 1
        return ans

# V0'
# -> TIME OUT ERROR
# class Solution(object):
#     def subarraySum(self, nums, k):
#         pre_sum = [0] * (len(nums) + 1)
#         cur = 0
#         for i in range(len(nums)):
#             cur += nums[i]
#             pre_sum[i+1] += cur
#         res = 0
#         for i in range(len(pre_sum)-1):
#             l = 0
#             while l <= i:
#                 tmp_sum = pre_sum[i+1] - pre_sum[l]
#                 if tmp_sum == k:
#                     res += 1
#                 l += 1
#         print ("res = " + str(res))
#         return res

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82767119
# IDEA : Cum-SUM + collections.defaultdict
# DEMO :
# nums =  [1,1,1]
# k =  2 
# s = Solution()
# r = s.subarraySum(nums, k)
# print (r)
# sum : 0
# res : 0
# d : defaultdict(<class 'int'>, {0: 1})
# sum : 1
# res : 0
# d : defaultdict(<class 'int'>, {0: 1, 1: 1})
# sum : 2
# res : 1
# d : defaultdict(<class 'int'>, {0: 1, 1: 1, 2: 1})
# 2
import collections
class Solution(object):
    def subarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        n = len(nums)
        d = collections.defaultdict(int)
        d[0] = 1
        sum = 0
        res = 0
        for i in range(n):
            sum += nums[i]
            if sum - k in d:
                res += d[sum - k]
            d[sum] += 1
        return res

# V1' 
# http://bookshadow.com/weblog/2017/04/30/leetcode-subarray-sum-equals-k/
import collections
class Solution(object):
    def subarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        ans = sums = 0
        cnt = collections.Counter()
        for num in nums:
            cnt[sums] += 1
            sums += num
            ans += cnt[sums - k]
        return ans

# V1''
# https://www.jiuzhang.com/solution/subarray-sum-equals-k/#tag-highlight-lang-python
class Solution:
    """
    @param nums: a list of integer
    @param k: an integer
    @return: return an integer, denote the number of continuous subarrays whose sum equals to k
    """
    def subarraySumEqualsK(self, nums, k):
        # write your code here
        for i in range(1, len(nums)):
            nums[i] += nums[i - 1]
        d, ans = {0 : 1}, 0
        for i in range(len(nums)):
            if(d.get(nums[i] - k) != None):
                ans += d[nums[i] - k]
            if(d.get(nums[i]) == None):
                d[nums[i]] = 1
            else:
                d[nums[i]] += 1
        return ans

# V1'''
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/subarray-sum-equals-k/solution/
# JAVA
# public class Solution {
#     public int subarraySum(int[] nums, int k) {
#         int count = 0;
#         for (int start = 0; start < nums.length; start++) {
#             for (int end = start + 1; end <= nums.length; end++) {
#                 int sum = 0;
#                 for (int i = start; i < end; i++)
#                     sum += nums[i];
#                 if (sum == k)
#                     count++;
#             }
#         }
#         return count;
#     }
# }

# V1
# https://leetcode.com/problems/subarray-sum-equals-k/solution/
# IDEA : Using Cumulative Sum
# JAVA
# public class Solution {
#     public int subarraySum(int[] nums, int k) {
#         int count = 0;
#         int[] sum = new int[nums.length + 1];
#         sum[0] = 0;
#         for (int i = 1; i <= nums.length; i++)
#             sum[i] = sum[i - 1] + nums[i - 1];
#         for (int start = 0; start < nums.length; start++) {
#             for (int end = start + 1; end <= nums.length; end++) {
#                 if (sum[end] - sum[start] == k)
#                     count++;
#             }
#         }
#         return count;
#     }
# }

# V1
# https://leetcode.com/problems/subarray-sum-equals-k/solution/
# IDEA : WITH SPACE
# JAVA
# public class Solution {
#     public int subarraySum(int[] nums, int k) {
#         int count = 0;
#         for (int start = 0; start < nums.length; start++) {
#             int sum=0;
#             for (int end = start; end < nums.length; end++) {
#                 sum+=nums[end];
#                 if (sum == k)
#                     count++;
#             }
#         }
#         return count;
#     }
# }

# V1
# https://leetcode.com/problems/subarray-sum-equals-k/solution/
# IDEA : HASHMAP
# JAVA
# public class Solution {
#     public int subarraySum(int[] nums, int k) {
#         int count = 0, sum = 0;
#         HashMap < Integer, Integer > map = new HashMap < > ();
#         map.put(0, 1);
#         for (int i = 0; i < nums.length; i++) {
#             sum += nums[i];
#             if (map.containsKey(sum - k))
#                 count += map.get(sum - k);
#             map.put(sum, map.getOrDefault(sum, 0) + 1);
#         }
#         return count;
#     }
# }

# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def subarraySum(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        result = 0
        accumulated_sum = 0
        lookup = collections.defaultdict(int)
        lookup[0] += 1
        for num in nums:
            accumulated_sum += num
            result += lookup[accumulated_sum - k]
            lookup[accumulated_sum] += 1
        return result