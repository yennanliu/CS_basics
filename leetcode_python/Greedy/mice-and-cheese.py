"""

2611. Mice and Cheese
Medium

There are two mice and n different types of cheese, each type of cheese should be eaten by exactly one mouse.

A point of the cheese with index i (0-indexed) is:

reward1[i] if the first mouse eats it.
reward2[i] if the second mouse eats it.

You are given a positive integer array reward1, a positive integer array reward2, and a non-negative integer k.

Return the maximum points the mice can achieve if the first mouse eats exactly k types of cheese.


Example 1:

Input: reward1 = [1,1,3,4], reward2 = [4,4,1,1], k = 2
Output: 15
Explanation: In this example, the first mouse eats the 2nd (0-indexed) and the 3rd types of cheese, and the second mouse eats the 0th and the 1st types of cheese.
The total points are 4 + 4 + 3 + 4 = 15.
It can be proven that 15 is the maximum total points that the mice can achieve.

Example 2:

Input: reward1 = [1,1], reward2 = [1,1], k = 2
Output: 2
Explanation: In this example, the first mouse eats the 0th (0-indexed) and 1st types of cheese, and the second mouse does not eat any cheese.
The total points are 1 + 1 = 2.
It can be proven that 2 is the maximum total points that the mice can achieve.


Constraints:

1 <= n == reward1.length == reward2.length <= 10^5
1 <= reward1[i], reward2[i] <= 1000
0 <= k <= n

"""

# V0
# IDEA : GREEDY (start from "mouse 2 eats everything", then buy back k swaps)
#
#   baseline : let mouse 2 eat every cheese  -> total = sum(reward2)
#
#   handing cheese i over to mouse 1 changes the total by exactly
#       gain(i) = reward1[i] - reward2[i]
#   and the gains are INDEPENDENT of each other (each cheese is a separate
#   +/- term), so with exactly k hand-overs we simply take the k largest gains.
#
#   NOTE : the count is "exactly k", not "at most k" -> we must take the top
#          k gains even when some of them are negative (see example 2, where
#          both gains are 0 and k == n).
#
# time = O(n * log(n)), space = O(n)
class Solution(object):
    def miceAndCheese(self, reward1, reward2, k):
        total = sum(reward2)
        gains = [reward1[i] - reward2[i] for i in range(len(reward1))]
        gains.sort(reverse=True)
        return total + sum(gains[:k])
