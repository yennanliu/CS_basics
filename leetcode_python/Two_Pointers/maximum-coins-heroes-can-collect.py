"""

2838. Maximum Coins Heroes Can Collect
Medium

There is a battle and n heroes are trying to defeat m monsters. You are given two 1-indexed arrays of positive integers heroes and monsters of length n and m, respectively. heroes[i] is the power of ith hero, and monsters[i] is the power of ith monster.

The ith hero can defeat the jth monster if monsters[j] <= heroes[i].

You are also given a 1-indexed array coins of length m consisting of positive integers. coins[i] is the number of coins that each hero earns after defeating the ith monster.

Return an array ans of length n where ans[i] is the maximum number of coins that the ith hero can collect from this battle.

Notes

- The health of a hero doesn't get reduced after defeating a monster.
- Multiple heroes can defeat a monster, but each monster can be defeated by a given hero only once.


Example 1:

Input: heroes = [1,4,2], monsters = [1,1,5,2,3], coins = [2,3,4,5,6]
Output: [5,16,10]
Explanation: For each hero, we list the index of all the monsters he can defeat:
1st hero: [1,2] since the power of this hero is 1 and monsters[1], monsters[2] <= 1. So this hero collects coins[1] + coins[2] = 5 coins.
2nd hero: [1,2,4,5] since the power of this hero is 4 and monsters[1], monsters[2], monsters[4], monsters[5] <= 4. So this hero collects coins[1] + coins[2] + coins[4] + coins[5] = 16 coins.
3rd hero: [1,2,4] since the power of this hero is 2 and monsters[1], monsters[2], monsters[4] <= 2. So this hero collects coins[1] + coins[2] + coins[4] = 10 coins.
So the answer would be [5,16,10].

Example 2:

Input: heroes = [5], monsters = [2,3,1,2], coins = [10,6,5,2]
Output: [23]
Explanation: This hero can defeat all the monsters since monsters[i] <= 5. So he collects all of the coins: coins[1] + coins[2] + coins[3] + coins[4] = 23, and the answer would be [23].

Example 3:

Input: heroes = [4,4], monsters = [5,7,8], coins = [1,1,1]
Output: [0,0]
Explanation: In this example, no hero can defeat a monster. So the answer would be [0,0],


Constraints:

1 <= n == heroes.length <= 10^5
1 <= m == monsters.length <= 10^5
coins.length == m
1 <= heroes[i], monsters[i], coins[i] <= 10^9

"""

# V0
# IDEA : SORT + PREFIX SUM + TWO POINTERS
#
#   a hero of power h collects EVERY monster with power <= h (no health cost,
#   no competition between heroes), so the answer only depends on h.
#
#   sort the (monster power, coin) pairs by power and build a prefix sum over
#   the coins. then ans[i] = prefix[ #monsters with power <= heroes[i] ].
#
#   to keep it O((n + m) log(n + m)) without repeated binary searches, sort the
#   HEROES by power too and sweep both sorted lists with two pointers: since
#   hero powers are non-decreasing, the monster pointer only moves forward.
#
#   NOTE : the output must follow the ORIGINAL hero order, so sort hero indices
#          (not the values) and scatter the results back.
#   NOTE : sums reach 10^5 * 10^9 = 10^14 - needs 64-bit in a Java/C++ port.
#
# time = O(n log n + m log m), space = O(n + m)
class Solution(object):
    def maximumCoins(self, heroes, monsters, coins):
        m = len(monsters)
        pairs = sorted(zip(monsters, coins))

        pre = [0] * (m + 1)
        for i in range(m):
            pre[i + 1] = pre[i] + pairs[i][1]

        n = len(heroes)
        ans = [0] * n
        j = 0
        for i in sorted(range(n), key=lambda t: heroes[t]):
            h = heroes[i]
            while j < m and pairs[j][0] <= h:
                j += 1
            ans[i] = pre[j]
        return ans
