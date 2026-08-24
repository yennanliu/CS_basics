"""

3147. Taking Maximum Energy From the Mystic Dungeon
Medium

In a mystic dungeon, n magicians are standing in a line. Each magician has an attribute that gives you energy. Some magicians can give you negative energy, which means taking energy from you.

You have been cursed in such a way that after absorbing energy from magician i, you will be instantly transported to magician (i + k). This process will be repeated until you reach the magician where (i + k) does not exist.

In other words, you will choose a starting point and then teleport with k jumps until you reach the end of the magicians' sequence, absorbing all the energy during the journey.

You are given an array energy and an integer k. Return the maximum possible energy you can gain.


Example 1:

Input: energy = [5,2,-10,-5,1], k = 3
Output: 3
Explanation: We can gain a total energy of 3 by starting from magician 1 absorbing 2 + 1 = 3.

Example 2:

Input: energy = [-2,-3,-1], k = 2
Output: -1
Explanation: We can gain a total energy of -1 by starting from magician 2.


Constraints:

1 <= energy.length <= 10^5
-1000 <= energy[i] <= 1000
1 <= k <= energy.length - 1

"""

# V0
# IDEA : SUFFIX SUMS ALONG EACH JUMP CHAIN, BUILT FROM THE BACK
#
#   starting at i the journey is forced : i, i+k, i+2k, ... to the end. so
#   the total from i is
#       energy[i] + total from (i + k)
#   which one right-to-left pass computes in place, each cell reading the one
#   k positions later (or nothing, when the chain ends).
#
#   the answer is the best starting cell — every index is a legal start.
#
"""

DP def
    starting at i the journey is FORCED: i, i+k, i+2k, ... to the end

    total[i]: the energy collected starting from index i

DP eq

     total[i] = energy[i] + total[i + k]

                (or just energy[i] when the chain ends here)


    -> e.g. ONE right-to-left pass computes it in place, each cell reading
              the one k positions later

     every index is a legal START, so

     ans = max(total)

"""
# time = O(n), space = O(1) beyond the copy
class Solution(object):
    def maximumEnergy(self, energy, k):
        n = len(energy)
        total = list(energy)
        for i in range(n - 1 - k, -1, -1):
            total[i] += total[i + k]
        return max(total)
