"""

2551. Put Marbles in Bags
Hard

You have k bags. You are given a 0-indexed integer array weights where weights[i] is the weight of the ith marble. You are also given the integer k.

Divide the marbles into the k bags according to the following rules:

- No bag is empty.
- If the ith marble and jth marble are in a bag, then all marbles with an index between the ith and jth indices should also be in that same bag.
- If a bag consists of all the marbles with an index from i to j inclusively, then the cost of the bag is weights[i] + weights[j].

The score after distributing the marbles is the sum of the costs of all the k bags.

Return the difference between the maximum and minimum scores among marble distributions.


Example 1:

Input: weights = [1,3,5,1], k = 2
Output: 4
Explanation:
The distribution [1],[3,5,1] results in the minimal score of (1+1) + (3+1) = 6.
The distribution [1,3],[5,1], results in the maximal score of (1+3) + (5+1) = 10.
Thus, we return their difference 10 - 6 = 4.

Example 2:

Input: weights = [1, 3], k = 2
Output: 0
Explanation: The only distribution possible is [1],[3].
Since both the maximal and minimal score are the same, we return 0.


Constraints:

1 <= k <= weights.length <= 10^5
1 <= weights[i] <= 10^9

"""

# V0
# IDEA : GREEDY (SPLIT POINTS) + SORTING
#
#   splitting the array into k bags means picking k-1 "cut" positions.
#   whatever the cuts are, weights[0] and weights[n-1] are ALWAYS counted
#   (they are the outer ends of the first / last bag), so they cancel out
#   when we take (max score - min score).
#
#   a cut between index i and i+1 closes one bag at i and opens the next
#   at i+1, contributing weights[i] + weights[i+1] to the score.
#   so the score = const + (sum of the k-1 chosen pair sums), and every set
#   of k-1 distinct cuts is achievable.
#
#   => build pair[i] = weights[i] + weights[i+1] (n-1 of them), sort it,
#      answer = (sum of largest k-1) - (sum of smallest k-1).
#
#   NOTE : k == 1 -> no cuts at all -> answer is 0, and the slicing below
#          already handles it (both slices are empty).
#
# time = O(n log n), space = O(n)
class Solution(object):
    def putMarbles(self, weights, k):
        n = len(weights)
        pairs = sorted(weights[i] + weights[i + 1] for i in range(n - 1))
        if k <= 1:
            return 0
        return sum(pairs[len(pairs) - (k - 1):]) - sum(pairs[:k - 1])
