"""
Given a string s and a non-empty string p, find all the start indices of p's anagrams in s.

Strings consists of lowercase English letters only and the length of both strings s and p will not be larger than 20,100.

The order of output does not matter.

Example 1:

Input:
s: "cbaebabacd" p: "abc"

Output:
[0, 6]

Explanation:
The substring with start index = 0 is "cba", which is an anagram of "abc".
The substring with start index = 6 is "bac", which is an anagram of "abc".
Example 2:

Input:
s: "abab" p: "ab"

Output:
[0, 1, 2]

Explanation:
The substring with start index = 0 is "ab", which is an anagram of "ab".
The substring with start index = 1 is "ba", which is an anagram of "ab".
The substring with start index = 2 is "ab", which is an anagram of "ab".

"""
# Time:  O(n)
# Space: O(1)

# Given a string s and a non-empty string p, find all the start indices
# of p's anagrams in s.
#
# Strings consists of lowercase English letters only and the length of
# both strings s and p will not be larger than 20,100.
#
# The order of output does not matter.
#
# Example 1:
#
# Input:
# s: "cbaebabacd" p: "abc"
#
# Output:
# [0, 6]
#
# Explanation:
# The substring with start index = 0 is "cba", which is an anagram of "abc".
# The substring with start index = 6 is "bac", which is an anagram of "abc".
# Example 2:
#
# Input:
# s: "abab" p: "ab"
#
# Output:
# [0, 1, 2]
#
# Explanation:
# The substring with start index = 0 is "ab", which is an anagram of "ab".
# The substring with start index = 1 is "ba", which is an anagram of "ab".
# The substring with start index = 2 is "ab", which is an anagram of "ab".

# V0
# IDEA : SLIDING WINDOW + collections.Counter()
class Solution(object):
    def findAnagrams(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: List[int]
        """
        ls, lp = len(s), len(p)
        cp = collections.Counter(p)
        cs = collections.Counter()
        ans = []
        for i in range(ls):
            cs[s[i]] += 1
            if i >= lp:
                cs[s[i - lp]] -= 1
                ### BE AWARE OF IT
                if cs[s[i - lp]] == 0:
                    del cs[s[i - lp]]
            if cs == cp:
                ans.append(i - lp + 1)
        return ans
      
# V0' : DEV : LTE (LIMITED TIME OUT ERROR)
# from collections import Counter
# class Solution(object):
#     def findAnagrams(self, s, p):
#         answer = []
#         m, n = len(s), len(p)
#         if m < n:
#             return answer
#         pCounter = Counter(p)
#         sCounter = Counter(s[:n-1])
#         for i in range(0, len(s) - len(p)+1):
#             tmp = s[i : i+len(p)]
#             if Counter(tmp) == pCounter:
#                 answer.append(i)
#         return answer

# V0''
from collections import Counter
class Solution(object):
    def findAnagrams(self, s, p):
        answer = []
        m, n = len(s), len(p)
        if m < n:
            return answer
        pCounter = Counter(p)
        sCounter = Counter(s[:n-1])
        index = 0
        for index in range(n - 1, m):
                sCounter[s[index]] += 1
                if sCounter == pCounter:
                    answer.append(index - (n -1))
                sCounter[s[index - (n - 1)]] -= 1
                if sCounter[s[index - (n - 1)]] == 0:   # NOTE : HAVE TO REMOVE "COUNT = 0" CASE IN COUNTER 
                    del sCounter[s[index - (n - 1)]]
        return answer

# V1 
# http://bookshadow.com/weblog/2016/10/23/leetcode-find-all-anagrams-in-a-string/
# https://blog.csdn.net/fuxuemingzhu/article/details/79184109
# IDEA : collections.Counter 
class Solution(object):
    def findAnagrams(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: List[int]
        """
        ls, lp = len(s), len(p)
        cp = collections.Counter(p)
        cs = collections.Counter()
        ans = []
        for i in range(ls):
            cs[s[i]] += 1
            if i >= lp:
                cs[s[i - lp]] -= 1
                if cs[s[i - lp]] == 0:
                    del cs[s[i - lp]]
            if cs == cp:
                ans.append(i - lp + 1)
        return ans

 
# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79184109
# IDEA : collections.Counter
from collections import Counter
class Solution(object):
    def findAnagrams(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: List[int]
        """
        answer = []
        m, n = len(s), len(p)
        if m < n:
            return answer
        pCounter = Counter(p)
        sCounter = Counter(s[:n-1])
        index = 0
        for index in range(n - 1, m):
                sCounter[s[index]] += 1
                if sCounter == pCounter:
                    answer.append(index - n + 1)
                sCounter[s[index - n + 1]] -= 1
                if sCounter[s[index - n + 1]] == 0:   # NOTE : HAVE TO REMOVE "COUNT = 0" CASE IN COUNTER 
                    del sCounter[s[index - n + 1]]
        return answer

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79184109
# IDEA : TWO POINTERS 
class Solution(object):
    def findAnagrams(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: List[int]
        """
        count = collections.Counter()
        M, N = len(s), len(p)
        left, right = 0, 0
        pcount = collections.Counter(p)
        res = []
        while right < M:
            count[s[right]] += 1
            if right - left + 1 == N:
                if count == pcount:
                    res.append(left)
                count[s[left]] -= 1
                if count[s[left]] == 0:
                    del count[s[left]]
                left += 1
            right += 1
        return res

# V1''
# http://zxi.mytechroad.com/blog/hashtable/leetcode-438-find-all-anagrams-in-a-string/
# C++
# // Author: Huahua
# // Running time: 35 ms
# class Solution {
# public:
#   vector<int> findAnagrams(string s, string p) {
#     int n = s.length();
#     int l = p.length();
#     vector<int> ans;
#     vector<int> vp(26, 0);
#     vector<int> vs(26, 0);
#     for (char c : p) ++vp[c - 'a'];    
#     for (int i = 0; i < n; ++i) {
#       if (i >= l) --vs[s[i - l] - 'a'];        
#       ++vs[s[i] - 'a'];
#       if (vs == vp) ans.push_back(i + 1 - l);
#     }
#     return ans;
#   }
# };

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findAnagrams(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: List[int]
        """
        result = []

        cnts = [0] * 26
        for c in p:
            cnts[ord(c) - ord('a')] += 1

        left, right = 0, 0
        while right < len(s):
            cnts[ord(s[right]) - ord('a')] -= 1
            while left <= right and cnts[ord(s[right]) - ord('a')] < 0:
                cnts[ord(s[left]) - ord('a')] += 1
                left += 1
            if right - left + 1 == len(p):
                result.append(left)
            right += 1

        return result