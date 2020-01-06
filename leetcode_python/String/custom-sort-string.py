# V0 
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
        
# V0'
# IDEA : COUNTER + SORT KEY LAMBDA 
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

# V1''
# https://www.jiuzhang.com/solution/custom-sort-string/#tag-highlight-lang-python
class Solution:
    """
    @param S: The given string S
    @param T: The given string T
    @return: any permutation of T (as a string) that satisfies this property
    """
    def customSortString(self, S, T):
        # Write your code here
        res = ""
        cnt, t = [0] * 26, 0
        for i in range(0, len(T)):
            cnt[ord(T[i]) - ord('a')] += 1
        for i in range(0, len(S)):
            for j in range(0, cnt[ord(S[i]) - ord('a')]):
                res += S[i]
            cnt[ord(S[i]) - ord('a')] = 0
        for i in range(0, 26):
            for j in range(0, cnt[i]):
                res += chr(ord('a') + i)
        return res

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