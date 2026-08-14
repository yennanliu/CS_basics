"""

1408. String Matching in an Array
Easy

Given an array of string words, return all strings in words that are a substring
of another word. You can return the answer in any order.


Example 1:

Input: words = ["mass","as","hero","superhero"]
Output: ["as","hero"]
Explanation: "as" is substring of "mass" and "hero" is substring of "superhero".
["hero","as"] is also a valid answer.

Example 2:

Input: words = ["leetcode","et","code"]
Output: ["et","code"]
Explanation: "et", "code" are substring of "leetcode".

Example 3:

Input: words = ["blue","green","bu"]
Output: []
Explanation: No string of words is substring of another string.


Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 30
words[i] contains only lowercase English letters.
All the strings of words are unique.

"""

# V0
# IDEA : BRUTE FORCE (n is tiny : n <= 100, len <= 30)
# time = O(n^2 * L^2), L = max word length
# space = O(n)
class Solution(object):
    def stringMatching(self, words):
        res = []
        for i, s in enumerate(words):
            for j, t in enumerate(words):
                if i != j and s in t:
                    res.append(s)
                    break
        return res

# V1
# IDEA : SORT BY LENGTH, only compare against strictly longer words
#        (all words are unique, so a substring must be shorter or equal;
#         equal-length distinct words can never contain each other)
# time = O(n^2 * L)
# space = O(n)
class Solution(object):
    def stringMatching(self, words):
        arr = sorted(words, key=len)
        res = []
        for i in range(len(arr)):
            for j in range(i + 1, len(arr)):
                if arr[i] in arr[j]:
                    res.append(arr[i])
                    break
        return res
