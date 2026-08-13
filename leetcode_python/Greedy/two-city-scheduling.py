"""

1029. Two City Scheduling
Medium

A company is planning to interview 2n people. Given the array costs where
costs[i] = [aCost_i, bCost_i], the cost of flying the ith person to city a is aCost_i,
and the cost of flying the ith person to city b is bCost_i.

Return the minimum cost to fly every person to a city such that exactly n people
arrive in each city.


Example 1:

Input: costs = [[10,20],[30,200],[400,50],[30,20]]
Output: 110
Explanation:
The first person goes to city A for a cost of 10.
The second person goes to city A for a cost of 30.
The third person goes to city B for a cost of 50.
The fourth person goes to city B for a cost of 20.

The total minimum cost is 10 + 30 + 50 + 20 = 110 to have half the people
interviewing in each city.

Example 2:

Input: costs = [[259,770],[448,54],[926,667],[184,139],[840,118],[577,469]]
Output: 1859

Example 3:

Input: costs = [[515,563],[451,713],[537,709],[343,819],[855,779],[457,60],[650,359],[631,42]]
Output: 3086


Constraints:

2 * n == costs.length
2 <= costs.length <= 100
costs.length is even.
1 <= aCost_i, bCost_i <= 1000

"""

# V0
# IDEA : GREEDY + SORT ON (aCost - bCost)
#
#  - send everyone to city B first  -> base = sum(bCost)
#  - moving person i to city A costs an extra (aCost_i - bCost_i)
#  - to pick exactly n people for city A, just pick the n smallest deltas
#    => sort by (aCost - bCost), first half goes to A, second half goes to B
#
# time = O(n log n)
# space = O(1)
#   space is O(1) extra (the input list is sorted in place)
class Solution(object):
    def twoCitySchedCost(self, costs):
        costs.sort(key=lambda x: x[0] - x[1])
        n = len(costs) // 2
        res = 0
        for i in range(n):
            res += costs[i][0] + costs[i + n][1]
        return res


# V1
# IDEA : BASE SUM + SORTED DELTA (same greedy, written explicitly)
# time = O(n log n)
# space = O(n)
class Solution2(object):
    def twoCitySchedCost(self, costs):
        n = len(costs) // 2
        base = sum(b for _, b in costs)
        deltas = sorted(a - b for a, b in costs)
        return base + sum(deltas[:n])
