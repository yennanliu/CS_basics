"""

1181. Before and After Puzzle
Medium

Given a list of phrases, generate a list of Before and After puzzles.

A phrase is a string that consists of lowercase English letters and spaces only.
No space appears in the start or the end of a phrase. There are no consecutive spaces in a phrase.

Before and After puzzles are phrases that are formed by merging two phrases where the last word of the
first phrase is the same as the first word of the second phrase. Note that only the last word of the
first phrase and the first word of the second phrase are merged in this process.

Return the Before and After puzzles that can be formed by every two phrases phrases[i] and phrases[j]
where i != j. Note that the order of matching two phrases matters, we want to consider both orders.

You should return a list of distinct strings sorted lexicographically, after removing all duplicate
phrases in the generated Before and After puzzles.

Example 1:

Input: phrases = ["writing code","code rocks"]
Output: ["writing code rocks"]

Example 2:

Input: phrases = ["mission statement","a quick bite to eat","a chip off the old block","chocolate bar",
                  "mission impossible","a man on a mission","block party","eat my words","bar of soap"]
Output: ["a chip off the old block party","a man on a mission impossible","a man on a mission statement",
         "a quick bite to eat my words","chocolate bar of soap"]

Example 3:

Input: phrases = ["a","b","a"]
Output: ["a"]

Example 4:

Input: phrases = ["ab ba","ba ab","ab ba"]
Output: ["ab ba ab","ba ab ba"]

Constraints:

1 <= phrases.length <= 100
1 <= phrases[i].length <= 100

"""

# V0
# IDEA : HASH TABLE (index the first word) + SET dedup
#
#  group phrase indexes by their FIRST word, then for each phrase
#  look up only the phrases starting with its LAST word.
#
#  note : i != j is on the INDEX, so two identical phrases at different
#         indexes are still a legal pair (see example 3).
#
# time = O(n^2 * L) worst case
# space = O(n * L)
from collections import defaultdict
class Solution(object):
    def beforeAndAfterPuzzles(self, phrases):
        words = [p.split(" ") for p in phrases]

        by_first = defaultdict(list)
        for i, w in enumerate(words):
            by_first[w[0]].append(i)

        res = set()
        for i, w in enumerate(words):
            for j in by_first[w[-1]]:
                if i == j:
                    continue
                res.add(" ".join(w + words[j][1:]))
        return sorted(res)
