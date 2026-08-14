"""

1178. Number of Valid Words for Each Puzzle
Hard

With respect to a given puzzle string, a word is valid if both the following conditions are satisfied:

word contains the first letter of puzzle.
For each letter in word, that letter is in puzzle.
    For example, if the puzzle is "abcdefg", then valid words are "faced", "cabbage", and "baggage", while
    invalid words are "beefed" (does not include 'a') and "based" (includes 's' which is not in the puzzle).

Return an array answer, where answer[i] is the number of words in the given word list words that is valid
with respect to the puzzle puzzles[i].

Example 1:

Input: words = ["aaaa","asas","able","ability","actt","actor","access"],
       puzzles = ["aboveyz","abrodyz","abslute","absoryz","actresz","gaswxyz"]
Output: [1,1,3,2,4,0]
Explanation:
1 valid word for "aboveyz" : "aaaa"
1 valid word for "abrodyz" : "aaaa"
3 valid words for "abslute" : "aaaa", "asas", "able"
2 valid words for "absoryz" : "aaaa", "asas"
4 valid words for "actresz" : "aaaa", "asas", "actt", "access"
There are no valid words for "gaswxyz" cause none of the words in the list contains letter 'g'.

Example 2:

Input: words = ["apple","pleas","please"], puzzles = ["aelwxyz","aelpxyz","aelpsxy","saelpxy","xaelpsy"]
Output: [0,1,3,2,0]

Constraints:

1 <= words.length <= 10^5
4 <= words[i].length <= 50
1 <= puzzles.length <= 10^4
puzzles[i].length == 7
words[i] and puzzles[i] consist of lowercase English letters.
Each puzzles[i] does not contain repeated characters.

"""

# V0
# IDEA : BITMASK + HASH TABLE + SUBSET ENUMERATION
#
#  a word only matters by its SET of letters -> compress to a 26-bit mask,
#  and count how many words share each mask.
#
#  a puzzle has exactly 7 letters => only 2^7 = 128 subsets.
#  for every subset that contains the puzzle's first letter,
#  add cnt[subset] (those are exactly the words whose letter-set == subset).
#
#  subset enumeration trick : sub = (sub - 1) & mask
#
# time = O(L + m * 2^7), L = total length of words, m = len(puzzles)
# space = O(n)
from collections import Counter
class Solution(object):
    def findNumOfValidWords(self, words, puzzles):
        cnt = Counter()
        for w in words:
            mask = 0
            for c in w:
                mask |= 1 << (ord(c) - ord('a'))
            cnt[mask] += 1

        res = []
        for p in puzzles:
            first = 1 << (ord(p[0]) - ord('a'))
            full = 0
            for c in p:
                full |= 1 << (ord(c) - ord('a'))

            total = 0
            sub = full
            while True:
                if sub & first:
                    total += cnt[sub]
                if sub == 0:
                    break
                sub = (sub - 1) & full
            res.append(total)
        return res
