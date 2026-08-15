"""

3259. Maximum Energy Boost From Two Drinks
Medium

You are given two integer arrays energyDrinkA and energyDrinkB of the same length n by a futuristic sports scientist. These arrays represent the energy boosts per hour provided by two different energy drinks, A and B, respectively.

You want to maximize your total energy boost by drinking one energy drink per hour. However, if you want to switch from consuming one energy drink to the other, you need to wait for one hour to cleanse your system (meaning you won't get any energy boost in that hour).

Return the maximum total energy boost you can gain in the next n hours.

Note that you can start consuming either of the two energy drinks.


Example 1:

Input: energyDrinkA = [1,3,1], energyDrinkB = [3,1,1]
Output: 5
Explanation:
To gain an energy boost of 5, drink only the energy drink A (or only B).

Example 2:

Input: energyDrinkA = [4,1,1], energyDrinkB = [1,1,3]
Output: 7
Explanation:
To gain an energy boost of 7:
Drink the energy drink A for the first hour.
Switch to the energy drink B and we lose the energy boost of the second hour.
Gain the energy boost of the drink B in the third hour.


Constraints:

n == energyDrinkA.length == energyDrinkB.length
3 <= n <= 10^5
1 <= energyDrinkA[i], energyDrinkB[i] <= 10^5

"""

# V0
# IDEA : TWO ROLLING DP CHAINS, ONE PER DRINK
#
#   let a[i] be the best total that ends with drink A at hour i. it either
#   continued from A last hour, or switched from B — which costs the
#   intervening hour, so the switch reaches back TWO hours :
#
#       a[i] = A[i] + max(a[i-1], b[i-2])
#       b[i] = B[i] + max(b[i-1], a[i-2])
#
#   only the previous two values of each chain are ever read, so four
#   variables suffice.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxEnergyBoost(self, energyDrinkA, energyDrinkB):
        n = len(energyDrinkA)
        a_prev2 = b_prev2 = 0
        a_prev = energyDrinkA[0]
        b_prev = energyDrinkB[0]

        for i in range(1, n):
            a_cur = energyDrinkA[i] + max(a_prev, b_prev2)
            b_cur = energyDrinkB[i] + max(b_prev, a_prev2)
            a_prev2, b_prev2 = a_prev, b_prev
            a_prev, b_prev = a_cur, b_cur

        return max(a_prev, b_prev)
