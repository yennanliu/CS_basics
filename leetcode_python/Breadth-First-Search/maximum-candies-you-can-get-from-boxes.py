"""

1298. Maximum Candies You Can Get from Boxes
Hard

You have n boxes labeled from 0 to n - 1. You are given four arrays: status, candies, keys,
and containedBoxes where:

status[i] is 1 if the ith box is open and 0 if the ith box is closed,
candies[i] is the number of candies in the ith box,
keys[i] is a list of the labels of the boxes you can open after opening the ith box.
containedBoxes[i] is a list of the boxes you found inside the ith box.

You are given an integer array initialBoxes that contains the labels of the boxes you initially have.
You can take all the candies in any open box and you can use the keys in it to open new boxes
and you also can use the boxes you find in it.

Return the maximum number of candies you can get following the rules above.


Example 1:

Input: status = [1,0,1,0], candies = [7,5,4,100], keys = [[],[],[1],[]], containedBoxes = [[1,2],[3],[],[]], initialBoxes = [0]
Output: 16
Explanation: You will be initially given box 0. You will find 7 candies in it and boxes 1 and 2.
Box 1 is closed and you do not have a key for it so you will open box 2. You will find 4 candies
and a key to box 1 in box 2.
In box 1, you will find 5 candies and box 3 but you will not find a key to box 3 so box 3 will
remain closed.
Total number of candies collected = 7 + 4 + 5 = 16 candy.

Example 2:

Input: status = [1,0,0,0,0,0], candies = [1,1,1,1,1,1], keys = [[1,2,3,4,5],[],[],[],[],[]], containedBoxes = [[1,2,3,4,5],[],[],[],[],[]], initialBoxes = [0]
Output: 6
Explanation: You have initially box 0. Opening it you can find boxes 1,2,3,4 and 5 and their keys.
The total number of candies will be 6.


Constraints:

n == status.length == candies.length == keys.length == containedBoxes.length
1 <= n <= 1000
status[i] is either 0 or 1.
1 <= candies[i] <= 1000
0 <= keys[i].length <= n
0 <= keys[i][j] < n
All values of keys[i] are unique.
0 <= containedBoxes[i].length <= n
0 <= containedBoxes[i][j] < n
All values of containedBoxes[i] are unique.
Each box is contained in one box at most.
0 <= initialBoxes.length <= n
0 <= initialBoxes[i] < n

"""

# V0
# IDEA : BFS (a box becomes openable when it is BOTH owned and unlocked)
#
#   two independent facts gate a box :
#     owned[b]    -> we physically hold it (from initialBoxes or a parent box)
#     status[b]   -> it is unlocked (initially open, or a key was found)
#
#   process a queue of boxes we can actually open. Opening box b :
#     - collect candies[b]
#     - each key k          -> status[k] = 1, and if k is already owned, queue it
#     - each contained box c-> owned[c] = 1, and if c is already open, queue it
#
#   NOTE : the "if the other half is already true" check on both sides is what
#          avoids re-scanning -- whichever fact arrives second triggers the push.
#   NOTE : `opened` guards against pushing the same box twice (a key may be
#          found for an already-open box).
#
# time = O(n + total keys + total contained), space = O(n)
from collections import deque
class Solution(object):
    def maxCandies(self, status, candies, keys, containedBoxes, initialBoxes):
        owned = set(initialBoxes)
        opened = set()
        q = deque()
        res = 0

        for b in initialBoxes:
            if status[b] == 1:
                opened.add(b)
                q.append(b)

        while q:
            b = q.popleft()
            res += candies[b]

            for k in keys[b]:
                status[k] = 1
                if k in owned and k not in opened:
                    opened.add(k)
                    q.append(k)

            for c in containedBoxes[b]:
                owned.add(c)
                if status[c] == 1 and c not in opened:
                    opened.add(c)
                    q.append(c)

        return res
