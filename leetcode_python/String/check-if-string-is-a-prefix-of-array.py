"""

1961. Check If String Is a Prefix of Array
Easy

Given a string s and an array of strings words, determine whether s is a prefix string of words.

A string s is a prefix string of words if s can be made by concatenating the first k strings in words for some positive k no larger than words.length.

Return true if s is a prefix string of words, or false otherwise.


Example 1:

Input: s = "iloveleetcode", words = ["i","love","leetcode","apples"]
Output: true
Explanation:
s can be made by concatenating "i", "love", and "leetcode" together.

Example 2:

Input: s = "iloveleetcode", words = ["apples","i","love","leetcode"]
Output: false
Explanation:
It is impossible to make s using a prefix of arr.


Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 20
1 <= s.length <= 1000
words[i] and s consist of only lowercase English letters.

"""

# V0
# IDEA : LINEAR SCAN (match words against s with a moving cursor)
#
#   keep a pointer `i` into s; for each word check that s[i:i+len(w)] == w.
#     - mismatch -> False immediately
#     - once i reaches len(s) we consumed exactly the first k words -> True
#
#   NOTE : running past the end of s is also a failure - the slice would just
#          be shorter than w, so the equality check catches it.
#
# time = O(n), n = len(s), space = O(1)
class Solution(object):
    def isPrefixString(self, s, words):
        i = 0
        n = len(s)
        for w in words:
            if s[i:i + len(w)] != w:
                return False
            i += len(w)
            if i == n:
                return True
        return False
