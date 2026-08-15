"""

3185. Count Pairs That Form a Complete Day II
Medium

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

1 <= hours.length <= 5 * 10^5
1 <= hours[i] <= 10^9

"""

# V0
# IDEA : ONLY THE RESIDUE MOD 24 MATTERS — COUNT THE COMPLEMENT ON THE FLY
#
#   hours[i] + hours[j] is a multiple of 24 exactly when their residues sum
#   to 0 mod 24, so bucket the values into 24 counters.
#
#   sweeping left to right and, before inserting the current residue r,
#   adding the count of its complement (24 - r) % 24 pairs every element with
#   the ones BEFORE it — so each pair is counted once and the i < j ordering
#   is automatic.
#
# time = O(n), space = O(1)  (24 counters)
class Solution(object):
    def countCompleteDayPairs(self, hours):
        cnt = [0] * 24
        res = 0
        for h in hours:
            r = h % 24
            res += cnt[(24 - r) % 24]
            cnt[r] += 1
        return res
