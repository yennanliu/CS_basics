"""

3647. Maximum Weight in Two Bags
Medium

You are given an integer array weights and two integers w1 and w2
representing the maximum capacities of two bags.

Each item may be placed in at most one bag such that:

Bag 1 holds at most w1 total weight.
Bag 2 holds at most w2 total weight.

Return the maximum total weight that can be packed into the two bags.


Example 1:

Input: weights = [1,4,3,2], w1 = 5, w2 = 4
Output: 9
Explanation:
Bag 1: Place weights[2] = 3 and weights[3] = 2 as 3 + 2 = 5 <= w1
Bag 2: Place weights[1] = 4 as 4 <= w2
Total weight: 5 + 4 = 9

Example 2:

Input: weights = [3,6,4,8], w1 = 9, w2 = 7
Output: 15
Explanation:
Bag 1: Place weights[3] = 8 as 8 <= w1
Bag 2: Place weights[0] = 3 and weights[2] = 4 as 3 + 4 = 7 <= w2
Total weight: 8 + 7 = 15

Example 3:

Input: weights = [5,7], w1 = 2, w2 = 3
Output: 0
Explanation:
No weight fits in either bag, thus the answer is 0.


Constraints:

1 <= weights.length <= 100
1 <= weights[i] <= 100
1 <= w1, w2 <= 300

"""

# V0
# IDEA : TWO-DIMENSIONAL REACHABILITY (KNAPSACK OVER A PAIR OF LOADS)
#
#   the two bags are coupled only through the items -- an item used in bag 1
#   is unavailable to bag 2 -- so filling them one after the other greedily
#   is wrong. the state that captures everything is the PAIR of current
#   loads (a, b), and the question is purely which pairs are reachable;
#   the objective max(a + b) is then read off at the end.
#
#   each item offers three choices: skip, add to a, add to b. so
#     reach' = reach | (reach shifted by w in the a-axis)
#                    | (reach shifted by w in the b-axis)
#   and building reach' entirely from the old reach is what stops an item
#   from being used twice (the classic 0/1 knapsack ordering trap).
#
#   the b-axis is stored as a python big-int bitmask, one bit per load, so
#   the whole "shift by w and clip at w2" step is a single machine-word-
#   parallel shift instead of an inner loop.
#
# time = O(n * w1 * w2 / 64), space = O(w1 * w2 / 64)
class Solution(object):
    def maxWeight(self, weights, w1, w2):
        maskb = (1 << (w2 + 1)) - 1
        # reach[a] = bitmask of loads b reachable in bag 2 while bag 1 holds a
        reach = [0] * (w1 + 1)
        reach[0] = 1
        for w in weights:
            nxt = reach[:]
            for a in range(w1, -1, -1):
                cur = reach[a]
                if not cur:
                    continue
                nxt[a] |= (cur << w) & maskb
                if a + w <= w1:
                    nxt[a + w] |= cur
            reach = nxt

        best = 0
        for a in range(w1 + 1):
            if reach[a]:
                b = reach[a].bit_length() - 1
                if a + b > best:
                    best = a + b
        return best
