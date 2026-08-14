"""

2305. Fair Distribution of Cookies
Medium

You are given an integer array cookies, where cookies[i] denotes the number of cookies in the ith bag. You are also given an integer k that denotes the number of children to distribute all the bags of cookies to. All the cookies in the same bag must go to the same child and cannot be split up.

The unfairness of a distribution is defined as the maximum total cookies obtained by a single child in the distribution.

Return the minimum unfairness of all distributions.


Example 1:

Input: cookies = [8,15,10,20,8], k = 2
Output: 31
Explanation: One optimal distribution is [8,15,8] and [10,20]
- The 1st child receives [8,15,8] which has a total of 8 + 15 + 8 = 31 cookies.
- The 2nd child receives [10,20] which has a total of 10 + 20 = 30 cookies.
The unfairness of the distribution is max(31,30) = 31.
It can be shown that there is no distribution with an unfairness less than 31.

Example 2:

Input: cookies = [6,1,3,2,2,4,1,2], k = 3
Output: 7
Explanation: One optimal distribution is [6,1], [3,2,2], and [4,1,2]
- The 1st child receives [6,1] which has a total of 6 + 1 = 7 cookies.
- The 2nd child receives [3,2,2] which has a total of 3 + 2 + 2 = 7 cookies.
- The 3rd child receives [4,1,2] which has a total of 4 + 1 + 2 = 7 cookies.
The unfairness of the distribution is max(7,7,7) = 7.
It can be shown that there is no distribution with an unfairness less than 7.


Constraints:

2 <= cookies.length <= 8
1 <= cookies[i] <= 10^5
2 <= k <= cookies.length

"""

# V0
# IDEA : BITMASK DP OVER SUBSETS (n <= 8, so 2^n masks are cheap)
#
#   tot[mask] = total cookies of the bags in mask.
#   dp[mask]  = min unfairness when mask is split among the children
#               handed out so far.
#   start with dp = tot (one child takes the whole mask), then add one
#   child at a time:
#       nxt[mask] = min over submask sub of mask:
#                       max( dp[mask ^ sub], tot[sub] )
#   after k-1 rounds dp[full] is the answer.
#
#   NOTE : sub is allowed to be empty -> a child may receive no bag,
#          which the problem permits.
#
# time = O(k * 3^n), space = O(2^n)
class Solution(object):
    def distributeCookies(self, cookies, k):
        n = len(cookies)
        full = (1 << n) - 1

        tot = [0] * (1 << n)
        for mask in range(1, 1 << n):
            low = mask & (-mask)
            i = low.bit_length() - 1
            tot[mask] = tot[mask ^ low] + cookies[i]

        dp = list(tot)
        for _ in range(k - 1):
            nxt = [float('inf')] * (1 << n)
            for mask in range(1 << n):
                sub = mask
                while True:
                    cand = dp[mask ^ sub]
                    if tot[sub] > cand:
                        cand = tot[sub]
                    if cand < nxt[mask]:
                        nxt[mask] = cand
                    if sub == 0:
                        break
                    sub = (sub - 1) & mask
            dp = nxt

        return dp[full]
