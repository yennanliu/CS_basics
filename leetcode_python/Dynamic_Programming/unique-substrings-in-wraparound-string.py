# V0 

# V1
# http://bookshadow.com/weblog/2016/12/04/leetcode-unique-substrings-in-wraparound-string/
"""

DP def
    every substring of the infinite wraparound string is uniquely identified by
    its FIRST letter and its LENGTH - so counting distinct substrings means,
    per starting letter, keeping only the LONGEST one.

    cmap[c]: the length of the longest contiguous run in p that STARTS with

             letter c and is a substring of "...zabcdefghijklmnopqrstuvwxyz..."

DP eq

     split p into maximal runs where each adjacent pair is consecutive in the
     alphabet (with z -> a allowed). for a run p[start:end]:

        cmap[p[x]] = max( cmap[p[x]], end - x )   for every x in [start, end)


    -> e.g. a run of length L contains exactly L distinct valid substrings
              starting at its first letter, L-1 at its second, and so on -
              taking the MAX per letter de-duplicates across runs

     ans = sum(cmap.values())

"""
# time = O(n), n = len(p)
# space = O(1)  # cmap bounded by 26 letters
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
"""

DP def
    every substring of the infinite wraparound string is uniquely identified by
    its FIRST letter and its LENGTH - so counting distinct substrings means,
    per starting letter, keeping only the LONGEST one.

    cmap[c]: the length of the longest contiguous run in p that STARTS with

             letter c and is a substring of "...zabcdefghijklmnopqrstuvwxyz..."

DP eq

     split p into maximal runs where each adjacent pair is consecutive in the
     alphabet (with z -> a allowed). for a run p[start:end]:

        cmap[p[x]] = max( cmap[p[x]], end - x )   for every x in [start, end)


    -> e.g. a run of length L contains exactly L distinct valid substrings
              starting at its first letter, L-1 at its second, and so on -
              taking the MAX per letter de-duplicates across runs

     ans = sum(cmap.values())

"""
# time = O(n), n = len(p)
# space = O(1)  # count bounded by 26 letters
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
"""

DP def
    every substring of the infinite wraparound string is uniquely identified by
    its FIRST letter and its LENGTH - so counting distinct substrings means,
    per starting letter, keeping only the LONGEST one.

    cmap[c]: the length of the longest contiguous run in p that STARTS with

             letter c and is a substring of "...zabcdefghijklmnopqrstuvwxyz..."

DP eq

     split p into maximal runs where each adjacent pair is consecutive in the
     alphabet (with z -> a allowed). for a run p[start:end]:

        cmap[p[x]] = max( cmap[p[x]], end - x )   for every x in [start, end)


    -> e.g. a run of length L contains exactly L distinct valid substrings
              starting at its first letter, L-1 at its second, and so on -
              taking the MAX per letter de-duplicates across runs

     ans = sum(cmap.values())

"""
# time = O(n), n = len(p)
# space = O(1)  # res bounded by 26 letters
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
"""

DP def
    every substring of the infinite wraparound string is uniquely identified by
    its FIRST letter and its LENGTH - so counting distinct substrings means,
    per starting letter, keeping only the LONGEST one.

    cmap[c]: the length of the longest contiguous run in p that STARTS with

             letter c and is a substring of "...zabcdefghijklmnopqrstuvwxyz..."

DP eq

     split p into maximal runs where each adjacent pair is consecutive in the
     alphabet (with z -> a allowed). for a run p[start:end]:

        cmap[p[x]] = max( cmap[p[x]], end - x )   for every x in [start, end)


    -> e.g. a run of length L contains exactly L distinct valid substrings
              starting at its first letter, L-1 at its second, and so on -
              taking the MAX per letter de-duplicates across runs

     ans = sum(cmap.values())

"""
# time = O(n), n = len(p)
# space = O(1)  # letters array of fixed size 26
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