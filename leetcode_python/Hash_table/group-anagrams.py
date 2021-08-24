"""

Given an array of strings strs, group the anagrams together. You can return the answer in any order.

An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

 

Example 1:

Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
Example 2:

Input: strs = [""]
Output: [[""]]
Example 3:

Input: strs = ["a"]
Output: [["a"]]
 

Constraints:

1 <= strs.length <= 104
0 <= strs[i].length <= 100
strs[i] consists of lowercase English letters.

"""

# V0
# IDEA : HASH TABLE
class Solution:
    def groupAnagrams(self, strs):
        res = {}
        for item in strs:
            k = ''.join(sorted(item))  # sort the string 
            if k not in res:  #  check if exists in res 
                res[k] = []
            res[k].append(item)  # if same, put all the same string into dict k 
        return [res[x] for x in res]  # output the result 

# V0'
# IDEA : SORT LIST strs.sort(key = lambda x : ''.join(sorted(x)) )
class Solution:
    def groupAnagrams(self, strs):
        r = []
        cache = set()
        strs.sort(key = lambda x : ''.join(sorted(x)) )
        j = 0
        for i in range(len(strs)):
            tmp = ''.join(sorted(strs[i]))
            if j >= len(r):
                r.append([])
            if tmp not in cache:
                cache.add(tmp)
                j += 1
            r[j-1].append(strs[i])
        return [x for x in r if len(x) > 0]
      
# V1 
# https://blog.csdn.net/XX_123_1_RJ/article/details/81145095
# IDEA : SORT + DICT 
class Solution:
    def groupAnagrams(self, strs):
        res = {}
        for item in strs:
            k = ''.join(sorted(item))  # sort the string 
            if k not in res:  #  check if exists in res 
                res[k] = []
            res[k].append(item)  # if same, put all the same string into dict k 
        return [res[x] for x in res]  # output the result 

# V1' 
# https://blog.csdn.net/XX_123_1_RJ/article/details/81145095
class Solution:
    def groupAnagrams(self, strs):
        res = {}
        for s in strs:
            count = [0] * 26  
            for c in s:  
                count[ord(c) - ord('a')] += 1
            if tuple(count) not in res:
                res[tuple(count)] = []
            res[tuple(count)].append(s)  
        return list(res.values())  

# V1''
# https://www.jiuzhang.com/solution/group-anagrams/#tag-highlight-lang-python
class Solution:
    def groupAnagrams(self, strs):
        dic = {}
        for item in sorted(strs):
            sortedItem = ''.join(sorted(item))
            dic[sortedItem] = dic.get(sortedItem, []) + [item]
        return dic.values()

# V2 
# Time:  O(n * glogg), g is the max size of groups.
# Space: O(n)
import collections
class Solution(object):
    def groupAnagrams(self, strs):
        """
        :type strs: List[str]
        :rtype: List[List[str]]
        """
        anagrams_map, result = collections.defaultdict(list), []
        for s in strs:
            sorted_str = ("").join(sorted(s))
            anagrams_map[sorted_str].append(s)
        for anagram in anagrams_map.values():
            anagram.sort()
            result.append(anagram)
        return result