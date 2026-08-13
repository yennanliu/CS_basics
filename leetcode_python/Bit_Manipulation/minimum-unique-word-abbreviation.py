"""

411. Minimum Unique Word Abbreviation
Hard

A string can be abbreviated by replacing any number of non-adjacent substrings
with their lengths. For example, a string such as "substitution" could be
abbreviated as (but not limited to):

- "s10n"        ("s ubstitutio n")
- "sub4u4"      ("sub stit u tion")
- "12"          ("substitution")
- "su3i1u2on"   ("su bst i t u ti on")
- "substitution" (no substrings replaced)

Note that "s55n" ("s ubsti tutio n") is not a valid abbreviation of
"substitution" because the replaced substrings are adjacent.

The length of an abbreviation is the number of letters that were not replaced
plus the number of substrings that were replaced. For example, the abbreviation
"s10n" has a length of 3 (2 letters + 1 substring) and "su3i1u2on" has a length
of 9 (6 letters + 3 substrings).

Given a target string target and an array of strings dictionary, return an
abbreviation of target with the shortest possible length such that it is not an
abbreviation of any string in dictionary. If there are multiple shortest
abbreviations, return any of them.

Example 1:

Input: target = "apple", dictionary = ["blade"]
Output: "a4"
Explanation: The shortest abbreviation of "apple" is "5", but this is also an
abbreviation of "blade".
The next shortest abbreviations are "a4" and "4e". "4e" is an abbreviation of
blade while "a4" is not. Hence, return "a4".

Example 2:

Input: target = "apple", dictionary = ["blade","plain","amber"]
Output: "1p3"
Explanation: "5" is an abbreviation of both "apple" but also every word in the
dictionary.
"a4" is an abbreviation of "apple" but also "amber".
"4e" is an abbreviation of "apple" but also "blade".
"1p3", "2p2", and "3l1" are the next shortest abbreviations of "apple".
Since none of them are abbreviations of words in the dictionary, returning any
of them is correct.

Constraints:

m == target.length
n == dictionary.length
1 <= m <= 21
0 <= n <= 1000
1 <= dictionary[i].length <= 100
log2(n) + m <= 21 if n > 0
target and dictionary[i] consist of lowercase English letters.
dictionary does not contain target.

"""

# V0
# IDEA : BITMASK ENUMERATION
#
#  Only dictionary words with the SAME length as target can ever collide.
#  For each such word w build a "diff mask": bit i set <=> w[i] != target[i].
#
#  An abbreviation is described by a mask over target's positions:
#     bit i set  -> position i is replaced (folded into a number)
#     bit i clear-> position i keeps its letter
#
#  The abbreviation still matches word w iff every position where they differ
#  got replaced, i.e.  diff & keep == 0  (keep = complement of mask).
#  So the abbreviation is UNIQUE iff  diff & keep != 0  for every diff.
#
#  Enumerate all 2^m masks, keep the one with the smallest abbreviation length.
#  The constraint log2(n) + m <= 21 bounds n * 2^m by ~2 * 10^6.
#
# time = O(2^m * n)   # m = len(target), n = number of same-length dict words
# space = O(n)
class Solution(object):
    def minAbbreviation(self, target, dictionary):
        m = len(target)

        diffs = []
        for w in dictionary:
            if len(w) != m:
                # a different length can never be abbreviated to the same thing
                continue
            d = 0
            for i in range(m):
                if w[i] != target[i]:
                    d |= 1 << i
            diffs.append(d)

        # nothing can collide -> fold the whole word
        if not diffs:
            return str(m)

        full = (1 << m) - 1
        best_mask, best_len = 0, m + 1

        for mask in range(1 << m):
            keep = full ^ mask
            # every dictionary word must differ at some KEPT position
            ok = True
            for d in diffs:
                if d & keep == 0:
                    ok = False
                    break
            if not ok:
                continue

            length = self._abbr_len(mask, m)
            if length < best_len:
                best_len = length
                best_mask = mask

        return self._to_abbr(target, best_mask)

    def _abbr_len(self, mask, m):
        # a run of replaced positions costs 1 (the number), a kept letter costs 1
        length = 0
        i = 0
        while i < m:
            if mask >> i & 1:
                while i < m and (mask >> i & 1):
                    i += 1
                length += 1
            else:
                length += 1
                i += 1
        return length

    def _to_abbr(self, target, mask):
        m = len(target)
        out = []
        i = 0
        while i < m:
            if mask >> i & 1:
                j = i
                while j < m and (mask >> j & 1):
                    j += 1
                out.append(str(j - i))
                i = j
            else:
                out.append(target[i])
                i += 1
        return "".join(out)
