"""

1320. Minimum Distance to Type a Word Using Two Fingers
Hard

You have a keyboard layout in the X-Y plane, where each English uppercase letter is located
at some coordinate.

For example, the letter 'A' is located at coordinate (0, 0), the letter 'B' is located at
coordinate (0, 1), the letter 'P' is located at coordinate (2, 3) and the letter 'Z' is located
at coordinate (4, 1).

Given the string word, return the minimum total distance to type such string using only two fingers.

The distance between coordinates (x1, y1) and (x2, y2) is |x1 - x2| + |y1 - y2|.

Note that the initial positions of your two fingers are considered free so do not count towards
your total distance, also your two fingers do not have to start at the first letter or the first
two letters.


Example 1:

Input: word = "CAKE"
Output: 3
Explanation: Using two fingers, one optimal way to type "CAKE" is:
Finger 1 on letter 'C' -> cost = 0
Finger 1 on letter 'A' -> cost = Distance from letter 'C' to letter 'A' = 2
Finger 2 on letter 'K' -> cost = 0
Finger 2 on letter 'E' -> cost = Distance from letter 'K' to letter 'E' = 1
Total distance = 3

Example 2:

Input: word = "HAPPY"
Output: 6
Explanation: Using two fingers, one optimal way to type "HAPPY" is:
Finger 1 on letter 'H' -> cost = 0
Finger 1 on letter 'A' -> cost = Distance from letter 'H' to letter 'A' = 2
Finger 2 on letter 'P' -> cost = 0
Finger 2 on letter 'P' -> cost = Distance from letter 'P' to letter 'P' = 0
Finger 1 on letter 'Y' -> cost = Distance from letter 'A' to letter 'Y' = 4
Total distance = 6


Constraints:

2 <= word.length <= 300
word consists of uppercase English letters.

"""

# V0
# IDEA : DP over "where the OTHER finger rests" (the typing finger is implicit)
#
#   after typing word[i], one finger necessarily sits on word[i]; the only
#   free information is where the second finger is. So :
#     dp[j] = min cost so far, one finger on word[i], the other on letter j
#   with j = 26 meaning "the second finger has not been placed yet" (free).
#
#   moving from word[i-1] to word[i] there are exactly 2 choices :
#     1) the finger already on word[i-1] types it
#        -> dp'[j]        = dp[j] + dist(word[i-1], word[i])
#     2) the OTHER finger (sitting on j) types it, so word[i-1] becomes
#        the resting letter
#        -> dp'[word[i-1]] = dp[j] + dist(j, word[i])
#
#   NOTE : dist(26, x) = 0 -- the first placement of a finger is free, which
#          is exactly what the problem's "initial positions are free" means.
#   NOTE : the keyboard is a 6-wide grid, so letter k sits at (k // 6, k % 6).
#
# time = O(26 * n), space = O(26)
class Solution(object):
    def minimumDistance(self, word):
        FREE = 26
        INF = float('inf')

        def dist(a, b):
            if a == FREE or b == FREE:
                return 0
            return abs(a // 6 - b // 6) + abs(a % 6 - b % 6)

        idx = [ord(ch) - ord('A') for ch in word]

        # after typing word[0] : one finger on idx[0], the other still free
        dp = [INF] * 27
        dp[FREE] = 0

        for i in range(1, len(idx)):
            prev, cur = idx[i - 1], idx[i]
            step = dist(prev, cur)
            ndp = [INF] * 27
            for j in range(27):
                if dp[j] == INF:
                    continue
                # same finger keeps typing
                if dp[j] + step < ndp[j]:
                    ndp[j] = dp[j] + step
                # the resting finger takes over
                cost = dp[j] + dist(j, cur)
                if cost < ndp[prev]:
                    ndp[prev] = cost
            dp = ndp

        return min(dp)
