"""

466. Count The Repetitions
Hard

We define str = [s, n] as the string str which consists of the string s
concatenated n times.

- For example, str == ["abc", 3] == "abcabcabc".

We define that string s1 can be obtained from string s2 if we can remove some
characters from s2 such that it becomes s1.

- For example, s1 = "abc" can be obtained from s2 = "abdbec" based on our
  definition by removing the bolded underlined characters.

You are given two strings s1 and s2 and two integers n1 and n2. You have the two
strings str1 = [s1, n1] and str2 = [s2, n2].

Return the maximum integer m such that str = [str2, m] can be obtained from str1.

Example 1:

Input: s1 = "acb", n1 = 4, s2 = "ab", n2 = 2
Output: 2

Example 2:

Input: s1 = "acb", n1 = 1, s2 = "acb", n2 = 1
Output: 1

Constraints:

1 <= s1.length, s2.length <= 100
s1 and s2 consist of lowercase English letters.
1 <= n1, n2 <= 10^6

"""

# V0
# IDEA : PRECOMPUTE ONE PASS OF s1, THEN ITERATE
#
#  Matching str1 greedily against repeated copies of s2 is deterministic: the
#  ONLY state we carry from one copy of s1 to the next is "which index of s2
#  are we currently trying to match" (a value in [0, len(s2))).
#
#  So precompute, for every start index i in s2:
#      d[i] = (cnt, j)
#        cnt = how many FULL copies of s2 get matched while scanning one s1
#        j   = the index in s2 we end up at
#
#  Then walk n1 copies of s1, accumulating cnt and following j.
#  Finally, [str2, m] fits m = total_s2_matched // n2 times.
#
"""

DP def
    matching str1 greedily against copies of s2 is DETERMINISTIC: the only
    state carried from one copy of s1 to the next is "which index of s2 are
    we currently trying to match" (a value in [0, len(s2)))

    d[i] = (cnt, j): scanning ONE whole copy of s1 starting at s2 index i

                     matches `cnt` FULL copies of s2 and ends at s2 index j

DP eq

     precompute, for every start i in [0, len(s2)):

        walk s1 once, advancing j on a character match,
        and each time j wraps past len(s2): cnt += 1, j = 0

     then follow the chain n1 times:

        cnt, j = d[j];  total += cnt


    -> e.g. this is a "functional graph" jump table - the same j always
              yields the same (cnt, j'), so n1 copies are just n1 lookups

     ans = total // n2      # [str2, m] fits m = total_s2_matched // n2 times

"""
# time = O(len(s1) * len(s2) + n1)
# space = O(len(s2))
class Solution(object):
    def getMaxRepetitions(self, s1, n1, s2, n2):
        n = len(s2)
        d = {}

        # one pass of s1 starting at s2 index i
        for i in range(n):
            cnt = 0
            j = i
            for c in s1:
                if c == s2[j]:
                    j += 1
                    if j == n:      # a full s2 has been matched
                        cnt += 1
                        j = 0
            d[i] = (cnt, j)

        total = 0
        j = 0
        for _ in range(n1):
            cnt, j = d[j]
            total += cnt

        return total // n2


# V1
# IDEA : SAME PRECOMPUTE + CYCLE DETECTION (O(len(s2)) instead of O(n1) steps)
#
#  The mapping j -> d[j][1] is a function on a set of at most len(s2) states, so
#  after at most len(s2) copies of s1 we must revisit a state -> a cycle.
#  Jump over the whole cycle with arithmetic instead of looping n1 times.
#  Useful when n1 is far larger than 10^6.
#
"""

DP def
    matching str1 greedily against copies of s2 is DETERMINISTIC: the only
    state carried from one copy of s1 to the next is "which index of s2 are
    we currently trying to match" (a value in [0, len(s2)))

    d[i] = (cnt, j): scanning ONE whole copy of s1 starting at s2 index i

                     matches `cnt` FULL copies of s2 and ends at s2 index j

DP eq

     precompute, for every start i in [0, len(s2)):

        walk s1 once, advancing j on a character match,
        and each time j wraps past len(s2): cnt += 1, j = 0

     then follow the chain n1 times:

        cnt, j = d[j];  total += cnt


    -> e.g. this is a "functional graph" jump table - the same j always
              yields the same (cnt, j'), so n1 copies are just n1 lookups

     ans = total // n2      # [str2, m] fits m = total_s2_matched // n2 times

"""
# time = O(len(s1) * len(s2))
# space = O(len(s2))
class Solution2(object):
    def getMaxRepetitions(self, s1, n1, s2, n2):
        n = len(s2)
        d = {}
        for i in range(n):
            cnt = 0
            j = i
            for c in s1:
                if c == s2[j]:
                    j += 1
                    if j == n:
                        cnt += 1
                        j = 0
            d[i] = (cnt, j)

        seen = {}            # s2 index -> (copies of s1 used, s2 matched so far)
        total = 0
        j = 0
        used = 0
        jumped = False       # a cycle can only be cashed in once

        while used < n1:
            if not jumped and j in seen:
                prev_used, prev_total = seen[j]
                cycle_len = used - prev_used
                cycle_gain = total - prev_total
                loops = (n1 - used) // cycle_len
                total += loops * cycle_gain
                used += loops * cycle_len
                jumped = True
                continue     # the leftover copies are walked one by one below
            if not jumped:
                seen[j] = (used, total)
            cnt, j = d[j]
            total += cnt
            used += 1

        return total // n2
