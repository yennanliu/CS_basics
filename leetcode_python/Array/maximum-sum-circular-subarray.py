# V0

# V1 
# https://buptwc.com/2018/10/08/Leetcode-918-Maximum-Sum-Circular-Subarray/
class Solution(object):
    def maxSubarraySumCircular(self, A):
        left = [A[0]] * len(A)
        s = [A[0]] * len(A)
        r_max = [s[0]] * len(A)
        for i in range(1,len(A)):
            left[i] = max(A[i], left[i-1]+A[i])
            s[i] = s[i-1] + A[i]
            r_max[i] = max(s[i], r_max[i-1])

        res = max(left)
        for i in range(len(A)):
            res = max(res, s[-1] - s[i] + r_max[i])
        return res

# V1'
# https://www.jiuzhang.com/solution/maximum-sum-circular-subarray/#tag-highlight-lang-python
class Solution(object):
    def maxSubarraySumCircular(self, A):
        N = len(A)

        ans = cur = None
        for x in A:
            cur = x + max(cur, 0)
            ans = max(ans, cur)

        # ans is the answer for 1-interval subarrays.
        # Now, let's consider all 2-interval subarrays.
        # For each i, we want to know
        # the maximum of sum(A[j:]) with j >= i+2

        # rightsums[i] = sum(A[i:])
        rightsums = [None] * N
        rightsums[-1] = A[-1]
        for i in range(N-2, -1, -1):
            rightsums[i] = rightsums[i+1] + A[i]

        # maxright[i] = max_{j >= i} rightsums[j]
        maxright = [None] * N
        maxright[-1] = rightsums[-1]
        for i in range(N-2, -1, -1):
            maxright[i] = max(maxright[i+1], rightsums[i])

        leftsum = 0
        for i in range(N-2):
            leftsum += A[i]
            ans = max(ans, leftsum + maxright[i+2])
        return ans
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maxSubarraySumCircular(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        total, max_sum, cur_max, min_sum, cur_min = 0, -float("inf"), 0, float("inf"), 0
        for a in A:
            cur_max = max(cur_max+a, a)
            max_sum = max(max_sum, cur_max)
            cur_min = min(cur_min+a, a)
            min_sum = min(min_sum, cur_min)
            total += a
        return max(max_sum, total-min_sum) if max_sum > 0 else max_sum