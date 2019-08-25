# V0 

# V1
# IDEA : STRING 
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
# Time:  O(n)
# Space: O(1)
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