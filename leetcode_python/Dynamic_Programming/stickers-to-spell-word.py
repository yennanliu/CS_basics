"""

691. Stickers to Spell Word
Hard

We are given n different types of stickers. Each sticker has a lowercase English word on it.

You would like to spell out the given string target by cutting individual letters from
your collection of stickers and rearranging them. You can use each sticker more than once
if you want, and you have infinite quantities of each sticker.

Return the minimum number of stickers that you need to spell out target.
If the task is impossible, return -1.

Note: In all test cases, all words were chosen randomly from the 1000 most common
US English words, and target was chosen as a concatenation of two random words.

Example 1:

Input: stickers = ["with","example","science"], target = "thehat"
Output: 3
Explanation:
We can use 2 "with" stickers, and 1 "example" sticker.
After cutting and rearrange the letters of those stickers, we can form the target "thehat".
Also, this is the minimum number of stickers necessary to form the target string.

Example 2:

Input: stickers = ["notice","possible"], target = "basicbasic"
Output: -1
Explanation:
We cannot form the target "basicbasic" from cutting letters from the given stickers.

Constraints:

n == stickers.length
1 <= n <= 50
1 <= stickers[i].length <= 10
1 <= target.length <= 15
stickers[i] and target consist of lowercase English letters.

"""

# V0
# IDEA : BITMASK DP over the POSITIONS of target
#
#  target is at most 15 chars, so the set of positions already covered fits in an
#  int bitmask (bit i set == target[i] has been supplied by some sticker).
#
#  DP def:
#    - dp[mask] = minimum number of stickers needed to cover exactly the
#                 positions in `mask`
#
#  DP eq:
#    - from every reachable mask, try each sticker: hand out its letters to the
#      still-uncovered positions, cheapest-index-first, producing `nxt`
#        dp[nxt] = min(dp[nxt], dp[mask] + 1)
#
#  Why filling uncovered positions in left-to-right index order is safe: within a
#  single letter, all positions needing that letter are interchangeable, so which
#  ones a given sticker fills does not matter -- only how many.
#
#  Masks are processed in increasing order, and a sticker never clears a bit, so
#  dp[mask] is final by the time it is read.
#
# time = O(2^L * n * L), L = len(target), n = len(stickers)
# space = O(2^L)
from collections import Counter
class Solution(object):
    def minStickers(self, stickers, target):
        L = len(target)
        full = 1 << L
        INF = float('inf')

        # letter multiset of each sticker; only letters appearing in target matter
        needed = set(target)
        sticker_counts = []
        for s in stickers:
            c = Counter(ch for ch in s if ch in needed)
            if c:
                sticker_counts.append(c)

        dp = [INF] * full
        dp[0] = 0

        for mask in range(full):
            if dp[mask] == INF:
                continue
            for count in sticker_counts:
                nxt = mask
                remain = count.copy()
                for i in range(L):
                    if not (nxt >> i) & 1:
                        ch = target[i]
                        if remain[ch] > 0:
                            remain[ch] -= 1
                            nxt |= 1 << i
                if dp[nxt] > dp[mask] + 1:
                    dp[nxt] = dp[mask] + 1

        return -1 if dp[full - 1] == INF else dp[full - 1]
