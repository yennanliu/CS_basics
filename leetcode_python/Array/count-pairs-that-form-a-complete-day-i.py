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
