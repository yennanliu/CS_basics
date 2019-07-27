# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79378688
# IDEA : COUNTER 
from collections import Counter
class Solution(object):
    def customSortString(self, S, T):
        """
        :type S: str
        :type T: str
        :rtype: str
        """
        count = Counter(T)
        answer = ''
        for s in S:
            answer += s * count[s]
            count[s] = 0
        for c in count:
            answer += c * count[c]
        return answer

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79378688
# IDEA : SORT
class Solution(object):
    def customSortString(self, S, T):
        """
        :type S: str
        :type T: str
        :rtype: str
        """
        pos = collections.defaultdict(int)
        for i in range(len(S)):
            pos[S[i]] = i
        res = sorted(T, key = lambda x : pos[x])
        return "".join(res)

# V2 
# Time:  O(n)
# Space: O(1)
import collections
class Solution(object):
    def customSortString(self, S, T):
        """
        :type S: str
        :type T: str
        :rtype: str
        """
        counter, s = collections.Counter(T), set(S)
        result = [c*counter[c] for c in S]
        result.extend([c*counter for c, counter in counter.iteritems() if c not in s])
        return "".join(result)