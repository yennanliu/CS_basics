"""

3144. Minimum Substring Partition of Equal Character Frequency
Medium

Given a string s, you need to partition it into one or more balanced substrings. For example, if s == "ababcc" then ("abab", "c", "c"), ("ab", "abc", "c"), and ("ababcc") are all valid partitions, but ("a", "bab", "cc"), ("aba", "bc", "c"), and ("ab", "abcc") are not. The unbalanced substrings are bolded.

Return the minimum number of substrings that you can partition s into.

Note: A balanced string is a string where each character in the string occurs the same number of times.


Example 1:

Input: s = "fabccddg"
Output: 3
Explanation:
We can partition the string s into 3 substrings in one of the following ways: ("fab, "ccdd", "g"), or ("fabc", "cd", "dg").

Example 2:

Input: s = "abababaccddb"
Output: 2
Explanation:
We can partition the string s into 2 substrings like so: ("abab", "abaccddb").


Constraints:

1 <= s.length <= 1000
s consists only of English lowercase letters.

"""

# V0
# IDEA : DP OVER PREFIXES, EXTENDING THE LAST PIECE LEFTWARDS
#
#   dp[i] = fewest balanced pieces covering s[:i]. to fill it, walk the split
#   point j from i - 1 down to 0, maintaining the letter counts of s[j..i-1]
#   incrementally, and whenever that window is balanced take
#
#       dp[i] = min(dp[i], dp[j] + 1)
#
#   "balanced" is cheap to test while sweeping : keep the running maximum
#   count and the number of DISTINCT letters, and the window qualifies iff
#       distinct * max_count == window length
#   (every present letter must hit the maximum, and their totals must fill
#   the window exactly).
#
# time = O(n^2), space = O(n)
class Solution(object):
    def minimumSubstringsInPartition(self, s):
        n = len(s)
        INF = float('inf')
        dp = [INF] * (n + 1)
        dp[0] = 0

        for i in range(1, n + 1):
            cnt = [0] * 26
            distinct = 0
            mx = 0
            for j in range(i - 1, -1, -1):
                c = ord(s[j]) - 97
                if cnt[c] == 0:
                    distinct += 1
                cnt[c] += 1
                if cnt[c] > mx:
                    mx = cnt[c]
                if distinct * mx == i - j and dp[j] + 1 < dp[i]:
                    dp[i] = dp[j] + 1
        return dp[n]
