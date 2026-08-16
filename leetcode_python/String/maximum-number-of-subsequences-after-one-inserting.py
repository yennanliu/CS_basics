"""

3628. Maximum Number of Subsequences After One Inserting
Medium

You are given a string s consisting of uppercase English letters.

You are allowed to insert at most one uppercase English letter at any
position (including the beginning or end) of the string.

Return the maximum number of "LCT" subsequences that can be formed in the
resulting string after at most one insertion.

Example 1:

Input: s = "LMCT"
Output: 2
Explanation:
We can insert a "L" at the beginning of the string s to make "LLMCT", which
has 2 subsequences, at indices [0, 3, 4] and [1, 3, 4].

Example 2:

Input: s = "LCCT"
Output: 4
Explanation:
We can insert a "L" at the beginning of the string s to make "LLCCT", which
has 4 subsequences, at indices [0, 2, 4], [0, 3, 4], [1, 2, 4] and [1, 3,
4].

Example 3:

Input: s = "L"
Output: 0
Explanation:
Since it is not possible to obtain the subsequence "LCT" by inserting a
single letter, the result is 0.

Constraints:

1 <= s.length <= 10^5
s consists of uppercase English letters.

"""

# V0
# IDEA : COUNT "LCT" SUBSEQUENCES + TRY THE THREE USEFUL INSERTIONS
#
#   inserting anything other than 'L', 'C' or 'T' can never create a new
#   "LCT", so only three candidate insertions matter.
#
#   an inserted 'L' is best put at the very front, where it pairs with every
#   existing "CT" subsequence; symmetrically an inserted 'T' is best put at
#   the very end and adds every existing "LC" subsequence.
#
#   an inserted 'C' at position i adds (#L strictly before i) * (#T at or
#   after i), so one left-to-right sweep maximises it.
#
#   the three counters #CT, #LC and the base #LCT all come from a single
#   sweep that keeps running counts of L, LC and LCT prefixes (and a
#   backward sweep for CT / T).
#
# time = O(n), space = O(1)
class Solution(object):
    def numOfSubsequences(self, s):
        n = len(s)
        # forward: number of "L", "LC" prefixes and complete "LCT"
        cnt_l = 0
        cnt_lc = 0
        cnt_lct = 0
        for ch in s:
            if ch == 'L':
                cnt_l += 1
            elif ch == 'C':
                cnt_lc += cnt_l
            elif ch == 'T':
                cnt_lct += cnt_lc
        # backward: number of "CT" subsequences
        cnt_t = 0
        cnt_ct = 0
        for i in range(n - 1, -1, -1):
            ch = s[i]
            if ch == 'T':
                cnt_t += 1
            elif ch == 'C':
                cnt_ct += cnt_t
        best_gain = max(cnt_ct, cnt_lc)  # insert 'L' at front / 'T' at end
        # insert a 'C' at every gap
        suffix_t = cnt_t
        prefix_l = 0
        # gap before index i has prefix_l L's on the left, suffix_t T's on the right
        for i in range(n + 1):
            if best_gain < prefix_l * suffix_t:
                best_gain = prefix_l * suffix_t
            if i < n:
                if s[i] == 'L':
                    prefix_l += 1
                elif s[i] == 'T':
                    suffix_t -= 1
        return cnt_lct + best_gain
