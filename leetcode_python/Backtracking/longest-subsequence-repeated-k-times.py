"""

2014. Longest Subsequence Repeated k Times
Hard

You are given a string s of length n, and an integer k. You are tasked to find the longest subsequence repeated k times in string s.

A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.

A subsequence seq is repeated k times in the string s if seq * k is a subsequence of s, where seq * k represents a string constructed by concatenating seq k times.

For example, "bba" is repeated 2 times in the string "bababcba", because the string "bbabba", constructed by concatenating "bba" 2 times, is a subsequence of the string "bababcba".

Return the longest subsequence repeated k times in string s. If multiple such subsequences are found, return the lexicographically largest one. If there is no such subsequence, return an empty string.


Example 1:

Input: s = "letsleetcode", k = 2
Output: "let"
Explanation: There are two longest subsequences repeated 2 times: "let" and "ete".
"let" is the lexicographically largest one.

Example 2:

Input: s = "bb", k = 2
Output: "b"
Explanation: The longest subsequence repeated 2 times is "b".

Example 3:

Input: s = "ab", k = 2
Output: ""
Explanation: There is no subsequence repeated 2 times. Empty string is returned.


Constraints:

n == s.length
2 <= k <= 2000
2 <= n < min(2001, k * 8)
s consists of lowercase English letters.

"""

# V0
# IDEA : BFS OVER CANDIDATES (grow level by level, prune with a greedy check)
#
#   n < k * 8  =>  the answer is at most 7 characters long, and only letters
#   occurring at least k times can appear in it at all.
#
#   BFS from "" and extend every surviving candidate by each usable letter.
#   a candidate survives iff cand * k is a subsequence of s (greedy scan).
#   any prefix of a valid answer is itself valid, so pruning is safe.
#
#   NOTE : visiting the letters in ASCENDING order while the queue is FIFO makes
#          each BFS level come out in ascending lexicographic order, so the LAST
#          candidate accepted is the longest & lexicographically largest one.
#
# time = O(26^L * n) with L <= 7 but the k-repetition prune keeps it tiny
# space = O(number of surviving candidates)
from collections import Counter, deque
class Solution(object):
    def longestSubsequenceRepeatedK(self, s, k):
        def repeated_k(t):
            # is t * k a subsequence of s ?
            need = k
            i = 0
            for ch in s:
                if ch == t[i]:
                    i += 1
                    if i == len(t):
                        i = 0
                        need -= 1
                        if need == 0:
                            return True
            return False

        cnt = Counter(s)
        letters = [c for c in "abcdefghijklmnopqrstuvwxyz" if cnt[c] >= k]

        res = ""
        q = deque([""])
        while q:
            cur = q.popleft()
            for c in letters:
                nxt = cur + c
                if repeated_k(nxt):
                    res = nxt
                    q.append(nxt)

        return res
