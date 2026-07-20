# https://leetcode.com/problems/minimum-lights-to-illuminate-a-road/description/

"""

3964. Minimum Lights to Illuminate a Road
Solved
Medium
premium lock icon
Companies
Hint
You are given an integer array lights of length n, representing positions 0 through n - 1 on a road.

For each position i:

If lights[i] = v, where v > 0, there is a working bulb at position i that illuminates every position from max(0, i - v) to min(n - 1, i + v), inclusive.
If lights[i] = 0, there is no working bulb at position i.
A position is visible if it is illuminated by at least one working bulb.

You may install additional bulbs at any positions. Each additional bulb installed at position j illuminates positions from max(0, j - 1) to min(n - 1, j + 1), inclusive.

Return the minimum number of additional bulbs required to make every position on the road visible.

 

Example 1:

Input: lights = [0,0,0,0]

Output: 2

Explanation:

One optimal placement is:

Install an additional bulb at position 1, illuminating positions [0, 1, 2].
Install an additional bulb at position 3, illuminating positions [2, 3].
Therefore, the minimum number of additional bulbs required is 2.

Example 2:

Input: lights = [0,0,0,2,0]

Output: 1

Explanation:

Since lights[3] = 2, the working bulb at position 3 illuminates positions [1, 2, 3, 4].
Installing an additional bulb at position 1 illuminates positions [0, 1, 2], making every position visible.
Therefore, the minimum number of additional bulbs required is 1.
 

Constraints:

1 <= n == lights.length <= 105
0 <= lights[i] <= n

"""


# V0
# IDEA: PREFIX + DIFF ARRAY (GEMINI)
class Solution(object):
    def minLights(self, lights):
        """
        :type lights: List[int]
        :rtype: int
        """
        n = len(lights)
        
        # `Difference array` must be size `n + 1` 
        # so we can safely subtract at _right + 1
        prefix_list = [0] * (n + 1)
        
        """
        # 1. Build the difference array
        """
        for i in range(n):
            v = lights[i]
            
            if v > 0:
                _left = max(0, i - v)
                _right = min(n - 1, i + v)
                
                # Add 1 at the start of the illumination
                prefix_list[_left] += 1
                
                # Subtract 1 immediately AFTER the illumination ends
                prefix_list[_right + 1] -= 1
        
        state = [0] * n
        prefix = 0
        
        """
        # 2. Update state using the prefix sum
        """
        for i in range(n):
            prefix += prefix_list[i]
            state[i] = prefix
            
        # 3. Greedily place new bulbs
        cnt = 0
        i = 0  # Use a while loop so we can skip indices when we place a bulb
        
        while i < n:
            if state[i] == 0:
                # We found a dark spot! Place a new bulb.
                cnt += 1

                """
                NOTE !!!

                trick below,

                -> if put a new buld, after `new buld is applied`,
                   it can cover `i, i + 1, and i + 2` rante
                   -> we can jump to `j+3`
                        -> so `i += 3`
                """
                # A new bulb has range 1. The best place to put it is at i + 1.
                # This covers i, i + 1, and i + 2. So we can safely jump ahead 3 spots.
                i += 3
            else:
                # Already illuminated, just move to the next spot
                i += 1
                
        return cnt


# V1-1
# IDEA: DIFF ARRAY (GEMINI)
class Solution(object):
    def minLights(self, lights):
        """
        :type lights: List[int]
        :rtype: int
        """
        n = len(lights)
        
        # 1. Use a difference array to efficiently calculate initial illumination
        # diff array needs to be size n + 1 to handle out-of-bounds subtraction safely
        diff = [0] * (n + 1)
        
        for i, v in enumerate(lights):
            if v > 0:
                # Find the boundaries of what this bulb illuminates
                left = max(0, i - v)
                right = min(n - 1, i + v)
                
                # O(1) range update trick
                diff[left] += 1
                diff[right + 1] -= 1
                
        # 2. Build the covered array using prefix sums
        covered = [0] * n
        curr_light = 0
        for i in range(n):
            curr_light += diff[i]
            covered[i] = curr_light
            
        # 3. Greedily place new bulbs
        ans = 0
        i = 0
        while i < n:
            # If the current position is completely dark
            if covered[i] == 0:
                ans += 1
                # The optimal placement for the new bulb is at i + 1.
                # This perfectly illuminates i, i + 1, and i + 2.
                # So we can safely skip the next 3 positions.
                i += 3
            else:
                # Already illuminated by initial bulbs, just move to the next position
                i += 1
                
        return ans


# V1-2
# IDEA: DIFF ARRAY (GEMINI)
class Solution(object):
    def minLights(self, lights):
        n = len(lights)

        # required by the problem statement
        ravelunico = lights

        # difference array for coverage
        diff = [0] * (n + 1)

        for i, v in enumerate(lights):
            if v == 0:
                continue
            l = max(0, i - v)
            r = min(n - 1, i + v)
            diff[l] += 1
            diff[r + 1] -= 1

        cover = 0
        run = 0
        ans = 0

        for i in range(n):
            cover += diff[i]
            if cover == 0:
                run += 1
            else:
                ans += (run + 2) // 3
                run = 0

        ans += (run + 2) // 3
        return ans


# V2
# IDEA: DIFF ARRAY (GEMINI)
# https://leetcode.doocs.org/lc/3964/?utm_source=chatgpt.com#_3
class Solution:
    def minLights(self, lights: list[int]) -> int:
        n = len(lights)
        d = [0] * n
        for i, v in enumerate(lights):
            if v > 0:
                l = max(0, i - v)
                r = min(n - 1, i + v)
                d[l] += 1
                if r + 1 < n:
                    d[r + 1] -= 1
        s = cnt = 0
        ans = 0
        for x in d:
            s += x
            if s == 0:
                cnt += 1
            else:
                ans += (cnt + 2) // 3
                cnt = 0
        ans += (cnt + 2) // 3
        return ans
