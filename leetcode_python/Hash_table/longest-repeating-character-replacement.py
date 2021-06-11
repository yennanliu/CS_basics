"""

You are given a string s and an integer k. 
You can choose any character of the string and change it to any other uppercase English character. 
You can perform this operation at most k times.
Return the length of the longest substring containing the same letter you can get after performing the above operations.

 

Example 1:

Input: s = "ABAB", k = 2
Output: 4
Explanation: Replace the two 'A's with two 'B's or vice versa.

Example 2:

Input: s = "AABABBA", k = 1
Output: 4
Explanation: Replace the one 'A' in the middle with 'B' and form "AABBBBA".
The substring "BBBB" has the longest repeating letters, which is 4.
 

Constraints:

1 <= s.length <= 105
s consists of only uppercase English letters.
0 <= k <= s.length

"""

# V0
# IDEA : SLIDING WINDOW + DICT
class Solution(object):
    def characterReplacement(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        table = collections.Counter()
        res = 0
        p1 = p2 = 0
        while p2 < len(s):
            table[s[p2]] += 1
            p2 += 1
            # NOTE : if remain elements > k, means there is no possibility to make this substring as "longest substring containing the same letter"
            #       ->  remain elements = p1 - p2 - max(table.values())
            #       ->  so we need to clean the "potentail candidate" for next iteration
            while p2 - p1 - max(table.values()) > k:
                table[s[p1]] -= 1
                p1 += 1
            res = max(res, p2 - p1)
        return res
    
# V0'
from collections import defaultdict
class Solution:
    """
    @param s: a string
    @param k: a integer
    @return: return a integer
    """
    def characterReplacement(self, s, k):
        # write your code here
        n = len(s)
        char2count = defaultdict(int)
        
        maxLen = 0
        start = 0
        for end in range(n):
            char2count[s[end]] += 1
            
            while end - start + 1 - char2count[s[start]] > k:
                char2count[s[start]] -= 1
                start += 1

            maxLen = max(maxLen, end - start + 1)     
            
        return maxLen

# V1
# https://www.jiuzhang.com/solution/longest-repeating-character-replacement/#tag-highlight-lang-python
# IDEA  : collections.defaultdict, SLDING WINDOW  + TWO POINTERS 
# STEPS : 
# -> USE char2count TO RECORD COUNT OF ELEMENTS  
# -> EACH TIME WHEN RIGHT POINT MOVE (RIGHT), IF THE CURRENT WINDOW CAN BE ALL REPLACED BY THE LEGT POINT ELEMENT
# -> THEN WE UPDATE THE MAX LENGTH,
# -> IF NOT, MOVE LEFT POINT (RIGHT) AND UPDATE char2count
from collections import defaultdict
class Solution:
    """
    @param s: a string
    @param k: a integer
    @return: return a integer
    """
    def characterReplacement(self, s, k):
        # write your code here
        n = len(s)
        char2count = defaultdict(int)
        
        maxLen = 0
        start = 0
        for end in range(n):
            char2count[s[end]] += 1
            
            while end - start + 1 - char2count[s[start]] > k:
                char2count[s[start]] -= 1
                start += 1

            maxLen = max(maxLen, end - start + 1)     

        return maxLen

# V1' 
# http://bookshadow.com/weblog/2016/10/16/leetcode-longest-repeating-character-replacement/
# IDEA : SLIDING WINDOW 
class Solution(object):
    def characterReplacement(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        table = collections.Counter()
        res = 0
        p1 = p2 = 0
        while p2 < len(s):
            table[s[p2]] += 1
            p2 += 1
            while p2 - p1 - max(table.values()) > k:
                table[s[p1]] -= 1
                p1 += 1
            res = max(res, p2 - p1)
        return res

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79527303
# IDEA : SLIDING WINDOW 
class Solution(object):
    def characterReplacement(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        count = collections.Counter()
        res = 0
        start = 0
        for i, char in enumerate(s):
            count[char] += 1
            maxCnt = count.most_common(1)[0][1]
            while i - start + 1 - maxCnt > k:
                count[s[start]] = count[s[start]] - 1
                start += 1
            res = max(res, i - start + 1)
        return res

# V1''' 
# http://bookshadow.com/weblog/2016/10/16/leetcode-longest-repeating-character-replacement/
class Solution(object):
    def characterReplacement(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        sizes = len(s)
        letters = set(s)

        cdict = collections.defaultdict(list)
        li, lc = 0, (s[0] if s else None)
        for i, c in enumerate(s):
            if c != lc:
                cdict[lc].append( (li, i - 1) )
                li, lc = i, c
        cdict[lc].append( (li, sizes - 1) )

        ans = 0
        for c in letters:
            invs = cdict[c]
            ans = max(ans, max(y - x + 1 + min(k, x + sizes - 1 - y) for x, y in invs))
            sizec = len(invs)
            cnt = k
            sp = 0
            ep = 1
            while sp < sizec and ep < sizec:
                if cnt >= invs[ep][0] - invs[ep - 1][1] - 1:
                    cnt -= invs[ep][0] - invs[ep - 1][1] - 1
                    lenc = invs[ep][1] - invs[sp][0] + 1 + min(cnt, invs[sp][0] + sizes - 1 - invs[ep][1])
                    ans = max(ans, lenc)
                    ep += 1
                else:
                    sp += 1
                    cnt += invs[sp][0] - invs[sp - 1][1] - 1
        return ans

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def characterReplacement(self, s, k):
        """
        :type s: str
        :type k: int
        :rtype: int
        """
        res = 0
        cnts = [0] * 26
        times, i, j = k, 0, 0
        while j < len(s):
            cnts[ord(s[j]) - ord('A')] += 1
            if s[j] != s[i]:
                times -= 1
                if times < 0:
                    res = max(res, j - i)
                    while i < j and times < 0:
                        cnts[ord(s[i]) - ord('A')] -= 1
                        i += 1
                        times = k - (j - i + 1 - cnts[ord(s[i]) - ord('A')])
            j += 1
        return max(res, j - i + min(i, times))
