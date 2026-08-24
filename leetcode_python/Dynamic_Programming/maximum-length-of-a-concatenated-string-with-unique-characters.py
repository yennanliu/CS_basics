"""

1239. Maximum Length of a Concatenated String with Unique Characters
Medium

You are given an array of strings arr. A string s is formed by the concatenation of a subsequence of arr that has unique characters.

Return the maximum possible length of s.

A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: arr = ["un","iq","ue"]
Output: 4
Explanation: All the valid concatenations are:
- ""
- "un"
- "iq"
- "ue"
- "uniq" ("un" + "iq")
- "ique" ("iq" + "ue")
Maximum length is 4.

Example 2:

Input: arr = ["cha","r","act","ers"]
Output: 6
Explanation: Possible longest valid concatenations are "chaers" ("cha" + "ers") and "acters" ("act" + "ers").

Example 3:

Input: arr = ["abcdefghijklmnopqrstuvwxyz"]
Output: 26
Explanation: The only string in arr has all 26 characters.


Constraints:

1 <= arr.length <= 16
1 <= arr[i].length <= 26
arr[i] contains only lowercase English letters.

"""

# V0
# IDEA: BITMASK DP (build set of reachable "used letters" states)
"""
 DP def:
    - masks = list of all reachable 26-bit states, where bit k = 1
      means letter 'a'+k is already used by the chosen subsequence
    - start with masks = [0] (choose nothing)

 DP eq:
    - for each word w with mask x (skip w if w itself has dup letters):
        for every already reachable state y:
            if x & y == 0  ->  x | y is also reachable

 answer = max popcount over all reachable states
"""
# time = O(2^n + L), n = len(arr), L = total chars
# space = O(2^n)
class Solution(object):
    def maxLength(self, arr):
        masks = [0]
        res = 0
        for w in arr:
            # build the bitmask of w ; skip w if it has a repeated letter
            x = 0
            dup = False
            for ch in w:
                b = 1 << (ord(ch) - ord('a'))
                if x & b:
                    dup = True
                    break
                x |= b
            if dup:
                continue
            """
            NOTE !!!

                iterate over a SNAPSHOT (masks[:]) of the list,
                since we append to `masks` inside the loop.
                (each word may be used at most once)
            """
            for y in masks[:]:
                if x & y == 0:
                    nxt = x | y
                    masks.append(nxt)
                    res = max(res, bin(nxt).count('1'))
        return res


# V1
# IDEA: BACKTRACKING (pick / not pick each word)
"""

DP def
    masks: the set of all REACHABLE 26-bit states, where bit k = 1 means

           letter 'a'+k is already used by the chosen subsequence

           -> start with masks = [0] (choose nothing)

DP eq

     for each word w with bitmask x (SKIP w if w itself has duplicate letters):

        for every already reachable state y:

            if x & y == 0:      # no shared letter

                x | y is also reachable


    -> e.g. NOTE !!! iterate over a SNAPSHOT of `masks`, since new states are
              appended inside the loop - each word may be used at most ONCE

     ans = max popcount over all reachable states

"""
# time = O(2^n * 26)
# space = O(n)
class Solution(object):
    def maxLength(self, arr):
        def dfs(i, used):
            if i == len(arr):
                return bin(used).count('1')
            # not pick arr[i]
            best = dfs(i + 1, used)
            # pick arr[i] (only if compatible)
            x = masks[i]
            if x != -1 and (x & used) == 0:
                best = max(best, dfs(i + 1, used | x))
            return best

        # pre-compute masks ; -1 marks a word with duplicated letters
        masks = []
        for w in arr:
            x = 0
            for ch in w:
                b = 1 << (ord(ch) - ord('a'))
                if x & b:
                    x = -1
                    break
                x |= b
            masks.append(x)

        return dfs(0, 0)
