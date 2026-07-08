"""

524. Longest Word in Dictionary through Deleting
Solved
Medium
Topics
premium lock icon
Companies
Given a string s and a string array dictionary, return the longest string in the dictionary that can be formed by deleting some of the given string characters. If there is more than one possible result, return the longest word with the smallest lexicographical order. If there is no possible result, return the empty string.

 

Example 1:

Input: s = "abpcplea", dictionary = ["ale","apple","monkey","plea"]
Output: "apple"
Example 2:

Input: s = "abpcplea", dictionary = ["a","b","c"]
Output: "a"
 

Constraints:

1 <= s.length <= 1000
1 <= dictionary.length <= 1000
1 <= dictionary[i].length <= 1000
s and dictionary[i] consist of lowercase English letters.
 

"""


# V0
class Solution(object):
    def findLongestWord(self, s, dictionary):
        """
        :type s: str
        :type dictionary: List[str]
        :rtype: str
        """
        pass


# V1 
# http://bookshadow.com/weblog/2017/02/26/leetcode-longest-word-in-dictionary-through-deleting/
# IDEA : GREEDY + BFS 
class Solution(object):
    def findLongestWord(self, s, d):
        """
        :type s: str
        :type d: List[str]
        :rtype: str
        """
        ans = []
        dmap = collections.defaultdict(list)
        for w in d:
            dmap[w[0]].append((0, w))
        for c in s:
            wlist = dmap[c]
            del dmap[c]
            for i, w in wlist:
                if i + 1 == len(w):
                    ans.append(w)
                else:
                    dmap[w[i + 1]].append((i + 1, w))
        if not ans: return ''
        maxl = len(max(ans, key = len))
        return min(w for w in ans if len(w) == maxl)

# V2 
# Time:  O((d * l) * logd), l is the average length of words
# Space: O(1)
class Solution(object):
    def findLongestWord(self, s, d):
        """
        :type s: str
        :type d: List[str]
        :rtype: str
        """
        d.sort(key = lambda x: (-len(x), x))
        for word in d:
            i = 0
            for c in s:
                if i < len(word) and word[i] == c:
                    i += 1
            if i == len(word):
                return word
        return ""
