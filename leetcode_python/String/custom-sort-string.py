"""

791. Custom Sort String
Medium

You are given two strings order and s. All the words of order are unique and were sorted in some custom order previously.

Permute the characters of s so that they match the order that order was sorted. More specifically, if a character x occurs before a character y in order, then x should occur before y in the permuted string.

Return any permutation of s that satisfies this property.

 

Example 1:

Input: order = "cba", s = "abcd"
Output: "cbad"
Explanation: 
"a", "b", "c" appear in order, so the order of "a", "b", "c" should be "c", "b", and "a". 
Since "d" does not appear in order, it can be at any position in the returned string. "dcba", "cdba", "cbda" are also valid outputs.
Example 2:

Input: order = "cbafg", s = "abcd"
Output: "cbad"
 

Constraints:

1 <= order.length <= 26
1 <= s.length <= 200
order and s consist of lowercase English letters.
All the characters of order are unique.

"""

# V0
# IDEA : COUNTER
from collections import Counter
class Solution(object):
    def customSortString(self, order, s):
        s_map = Counter(s)
        res = ""
        for o in order:
            if o in s_map:
                res += (o * s_map[o])
                del s_map[o]
        for s in s_map:
            res += s * s_map[s]
        return res

# V0'
# IDEA : COUNTER
from collections import Counter
class Solution(object):
    def customSortString(self, order, s):
        # edge case
        if (not order) or (not order and not s):
            return s
        s_cnt = Counter(s)
        res = ""
        for o in order:
            if not s_cnt:
                break
            if o in s_cnt:
                res += s_cnt[o] * o
                del s_cnt[o] 

        if s_cnt:
            for k in s_cnt:
                res += s_cnt[k] * k

        #print ("res = " + str(res))
        return res

# V0''
# IDEA : COUNTER 
from collections import Counter
class Solution(object):
    def customSortString(self, S, T):
        count = Counter(T)
        answer = ''
        for s in S:
            answer += s * count[s]
            count[s] = 0
        for c in count:
            answer += c * count[c]
        return answer
        
# V0'''
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