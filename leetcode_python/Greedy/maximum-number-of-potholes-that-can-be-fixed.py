"""

3119. Maximum Number of Potholes That Can Be Fixed
Medium
🔒 (premium)

You are given a string road, consisting only of characters "x" and ".", where each "x" denotes a pothole and each "." denotes a smooth road, and an integer budget.

In one repair operation, you can repair n consecutive potholes for a price of n + 1.

Return the maximum number of potholes that can be fixed such that the sum of the price of all of the fixes doesn't go over the given budget.


Example 1:

Input: road = "..", budget = 5
Output: 0
Explanation: There are no potholes to be fixed.

Example 2:

Input: road = "..xxxxx", budget = 4
Output: 3
Explanation: We fix the first three potholes (they are consecutive). The cost of this fix is 3 + 1 = 4.

Example 3:

Input: road = "x.....x", budget = 14
Output: 2
Explanation: We can fix both potholes. The cost of fixing the two potholes is 1 + 1 + 1 + 1 = 4.


Constraints:

1 <= road.length <= 10^5
1 <= budget <= 10^5 + 1
road consists only of characters '.' and 'x'.

"""

# V0
# IDEA : EACH REPAIR PAYS A FLAT +1 OVERHEAD — SO FIX THE LONGEST RUNS FIRST
#
#   repairing n consecutive potholes costs n + 1, i.e. 1 per pothole plus a
#   fixed 1 for the operation itself. the per-pothole part is unavoidable, so
#   the only lever is how many operations get paid for — and that is
#   minimised by covering whole runs, longest ones first.
#
#   walk the runs in descending length : if the budget covers len + 1, take
#   the whole run; otherwise the leftover pays for a PARTIAL run of
#   (budget - 1) potholes, and nothing is left afterwards.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maxPotholes(self, road, budget):
        runs = []
        cur = 0
        for ch in road:
            if ch == 'x':
                cur += 1
            elif cur:
                runs.append(cur)
                cur = 0
        if cur:
            runs.append(cur)

        runs.sort(reverse=True)
        fixed = 0
        for length in runs:
            if budget <= 1:
                break
            if length + 1 <= budget:
                budget -= length + 1
                fixed += length
            else:
                fixed += budget - 1        # partial run with what is left
                budget = 0
                break
        return fixed
