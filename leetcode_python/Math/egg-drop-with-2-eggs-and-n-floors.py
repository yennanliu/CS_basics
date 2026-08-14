"""

1884. Egg Drop With 2 Eggs and N Floors
Medium

You are given two identical eggs and you have access to a building with n floors labeled from 1 to n.

You know that there exists a floor f where 0 <= f <= n such that any egg dropped at a floor higher than f will break, and any egg dropped at or below floor f will not break.

In each move, you may take an unbroken egg and drop it from any floor x (where 1 <= x <= n). If the egg breaks, you can no longer use it. However, if the egg does not break, you may reuse it in future moves.

Return the minimum number of moves that you need to determine with certainty what the value of f is.


Example 1:

Input: n = 2
Output: 2
Explanation: We can drop the first egg from floor 1 and the second egg from floor 2.
If the first egg breaks, we know that f = 0.
If the second egg breaks but the first egg didn't, we know that f = 1.
Otherwise, if both eggs survive, we know that f = 2.

Example 2:

Input: n = 100
Output: 14
Explanation: One optimal strategy is:
- Drop the 1st egg at floor 9. If it breaks, we know f is between 0 and 8. Drop the 2nd egg starting from floor 1 and going up one at a time to find f within 8 more drops. Total drops is 1 + 8 = 9.
- If the 1st egg does not break, drop the 1st egg again at floor 22. If it breaks, we know f is between 9 and 21. Drop the 2nd egg starting from floor 10 and going up one at a time to find f within 12 more drops. Total drops is 2 + 12 = 14.
- If the 1st egg does not break again, follow a similar process dropping the 1st egg from floors 34, 45, 55, 64, 72, 79, 85, 90, 94, 97, 99, and 100.
Regardless of the outcome, it takes at most 14 drops to determine f.


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : MATH (with a budget of k drops, 2 eggs can cover k*(k+1)/2 floors)
#
#   invert the question : "how many floors can we clear with at most k
#   drops?". spend the 1st egg at floor k. if it breaks we have k-1 drops
#   left and one egg -> a linear scan covering k-1 floors below it.
#   if it survives, we still have k-1 drops and 2 eggs, so the next probe
#   climbs k-1 more floors, then k-2, ...
#
#   reach(k) = k + (k-1) + ... + 1 = k * (k + 1) / 2
#
#   so the answer is the SMALLEST k with k*(k+1)/2 >= n.
#   (check : n = 100 -> k = 13 gives 91 < 100, k = 14 gives 105 >= 100.)
#
# time = O(sqrt(n)), space = O(1)
class Solution(object):
    def twoEggDrop(self, n):
        k = 0
        while k * (k + 1) // 2 < n:
            k += 1
        return k
