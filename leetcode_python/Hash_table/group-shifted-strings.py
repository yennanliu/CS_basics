"""
Given a string, we can "shift" each of its letter to its successive letter, for example: "abc" -> "bcd". We can keep "shifting" which forms the sequence:

"abc" -> "bcd" -> ... -> "xyz"
Given a list of strings which contains only lowercase alphabets, group all strings that belong to the same shifting sequence.

For example, given: ["abc", "bcd", "acef", "xyz", "az", "ba", "a", "z"], 
Return:

[
  ["abc","bcd","xyz"],
  ["az","ba"],
  ["acef"],
  ["a","z"]
]
 
Note: For the return value, each inner list's elements must follow the lexicographic order.

"""

# V0 

# V1 
# https://www.jiuzhang.com/solution/group-shifted-strings/#tag-highlight-lang-python
# time = O(N * K), N = len(strings), K = max string length
# space = O(N * K)
class Solution:
    """
    @param strings: a string array
    @return: return a list of string array
    """
    def groupStrings(self, strings):
        # write your code here
        ans, mp = [], {}
        for str in strings:
            x = ord(str[0]) - ord('a')
            tmp = ""
            for c in str:
                c = chr(ord(c) - x)
                if(c < 'a'):
                    c = chr(ord(c) + 26)
                tmp += c
            if(mp.get(tmp) == None):
                mp[tmp] = []
            mp[tmp].append(str)
        for x in mp:
            ans.append(mp[x])
        return ans

# V1'
# http://www.voidcn.com/article/p-znzuctot-qp.html
# time = O(n^2)  # n = len(strings); insertStr does a linear-time insertion into a sorted list for each string
# space = O(n * k)  # k = avg string length, dic stores hashed keys and string values
class Solution(object):
    def groupStrings(self, strings):
        """
        :type strings: List[str]
        :rtype: List[List[str]]
        """
        dic = {}
        for s in strings:
            hash = self.shiftHash(s)

            if hash not in dic:
                dic[hash] = [s]
            else:
                self.insertStr(dic[hash], s)

        return dic.values()

    # time = O(k)  # k = len(astring)
    # space = O(k)
    def shiftHash(self, astring):
        hashlist = [(ord(i) - ord(astring[0])) % 26 for i in astring]
        return tuple(hashlist)

    # time = O(m)  # m = current size of alist (linear scan + slicing insert)
    # space = O(m)  # slicing creates a new list
    def insertStr(self, alist, astring):
        i = 0
        while i < len(alist) and ord(astring[0]) > ord(alist[i][0]):
            i += 1
        if i == len(alist):
            alist.append(astring)
        else:
            alist[:] = alist[0:i] + [astring] + alist[i:]
                       
# V2
# time = O(n log n)  # n = len(strings), dominated by sorting each group
# space = O(n)
import collections
class Solution(object):
    # @param {string[]} strings
    # @return {string[][]}
    def groupStrings(self, strings):
        groups = collections.defaultdict(list)
        for s in strings:  # Grouping.
            groups[self.hashStr(s)].append(s)

        result = []
        for key, val in groups.iteritems():
            result.append(sorted(val))

        return result

    # time = O(k)  # k = len(s)
    # space = O(k)
    def hashStr(self, s):
        base = ord(s[0])
        hashcode = ""
        for i in range(len(s)):
            if ord(s[i]) - base >= 0:
                hashcode += unichr(ord('a') + ord(s[i]) - base)
            else:
                hashcode += unichr(ord('a') + ord(s[i]) - base + 26)
        return hashcode
