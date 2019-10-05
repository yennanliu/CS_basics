# Time:  O(n)
# Space: O(n)
# Given a string, find the first non-repeating character in it and
# return it's index. If it doesn't exist, return -1.
#
# Examples:
#
# s = "leetcode"
# return 0.  --> since "l" only exists once in the string and is the first one, so return 0 
#
# s = "loveleetcode",
# return 2.  --> since "v" only exists once in the string and is the first one, so return 2 
#
# Note: You may assume the string contain only lowercase letters.

# V0 
import collections
class Solution(object):
    def firstUniqChar(self, s):
        """
        :type s: str
        :rtype: int
        """
        d = collections.Counter(s)
        ans = -1
        for x, c in enumerate(s):
            if d[c] == 1:
                ans = x
                break
        return ans

# V1 
# http://bookshadow.com/weblog/2016/08/21/leetcode-first-unique-character-in-a-string/
import collections
class Solution(object):
    def firstUniqChar(self, s):
        """
        :type s: str
        :rtype: int
        """
        d = collections.Counter(s)
        ans = -1
        for x, c in enumerate(s):
            if d[c] == 1:
                ans = x
                break
        return ans

# V1'
# https://www.jiuzhang.com/solution/first-unique-character-in-a-string/#tag-highlight-lang-python
class Solution:
    """
    @param str: str: the given string
    @return: char: the first unique character in a given string
    """
    def firstUniqChar(self, str):
        counter = {}

        for c in str:
            counter[c] = counter.get(c, 0) + 1

        for c in str:
            if counter[c] == 1:
                return c

# V2 
class Solution(object):
    def firstUniqChar(self, s):
        s_ = ''.join(set(s))
        non_uniq_list = set([ x for x in s_ if s.count(x) > 1  ])
        for i,j in enumerate(s):
            if j in non_uniq_list:
                pass
            else:
                return i            
        return -1 

# V3 
# IDEA : hash table 
class Solution(object):
    def firstUniqChar(self, s):
        counts = {}
        for i, j in enumerate(s):
            if j in counts:
                counts[j] += 1 
            else:
                counts[j] = 1 
        print (counts)
        for i, j in enumerate(s):
            if counts[s[i]] == 1:
                return i
        return -1 

# V4 
from collections import defaultdict
class Solution(object):
    def firstUniqChar(self, s):
        """
        :type s: str
        :rtype: int
        """
        lookup = defaultdict(int)
        candidtates = set()
        for i, c in enumerate(s):
            if lookup[c]:
                candidtates.discard(lookup[c])
            else:
                lookup[c] = i+1
                candidtates.add(i+1)
        return min(candidtates)-1 if candidtates else -1