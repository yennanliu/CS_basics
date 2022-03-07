"""

1109. Corporate Flight Bookings
Medium

There are n flights that are labeled from 1 to n.

You are given an array of flight bookings bookings, where bookings[i] = [firsti, lasti, seatsi] represents a booking for flights firsti through lasti (inclusive) with seatsi seats reserved for each flight in the range.

Return an array answer of length n, where answer[i] is the total number of seats reserved for flight i.

 

Example 1:

Input: bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
Output: [10,55,45,25,25]
Explanation:
Flight labels:        1   2   3   4   5
Booking 1 reserved:  10  10
Booking 2 reserved:      20  20
Booking 3 reserved:      25  25  25  25
Total seats:         10  55  45  25  25
Hence, answer = [10,55,45,25,25]
Example 2:

Input: bookings = [[1,2,10],[2,2,15]], n = 2
Output: [10,25]
Explanation:
Flight labels:        1   2
Booking 1 reserved:  10  10
Booking 2 reserved:      15
Total seats:         10  25
Hence, answer = [10,25]

 

Constraints:

1 <= n <= 2 * 104
1 <= bookings.length <= 2 * 104
bookings[i].length == 3
1 <= firsti <= lasti <= n
1 <= seatsi <= 104

"""

# V0

# V1
# IDEA : ARRAY + prefix sum
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328856/JavaC%2B%2BPython-Sweep-Line
# IDEA :
# Set the change of seats for each day.
# If booking = [i, j, k],
# it needs k more seat on ith day,
# and we don't need these seats on j+1th day.
# We accumulate these changes then we have the result that we want.
# Complexity
# Time O(booking + N) for one pass on bookings
# Space O(N) for the result
class Solution:
    def corpFlightBookings(self, bookings, n):
        res = [0] * (n + 1)
        for i, j, k in bookings:
            res[i - 1] += k
            res[j] -= k
        for i in range(1, n):
            res[i] += res[i - 1]
        return res[:-1]

# V1'
# IDEA : ARRAY + prefix sum
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328949/Simple-Python-solution
class Solution:
    def corpFlightBookings(self, bookings: List[List[int]], n: int) -> List[int]:
            answer = n * [0]
            lst = []
            for i, j, num in bookings:
                lst.append((i - 1, num))
                lst.append((j, -num))
            lst.sort()
            curr_num = 0
            prev_i = 0
            for i, num in lst:
                for j in range(prev_i, i):
                    answer[j] += curr_num
                prev_i = i
                curr_num += num
            return answer

# V1''
# IDEA : ARRAY
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328893/Short-python-solution
# IDEA : Simply use two arrays to keep track of how many bookings are added for every flight.
class Solution:
    def corpFlightBookings(self, bookings: List[List[int]], n: int) -> List[int]:        
        opens = [0]*n
        closes = [0]*n
        
        for e in bookings:
            opens[e[0]-1] += e[2]
            closes[e[1]-1] += e[2]
            
        ret, tmp = [0]*n, 0
        for i in range(n):
            tmp += opens[i]
            ret[i] = tmp
            tmp -= closes[i]
            
        return ret

# V1'''
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328986/Python-linear-solution
class Solution:
    def corpFlightBookings(self, bookings: List[List[int]], n: int) -> List[int]:
        res = [0] * (n + 2)
        for booking in bookings:
            start, end, seats = booking
            res[start] += seats
            res[end + 1] -= seats
        
        for i in range(1, len(res)):
            res[i] += res[i - 1]
        
		# don't keep first because bookings are 1-based
		# don't keep last because it's out of range
        return res[1:-1] 

# V1''''
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328863/Python-concise-sum
class Solution:
    def corpFlightBookings(self, bookings: List[List[int]], n: int) -> List[int]:
        res = [0] * n
        i = cur = 0
        for j, val in sorted([[i - 1, k] for i, j, k in bookings] + [[j, -k] for i, j, k in bookings]):
            while i < j:
                res[i] = cur
                i += 1
            cur += val
        return res

# V1''''''
# https://zxi.mytechroad.com/blog/math/leetcode-1109-corporate-flight-bookings/
# C++
# class Solution {
# public:
#   vector<int> corpFlightBookings(vector<vector<int>>& bookings, int n) {
#     vector<int> ans(n + 1);
#     for (const auto& b : bookings) {
#       ans[b[0] - 1] += b[2];
#       ans[b[1]] -= b[2];
#     }
#     for (int i = 1; i < n; ++i)
#       ans[i] += ans[i - 1];
#     ans.pop_back();
#     return ans;
#   }
# };

# V1''''''''
# https://blog.51cto.com/u_15344287/3646723
class Solution:
    def corpFlightBookings(self, bookings: List[List[int]], n: int) -> List[int]:
        lst = [0] * (n + 1)
        for j, k, l in bookings:
            lst[j - 1] += l
            lst[k] -= l

        lst.pop()

        ans = []
        now = 0
        for i in range(len(lst)):
            now += lst[i]
            ans.append(now)

        return ans

# V2