# V0 

# V1
# http://bookshadow.com/weblog/2016/12/04/leetcode-unique-substrings-in-wraparound-string/
class Solution(object):
    def findSubstringInWraproundString(self, p):
        """
        :type p: str
        :rtype: int
        """
        pattern = 'zabcdefghijklmnopqrstuvwxyz'
        cmap = collections.defaultdict(int)
        start = end = 0
        for c in range(len(p)):
            if c and p[c-1:c+1] not in pattern:
                for x in range(start, end):
                    cmap[p[x]] = max(end - x, cmap[p[x]])
                start = c
            end = c + 1
        for x in range(start, end):
            cmap[p[x]] = max(end - x, cmap[p[x]])
        return sum(cmap.values())

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83088406
class Solution:
    def findSubstringInWraproundString(self, p):
        """
        :type p: str
        :rtype: int
        """
        count = collections.defaultdict(int)
        N = len(p)
        _len = 0
        for i in range(N):
            if i > 0 and (ord(p[i]) - ord(p[i - 1]) == 1 or (p[i] == 'a' and p[i - 1] == 'z')):
                _len += 1
            else:
                _len = 1
            count[p[i]] = max(count[p[i]], _len)
        return sum(count.values())

# V1''
# https://www.jiuzhang.com/solution/unique-substrings-in-wraparound-string/#tag-highlight-lang-python
class Solution:
    """
    @param p: the given string
    @return: the number of different non-empty substrings of p in the string s
    """
    def findSubstringInWraproundString(self, p):
        res = {i: 1 for i in p}
        l = 1
        for i, j in zip(p, p[1:]):
            l = l + 1 if (ord(j) - ord(i)) % 26 == 1 else 1
            res[j] = max(res[j], l)
        return sum(res.values())

# V2
class Solution(object):
    def findSubstringInWraproundString(self, p):
        """
        :type p: str
        :rtype: int
        """
        letters = [0] * 26
        result, length = 0, 0
        for i in range(len(p)):
            curr = ord(p[i]) - ord('a')
            if i > 0 and ord(p[i-1]) != (curr-1)%26 + ord('a'):
                length = 0
            length += 1
            if length > letters[curr]:
                result += length - letters[curr]
                letters[curr] = length
        return result