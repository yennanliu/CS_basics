"""

49. Group Anagrams
Medium

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
# time = O(n + m log m), n = len(nums), m = number of unique elements
# space = O(m)
class Solution(object):
    def topKFrequent(self, nums, k):
        # edge
        if nums is None:
            return []

        # frequency map
        freq = {}
        for x in nums:
            if x not in freq:
                freq[x] = 1
            else:
                freq[x] += 1

        # if k >= unique count
        if k >= len(freq):
            return list(freq.keys())

        # [num, count]
        num_cnt = []
        for num, cnt in freq.items():
            num_cnt.append([num, cnt])

        # sort by frequency descending
        num_cnt.sort(key=lambda x: x[1], reverse=True)

        res = []
        for i in range(k):
            res.append(num_cnt[i][0])

        return res



# V0-0-X
# IDEA : HASH TABLE
# time = O(n + m log m), n = len(nums), m = number of unique elements
# space = O(m)
class Solution(object):
    def topKFrequent(self, nums, k):
        freq = {}

        for n in nums:
            freq[n] = freq.get(n, 0) + 1

        arr = sorted(freq.items(), key=lambda x: x[1], reverse=True)

        return [num for num, cnt in arr[:k]]


# V0-0-1
# IDEA : HASH TABLE
# time = O(N * K log K), N = len(strs), K = max length of a string
# space = O(N * K)
class Solution:
    def groupAnagrams(self, strs):
        res = {}
        for item in strs:
            k = ''.join(sorted(item))  # sort the string 
            if k not in res:  #  check if exists in res 
                res[k] = []
            res[k].append(item)  # if same, put all the same string into dict k 
        return [res[x] for x in res]  # output the result 



# V0-1
# time = O(N * K log K), N = len(strs), K = max length of a string
# space = O(N * K)
class Solution(object):
    def groupAnagrams(self, strs):
        groups = {}

        for s in strs:
            key = "".join(sorted(s))

            if key not in groups:
                groups[key] = []

            groups[key].append(s)

        return list(groups.values())


# V0-0-1
# time = O(N * K), N = len(strs), K = max length of a string
# space = O(N * K)
class Solution(object):
    def groupAnagrams(self, strs):
        groups = {}

        for s in strs:
            count = [0] * 26

            for c in s:
                count[ord(c) - ord('a')] += 1

            key = tuple(count)

            if key not in groups:
                groups[key] = []

            groups[key].append(s)

        return list(groups.values())


# V0'
# IDEA : SORT LIST strs.sort(key = lambda x : ''.join(sorted(x)) )
# time = O(N * K * (log N + log K)), N = len(strs), K = max length of a string
# space = O(N * K)
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

# V0''
# IDEA : SORT + DICT
# time = O(N * K log K), N = len(strs), K = max length of a string
# space = O(N * K)
class Solution(object):
    def groupAnagrams(self, strs):

        r = []
        d = {}

        if len(strs) == 1:
            return [[strs[0]]]

        for _str in strs:
            tmp = list(_str)
            tmp.sort()
            _str_sorted = ''.join(tmp)

            if _str_sorted not in d:
                d[_str_sorted] = [_str]
            else:
                d[_str_sorted].append(_str)

        for k in d.keys():
            r.append(d[k])

        return r

# V1 
# https://blog.csdn.net/XX_123_1_RJ/article/details/81145095
# IDEA : SORT + DICT
# time = O(N * K log K), N = len(strs), K = max length of a string
# space = O(N * K)
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
# time = O(N * K), N = len(strs), K = max length of a string
# space = O(N * K)
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
# time = O(N * K * (log N + log K)), N = len(strs), K = max length of a string
# space = O(N * K)
class Solution:
    def groupAnagrams(self, strs):
        dic = {}
        for item in sorted(strs):
            sortedItem = ''.join(sorted(item))
            dic[sortedItem] = dic.get(sortedItem, []) + [item]
        return dic.values()

# V2
# time = O(n * g log g), g is the max size of groups
# space = O(n)
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