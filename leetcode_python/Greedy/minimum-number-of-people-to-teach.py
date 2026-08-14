"""

1733. Minimum Number of People to Teach
Medium

On a social network consisting of m users and some friendships between users, two users can communicate with each other if they know a common language.

You are given an integer n, an array languages, and an array friendships where:

There are n languages numbered 1 through n,
languages[i] is the set of languages the ith user knows, and
friendships[i] = [ui, vi] denotes a friendship between the users ui and vi.

You can choose one language and teach it to some users so that all friends can communicate with each other. Return the minimum number of users you need to teach.

Note that friendships are not transitive, meaning if x is a friend of y and y is a friend of z, this doesn't guarantee that x is a friend of z.


Example 1:

Input: n = 2, languages = [[1],[2],[1,2]], friendships = [[1,2],[1,3],[2,3]]
Output: 1
Explanation: You can either teach user 1 the second language or user 2 the first language.

Example 2:

Input: n = 3, languages = [[2],[1,3],[1,2],[3]], friendships = [[1,4],[1,2],[3,4],[2,3]]
Output: 2
Explanation: Teach the third language to users 1 and 3, yielding two users to teach.


Constraints:

2 <= n <= 500
languages.length == m
1 <= m <= 500
1 <= languages[i].length <= n
1 <= languages[i][j] <= n
1 <= ui < vi <= languages.length
1 <= friendships.length <= 500
All tuples (ui, vi) are unique
languages[i] contains only unique values

"""

# V0
# IDEA : GREEDY / COUNTING (only BROKEN friendships matter, then pick the best language)
#
#   two observations collapse the problem:
#
#   1) a friendship whose two users ALREADY share a language is fine no
#      matter what we teach - teaching never removes a language. so only
#      users inside a broken friendship are candidates; collect them in S.
#
#   2) we may teach exactly ONE language L. then every user in S who does
#      not already know L must be taught -> cost = |S| - (#users in S who
#      know L). so try every L and keep the largest such count.
#
#   answer = |S| - max over L of cnt[L].
#
#   NOTE : teaching L to all of S fixes EVERY broken pair at once, because
#          both endpoints of a broken pair are in S and end up knowing L.
#   NOTE : if nothing is broken, S is empty and the answer is 0.
#
# time = O(k * L + |S| * L), k = len(friendships), space = O(m * L)
from collections import Counter
class Solution(object):
    def minimumTeachings(self, n, languages, friendships):
        known = [set(x) for x in languages]

        # users sitting on a friendship that cannot communicate
        bad = set()
        for u, v in friendships:
            if not (known[u - 1] & known[v - 1]):
                bad.add(u)
                bad.add(v)

        if not bad:
            return 0

        cnt = Counter()
        for u in bad:
            for lang in known[u - 1]:
                cnt[lang] += 1

        return len(bad) - max(cnt.values())
