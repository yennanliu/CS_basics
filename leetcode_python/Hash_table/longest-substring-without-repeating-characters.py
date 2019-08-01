# V0

# V1
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

# V1'
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
