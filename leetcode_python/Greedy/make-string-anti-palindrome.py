"""

3088. Make String Anti-palindrome
Hard
🔒 (premium)

We call a string s of even length n an anti-palindrome if for each index 0 <= i < n, s[i] != s[n - i - 1].

Given a string s, your task is to make s an anti-palindrome by doing any number of operations (including zero).

In one operation, you can select two characters from s and swap them.

Return the resulting string. If multiple strings meet the conditions, return the lexicographically smallest one. If it can't be made into an anti-palindrome, return "-1".


Example 1:

Input: s = "abca"
Output: "aabc"
Explanation: "aabc" is an anti-palindrome string since s[0] != s[3] and s[1] != s[2]. Also, it is a rearrangement of "abca". It can be proven that it's the lexicographically smallest.

Example 2:

Input: s = "abba"
Output: "aabb"
Explanation: "aabb" is an anti-palindrome string since s[0] != s[3] and s[1] != s[2]. Also, it is a rearrangement of "abba". It can be proven that it's the lexicographically smallest.

Example 3:

Input: s = "cccd"
Output: "-1"
Explanation: It can be proven that there is no way to make "cccd" an anti-palindrome string.


Constraints:

2 <= s.length <= 10^5
s.length % 2 == 0
s consists only of lowercase English letters.

"""

# V0
# IDEA : SORT, THEN REPAIR THE MIRROR CLASHES BY TOUCHING THE *RIGHT* HALF ONLY
#
#   sorting gives the lexicographically smallest arrangement outright, so the
#   only work is fixing positions where the sorted string mirrors itself.
#
#   feasibility first : a letter appearing more than n/2 times must collide
#   with itself under any pairing, so that case is "-1".
#
#   in a sorted string, t[i] == t[n-1-i] forces everything between them to be
#   that same letter — so all the clashes sit in one contiguous block of some
#   letter c straddling the centre. to repair a clash at mirror position
#   p = n-1-i, swap t[p] with the nearest position AFTER the c-block; that
#   position holds the smallest letter greater than c still available.
#
#   repairing only the right half keeps the (more significant) left half
#   untouched, and taking the nearest larger letter keeps the replacement as
#   small as possible — both are what lexicographic minimality wants.
#
# time = O(n log n), space = O(n)
from collections import Counter


class Solution(object):
    def makeAntiPalindrome(self, s):
        n = len(s)
        cnt = Counter(s)
        if max(cnt.values()) > n // 2:
            return "-1"

        t = sorted(s)
        # every clash involves the letter sitting just left of the centre
        c = t[n // 2 - 1]
        # the block of c, and the first index past it
        j = n // 2
        while j < n and t[j] == c:
            j += 1

        for p in range(n // 2, n):
            if t[p] != t[n - 1 - p]:
                continue
            while j < n and t[j] == c:
                j += 1
            if j == n:
                return "-1"
            t[p], t[j] = t[j], t[p]
            j += 1
        return ''.join(t)
