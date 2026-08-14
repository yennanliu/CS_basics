"""

1419. Minimum Number of Frogs Croaking
Medium

You are given the string croakOfFrogs, which represents a combination of the string "croak" from different frogs, that is, multiple frogs can croak at the same time, so multiple "croak" are mixed.

Return the minimum number of different frogs to finish all the croaks in the given string.

A valid "croak" means a frog is printing five letters 'c', 'r', 'o', 'a', and 'k' sequentially. The frogs have to print all five letters to finish a croak. If the given string is not a combination of a valid "croak" return -1.


Example 1:

Input: croakOfFrogs = "croakcroak"
Output: 1
Explanation: One frog yelling "croak" twice.

Example 2:

Input: croakOfFrogs = "crcoakroak"
Output: 2
Explanation: The minimum number of frogs is two.
The first frog could yell "crcoakroak".
The second frog could yell later "crcoakroak".

Example 3:

Input: croakOfFrogs = "croakcrook"
Output: -1
Explanation: The given string is an invalid combination of "croak" from different frogs.


Constraints:

1 <= croakOfFrogs.length <= 10^5
croakOfFrogs is either 'c', 'r', 'o', 'a', or 'k'.

"""

# V0
# IDEA : GREEDY COUNTING OF FROGS IN EACH STAGE
#
#   cnt[i] = how many frogs are currently sitting right after letter i of
#   "croak". a 'c' starts a new frog; any other letter i must consume a
#   frog waiting at stage i-1, else the string is malformed -> -1.
#   a 'k' finishes a frog, freeing it for reuse, so the answer is the peak
#   number of simultaneously active (started but unfinished) frogs.
#   NOTE : at the very end every frog must have reached 'k' -> if any
#          stage still holds frogs the string is invalid.
#
# time = O(n), space = O(1)
class Solution(object):
    def minNumberOfFrogs(self, croakOfFrogs):
        order = "croak"
        pos = dict((c, i) for i, c in enumerate(order))
        cnt = [0] * 5
        active = 0
        res = 0

        for ch in croakOfFrogs:
            if ch not in pos:
                return -1
            i = pos[ch]
            if i == 0:
                cnt[0] += 1
                active += 1
                res = max(res, active)
            else:
                if cnt[i - 1] == 0:
                    return -1
                cnt[i - 1] -= 1
                if i == 4:
                    active -= 1     # frog done, can be reused
                else:
                    cnt[i] += 1

        return -1 if active else res
