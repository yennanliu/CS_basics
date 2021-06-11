"""

Given a string s, find the length of the longest substring without repeating characters.


Example 1:

Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3.

Example 2:

Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.

Example 3:

Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
Example 4:

Input: s = ""
Output: 0
 

Constraints:

0 <= s.length <= 5 * 104
s consists of English letters, digits, symbols and spaces.

"""

# V0
# IDEA : SLIDING WINDOW + DICT
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        """
        :type s: str
        :rtype: int
        """
        left, right = 0, 0
        res = 0
        chars = dict()
        for right in range(len(s)):
            if s[right] in chars:
                left = max(left, chars[s[right]] + 1)
            chars[s[right]] = right
            res = max(res, right - left + 1)
        return res

# V1
# IDEA : BRUTE FORCE
# Time COMPLEXITY : O(n^3)
# Space COMPLEXITY : O(mim(n,m))
# https://leetcode.com/problems/longest-substring-without-repeating-characters/solution/
class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        def check(start, end):
            chars = [0] * 128
            for i in range(start, end + 1):
                c = s[i]
                chars[ord(c)] += 1
                if chars[ord(c)] > 1:
                    return False
            return True

        n = len(s)

        res = 0
        for i in range(n):
            for j in range(i, n):
                if check(i, j):
                    res = max(res, j - i + 1)
        return res  

# V1'
# IDEA : SLIDING WINDOW
# Time COMPLEXITY : O(n)
# Space COMPLEXITY : O(mim(n,m))
# https://leetcode.com/problems/longest-substring-without-repeating-characters/solution/
class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        chars = [0] * 128

        left = right = 0

        res = 0
        while right < len(s):
            r = s[right]
            chars[ord(r)] += 1

            while chars[ord(r)] > 1:
                l = s[left]
                chars[ord(l)] -= 1
                left += 1

            res = max(res, right - left + 1)

            right += 1
        return res

# V1''
# IDEA :  SLIDING WINDOW OPTIMIZED
# Time COMPLEXITY : O(n)
# Space COMPLEXITY : O(mim(n,m))
# https://leetcode.com/problems/longest-substring-without-repeating-characters/solution/
class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        n = len(s)
        ans = 0
        # mp stores the current index of a character
        mp = {}

        i = 0
        # try to extend the range [i, j]
        for j in range(n):
            if s[j] in mp:
                i = max(mp[s[j]], i)

            ans = max(ans, j - i + 1)
            mp[s[j]] = j + 1

        return ans 
    
# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/82022530
# IDEA : GREEDY 
class Solution(object):
    def lengthOfLongestSubstring(self, s):
        """
        :type s: str
        :rtype: int
        """
        left, right = 0, 0
        chars = set()
        res = 0
        while left < len(s) and right < len(s):
            if s[right] in chars:
                if s[left] in chars:
                    chars.remove(s[left])
                left += 1
            else:
                chars.add(s[right])
                right += 1
                res = max(res, len(chars))
        return res

# V1'''''
# http://bookshadow.com/weblog/2015/04/05/leetcode-longest-substring-without-repeating-characters/
# IDEA : MOVING WINDOW + DICT 
class Solution:
    # @return an integer
    def lengthOfLongestSubstring(self, s):
        ans, start, end = 0, 0, 0
        countDict = {}
        for c in s:
            end += 1
            countDict[c] = countDict.get(c, 0) + 1
            while countDict[c] > 1:
                countDict[s[start]] -= 1
                start += 1
            ans = max(ans, end - start)
        return ans 

# V1'''''
# https://www.jiuzhang.com/solution/longest-substring-without-repeating-characters/#tag-highlight-lang-python
class Solution:
    """
    @param s: a string
    @return: an integer
    """
    def lengthOfLongestSubstring(self, s):
        unique_chars = set([])
        j = 0
        n = len(s)
        longest = 0
        for i in range(n):
            while j < n and s[j] not in unique_chars:
                unique_chars.add(s[j])
                j += 1
            longest = max(longest, j - i)
            unique_chars.remove(s[i])    
        return longest 

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @return an integer
    def lengthOfLongestSubstring(self, s):
        longest, start, visited = 0, 0, [False for _ in range(256)]
        for i, char in enumerate(s):
            if visited[ord(char)]:
                while char != s[start]:
                    visited[ord(s[start])] = False
                    start += 1
                start += 1
            else:
                visited[ord(char)] = True
            longest = max(longest, i - start + 1)
        return longest
