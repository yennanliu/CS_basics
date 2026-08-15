"""

3075. Maximize Happiness of Selected Children
Medium

You are given an array happiness of length n, and a positive integer k.

There are n children standing in a queue, where the ith child has happiness value happiness[i]. You want to select k children from these n children in k turns.

In each turn, when you select a child, the happiness value of all the children that have not been selected till now decreases by 1. Note that the happiness value cannot become negative and gets decremented only if it is positive.

Return the maximum sum of the happiness values of the selected children you can achieve by selecting k children.


Example 1:

Input: happiness = [1,2,3], k = 2
Output: 4
Explanation: We can pick 2 children in the following way:
- Pick the child with the happiness value == 3. The happiness value of the remaining children becomes [0,1].
- Pick the child with the happiness value == 1. The happiness value of the remaining child becomes [0]. Note that the happiness value cannot become less than 0.
The sum of the happiness values of the selected children is 3 + 1 = 4.

Example 2:

Input: happiness = [1,1,1,1], k = 2
Output: 1
Explanation: We can pick 2 children in the following way:
- Pick any child with the happiness value == 1. The happiness value of the remaining children becomes [0,0,0].
- Pick the child with the happiness value == 0. The happiness value of the remaining child becomes [0,0].
The sum of the happiness values of the selected children is 1 + 0 = 1.

Example 3:

Input: happiness = [2,3,4,5], k = 1
Output: 5
Explanation: We can pick 1 child in the following way:
- Pick the child with the happiness value == 5. The happiness value of the remaining children becomes [1,2,3].
The sum of the happiness values of the selected children is 5.


Constraints:

1 <= n == happiness.length <= 2 * 10^5
1 <= happiness[i] <= 10^8
1 <= k <= n

"""

# V0
# IDEA : PICK THE HAPPIEST FIRST — THE i-TH PICK HAS ALREADY LOST i POINTS
#
#   the decay does not depend on WHICH children are picked, only on how many
#   picks came before : a child selected on turn i (0-based) has been
#   decremented exactly i times.
#
#   so the order is forced to be descending — giving the largest values the
#   smallest penalties — and the total is
#
#       sum over i < k of max(0, sorted_desc[i] - i)
#
#   the max(0, ...) is the "happiness never goes negative" clause, and once
#   one term hits 0 every later one does too.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maximumHappinessSum(self, happiness, k):
        vals = sorted(happiness, reverse=True)
        res = 0
        for i in range(k):
            gain = vals[i] - i
            if gain <= 0:
                break
            res += gain
        return res
