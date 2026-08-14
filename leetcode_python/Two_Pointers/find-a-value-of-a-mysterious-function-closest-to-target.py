"""

1521. Find a Value of a Mysterious Function Closest to Target
Hard

Winston was given a mysterious function func. He has an integer array arr and an integer target and he wants to find the values l and r that make the value |func(arr, l, r) - target| minimum possible.

func(arr, l, r) is the bitwise AND of arr[l], arr[l+1], ..., arr[r].

Return the minimum possible value of |func(arr, l, r) - target|.

Notice that func should be called with the values l and r where 0 <= l, r < arr.length.


Example 1:

Input: arr = [9,12,3,7,15], target = 5
Output: 2
Explanation: Calling func with all the pairs of [l,r] = [[0,0],[1,1],[2,2],[3,3],[4,4],[0,1],[1,2],[2,3],[3,4],[0,2],[1,3],[2,4],[0,3],[1,4],[0,4]], Winston got the following results [9,12,3,7,15,8,0,3,7,0,0,3,0,0,0]. The value closest to 5 is 7 and 3, thus the minimum difference is 2.

Example 2:

Input: arr = [1000000,1000000,1000000], target = 1
Output: 999999
Explanation: Winston called the func with all possible values of [l,r] and he always got 1000000, thus the min difference is 999999.

Example 3:

Input: arr = [1,2,4,8,16], target = 0
Output: 0


Constraints:

1 <= arr.length <= 10^5
1 <= arr[i] <= 10^6
0 <= target <= 10^7

"""

# V0
# IDEA : SET OF DISTINCT AND-VALUES ENDING AT EACH INDEX
#
#   fix the right end r. as the left end l moves left, the AND
#   arr[l] & ... & arr[r] only ever CLEARS bits, so it is monotonically
#   non-increasing and can change at most ~20 times (arr[i] <= 10^6).
#   => at most ~20 distinct values for each r.
#
#   keep that small set and roll it forward :
#     S(r) = { x & arr[r] : x in S(r-1) } union { arr[r] }
#
#   check every member against target as we go.
#   NOTE : the set stays tiny (log(max) entries), which is what turns the
#          O(n^2) pair enumeration into O(n log M).
#
# time = O(n * log M), space = O(log M)
class Solution(object):
    def closestToTarget(self, arr, target):
        res = abs(arr[0] - target)
        cur = set()
        for x in arr:
            nxt = set([x])
            for y in cur:
                nxt.add(x & y)
            cur = nxt
            for y in cur:
                d = abs(y - target)
                if d < res:
                    res = d
            if res == 0:
                return 0
        return res
