"""

830. Positions of Large Groups
Easy

In a string s of lowercase letters, these letters form consecutive groups of the same character.

For example, a string like s = "abbxxxxzyy" has the groups "a", "bb", "xxxx", "z", and "yy".

A group is identified by an interval [start, end], where start and end denote the start and end indices (inclusive) of the group. In the above example, "xxxx" has the interval [3,6].

A group is considered large if it has 3 or more characters.

Return the intervals of every large group sorted in increasing order by start index.

Example 1:

Input: s = "abbxxxxzzy"
Output: [[3,6]]
Explanation: "xxxx" is the only large group with start index 3 and end index 6.

Example 2:

Input: s = "abc"
Output: []
Explanation: We have groups "a", "b", and "c", none of which are large groups.

Example 3:

Input: s = "abcdddeeeeaabbbcd"
Output: [[3,5],[6,9],[12,14]]
Explanation: The large groups are "ddd", "eeee", and "bbb".

Constraints:

1 <= s.length <= 1000
s contains lowercase English letters only.

"""

# V0 

# V1
# IDEA : STRING
# time = O(n)
# space = O(1)
class Solution(object):
    def largeGroupPositions(self, S):
        """
        :type S: str
        :rtype: List[List[int]]
        """
        res = []
        i = 0
        while i < len(S):
            j = i
            while j < len(S) and S[i] == S[j]:
                j += 1
            if j - i >= 3:
                res.append([i, j - 1])
                i = j
            else:
                i += 1
        return res

# V1'
# http://bookshadow.com/weblog/2018/05/06/leetcode-positions-of-large-groups/
# IDEA : TWO POINTERS
# time = O(n)
# space = O(1)
class Solution(object):
    def largeGroupPositions(self, S):
        """
        :type S: str
        :rtype: List[List[int]]
        """
        j = -1
        d = ''
        ans = []
        for i, c in enumerate(S + '#'):
            if c != d:
                if i - j >= 3:
                    ans.append([j, i - 1])
                j = i
            d = c
        return ans

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/80472242
# IDEA : GREEDY
# time = O(n)
# space = O(1)
class Solution:
    def largeGroupPositions(self, S):
        """
        :type S: str
        :rtype: List[List[int]]
        """
        groups = []
        before_index, before_char = 0, S[0]
        for i, s in enumerate(S):
            if s != before_char:
                if i - before_index >= 3:
                    groups.append([before_index, i - 1])
                before_index = i
                before_char = s
        if i - before_index >= 2:
            groups.append([before_index, i])
        return groups

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/80472242
# IDEA : GREEDY
# time = O(n)
# space = O(1)
class Solution:
    def largeGroupPositions(self, S):
        """
        :type S: str
        :rtype: List[List[int]]
        """
        S = S + "A"
        groups = []
        previndex, prevc = 0, ""
        for i, c in enumerate(S):
            if not prevc:
                prevc = c
                previndex = i
            elif prevc != c:
                if i - previndex >= 3:
                    groups.append([previndex, i - 1])
                previndex = i
                prevc = c
        return groups

# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def largeGroupPositions(self, S):
        """
        :type S: str
        :rtype: List[List[int]]
        """
        result = []
        i = 0
        for j in range(len(S)):
            if j == len(S)-1 or S[j] != S[j+1]:
                if j-i+1 >= 3:
                    result.append([i, j])
                i = j+1
        return result