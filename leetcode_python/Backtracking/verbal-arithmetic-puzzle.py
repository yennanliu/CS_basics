"""

1307. Verbal Arithmetic Puzzle
Hard

Given an equation, represented by words on the left side and the result on the
right side.

You need to check if the equation is solvable under the following rules:

- Each character is decoded as one digit (0 - 9).
- No two characters can map to the same digit.
- Each words[i] and result are decoded as one number without leading zeros.
- Sum of numbers on the left side (words) will equal to the number on the
  right side (result).

Return true if the equation is solvable, otherwise return false.


Example 1:

Input: words = ["SEND","MORE"], result = "MONEY"
Output: true
Explanation: Map 'S'-> 9, 'E'->5, 'N'->6, 'D'->7, 'M'->1, 'O'->0, 'R'->8, 'Y'->'2'
Such that: "SEND" + "MORE" = "MONEY" ,  9567 + 1085 = 10652

Example 2:

Input: words = ["SIX","SEVEN","SEVEN"], result = "TWENTY"
Output: true
Explanation: Map 'S'-> 6, 'I'->5, 'X'->0, 'E'->8, 'V'->7, 'N'->2, 'T'->1, 'W'->'3', 'Y'->4
Such that: "SIX" + "SEVEN" + "SEVEN" = "TWENTY" ,  650 + 68782 + 68782 = 138214

Example 3:

Input: words = ["LEET","CODE"], result = "POINT"
Output: false
Explanation: There is no possible mapping to satisfy the equation, so we
return false. Note that two different characters cannot map to the same digit.


Constraints:

2 <= words.length <= 5
1 <= words[i].length, result.length <= 7
words[i], result contain only uppercase English letters.
The number of different characters used in the expression is at most 10.

"""

# V0
# IDEA : coefficient backtracking
#        Turn the whole equation into a single linear form:
#            sum(coef[ch] * digit[ch]) == 0
#        where a letter in words[i] contributes +10^p and a letter in result
#        contributes -10^p. Then backtrack over the (at most 10) distinct
#        letters, assigning distinct digits, with a running best/worst bound
#        used for pruning.
# time = O(10!) worst case, heavily pruned in practice
# space = O(1), at most 10 distinct letters
class Solution(object):
    def isSolvable(self, words, result):
        # a word longer than the result can never sum to the result
        for w in words:
            if len(w) > len(result):
                return False

        coef = {}
        leading = set()
        for w in words:
            for i, ch in enumerate(w):
                coef[ch] = coef.get(ch, 0) + 10 ** (len(w) - 1 - i)
            if len(w) > 1:
                leading.add(w[0])
        for i, ch in enumerate(result):
            coef[ch] = coef.get(ch, 0) - 10 ** (len(result) - 1 - i)
        if len(result) > 1:
            leading.add(result[0])

        # biggest |coefficient| first -> the bound prunes earlier
        letters = sorted(coef.keys(), key=lambda c: -abs(coef[c]))
        n = len(letters)
        if n > 10:
            return False

        # suffix bounds: max / min contribution still reachable from index i on
        pos_suf = [0] * (n + 1)
        neg_suf = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            c = coef[letters[i]]
            pos_suf[i] = pos_suf[i + 1] + (9 * c if c > 0 else 0)
            neg_suf[i] = neg_suf[i + 1] + (9 * c if c < 0 else 0)

        used = [False] * 10

        def backtrack(i, total):
            if i == n:
                return total == 0
            # even the most positive / most negative completion cannot reach 0
            if total + pos_suf[i] < 0 or total + neg_suf[i] > 0:
                return False
            ch = letters[i]
            c = coef[ch]
            start = 1 if ch in leading else 0
            for d in range(start, 10):
                if used[d]:
                    continue
                used[d] = True
                if backtrack(i + 1, total + c * d):
                    return True
                used[d] = False
            return False

        return backtrack(0, 0)
