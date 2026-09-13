"""

916. Word Subsets
Medium

You are given two string arrays words1 and words2.

A string b is a subset of string a if every letter in b occurs in a including multiplicity.

For example, "wrr" is a subset of "warrior" but is not a subset of "world".

A string a from words1 is universal if for every string b in words2, b is a subset of a.

Return an array of all the universal strings in words1. You may return the answer in any order.

Example 1:

Input: words1 = ["amazon","apple","facebook","google","leetcode"], words2 = ["e","o"]
Output: ["facebook","google","leetcode"]

Example 2:

Input: words1 = ["amazon","apple","facebook","google","leetcode"], words2 = ["lc","eo"]
Output: ["leetcode"]

Example 3:

Input: words1 = ["acaac","cccbb","aacbb","caacc","bcbbb"], words2 = ["c","cc","b"]
Output: ["cccbb"]

Constraints:

1 <= words1.length, words2.length <= 10^4
1 <= words1[i].length, words2[i].length <= 10
words1[i] and words2[i] consist only of lowercase English letters.
All the strings of words1 are unique.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82914193
# time = O(m + n)
# space = O(n)
class Solution(object):
    def wordSubsets(self, A, B):
        """
        :type A: List[str]
        :type B: List[str]
        :rtype: List[str]
        """
        B = set(B)
        res = []
        count = collections.defaultdict(int)
        for b in B:
            cb = collections.Counter(b)
            for c, v in cb.items():
                count[c] = max(count[c], v)
        res = []
        for a in A:
            ca = collections.Counter(a)
            isSuccess = True
            for c, v in count.items():
                if v > ca[c]:
                    isSuccess = False
                    break
            if isSuccess:
                res.append(a)
        return res
        
# V2
# time = O(m + n)
# space = O(1)
import collections
class Solution(object):
    def wordSubsets(self, A, B):
        """
        :type A: List[str]
        :type B: List[str]
        :rtype: List[str]
        """
        count = collections.Counter()
        for b in B:
            for c, n in collections.Counter(b).items():
                count[c] = max(count[c], n)
        result = []
        for a in A:
            count = collections.Counter(a)
            if all(count[c] >= count[c] for c in count):
                result.append(a)
        return result
