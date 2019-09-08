# V0 

# V1 
# https://blog.csdn.net/GQxxxxxl/article/details/84928066
# https://blog.csdn.net/fuxuemingzhu/article/details/84925747
import collections
class Solution:
    def canReorderDoubled(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """

        count = collections.Counter(A)
        for x in sorted(A, key = abs):
            if count[x] == 0: continue
            if count[2*x] == 0: return False
            count[x] -= 1
            count[2*x] -= 1

        return all(v == 0 for v in count.values())

# V2 
# Time:  O(n + klogk)
# Space: O(k)
import collections
class Solution(object):
    def canReorderDoubled(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        count = collections.Counter(A)
        for x in sorted(count, key=abs):
            if count[x] > count[2*x]:
                return False
            count[2*x] -= count[x]
        return True