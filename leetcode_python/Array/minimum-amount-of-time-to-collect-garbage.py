"""

2391. Minimum Amount of Time to Collect Garbage
Medium

You are given a 0-indexed array of strings garbage where garbage[i] represents the assortment of garbage at the ith house. garbage[i] consists only of the characters 'M', 'P' and 'G' representing one unit of metal, paper and glass garbage respectively. Picking up one unit of any type of garbage takes 1 minute.

You are also given a 0-indexed integer array travel where travel[i] is the number of minutes needed to go from house i to house i + 1.

There are three garbage trucks in the city, each responsible for picking up one type of garbage. Each garbage truck starts at house 0 and must visit each house in order; however, they do not need to visit every house.

Only one garbage truck may be used at any given moment. While one truck is driving or picking up garbage, the other two trucks cannot do anything.

Return the minimum number of minutes needed to pick up all the garbage.


Example 1:

Input: garbage = ["G","P","GP","GG"], travel = [2,4,3]
Output: 21
Explanation:
The paper garbage truck:
1. Travels from house 0 to house 1
2. Collects the paper garbage at house 1
3. Travels from house 1 to house 2
4. Collects the paper garbage at house 2
The paper garbage truck takes 8 minutes to collect all the paper garbage.
The glass garbage truck:
1. Collects the glass garbage at house 0
2. Travels from house 0 to house 1
3. Travels from house 1 to house 2
4. Collects the glass garbage at house 2
5. Travels from house 2 to house 3
6. Collects the glass garbage at house 3
The glass garbage truck takes 13 minutes to collect all the glass garbage.
Since there is no metal garbage, we do not need to consider the metal garbage truck.
Therefore, it takes a total of 8 + 13 = 21 minutes to collect all the garbage.

Example 2:

Input: garbage = ["MMM","PGM","GP"], travel = [3,10]
Output: 37
Explanation:
The metal garbage truck takes 7 minutes to collect all the metal garbage.
The paper garbage truck takes 15 minutes to collect all the paper garbage.
The glass garbage truck takes 13 minutes to collect all the glass garbage.
It takes a total of 7 + 15 + 13 = 37 minutes to collect all the garbage.


Constraints:

2 <= garbage.length <= 10^5
garbage[i] consists of only the characters 'M', 'P', and 'G'.
1 <= garbage[i].length <= 10
travel.length == garbage.length - 1
1 <= travel[i] <= 10^5

"""

# V0
# IDEA : PICKUP TIME IS FIXED; ONLY THE DRIVING DEPENDS ON THE LAST HOUSE
#
#   every unit of garbage costs 1 minute no matter who collects it, so the
#   pickup total is just the combined length of all the strings.
#
#   a truck only ever needs to drive as far as the LAST house holding its
#   type — anything beyond is wasted. so its driving cost is the prefix sum
#   of `travel` up to that house.
#
#   one pass records the last occurrence of each type, a second turns those
#   into prefix distances. the trucks never overlap in time, so the three
#   costs simply add.
#
# time = O(n), space = O(n)
class Solution(object):
    def garbageCollection(self, garbage, travel):
        res = sum(len(g) for g in garbage)         # every pickup costs 1 minute

        last = {}
        for i, g in enumerate(garbage):
            for c in g:
                last[c] = i

        prefix = [0] * len(garbage)
        for i in range(1, len(garbage)):
            prefix[i] = prefix[i - 1] + travel[i - 1]

        for i in last.values():
            res += prefix[i]
        return res


# V0-1
# IDEA : SCAN FROM THE RIGHT, COUNT HOW MANY TRUCKS STILL HAVE TO PASS
#
#   walk the houses backwards while remembering which types have already been
#   met on the right. a truck must drive over the edge travel[i-1] exactly
#   when its type still appears at house i or further right, i.e. once for
#   every type currently in `seen`.
#
#   so each edge is charged len(seen) times, which folds the three prefix
#   sums of V0 into a single backward pass with no auxiliary array.
#
# time = O(n), space = O(1)
class Solution(object):
    def garbageCollection(self, garbage, travel):
        res = sum(len(g) for g in garbage)
        seen = set()
        for i in range(len(garbage) - 1, 0, -1):
            seen.update(garbage[i])
            res += len(seen) * travel[i - 1]
        return res


# V0-2
# IDEA : SIMULATE EACH TRUCK SEPARATELY
#
#   run three independent simulations, one per garbage type. a truck walks the
#   houses left to right and buffers the driving time of the edges it crosses
#   in `pending`; the buffer is only paid (and cleared) when the truck
#   actually meets its own type, so edges past the last occurrence are never
#   charged and nothing has to be trimmed afterwards.
#
#   slower in constant factor (three passes) but it mirrors the problem
#   statement directly instead of relying on the prefix-sum shortcut.
#
# time = O(3n) = O(n), space = O(1)
class Solution(object):
    def garbageCollection(self, garbage, travel):
        total = 0
        for t in "MPG":
            pending = 0
            for i, g in enumerate(garbage):
                if i:
                    pending += travel[i - 1]
                cnt = g.count(t)
                if cnt:
                    total += pending + cnt
                    pending = 0
        return total
