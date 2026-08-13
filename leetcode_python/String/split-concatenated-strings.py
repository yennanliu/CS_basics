"""

555. Split Concatenated Strings
Medium

You are given an array of strings strs. You could concatenate these strings together into a loop,
where for each string, you could choose to reverse it or not. Among all the possible loops

Return the lexicographically largest string after cutting the loop, which will make the looped string
into a regular one.

Specifically, to find the lexicographically largest string, you need to experience two phases:

1. Concatenate all the strings into a loop, where you can reverse some strings or not and connect them
   in the same order as given.
2. Cut and make one breakpoint in any place of the loop, which will make the looped string into
   a regular one starting from the character at the cutpoint.

And your job is to find the lexicographically largest one among all the possible regular strings.

Example 1:

Input: strs = ["abc","xyz"]
Output: "zyxcba"
Explanation: You can get the looped string "-abcxyz-", "-abczyx-", "-cbaxyz-", "-cbazyx-",
where '-' represents the looped status.
The answer string came from the fourth looped one, where you could cut from the middle character 'a'
and get "zyxcba".

Example 2:

Input: strs = ["abc"]
Output: "cba"


Constraints:

1 <= strs.length <= 1000
1 <= strs[i].length <= 1000
1 <= sum(strs[i].length) <= 1000
strs[i] consists of lowercase English letters.

"""

# V0
# IDEA : GREEDY
#
#   key observation : the cut happens INSIDE exactly one string strs[i].
#   For every OTHER string, its orientation is independent of the cut,
#   so we greedily keep max(s, reversed(s)) -> that is always at least as good.
#
#   Then for each i we try both orientations of strs[i] and every cut position j:
#       cur = cand[j:] + (rest of the loop) + cand[:j]
#   and keep the largest one.
#
# time = O(L * (L + n))  # L = total length of all strings, n = len(strs)
# space = O(L)
class Solution(object):
    def splitLoopedString(self, strs):
        # best orientation of every string (used for all strings except the cut one)
        best = [max(s, s[::-1]) for s in strs]
        n = len(strs)

        res = ""
        for i in range(n):
            # everything in the loop after strs[i], wrapping around to just before strs[i]
            mid = "".join(best[i + 1:]) + "".join(best[:i])

            # the cut string itself : try both orientations
            for cand in (strs[i], strs[i][::-1]):
                for j in range(len(cand)):
                    cur = cand[j:] + mid + cand[:j]
                    if cur > res:
                        res = cur

        return res
