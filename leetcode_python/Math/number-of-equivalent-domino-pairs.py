"""

1128. Number of Equivalent Domino Pairs
Easy

Given a list of dominoes, dominoes[i] = [a, b] is equivalent to dominoes[j] = [c, d]
if and only if either (a == c and b == d), or (a == d and b == c) - that is,
one domino can be rotated to be equal to another domino.

Return the number of pairs (i, j) for which 0 <= i < j < dominoes.length,
and dominoes[i] is equivalent to dominoes[j].


Example 1:

Input: dominoes = [[1,2],[2,1],[3,4],[5,6]]
Output: 1

Example 2:

Input: dominoes = [[1,2],[1,2],[1,1],[1,2],[2,2]]
Output: 3


Constraints:

1 <= dominoes.length <= 4 * 10^4
dominoes[i].length == 2
1 <= dominoes[i][j] <= 9

"""

# V0
# IDEA : HASH TABLE (counting) + normalize each domino to a 2 digit key
#        [a,b] and [b,a] are the same domino, so sort the pair
#        before encoding it as `small * 10 + big`.
#        while scanning, `cnt[key]` is exactly the number of
#        already seen dominoes that pair with the current one.
# time = O(n)
# space = O(1)
from collections import defaultdict
class Solution(object):
    def numEquivDominoPairs(self, dominoes):
        cnt = defaultdict(int)
        res = 0
        for a, b in dominoes:
            key = a * 10 + b if a < b else b * 10 + a
            res += cnt[key]
            cnt[key] += 1
        return res

# V1
# IDEA : COUNT FIRST, THEN SUM C(k, 2)
#        group identical (normalized) dominoes, a group of size k
#        contributes k * (k - 1) / 2 pairs.
# time = O(n)
# space = O(1)
from collections import Counter
class Solution(object):
    def numEquivDominoPairs(self, dominoes):
        cnt = Counter(tuple(sorted(d)) for d in dominoes)
        return sum(k * (k - 1) // 2 for k in cnt.values())
