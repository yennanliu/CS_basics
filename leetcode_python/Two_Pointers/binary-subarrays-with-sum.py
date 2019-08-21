# V0

# V1
# https://www.jiuzhang.com/solution/binary-subarrays-with-sum/#tag-highlight-lang-python
import collections
class Solution:
    """
    @param A: an array
    @param S: the sum
    @return: the number of non-empty subarrays
    """
    def numSubarraysWithSum(self, A, S):
        # Write your code here.
        c = collections.Counter({0: 1})
        psum = res = 0
        for i in A:
            psum += i
            res += c[psum - S]
            c[psum] += 1
        return res

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83478995
# IDEA : DICT 
import collections
class Solution(object):
    def numSubarraysWithSum(self, A, S):
        """
        :type A: List[int]
        :type S: int
        :rtype: int
        """
        N = len(A)
        res = 0
        preS = 0
        count = collections.Counter({0 : 1})
        for i in range(1, N + 1):
            preS += A[i - 1]
            res += count[preS - S]
            count[preS] += 1
        return res

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83478995
# IDEA : GREEDY 
class Solution(object):
    def numSubarraysWithSum(self, A, S):
        """
        :type A: List[int]
        :type S: int
        :rtype: int
        """
        N = len(A)
        tsum = [0] * (N + 1)
        for i in range(1, N + 1):
            tsum[i] = tsum[i - 1] + A[i - 1]
        res = 0
        for i in range(1, N + 1):
            remain = tsum[i] - S
            if remain < 0:
                continue
            left = bisect.bisect_left(tsum, remain)
            right = bisect.bisect_right(tsum, remain)
            right = min(i, right)
            res += right - left
        return res
        
# V2
# Time:  O(n)
# Space: O(1)
# Two pointers solution
class Solution(object):
    def numSubarraysWithSum(self, A, S):
        """
        :type A: List[int]
        :type S: int
        :rtype: int
        """
        result = 0
        left, right, sum_left, sum_right = 0, 0, 0, 0
        for i, a in enumerate(A):
            sum_left += a
            while left < i and sum_left > S:
                sum_left -= A[left]
                left += 1
            sum_right += a
            while right < i and \
                  (sum_right > S or (sum_right == S and not A[right])):
                sum_right -= A[right]
                right += 1
            if sum_left == S:
                result += right-left+1
        return result
