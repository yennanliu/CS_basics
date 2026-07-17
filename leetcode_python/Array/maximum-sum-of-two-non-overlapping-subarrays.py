# https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/description/

"""

1031. Maximum Sum of Two Non-Overlapping Subarrays
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given an integer array nums and two integers firstLen and secondLen, return the maximum sum of elements in two non-overlapping subarrays with lengths firstLen and secondLen.

The array with length firstLen could occur before or after the array with length secondLen, but they have to be non-overlapping.

A subarray is a contiguous part of an array.

 

Example 1:

Input: nums = [0,6,5,2,2,5,1,9,4], firstLen = 1, secondLen = 2
Output: 20
Explanation: One choice of subarrays is [9] with length 1, and [6,5] with length 2.
Example 2:

Input: nums = [3,8,1,3,2,1,8,9,0], firstLen = 3, secondLen = 2
Output: 29
Explanation: One choice of subarrays is [3,8,1] with length 3, and [8,9] with length 2.
Example 3:

Input: nums = [2,1,5,6,0,9,5,0,3,8], firstLen = 4, secondLen = 3
Output: 31
Explanation: One choice of subarrays is [5,6,0,9] with length 4, and [0,3,8] with length 3.
 

Constraints:

1 <= firstLen, secondLen <= 1000
2 <= firstLen + secondLen <= 1000
firstLen + secondLen <= nums.length <= 1000
0 <= nums[i] <= 1000


"""


# V0
# IDEA: PREFIX SUM (gpt)
"""

Core idea:


-> `precompute prefix sums`, then scan the array `twice`:

L before M: the firstLen subarray comes 
            before the secondLen subarray.

M before L: the reverse order.

During each scan, maintain the maximum sum 
of the left subarray seen so far,
then combine it with the current right subarray.


"""
class Solution(object):
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        n = len(nums)

        """
        NOTE !!!


        prefix size = `n+1`
        """
        prefix = [0] * (n + 1)
        for i in range(n):
            """
            NOTE !!!


            how we prepare prefix array:

            https://yennj12.js.org/CS_basics/cheatsheets/prefix_sum.html


            -> prefix[i+1] = prefix[i] + nums[i]
            """
            prefix[i + 1] = prefix[i] + nums[i]

        return max(
            self.helper(nums, prefix, firstLen, secondLen),
            self.helper(nums, prefix, secondLen, firstLen)
        )

    
    def helper(self, nums, prefix, firstLen, secondLen):
        n = len(nums)

        # NOTE !!!
        # we get cur `bestFirst` first
        bestFirst = prefix[firstLen] - prefix[0]
        ans = 0

        """
        NOTE !!!

        
        1. 
          ONLY 1 loop:
            range(firstLen + secondLen, n + 1)


        2. we get updated  `bestFirst`

        3. we get second

        4. we get cur max ans
        """
        for i in range(firstLen + secondLen, n + 1):
            bestFirst = max(
                bestFirst,
                prefix[i - secondLen] - prefix[i - secondLen - firstLen]
            )

            second = prefix[i] - prefix[i - secondLen]

            ans = max(ans, bestFirst + second)

        return ans



# V0-0-1
# IDEA: PREFIX SUM (gemini)
class Solution(object):
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        """
        :type nums: List[int]
        :type firstLen: int
        :type secondLen: int
        :rtype: int
        """
        n = len(nums)
        
        # Build an N+1 sized prefix sum array.
        # prefix[i] will store the sum of all elements before index i.
        prefix = [0] * (n + 1)
        for i in range(n):
            prefix[i + 1] = prefix[i] + nums[i]
            
        # Scenario 1: firstLen comes BEFORE secondLen
        v1 = self.helper(prefix, n, firstLen, secondLen)
        
        # Scenario 2: secondLen comes BEFORE firstLen
        v2 = self.helper(prefix, n, secondLen, firstLen)
        
        return max(v1, v2)
        

    # NOTE !!!!
    # below helper using `double loop`
    def helper(self, prefix, n, len1, len2):
        max_sum = 0
        
        # i is the start index of the first window
        # It can go up to n - len1 - len2 (to leave room for the second window)
        for i in range(n - len1 - len2 + 1):
            
            # The sum of nums[i : i + len1] using our prefix array
            sum1 = prefix[i + len1] - prefix[i]
            
            # j is the start index of the second window
            # It must start strictly after the first window ends (i + len1)
            for j in range(i + len1, n - len2 + 1):
                
                # The sum of nums[j : j + len2]
                sum2 = prefix[j + len2] - prefix[j]
                
                # Keep track of the maximum combined sum
                max_sum = max(max_sum, sum1 + sum2)
                
        return max_sum


# V0
# IDEA: BRUTE FORCE + `Try both orders` (gpt)
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Array/MaximumSumOfTwoNonOverlappingSubarrays.java#L50
"""
CORE IDEA:


 ```
 Try both orders:

 1. firstLen before secondLen
 2. secondLen before firstLen

 For each order:
 - maintain best L-window seen so far
 - combine with current M-window
 ```
"""
class Solution(object):
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        n = len(nums)
        max_sum = 0

        # firstLen before secondLen
        for i in range(n - firstLen + 1):
            f_window = nums[i:i + firstLen]

            for j in range(i + firstLen, n - secondLen + 1):
                s_window = nums[j:j + secondLen]
                max_sum = max(max_sum, sum(f_window) + sum(s_window))

        # secondLen before firstLen
        for i in range(n - secondLen + 1):
            f_window = nums[i:i + secondLen]

            for j in range(i + secondLen, n - firstLen + 1):
                s_window = nums[j:j + firstLen]
                max_sum = max(max_sum, sum(f_window) + sum(s_window))

        return max_sum


# V1-1
# IDEA: PREFIX SUM (gpt)
"""

Core idea:


-> `precompute prefix sums`, then scan the array `twice`:

L before M: the firstLen subarray comes 
            before the secondLen subarray.

M before L: the reverse order.

During each scan, maintain the maximum sum 
of the left subarray seen so far,
then combine it with the current right subarray.


"""
class Solution:
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        n = len(nums)

        # prefix[i] = sum(nums[:i])
        prefix = [0] * (n + 1)
        for i in range(n):
            prefix[i + 1] = prefix[i] + nums[i]

        def maxSum(L, M):
            # Best L-length window before current M window
            bestL = prefix[L] - prefix[0]
            ans = 0

            # i = starting index of M window
            for i in range(L, n - M + 1):
                # Update best L window ending before i
                bestL = max(bestL, prefix[i] - prefix[i - L])

                # Current M window
                currM = prefix[i + M] - prefix[i]

                ans = max(ans, bestL + currM)

            return ans

        return max(maxSum(firstLen, secondLen),
                   maxSum(secondLen, firstLen))



# V1-2
# IDEA: PREFIX SUM (gemini)
class Solution(object):
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        """
        :type nums: List[int]
        :type firstLen: int
        :type secondLen: int
        :rtype: int
        """
        n = len(nums)
        
        # 1. Build a prefix sum array to instantly calculate subarray sums
        # prefix[i] represents the sum of nums[0] to nums[i-1]
        prefix = [0] * (n + 1)
        for i in range(n):
            prefix[i + 1] = prefix[i] + nums[i]
            
        max_L = 0  # Maximum sum of a firstLen subarray seen so far
        max_M = 0  # Maximum sum of a secondLen subarray seen so far
        max_total = 0
        
        L = firstLen
        M = secondLen
        
        # 2. Iterate starting from the point where BOTH subarrays can fit (L + M)
        for i in range(L + M, n + 1):
            
            # --- Scenario A: L comes before M ---
            # Sum of the L-length subarray that ends exactly where the current M-length subarray starts
            curr_L = prefix[i - M] - prefix[i - M - L]
            max_L = max(max_L, curr_L)
            
            # Sum of the current M-length subarray ending at index i
            sum_M = prefix[i] - prefix[i - M]
            
            # --- Scenario B: M comes before L ---
            # Sum of the M-length subarray that ends exactly where the current L-length subarray starts
            curr_M = prefix[i - L] - prefix[i - L - M]
            max_M = max(max_M, curr_M)
            
            # Sum of the current L-length subarray ending at index i
            sum_L = prefix[i] - prefix[i - L]
            
            # Update the global maximum using the best combination from either scenario
            max_total = max(max_total, max_L + sum_M, max_M + sum_L)
            
        return max_total



# V1-3
# IDEA: PREFIX SUM (gpt)
class Solution:
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        n = len(nums)
        pre = [0]
        for x in nums:
            pre.append(pre[-1] + x)

        def solve(L, M):
            best = 0
            ans = 0
            for i in range(L, n - M + 1):
                best = max(best, pre[i] - pre[i - L])
                ans = max(ans, best + pre[i + M] - pre[i])
            return ans

        return max(solve(firstLen, secondLen),
                   solve(secondLen, firstLen))



# V2
# https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/solutions/6575097/maximum-sum-of-two-non-overlapping-subar-abkc/
class Solution(object):
    def maxSumTwoNoOverlap(self, nums, firstLen, secondLen):
        """
        :type nums: List[int]
        :type firstLen: int
        :type secondLen: int
        :rtype: int
        """
        def getMaxSum(first, second):
            maxFirst, res = 0, 0
            prefix = [0] * (len(nums) + 1)  

            for i in range(len(nums)):
                prefix[i + 1] = prefix[i] + nums[i]

            for i in range(first + second, len(nums) + 1):
                maxFirst = max(maxFirst, prefix[i - second] - prefix[i - first - second])
                res = max(res, maxFirst + prefix[i] - prefix[i - second])
            return res

        return max(getMaxSum(firstLen, secondLen), getMaxSum(secondLen, firstLen))


# V3
# https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/solutions/760194/python-clean-on-98-by-warmr0bot-9k9z/
class Solution:
    def maxSumTwoNoOverlap(self, A: List[int], L: int, M: int) -> int:
        prefix = [0]
        maxl = maxm = summ = 0
        for x in A:
            prefix.append(prefix[-1] + x)
        
        for x in range(M, len(prefix) - L):
            maxm = max(maxm, prefix[x] - prefix[x - M])
            summ = max(summ, maxm + prefix[x + L] - prefix[x])
        
        for x in range(L, len(prefix) - M):
            maxl = max(maxl, prefix[x] - prefix[x - L])
            summ = max(summ, maxl + prefix[x + M] - prefix[x])
        
        return summ
