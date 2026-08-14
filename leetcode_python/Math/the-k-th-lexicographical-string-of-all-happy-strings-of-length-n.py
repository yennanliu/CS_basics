"""

1415. The k-th Lexicographical String of All Happy Strings of Length n
Medium

A happy string is a string that:

consists only of letters of the set ['a', 'b', 'c'].
s[i] != s[i + 1] for all values of i from 1 to s.length - 1 (string is 1-indexed).

For example, strings "abc", "ac", "b" and "abcbabcbcb" are all happy strings and strings "aa", "baa" and "ababbc" are not happy strings.

Given two integers n and k, consider a list of all happy strings of length n sorted in lexicographical order.

Return the kth string of this list or return an empty string if there are less than k happy strings of length n.


Example 1:

Input: n = 1, k = 3
Output: "c"
Explanation: The list ["a", "b", "c"] contains all happy strings of length 1. The third string is "c".

Example 2:

Input: n = 1, k = 4
Output: ""
Explanation: There are only 3 happy strings of length 1.

Example 3:

Input: n = 3, k = 9
Output: "cab"
Explanation: There are 12 different happy string of length 3 ["aba", "abc", "aca", "acb", "bab", "bac", "bca", "bcb", "cab", "cac", "cba", "cbc"]. You will find the 9th string = "cab"


Constraints:

1 <= n <= 10
1 <= k <= 100

"""

# V0
# IDEA : COUNTING / DIGIT SELECTION (no need to enumerate every string)
#
#   the first letter has 3 options, every later letter has exactly 2
#   (anything but the previous letter), so there are 3 * 2^(n-1) happy
#   strings and each starting letter owns a contiguous block of 2^(n-1).
#   pick letters left to right: with `block` strings under each branch,
#   index // block chooses the branch, index %= block descends into it.
#   NOTE : the 2 remaining letters, taken in "abc" order, are already in
#          lexicographic order -> the block ordering is the right one.
#
# time = O(n), space = O(n)
class Solution(object):
    def getHappyString(self, n, k):
        total = 3 * (1 << (n - 1))
        if k > total:
            return ""

        idx = k - 1                 # 0-based rank
        block = 1 << (n - 1)        # strings per first-letter branch
        res = ["abc"[idx // block]]
        idx %= block

        for _ in range(n - 1):
            block >>= 1
            options = [c for c in "abc" if c != res[-1]]
            res.append(options[idx // block])
            idx %= block

        return "".join(res)
