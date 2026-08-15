"""

3296. Minimum Number of Seconds to Make Mountain Height Zero
Medium

You are given an integer mountainHeight denoting the height of a mountain.

You are also given an integer array workerTimes representing the work time of workers in seconds.

The workers work simultaneously to reduce the height of the mountain. For worker i:

To decrease the mountain's height by x, it takes workerTimes[i] + workerTimes[i] * 2 + ... + workerTimes[i] * x seconds. For example:
    To reduce the height of the mountain by 1, it takes workerTimes[i] seconds.
    To reduce the height of the mountain by 2, it takes workerTimes[i] + workerTimes[i] * 2 seconds, and so on.

Return an integer representing the minimum number of seconds required for the workers to make the height of the mountain 0.


Example 1:

Input: mountainHeight = 4, workerTimes = [2,1,1]
Output: 3
Explanation:
One way the height of the mountain can be reduced to 0 is:
Worker 0 reduces the height by 1, taking workerTimes[0] = 2 seconds.
Worker 1 reduces the height by 2, taking workerTimes[1] + workerTimes[1] * 2 = 3 seconds.
Worker 2 reduces the height by 1, taking workerTimes[2] = 1 second.
Since they work simultaneously, the minimum time needed is max(2, 3, 1) = 3 seconds.

Example 2:

Input: mountainHeight = 10, workerTimes = [3,2,2,4]
Output: 12
Explanation:
Worker 0 reduces the height by 2, taking workerTimes[0] + workerTimes[0] * 2 = 9 seconds.
Worker 1 reduces the height by 3, taking workerTimes[1] + workerTimes[1] * 2 + workerTimes[1] * 3 = 12 seconds.
Worker 2 reduces the height by 3, taking workerTimes[2] + workerTimes[2] * 2 + workerTimes[2] * 3 = 12 seconds.
Worker 3 reduces the height by 2, taking workerTimes[3] + workerTimes[3] * 2 = 12 seconds.
The number of seconds needed is max(9, 12, 12, 12) = 12 seconds.

Example 3:

Input: mountainHeight = 5, workerTimes = [1]
Output: 15
Explanation:
There is only one worker in this example, so the answer is workerTimes[0] + workerTimes[0] * 2 + workerTimes[0] * 3 + workerTimes[0] * 4 + workerTimes[0] * 5 = 15.


Constraints:

1 <= mountainHeight <= 10^5
1 <= workerTimes.length <= 10^4
1 <= workerTimes[i] <= 10^6

"""

# V0
# IDEA : BINARY SEARCH THE FINISH TIME — THE WORKERS ARE INDEPENDENT
#
#   the workers dig in parallel, so the elapsed time is the maximum over
#   workers. asking "can they finish within T seconds" splits per worker :
#   worker i digging x units spends t*(1 + 2 + ... + x) = t*x*(x+1)/2, so
#
#       x_i(T) = the largest x with t*x*(x+1)/2 <= T
#              = floor((sqrt(1 + 8T/t) - 1) / 2)
#
#   and the answer is feasible iff the x_i sum to at least mountainHeight.
#   more time never hurts, so binary search over T.
#
#   the closed form is corrected by a couple of integer nudges — floats lose
#   precision once T reaches 10^17.
#
# time = O(n log(max T)), space = O(1)
class Solution(object):
    def minNumberOfSeconds(self, mountainHeight, workerTimes):

        def digs(t, T):
            """largest x with t * x * (x+1) / 2 <= T"""
            x = int(((8.0 * T / t + 1) ** 0.5 - 1) / 2)
            while t * (x + 1) * (x + 2) // 2 <= T:
                x += 1
            while x > 0 and t * x * (x + 1) // 2 > T:
                x -= 1
            return x

        def feasible(T):
            total = 0
            for t in workerTimes:
                total += digs(t, T)
                if total >= mountainHeight:
                    return True
            return False

        slowest = max(workerTimes)
        lo, hi = 1, slowest * mountainHeight * (mountainHeight + 1) // 2
        while lo < hi:
            mid = (lo + hi) // 2
            if feasible(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
