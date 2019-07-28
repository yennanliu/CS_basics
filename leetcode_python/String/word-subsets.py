# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82914193
class Solution(object):
    def wordSubsets(self, A, B):
        """
        :type A: List[str]
        :type B: List[str]
        :rtype: List[str]
        """
        B = set(B)
        res = []
        count = collections.defaultdict(int)
        for b in B:
            cb = collections.Counter(b)
            for c, v in cb.items():
                count[c] = max(count[c], v)
        res = []
        for a in A:
            ca = collections.Counter(a)
            isSuccess = True
            for c, v in count.items():
                if v > ca[c]:
                    isSuccess = False
                    break
            if isSuccess:
                res.append(a)
        return res
        
# V2
# Time:  O(m + n)
# Space: O(1)
import collections
class Solution(object):
    def wordSubsets(self, A, B):
        """
        :type A: List[str]
        :type B: List[str]
        :rtype: List[str]
        """
        count = collections.Counter()
        for b in B:
            for c, n in collections.Counter(b).items():
                count[c] = max(count[c], n)
        result = []
        for a in A:
            count = collections.Counter(a)
            if all(count[c] >= count[c] for c in count):
                result.append(a)
        return result
