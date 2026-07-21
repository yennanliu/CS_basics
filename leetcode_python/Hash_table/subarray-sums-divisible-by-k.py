"""

Code
Testcase
Testcase
Test Result
974. Subarray Sums Divisible by K
Solved
Medium
Topics
premium lock icon
Companies
Given an integer array nums and an integer k, return the number of non-empty subarrays that have a sum divisible by k.

A subarray is a contiguous part of an array.

 

Example 1:

Input: nums = [4,5,0,-2,-3,1], k = 5
Output: 7
Explanation: There are 7 subarrays with a sum divisible by k = 5:
[4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
Example 2:

Input: nums = [5], k = 9
Output: 0
 

Constraints:

1 <= nums.length <= 3 * 104
-104 <= nums[i] <= 104
2 <= k <= 104
"""


# V0
# time = O(n)
# space = O(k)
class Solution(object):
    def subarraysDivByK(self, nums, k):
        # edge
        if len(nums) == 1:
            return 1 if nums[0] % k == 0 else 0

        # {remain: cnt}
        remain_map = {0: 1} # ??

        cnt = 0
        remain = 0

        for i in range(len(nums)):

            val = nums[i]

            remain += val
            remain = (remain % k)

            if remain in remain_map:
                cnt += remain_map.get(remain)

            remain_map[remain] = remain_map.get(remain, 0) + 1

        return cnt


# V0
# time = O(n)
# space = O(k)
class Solution(object):
    def subarraysDivByK(self, nums, k):
        count_map = {0: 1}
        prefix = 0
        cnt = 0

        for x in nums:
            prefix += x

            # normalize modulo (important for negatives)
            prefix %= k

            cnt += count_map.get(prefix, 0)

            count_map[prefix] = count_map.get(prefix, 0) + 1

        return cnt

# V0-1
# time = O(n)
# space = O(k)
class Solution(object):
    def subarraysDivByK(self, nums, k):
        if nums is None:
            return 0
            
        # Map to store: { prefix_remainder : frequency }
        # Base Case: A remainder of 0 has happened 1 time before we even start
        remainder_map = {0: 1}
        
        cnt = 0
        prefix = 0
        
        for x in nums:
            # Step 1: Accumulate the running prefix sum
            prefix += x
            
            # Step 2: Keep only the remainder when divided by k
            prefix = prefix % k
            
            # Step 3: If this remainder has been seen before, it means the 
            # elements in between sum up to a perfect multiple of k!
            if prefix in remainder_map:
                cnt += remainder_map[prefix]
                
            # Step 4: Record this remainder frequency into the map
            remainder_map[prefix] = remainder_map.get(prefix, 0) + 1
            
        return cnt


# V1
# https://blog.csdn.net/xx_123_1_rj/article/details/86549560
# time = O(n)
# space = O(n)
import collections
class Solution():
    def subarraysDivByK(self, A, K):
        P = [0]
        for x in A:
            P.append((P[-1] + x) % K)

        count = collections.Counter(P)
        return sum(v*(v-1)/2 for v in count.values())

# V1'
# https://blog.csdn.net/xx_123_1_rj/article/details/86549560
# time = O(n)
# space = O(k)
import collections
class Solution:
    def subarraysDivByK(self, a, k):
        counts = collections.defaultdict(int)  # for count # of residual
        counts[0] = 1  # initialization, since 0 is one of the accepted cases 
        cur_sum = 0  # record the sum of current array 
        ans = 0  # record the # of divisible nums 
        for num in a:
            cur_sum += num
            mod = cur_sum % k  # get residual 
            ans += counts[mod]  # sum 
            counts[mod] += 1  # count 
        return ans

# V2
# time = O(n)
# space = O(k)
import collections
class Solution(object):
    def subarraysDivByK(self, A, K):
        """
        :type A: List[int]
        :type K: int
        :rtype: int
        """
        count = collections.defaultdict(int)
        count[0] = 1
        result, prefix = 0, 0
        for a in A:
            prefix = (prefix+a) % K
            result += count[prefix]
            count[prefix] += 1
        return result