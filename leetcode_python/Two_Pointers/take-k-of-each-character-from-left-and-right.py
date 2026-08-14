"""

2516. Take K of Each Character From Left and Right
Medium

You are given a string s consisting of the characters 'a', 'b', and 'c' and a non-negative integer k. Each minute, you may take either the leftmost character of s, or the rightmost character of s.

Return the minimum number of minutes needed for you to take at least k of each character, or return -1 if it is not possible to take k of each character.


Example 1:

Input: s = "aabaaaacaabc", k = 2
Output: 8
Explanation:
Take three characters from the left of s. You now have two 'a' characters, and one 'b' character.
Take five characters from the right of s. You now have four 'a' characters, two 'b' characters, and two 'c' characters.
A total of 3 + 5 = 8 minutes is needed.
It can be proven that 8 is the minimum number of minutes needed.

Example 2:

Input: s = "a", k = 1
Output: -1
Explanation: It is not possible to take one 'b' or 'c' so return -1.


Constraints:

1 <= s.length <= 10^5
s consists of only the letters 'a', 'b', and 'c'.
0 <= k <= s.length

"""

# V0
# IDEA : SLIDING WINDOW ON THE COMPLEMENT (two pointers)
#
#   what we take is a prefix + a suffix, so what we LEAVE BEHIND is one
#   contiguous middle block. minimising "taken" == maximising that middle block.
#
#   so : slide a window [j..i] that represents the KEPT middle. cnt holds the
#   counts of the characters OUTSIDE the window (i.e. the ones we take). extend
#   i, removing s[i] from cnt; while some count drops below k the window is
#   illegal, so shrink from j (putting characters back into cnt).
#
#   answer = len(s) - (largest legal window).
#
#   NOTE : only the count of the just-removed character s[i] can have fallen
#          below k, so the inner while only needs to test that one character —
#          not all three.
#
#   NOTE : the impossibility check (any letter occurring < k times overall) must
#          run first, otherwise the shrink loop would run off the end.
#
# time = O(n), space = O(1)
class Solution(object):
    def takeCharacters(self, s, k):
        cnt = {'a': 0, 'b': 0, 'c': 0}
        for c in s:
            cnt[c] += 1

        for c in 'abc':
            if cnt[c] < k:
                return -1

        best = 0
        j = 0
        for i, c in enumerate(s):
            cnt[c] -= 1
            while cnt[c] < k:
                cnt[s[j]] += 1
                j += 1
            best = max(best, i - j + 1)

        return len(s) - best
