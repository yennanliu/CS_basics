# V0

# V1 
# https://blog.csdn.net/xx_123_1_rj/article/details/86549560
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
# Time:  O(n)
# Space: O(k)
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