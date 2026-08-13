"""

1196. How Many Apples Can You Put into the Basket
Easy

You have some apples and a basket that can carry up to 5000 units of weight.

Given an integer array weight where weight[i] is the weight of the i-th apple,
return the maximum number of apples you can put in the basket.


Example 1:

Input: weight = [100,200,150,1000]
Output: 4
Explanation: All 4 apples can be carried by the basket since their sum of
weights is 1450.

Example 2:

Input: weight = [900,950,800,1000,700,800]
Output: 5
Explanation: The sum of weights of the 6 apples exceeds 5000 so we choose any
5 of them.


Constraints:

1 <= weight.length <= 10^3
1 <= weight[i] <= 10^3

"""

# V0
# IDEA: GREEDY (sort ascending, take the lightest apples first)
# to fit the MOST apples, always pick the lightest remaining one
# time = O(n log n)
# space = O(n)
class Solution(object):
    def maxNumberOfApples(self, weight):
        LIMIT = 5000

        # sorted copy, so the caller's list is not mutated
        ws = sorted(weight)

        total = 0
        for i, w in enumerate(ws):
            total += w
            if total > LIMIT:
                return i

        return len(ws)
