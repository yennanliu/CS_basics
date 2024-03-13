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
# IDEA : SLIDING WINDOW + DICT + 2 POINTERS
from collections import Counter
class Solution(object):
    def characterReplacement(self, s, k):
        """
        NOTE !!!

         table record frequent of element met in while loop
        """
        table = Counter()
        res = 0
        """
        Define 2 pointers

        p1 : left pointer
        p2 : right pointer
        """
        p1 = p2 = 0
        # below can be either while or for loop
        while p2 < len(s):
            table[s[p2]] += 1
            p2 += 1
            """
            ### NOTE : if remain different elements > k, means there is no possibility to make this substring as "longest substring containing the same letter"
               ->  remain different elements = p1 - p2 - max(table.values())
               ->  e.g. if we consider "max(table.values()" as the "repeating character", then "p2 - p1 - max(table.values()" is the count of different elements we need to replace


               ->  we also need to clear "current candidate" for next iteration (while move left pointer)
               e.g.           
                    table[s[p1]] -= 1
                    p1 += 1
            """
            while p2 - p1 - max(table.values()) > k:
                table[s[p1]] -= 1
                p1 += 1
            res = max(res, p2 - p1)
        return res
    
# V0'
from collections import defaultdict
class Solution:
    def characterReplacement(self, s, k):
        cnt = defaultdict(int)
        maxLen = 0
        l = 0
        # below can be either while or for loop
        for r in range(len(s)):
            cnt[s[r]] += 1
            ### NOTE : this condition
            while r - l + 1 - max(cnt.values()) > k:
                cnt[s[l]] -= 1
                l += 1
            maxLen = max(maxLen, r - l + 1)     

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
    def characterReplacement(self, s, k):
        # write your code here
        n = len(s)
        char2count = defaultdict(int)
        
        maxLen = 0
        start = 0
        for end in range(n):
            char2count[s[end]] += 1
            
            ### NOTE : this condition
            while end - start + 1 - max(char2count.values()) > k:
                char2count[s[start]] -= 1
                start += 1

            maxLen = max(maxLen, end - start + 1)     

        return maxLen

# V1' 
# http://bookshadow.com/weblog/2016/10/16/leetcode-longest-repeating-character-replacement/
# IDEA : SLIDING WINDOW 
class Solution(object):
    def characterReplacement(self, s, k):
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

# V1''''
# https://leetcode.com/problems/longest-repeating-character-replacement/discuss/558076/Python-solution-without-library
class Solution:
    def characterReplacement(self, s, k):
        if k > len(s):
            return len(s)
        counter = [0 for _ in range(26)]
        ans = k
        deleted = 0
        for i in range(len(s)):
            counter[ord(s[i]) - ord('A')] += 1
            largest = max(counter)
            if largest + k < i - deleted + 1:           
                counter[ord(s[deleted]) - ord('A')] -= 1
                deleted += 1
            ans = max(ans, i - deleted + 1)
        return ans

# V1''''
# https://leetcode.com/problems/longest-repeating-character-replacement/discuss/867755/python-solution
class Solution:
    def characterReplacement(self, s, k):        
        cnt = collections.defaultdict(int)
        i = 0
        res = 0
        for j in range(len(s)):
            cnt[s[j]] += 1
            while j - i + 1 - max(cnt.values()) > k:
                cnt[s[i]] -= 1
                i += 1
            res = max(res, j - i + 1)
        return res

# V1'''''
# https://leetcode.com/problems/longest-repeating-character-replacement/discuss/535734/Python-two-pointers
# IDEA : 2 POINTERS
class Solution:
    def characterReplacement(self, s: str, k: int) -> int:
        seen = [0] * 26
        len_s = len(s)
        res, left = 0, 0
        for right in range(len_s):
            seen[ord(s[right])-ord('A')] += 1
            while right - left + 1 - max(seen) > k:
                seen[ord(s[left])-ord('A')] -= 1
                left += 1
            res = max(res, right - left + 1)
        return res

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