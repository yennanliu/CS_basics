# Time:  O(n)
# Space: O(n)

# Given a string, find the first non-repeating character in it and
# return it's index. If it doesn't exist, return -1.
#
# Examples:
#
# s = "leetcode"
# return 0.
#
# s = "loveleetcode",
# return 2.
# Note: You may assume the string contain only lowercase letters.


# V1 

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


# V2 
# hash table 
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


# V3 
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



