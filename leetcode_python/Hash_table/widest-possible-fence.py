"""

4007. Widest Possible Fence
Solved
Medium
premium lock icon
Companies
Hint
You are given an integer array planks, where planks[i] represents the height of the ith wooden plank. Each plank has a width of 1 unit.

You want to build a fence consisting of planks that all have the same height.

You may either use a plank as is, or combine exactly two distinct original planks into a single plank whose height equals the sum of their heights. Each original plank can be used at most once, and not all original planks need to be used.

Return the maximum possible width of the fence that can be built.

 

Example 1:

Input: planks = [1,3,2,5,7,5,4,2,1]

Output: 4

Explanation:

We can have four planks of height 5.

planks[3] = 5
planks[5] = 5
planks[0] + planks[6] = 1 + 4 = 5
planks[1] + planks[2] = 3 + 2 = 5
Hence, the maximum width is 4.

Example 2:

Input: planks = [2,3,7]

Output: 1

Explanation:

It is impossible to form two planks of the same height, even after combining two distinct original planks.
Since not all original planks need to be used, we can choose any one plank as the fence.
Therefore, the maximum possible width is 1.
 

Constraints:

1 <= planks.length <= 1000
1 <= planks[i] <= 109

"""

# V0
class Solution(object):
    def maximumWidth(self, planks):
        """
        :type planks: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: HASH MAP PAIRING (gemini)
from collections import Counter, defaultdict

class Solution(object):
    def maximumWidth(self, planks):
        """
        :type planks: List[int]
        :rtype: int
        """
        # Count the available frequencies of each plank height
        cnt = Counter(planks)
        
        # t[h] will store the maximum width of a fence built at height 'h'
        t = defaultdict(int)
        
        # Iterate through every unique plank height
        for x, v1 in cnt.items():
            
            # 1. We can use the plank 'x' exactly as is
            t[x] += v1
            
            # 2. We can combine two identical planks of height 'x' to form '2x'
            t[x * 2] += v1 // 2
            
            # 3. We can combine plank 'x' with a different plank 'y'
            for y, v2 in cnt.items():
                # Ensure we only process each (x, y) pair once by enforcing y > x
                if y > x:
                    t[x + y] += min(v1, v2)
                    
        # The optimal solution is simply the maximum width across all heights
        return max(t.values())


# V0-2
# IDEA: HASH MAP (gpt)
from collections import Counter

class Solution(object):
    def maximumWidth(self, planks):
        """
        :type planks: List[int]
        :rtype: int
        """
        freq = Counter(planks)
        values = list(freq.keys())

        # mp[width] = maximum number of groups
        # that can have this total width
        mp = {}

        ans = 1

        for i in range(len(values)):
            x = values[i]
            fx = freq[x]

            # Group: [x]
            mp[x] = mp.get(x, 0) + fx
            ans = max(ans, mp[x])

            # Group: [x, x]
            width = 2 * x
            cnt = fx // 2

            mp[width] = mp.get(width, 0) + cnt
            ans = max(ans, mp[width])

            # Group: [x, y]
            for j in range(i + 1, len(values)):
                y = values[j]
                fy = freq[y]

                width = x + y
                cnt = min(fx, fy)

                mp[width] = mp.get(width, 0) + cnt
                ans = max(ans, mp[width])

        return ans


# V1


# V2
