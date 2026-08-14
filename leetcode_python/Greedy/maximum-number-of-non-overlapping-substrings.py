"""

1520. Maximum Number of Non-Overlapping Substrings
Hard

Given a string s of lowercase letters, you need to find the maximum number of non-empty substrings of s that meet the following conditions:

1. The substrings do not overlap, that is for any two substrings s[i..j] and s[x..y], either j < x or i > y is true.
2. A substring that contains a certain character c must also contain all occurrences of c.

Find the maximum number of substrings that meet the above conditions. If there are multiple solutions with the same number of substrings, return the one with minimum total length. It can be shown that there exists a unique solution of minimum total length.

Notice that you can return the substrings in any order.


Example 1:

Input: s = "adefaddaccc"
Output: ["e","f","ccc"]
Explanation: The following are all the possible substrings that meet the conditions:
[
  "adefaddaccc"
  "adefadda",
  "ef",
  "e",
  "f",
  "ccc",
]
If we choose the first string, we cannot choose anything else and we'd get only 1. If we choose "adefadda", we are left with "ccc" which is the only one that doesn't overlap, thus obtaining 2 substrings. Notice also, that it's not optimal to choose "ef" since it can be split into two. Therefore, the optimal way is to choose ["e","f","ccc"] which gives us 3 substrings. No other solution of the same number of substrings exist.

Example 2:

Input: s = "abbaccd"
Output: ["d","bb","cc"]
Explanation: Notice that while the set of substrings ["d","abba","cc"] also has length 3, it's considered incorrect since it has larger total length.


Constraints:

1 <= s.length <= 10^5
s contains only lowercase English letters.

"""

# V0
# IDEA : SMALLEST VALID INTERVAL PER LETTER + GREEDY BY EARLIEST END
#
#   step 1 : first[c] / last[c] = first and last index of letter c.
#
#   step 2 : for each letter c, grow the candidate [l, r] = [first[c], last[c]]
#            by scanning i from l to r and pushing r out to last[s[i]].
#            if some s[i] has first[s[i]] < l the interval can NOT start at
#            l -> drop this candidate (it will be produced, correctly, when
#            we process that other letter instead).
#            this yields the MINIMAL valid interval anchored at first[c].
#
#   step 3 : the surviving intervals are pairwise either nested or disjoint,
#            so the classic "sort by right end, take it if it starts after
#            the last taken end" greedy maximises the count — and because a
#            nested interval always ends earlier, it also minimises total length.
#
# time = O(26 * n), space = O(n)
class Solution(object):
    def maxNumOfSubstrings(self, s):
        first, last = {}, {}
        for i, c in enumerate(s):
            if c not in first:
                first[c] = i
            last[c] = i

        intervals = []
        for c in first:
            l = first[c]
            r = last[c]
            i = l
            ok = True
            while i <= r:
                if first[s[i]] < l:
                    ok = False
                    break
                if last[s[i]] > r:
                    r = last[s[i]]
                i += 1
            if ok:
                intervals.append((r, l))

        intervals.sort()
        res = []
        prev_end = -1
        for r, l in intervals:
            if l > prev_end:
                res.append(s[l:r + 1])
                prev_end = r
        return res
