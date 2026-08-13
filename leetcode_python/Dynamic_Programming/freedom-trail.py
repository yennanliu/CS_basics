"""

514. Freedom Trail
Hard

In the video game Fallout 4, the quest "Road to Freedom" requires players to reach a metal dial
called the "Freedom Trail Ring" and use the dial to spell a specific keyword to open the door.

Given a string ring that represents the code engraved on the outer ring and another string key
that represents the keyword that needs to be spelled, return the minimum number of steps to spell
all the characters in the keyword.

Initially, the first character of the ring is aligned at the "12:00" direction.
You should spell all the characters in key one by one by rotating ring clockwise or anticlockwise
to make each character of the string key aligned at the "12:00" direction and then by pressing
the center button.

At the stage of rotating the ring to spell the key character key[i]:

1. You can rotate the ring clockwise or anticlockwise by one place, which counts as one step.
   The final purpose of the rotation is to align one of ring's characters at the "12:00" direction,
   where this character must equal key[i].
2. If the character key[i] has been aligned at the "12:00" direction, press the center button to spell,
   which also counts as one step. After the pressing, you could begin to spell the next character
   in the key (next stage). Otherwise, you have finished all the spelling.

Example 1:

Input: ring = "godding", key = "gd"
Output: 4
Explanation:
For the first key character 'g', since it is already in place, we just need 1 step to spell this character.
For the second key character 'd', we need to rotate the ring "godding" anticlockwise by two steps
to make it become "ddinggo".
Also, we need 1 more step for spelling.
So the final output is 4.

Example 2:

Input: ring = "godding", key = "godding"
Output: 13


Constraints:

1 <= ring.length, key.length <= 100
ring and key consist of only lower case English letters.
It is guaranteed that key could always be spelled by rotating ring.

"""

# V0
# IDEA : DP over the ring positions
#
#  DP def:
#    - dp[j] = min steps to spell key[0..i] with ring index j aligned at 12:00
#
#  DP eq:
#    - dp_new[j] = min over k in pos[key[i-1]] of
#                     dp[k] + min(|j - k|, n - |j - k|) + 1
#      ( rotating either way on a circle, + 1 for pressing the button )
#
# time = O(m * n^2)  # m = len(key), n = len(ring)
# space = O(n)
from collections import defaultdict
class Solution(object):
    def findRotateSteps(self, ring, key):
        n = len(ring)

        # char -> all indices it shows up in ring
        pos = defaultdict(list)
        for i, c in enumerate(ring):
            pos[c].append(i)

        # init : spell key[0] from index 0
        dp = {}
        for j in pos[key[0]]:
            dp[j] = min(j, n - j) + 1

        for i in range(1, len(key)):
            ndp = {}
            for j in pos[key[i]]:
                best = float("inf")
                for k, cost in dp.items():
                    d = abs(j - k)
                    best = min(best, cost + min(d, n - d) + 1)
                ndp[j] = best
            dp = ndp

        return min(dp.values())
