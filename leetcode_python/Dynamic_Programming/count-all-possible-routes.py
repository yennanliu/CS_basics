"""

1575. Count All Possible Routes
Hard

You are given an array of distinct positive integers locations where locations[i] represents the position of city i. You are also given integers start, finish and fuel representing the starting city, ending city, and the initial amount of fuel you have, respectively.

At each step, if you are at city i, you can pick any city j such that j != i and 0 <= j < locations.length and move to city j. Moving from city i to city j reduces the amount of fuel you have by |locations[i] - locations[j]|. Please notice that |x| denotes the absolute value of x.

Notice that fuel cannot become negative at any point in time, and that you are allowed to visit any city more than once (including start and finish).

Return the count of all possible routes from start to finish. Since the answer may be too large, return it modulo 10^9 + 7.

Example 1:

Input: locations = [2,3,6,8,4], start = 1, finish = 3, fuel = 5
Output: 4
Explanation: The following are all possible routes, each uses 5 units of fuel:
1 -> 3
1 -> 2 -> 3
1 -> 4 -> 3
1 -> 4 -> 2 -> 3

Example 2:

Input: locations = [4,3,1], start = 1, finish = 0, fuel = 6
Output: 5
Explanation: The following are all possible routes:
1 -> 0, used fuel = 1
1 -> 2 -> 0, used fuel = 5
1 -> 2 -> 1 -> 0, used fuel = 5
1 -> 0 -> 1 -> 0, used fuel = 3
1 -> 0 -> 1 -> 0 -> 1 -> 0, used fuel = 5

Example 3:

Input: locations = [5,2,1], start = 0, finish = 2, fuel = 3
Output: 0
Explanation: It is impossible to get from 0 to 2 using only 3 units of fuel since the shortest route needs 4 units of fuel.

Constraints:

2 <= locations.length <= 100
1 <= locations[i] <= 10^9
All integers in locations are distinct.
0 <= start, finish < locations.length
1 <= fuel <= 200

"""

# V0
# IDEA : DP over (fuel left, city) -- bottom up so no deep recursion
#
#   f[k][i] = number of routes that start at city i with k fuel and end
#             at "finish" (the trip may pass through finish and go on).
#     f[k][i] = (1 if i == finish else 0)
#             + sum over j != i with d(i, j) <= k of f[k - d(i,j)][j]
#   answer = f[fuel][start].
#   NOTE : reaching finish is counted immediately, that is why "+1" is
#          added at every level and not only when fuel runs out.
#
# time = O(fuel * n^2), space = O(fuel * n)
class Solution(object):
    def countRoutes(self, locations, start, finish, fuel):
        MOD = 10 ** 9 + 7
        n = len(locations)
        f = [[0] * n for _ in range(fuel + 1)]
        for k in range(fuel + 1):
            row = f[k]
            for i in range(n):
                v = 1 if i == finish else 0
                li = locations[i]
                for j in range(n):
                    if j == i:
                        continue
                    d = li - locations[j]
                    if d < 0:
                        d = -d
                    if d <= k:
                        v += f[k - d][j]
                row[i] = v % MOD
        return f[fuel][start]
