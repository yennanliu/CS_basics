"""

943. Find the Shortest Superstring
Hard

Given an array of strings words, return the smallest string that contains each string in words as a substring. If there are multiple valid strings of the smallest length, return any of them.

You may assume that no string in words is a substring of another string in words.

Example 1:

Input: words = ["alex","loves","leetcode"]
Output: "alexlovesleetcode"
Explanation: All permutations of "alex","loves","leetcode" would also be accepted.

Example 2:

Input: words = ["catg","ctaagt","gcta","ttca","atgcatc"]
Output: "gctaagttcatgcatc"

Constraints:

1 <= words.length <= 12
1 <= words[i].length <= 20
words[i] consists of lowercase English letters.
All the strings of words are unique.

"""

# V0
# IDEA : BITMASK DP (Travelling Salesman style) + PATH RECONSTRUCTION
#
#  - Since no word is a substring of another, the answer is simply some
#    permutation of the words glued together, sharing the maximum overlap
#    between neighbours.
#  - overlap[i][j] = length of the longest suffix of words[i] that is also
#    a prefix of words[j].
#  - dp[mask][i] = the MAXIMUM total overlap achievable when the set of used
#    words is `mask` and the last word placed is `i`.
#  - Total length = sum(len(w)) - max total overlap, so maximizing overlap
#    minimizes the superstring.
#
"""

DP def
    no word is a substring of another, so the answer is some PERMUTATION of
    the words glued together sharing the maximum overlap between neighbours
    -> a travelling-salesman bitmask DP

    overlap[i][j]: length of the longest SUFFIX of words[i] that is also a

                   PREFIX of words[j]

    dp[mask][i]  : MAXIMUM total overlap when the used words are `mask`

                   and the LAST word placed is i

DP eq

     dp[mask | (1 << j)][j] = max( dp[mask | (1<<j)][j],

                                   dp[mask][i] + overlap[i][j] )

        for every i in mask, every j not in mask


    -> e.g. total length = sum(len(w)) - max total overlap,
              so MAXIMISING overlap MINIMISES the superstring

     init: dp[1 << i][i] = 0
     ans = walk `parent` back from the best dp[full][i] to rebuild the order

"""
# time = O(n^2 * 2^n + n^2 * L), n = len(words), L = max word length
# space = O(n * 2^n)
class Solution(object):
    def shortestSuperstring(self, words):
        n = len(words)

        # overlap[i][j] : longest suffix of words[i] == prefix of words[j]
        overlap = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(n):
                if i == j:
                    continue
                for k in range(min(len(words[i]), len(words[j])), 0, -1):
                    if words[i][-k:] == words[j][:k]:
                        overlap[i][j] = k
                        break

        # dp[mask][i] = max overlap, -1 means "state not reachable"
        dp = [[-1] * n for _ in range(1 << n)]
        parent = [[-1] * n for _ in range(1 << n)]
        for i in range(n):
            dp[1 << i][i] = 0

        for mask in range(1 << n):
            for i in range(n):
                if dp[mask][i] < 0 or not (mask >> i) & 1:
                    continue
                for j in range(n):
                    if (mask >> j) & 1:
                        continue
                    nxt_mask = mask | (1 << j)
                    cand = dp[mask][i] + overlap[i][j]
                    if cand > dp[nxt_mask][j]:
                        dp[nxt_mask][j] = cand
                        parent[nxt_mask][j] = i

        full = (1 << n) - 1
        last = max(range(n), key=lambda i: dp[full][i])

        # walk the parent pointers backwards to recover the word order
        order = []
        mask, cur = full, last
        while cur != -1:
            order.append(cur)
            prev = parent[mask][cur]
            mask ^= (1 << cur)
            cur = prev
        order.reverse()

        res = [words[order[0]]]
        for a, b in zip(order, order[1:]):
            # drop the shared prefix of the next word
            res.append(words[b][overlap[a][b]:])
        return "".join(res)
