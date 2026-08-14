"""

2403. Minimum Time to Kill All Monsters
Hard
(premium / locked problem)

You are given an integer array power where power[i] is the minimum required energy to defeat the ith monster.

You start with 0 energy and you have an unlimited number of days to defeat all the monsters. Each day, you gain some energy and you may defeat a monster.

Each day, you gain mana points equal to your current gain. Initially, your gain is 1.
If your current mana points is greater than or equal to power[i], you may defeat the ith monster. Your mana points reset to 0 and your gain increases by 1.

Return the minimum number of days needed to defeat all the monsters.


Example 1:

Input: power = [3,1,4]
Output: 4
Explanation:
The optimal way to defeat all the monsters is as follows:
- Day 1: Gain 1 mana point to get a total of 1 mana point. Defeat the second monster.
- Day 2: Gain 2 mana points to get a total of 2 mana points.
- Day 3: Gain 2 mana points to get a total of 4 mana points. Defeat the third monster.
- Day 4: Gain 3 mana points to get a total of 3 mana points. Defeat the first monster.
It can be proven that 4 is the minimum number of days needed.

Example 2:

Input: power = [1,1,4]
Output: 4
Explanation:
The optimal way to defeat all the monsters is as follows:
- Day 1: Gain 1 mana point to get a total of 1 mana point. Defeat the first monster.
- Day 2: Gain 2 mana points to get a total of 2 mana points. Defeat the second monster.
- Day 3: Gain 3 mana points to get a total of 3 mana points.
- Day 4: Gain 3 mana points to get a total of 6 mana points. Defeat the third monster.
It can be proven that 4 is the minimum number of days needed.

Example 3:

Input: power = [1,2,4,9]
Output: 6
Explanation:
The optimal way to defeat all the monsters is as follows:
- Day 1: Gain 1 mana point to get a total of 1 mana point. Defeat the first monster.
- Day 2: Gain 2 mana points to get a total of 2 mana points. Defeat the second monster.
- Day 3: Gain 3 mana points to get a total of 3 mana points.
- Day 4: Gain 3 mana points to get a total of 6 mana points.
- Day 5: Gain 3 mana points to get a total of 9 mana points. Defeat the fourth monster.
- Day 6: Gain 4 mana points to get a total of 4 mana points. Defeat the third monster.
It can be proven that 6 is the minimum number of days needed.


Constraints:

1 <= power.length <= 17
1 <= power[i] <= 10^9

"""

# V0
# IDEA : BITMASK DP OVER WHICH MONSTERS ARE ALREADY DEAD
#
#   the ONLY thing that matters about the past is which monsters are dead —
#   that fixes the current gain (kills + 1) and the mana is always 0 right
#   after a kill. so :
#
#       dp[mask] = fewest days to have killed exactly the set `mask`
#       gain     = popcount(mask) + 1
#       dp[mask | 1<<i] = min(dp[mask] + ceil(power[i] / gain))
#
#   ceil is written as -(-p // g) to stay in exact integers (power reaches
#   10^9).
#
#   n <= 17, so the table has 2^17 entries with 17 transitions each.
#
# time = O(2^n * n), space = O(2^n)
class Solution(object):
    def minimumTime(self, power):
        n = len(power)
        size = 1 << n
        INF = float('inf')
        dp = [INF] * size
        dp[0] = 0

        for mask in range(size):
            if dp[mask] == INF:
                continue
            gain = bin(mask).count('1') + 1
            for i in range(n):
                if mask >> i & 1:
                    continue
                nxt = mask | (1 << i)
                cand = dp[mask] + -(-power[i] // gain)
                if cand < dp[nxt]:
                    dp[nxt] = cand
        return dp[size - 1]
