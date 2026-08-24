"""

2222. Number of Ways to Select Buildings
Medium

You are given a 0-indexed binary string s which represents the types of buildings along a street where:

s[i] = '0' denotes that the ith building is an office and
s[i] = '1' denotes that the ith building is a restaurant.

As a city official, you would like to select 3 buildings for random inspection. However, to ensure variety, no two consecutive buildings out of the selected buildings can be of the same type.

For example, given s = "001101", we cannot select the 1st, 3rd, and 5th buildings as that would form "011" which is not allowed due to having two consecutive buildings of the same type.

Return the number of valid ways to select 3 buildings.


Example 1:

Input: s = "001101"
Output: 6
Explanation:
The following sets of indices selected are valid:
- [0,2,4] from "001101" forms "010"
- [0,3,4] from "001101" forms "010"
- [1,2,4] from "001101" forms "010"
- [1,3,4] from "001101" forms "010"
- [2,4,5] from "001101" forms "101"
- [3,4,5] from "001101" forms "101"
No other selection is valid. Thus, there are 6 total ways.

Example 2:

Input: s = "11100"
Output: 0
Explanation: It can be shown that there are no valid selections.


Constraints:

3 <= s.length <= 10^5
s[i] is either '0' or '1'.

"""

# V0
# IDEA : COUNTING BY THE MIDDLE BUILDING
#
#   a valid triple is "010" or "101", so it is fully described by its MIDDLE
#   character x : both outer characters must equal x ^ 1.
#
#   sweep i as the middle index, maintaining
#     l[c] = count of character c strictly left of i
#     r[c] = count of character c strictly right of i
#   contribution of i = l[x ^ 1] * r[x ^ 1]
#
#   NOTE : this counts every triple exactly once (one middle per triple).
#
"""

DP def
    a valid triple is "010" or "101", so it is fully described by its MIDDLE
    character x - both outer characters must equal x ^ 1

    l[c]: count of character c strictly LEFT of the current index
    r[c]: count of character c strictly RIGHT of it

DP eq

     sweeping i as the MIDDLE index, with x = s[i]:

        res += l[x ^ 1] * r[x ^ 1]

     then l[x] += 1  (and r[x] was already decremented)


    -> e.g. every triple is counted EXACTLY ONCE, since it has exactly one
              middle

     ans = res

"""
# time = O(n), space = O(1)
class Solution(object):
    def numberOfWays(self, s):
        l = [0, 0]
        r = [s.count("0"), s.count("1")]
        res = 0
        for c in s:
            x = 1 if c == "1" else 0
            r[x] -= 1
            res += l[x ^ 1] * r[x ^ 1]
            l[x] += 1
        return res
