"""

3527. Find the Most Common Response
Medium

You are given a 2D string array responses where each responses[i] is an array of
strings representing survey responses from the i^th day.

Return the most common response across all days after removing duplicate
responses within each responses[i]. If there is a tie, return the
lexicographically smallest response.

Example 1:

Input: responses =
[["good","ok","good","ok"],["ok","bad","good","ok","ok"],["good"],["bad"]]

Output: "good"

Explanation:

After removing duplicates within each list, responses = [["good", "ok"], ["ok",
"bad", "good"], ["good"], ["bad"]].

"good" appears 3 times, "ok" appears 2 times, and "bad" appears 2 times.

Return "good" because it has the highest frequency.

Example 2:

Input: responses =
[["good","ok","good"],["ok","bad"],["bad","notsure"],["great","good"]]

Output: "bad"

Explanation:

After removing duplicates within each list we have responses = [["good", "ok"],
["ok", "bad"], ["bad", "notsure"], ["great", "good"]].

"bad", "good", and "ok" each occur 2 times.

The output is "bad" because it is the lexicographically smallest amongst the
words with the highest frequency.

Constraints:

1 <= responses.length <= 1000

1 <= responses[i].length <= 1000

1 <= responses[i][j].length <= 10

responses[i][j] consists of only lowercase English letters

"""

# V0
# IDEA : DEDUPLICATE PER DAY, THEN ARGMAX WITH A LEXICOGRAPHIC TIE-BREAK
#
#   a repeated answer inside one day must not inflate its count, so each day's
#   list is collapsed to a set before counting.
#
#   for the tie-break, sorting the whole counter would be wasteful; instead a
#   single pass keeps the current winner and replaces it only when the count is
#   strictly larger, or equal with a lexicographically smaller string.
#
# time = O(total tokens), space = O(distinct tokens)
from collections import Counter


class Solution(object):
    def findCommonResponse(self, responses):
        cnt = Counter()
        for day in responses:
            cnt.update(set(day))
        best = None
        best_c = -1
        for word, c in cnt.items():
            if c > best_c or (c == best_c and word < best):
                best = word
                best_c = c
        return best
