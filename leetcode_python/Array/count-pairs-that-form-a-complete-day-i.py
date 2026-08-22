"""

3184. Count Pairs That Form a Complete Day I
Easy

Given an integer array hours representing times in hours, return an integer denoting the number of pairs i, j where i < j and hours[i] + hours[j] forms a complete day.

A complete day is defined as a time duration that is an exact multiple of 24 hours.

For example, 1 day is 24 hours, 2 days is 48 hours, and so on.


Example 1:

Input: hours = [12,12,30,24,24]
Output: 2
Explanation:
The pairs of indices that form a complete day are (0, 1) and (3, 4).

Example 2:

Input: hours = [72,48,24,3]
Output: 3
Explanation:
The pairs of indices that form a complete day are (0, 1), (0, 2), and (1, 2).


Constraints:

1 <= hours.length <= 100
1 <= hours[i] <= 10^9

"""

# V0
# IDEA : n <= 100 — TEST EVERY PAIR
#
#   only 4950 pairs at the limit, so the definition can be applied directly.
#   the sequel (LC 3185) grows the array to 5*10^5 and needs residue counting.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def countCompleteDayPairs(self, hours):
        n = len(hours)
        return sum(1
                   for i in range(n)
                   for j in range(i + 1, n)
                   if (hours[i] + hours[j]) % 24 == 0)


# V0-1
# IDEA : ONE PASS + COMPLEMENTARY RESIDUE COUNTER
#
#   hours[i] + hours[j] == 0 (mod 24) means hours[j] % 24 is exactly the
#   complement (24 - hours[i] % 24) % 24. so scan left to right and, for each
#   element, add how many EARLIER elements carry the complementary residue —
#   every such element closes a pair with i < j, no scanning back needed.
#
#   only 24 residues exist, so the counter is constant space. this is the
#   version that still runs at the LC 3185 limit of 5*10^5.
#
# time = O(n), space = O(1)   (24 buckets)
class Solution(object):
    def countCompleteDayPairs(self, hours):
        seen = [0] * 24
        res = 0
        for h in hours:
            r = h % 24
            res += seen[(24 - r) % 24]
            seen[r] += 1
        return res


# V0-2
# IDEA : BUCKET ALL RESIDUES FIRST, THEN CLOSE THE PAIRS COMBINATORIALLY
#
#   instead of accumulating pair by pair, tally the 24 residue classes in one
#   sweep and then count pairs in bulk:
#     r == 0 and r == 12 pair inside their own bucket -> C(c, 2) each
#     1 <= r <= 11        pair with bucket 24 - r     -> cnt[r] * cnt[24 - r]
#
#   NOTE : 0 and 12 are the self-complementary residues, which is why they get
#          the C(c, 2) form and must not be double counted in the loop.
#
# time = O(n), space = O(1)   (24 buckets)
class Solution(object):
    def countCompleteDayPairs(self, hours):
        cnt = [0] * 24
        for h in hours:
            cnt[h % 24] += 1

        res = cnt[0] * (cnt[0] - 1) // 2 + cnt[12] * (cnt[12] - 1) // 2
        for r in range(1, 12):
            res += cnt[r] * cnt[24 - r]
        return res
