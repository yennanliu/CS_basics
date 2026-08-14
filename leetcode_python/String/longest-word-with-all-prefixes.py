"""

1858. Longest Word With All Prefixes
Medium

Given an array of strings words, find the longest string in words such that every prefix of it is also in words.

For example, let words = ["a", "app", "ap"]. The string "app" has prefixes "ap" and "a", all of which are in words.

Return the string described above. If there is more than one string with the same length, return the lexicographically smallest one, and if no string exists, return "".


Example 1:

Input: words = ["k","ki","kir","kira", "kiran"]
Output: "kiran"
Explanation: "kiran" has prefixes "kira", "kir", "ki", and "k", and all of them appear in words.

Example 2:

Input: words = ["a", "banana", "app", "appl", "ap", "apply", "apple"]
Output: "apple"
Explanation: Both "apple" and "apply" have all their prefixes in words.
However, "apple" is lexicographically smaller, so we return that.

Example 3:

Input: words = ["abc", "bc", "ab", "qwe"]
Output: ""


Constraints:

1 <= words.length <= 10^5
1 <= words[i].length <= 10^5
1 <= sum(words[i].length) <= 10^5
words[i] consists only of lowercase English letters.

"""

# V0
# IDEA : SORT BY LENGTH + INCREMENTAL DP ("good" set)
#
#   a word w is "good" (all its prefixes are present) iff
#     len(w) == 1               and w is in words, OR
#     w[:-1] is itself "good"
#   because w[:-1] being good already guarantees ALL shorter prefixes.
#
#   so process words in increasing length order : when w is examined its
#   parent w[:-1] has already been decided -> one O(len) check per word.
#
#   NOTE : sorting by length only (not lexicographically) is enough for
#          correctness; the tie-break (longest, then smallest) is applied
#          when updating the answer.
#
# time = O(L + n log n), L = total length of all words
# space = O(L)
class Solution(object):
    def longestWord(self, words):
        good = set()
        res = ""

        for w in sorted(words, key=len):
            if len(w) == 1 or w[:-1] in good:
                good.add(w)
                if len(w) > len(res) or (len(w) == len(res) and w < res):
                    res = w

        return res
