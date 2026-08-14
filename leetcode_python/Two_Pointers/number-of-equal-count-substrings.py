"""

2067. Number of Equal Count Substrings
Medium

You are given a 0-indexed string s consisting of only lowercase English letters, and an integer count. A substring of s is said to be an equal count substring if, for each unique letter in the substring, it appears exactly count times in the substring.

Return the number of equal count substrings in s.

A substring is a contiguous non-empty sequence of characters within a string.


Example 1:

Input: s = "aaabcbbcc", count = 3
Output: 3
Explanation:
The substring that starts at index 0 and ends at index 2 is "aaa".
The letter 'a' in the substring appears exactly 3 times.
The substring that starts at index 3 and ends at index 8 is "bcbbcc".
The letters 'b' and 'c' in the substring appear exactly 3 times.
The substring that starts at index 0 and ends at index 8 is "aaabcbbcc".
The letters 'a', 'b', and 'c' in the substring appear exactly 3 times.

Example 2:

Input: s = "abcd", count = 2
Output: 0
Explanation:
The number of times each letter appears in s is less than count.
Therefore, no substrings in s are equal count substrings, so return 0.

Example 3:

Input: s = "a", count = 5
Output: 0
Explanation:
The number of times each letter appears in s is less than count.
Therefore, no substrings in s are equal count substrings, so return 0


Constraints:

1 <= s.length <= 3 * 10^4
1 <= count <= 3 * 10^4
s consists only of lowercase English letters.

"""

# V0
# IDEA : FIXED-SIZE SLIDING WINDOW, ONE PASS PER DISTINCT-LETTER COUNT
#
#   if a substring contains exactly i distinct letters and each occurs
#   `count` times, its LENGTH is forced: i * count. i ranges over 1..26,
#   so 26 fixed-width sliding windows cover every candidate.
#
#   inside a pass, t tracks how many letters currently occur exactly
#   `count` times; the window is valid iff t == i (t == i means i letters
#   hit the target, and i * count characters are accounted for, leaving no
#   room for any other letter).
#
#   t is maintained in O(1) per move: a counter crossing `count` upward
#   adds 1, crossing count+1 removes it again, and symmetrically on eviction.
#
# time = O(26 * n), space = O(26)
class Solution(object):
    def equalCountSubstrings(self, s, count):
        n = len(s)
        res = 0
        for i in range(1, 27):
            k = i * count
            if k > n:
                break
            cnt = [0] * 26
            t = 0
            for j in range(n):
                a = ord(s[j]) - 97
                cnt[a] += 1
                if cnt[a] == count:
                    t += 1
                elif cnt[a] == count + 1:
                    t -= 1
                if j >= k:
                    b = ord(s[j - k]) - 97
                    cnt[b] -= 1
                    if cnt[b] == count:
                        t += 1
                    elif cnt[b] == count - 1:
                        t -= 1
                if t == i:
                    res += 1
        return res
