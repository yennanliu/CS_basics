"""

3557. Find Maximum Number of Non Intersecting Substrings
Medium

You are given a string word.

Return the maximum number of non-intersecting substrings of word that are at
least four characters long and start and end with the same letter.


Example 1:

Input: word = "abcdeafdef"
Output: 2
Explanation:
The two substrings are "abcdea" and "fdef".

Example 2:

Input: word = "bcdaaaab"
Output: 1
Explanation:
The only substring is "aaaa". Note that we cannot also choose "bcdaaaab" since
it intersects with the other substring.


Constraints:

1 <= word.length <= 2 * 10^5
word consists only of lowercase English letters.

"""

# V0
# IDEA : GREEDY — CLOSE EVERY CANDIDATE AT THE EARLIEST POSSIBLE END
#
#   this is interval scheduling: each valid substring is an interval and we want
#   the largest set of disjoint ones.  the classic optimum is "always take the
#   interval that finishes first", which here means scanning left to right and
#   accepting a substring the moment one becomes legal.
#
#   for a fixed right end, the *earliest* start with the same letter gives the
#   longest — and hence most likely legal — candidate, and it never hurts:
#   any later start with the same letter ends at the same place, so the interval
#   we picked finishes no later than any alternative.
#
#   after accepting, all bookkeeping is reset so later substrings cannot reach
#   back across the one we just took.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxSubstrings(self, word):
        first = {}
        res = 0
        for i, ch in enumerate(word):
            if ch in first:
                if i - first[ch] + 1 >= 4:
                    res += 1
                    first = {}
            else:
                first[ch] = i
        return res
