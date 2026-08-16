"""

3441. Minimum Cost Good Caption
Hard

You are given a string caption of length n. A good caption is a string where
every character appears in groups of at least 3 consecutive occurrences.

For example:

"aaabbb" and "aaaaccc" are good captions.
"aabbb" and "ccccd" are not good captions.

You can perform the following operation any number of times:

Choose an index i (where 0 <= i < n) and change the character at that index to
either:

The character immediately before it in the alphabet (if caption[i] != 'a').
The character immediately after it in the alphabet (if caption[i] != 'z').

Your task is to convert the given caption into a good caption using the minimum
number of operations, and return it. If there are multiple possible good
captions, return the lexicographically smallest one among them. If it is
impossible to create a good caption, return an empty string "".

Example 1:

Input: caption = "cdcd"

Output: "cccc"

Explanation:

It can be shown that the given caption cannot be transformed into a good caption
with fewer than 2 operations. The possible good captions that can be created
using exactly 2 operations are:

"dddd": Change caption[0] and caption[2] to their next character 'd'.
"cccc": Change caption[1] and caption[3] to their previous character 'c'.

Since "cccc" is lexicographically smaller than "dddd", return "cccc".

Example 2:

Input: caption = "aca"

Output: "aaa"

Explanation:

It can be proven that the given caption requires at least 2 operations to be
transformed into a good caption. The only good caption that can be obtained with
exactly 2 operations is as follows:

Operation 1: Change caption[1] to 'b'. caption = "aba".
Operation 2: Change caption[1] to 'a'. caption = "aaa".

Thus, return "aaa".

Example 3:

Input: caption = "bc"

Output: ""

Explanation:

It can be shown that the given caption cannot be converted to a good caption by
using any number of operations.

Constraints:

1 <= caption.length <= 5 * 10^4
caption consists only of lowercase English letters.

"""

# V0
# IDEA : SUFFIX DP WITH TWO STATES, THEN A GREEDY LEX-SMALLEST REPLAY
#
#   changing a letter to c costs |caption[i] - c|, so cost is a per-position
#   function and the only coupling is the "runs of length >= 3" rule.  that
#   makes a suffix DP with two mutually recursive quantities:
#
#     start[i][c] = cheapest suffix from i when a *fresh* run of letter c opens
#                   at i.  it must occupy at least i, i+1, i+2, so
#                   start[i][c] = cost(i,c)+cost(i+1,c)+cost(i+2,c) + cont[i+3][c]
#     cont[j][c]  = cheapest suffix from j when the run of c already has its
#                   three characters, so j is free to either extend it or open
#                   a brand new run:  cont[j][c] = min( best_start[j],
#                                                       cost(j,c) + cont[j+1][c] )
#
#   with best_start[j] = min over c of start[j][c].  note the new run is allowed
#   to reuse the same letter — two adjacent runs of the same letter simply merge
#   into a longer legal run — so no "different letter" side condition is needed.
#
#   for the lexicographically smallest optimum, replay the table forwards
#   carrying the exact budget that still has to be spent.  at each position try
#   letters in alphabetical order and keep the first one whose own cost plus the
#   value of the resulting state equals the remaining budget.  "equals the
#   budget" is precisely the condition for staying on an optimal path, so the
#   first letter that survives it is the smallest possible letter there.
#
# time = O(26 n), space = O(26 n)
class Solution(object):
    def minCostGoodCaption(self, caption):
        n = len(caption)
        if n < 3:
            return ""
        INF = float('inf')
        ch = [ord(x) - 97 for x in caption]

        # cost[j] is materialised lazily; costAt(j, c) = |ch[j] - c|
        def costAt(j, c):
            d = ch[j] - c
            return d if d >= 0 else -d

        cont = [[0] * 26 for _ in range(n + 1)]
        g = [INF] * (n + 1)
        g[n] = 0
        for j in range(n - 1, -1, -1):
            if j + 3 <= n:
                c3 = cont[j + 3]
                a, b, c = ch[j], ch[j + 1], ch[j + 2]
                bestStart = INF
                for col in range(26):
                    v = (abs(a - col) + abs(b - col) + abs(c - col)) + c3[col]
                    if v < bestStart:
                        bestStart = v
                g[j] = bestStart
            gj = g[j]
            row = cont[j]
            nxt = cont[j + 1]
            cur = ch[j]
            for col in range(26):
                v = abs(cur - col) + nxt[col]
                row[col] = gj if gj < v else v

        if g[0] == INF:
            return ""

        def value(j, c, s):
            # cheapest suffix from j given a run of letter c of length s (<=3)
            if s == 3:
                return cont[j][c]
            if s == 2:
                if j >= n:
                    return INF
                return costAt(j, c) + cont[j + 1][c]
            if j + 1 >= n:
                return INF
            return costAt(j, c) + costAt(j + 1, c) + cont[j + 2][c]

        res = []
        budget = g[0]
        prev = -1
        run = 0
        i = 0
        while i < n:
            if 0 < run < 3:
                cands = (prev,)
            else:
                cands = range(26)
            for x in cands:
                nt = 3 if (x == prev and run == 3) else (run + 1 if x == prev else 1)
                need = costAt(i, x) + value(i + 1, x, nt)
                if need == budget:
                    res.append(chr(97 + x))
                    budget -= costAt(i, x)
                    prev = x
                    run = nt
                    i += 1
                    break
        return "".join(res)
