# V0
class Solution:
    def groupAnagrams(self, strs):
        res = {}
        for item in strs:
            k = ''.join(sorted(item))  # sort the string 
            if k not in res:  #  check if exists in res 
                res[k] = []
            res[k].append(item)  # if same, put all the same string into dict k 
        return [res[x] for x in res]  # output the result 
        
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