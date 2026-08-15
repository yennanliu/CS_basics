"""

3273. Minimum Amount of Damage Dealt to Bob
Hard

You are given an integer power and two integer arrays damage and health, both having length n.

Bob has n enemies, where enemy i will deal Bob damage[i] points of damage per second while they are alive (i.e. health[i] > 0).

Every second, after the enemies deal damage to Bob, he chooses one of the enemies that is still alive and deals power points of damage to them.

Determine the minimum total amount of damage points that will be dealt to Bob before all n enemies are dead.


Example 1:

Input: power = 4, damage = [1,2,3,4], health = [4,5,6,8]
Output: 39
Explanation:
Attack enemy 3 in the first two seconds, after which enemy 3 will go down, the number of damage points dealt to Bob is 10 + 10 = 20 points.
Attack enemy 2 in the next two seconds, after which enemy 2 will go down, the number of damage points dealt to Bob is 6 + 6 = 12 points.
Attack enemy 0 in the next second, after which enemy 0 will go down, the number of damage points dealt to Bob is 3 points.
Attack enemy 1 in the next two seconds, after which enemy 1 will go down, the number of damage points dealt to Bob is 2 + 2 = 4 points.

Example 2:

Input: power = 1, damage = [1,1,1,1], health = [1,2,3,4]
Output: 20

Example 3:

Input: power = 8, damage = [40], health = [59]
Output: 320


Constraints:

1 <= power <= 10^4
1 <= n == damage.length == health.length <= 10^5
1 <= damage[i], health[i] <= 10^4

"""

# V0
# IDEA : EXCHANGE ARGUMENT — ORDER THE KILLS BY damage / time
#
#   Bob's attacks are never worth splitting : once an enemy is targeted it is
#   best to finish it, because leaving it alive keeps costing its damage. so
#   the whole problem is the ORDER of the kills.
#
#   enemy i needs t[i] = ceil(health[i] / power) seconds. comparing two
#   adjacent kills i then j against j then i, the totals differ by
#       t[i] * damage[j]   vs   t[j] * damage[i]
#   so i should come first when damage[i] * t[j] > damage[j] * t[i], i.e.
#   sort by the ratio damage / t descending.
#
#   then sweep the sorted order, accumulating elapsed time and charging every
#   remaining enemy for it — equivalently, each enemy pays for all the time up
#   to and including its own death.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minDamage(self, power, damage, health):
        n = len(damage)
        turns = [-(-health[i] // power) for i in range(n)]   # ceil(health / power)
        order = sorted(range(n), key=lambda i: -damage[i] / float(turns[i]))

        total = 0
        elapsed = 0
        for i in order:
            elapsed += turns[i]
            total += elapsed * damage[i]
        return total
