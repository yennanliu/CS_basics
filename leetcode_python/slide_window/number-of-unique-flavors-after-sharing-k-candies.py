"""

2107. Number of Unique Flavors After Sharing K Candies
Medium
🔒 (premium)

You are given a 0-indexed integer array candies, where candies[i] represents the flavor of the ith candy. Your mom wants you to share these candies with your little sister by giving her k consecutive candies, but you want to keep as many flavors of candies as possible.

Return the maximum number of unique flavors of candy you can keep after sharing with your sister.


Example 1:

Input: candies = [1,2,2,3,4,3], k = 3
Output: 3
Explanation:
Give the candies in the range [1, 3] (inclusive) with flavors [2,2,3].
You can eat candies with flavors [1,4,3].
There are 3 unique flavors, so return 3.

Example 2:

Input: candies = [2,2,2,2,3,3], k = 2
Output: 2
Explanation:
Give the candies in the range [3, 4] (inclusive) with flavors [2,3].
You can eat candies with flavors [2,2,2,3].
There are 2 unique flavors, so return 2.
Note that you can also share the candies with flavors [2,2] and eat the candies with flavors [2,2,3,3], which also return 2.

Example 3:

Input: candies = [2,4,5], k = 0
Output: 3
Explanation:
You do not have to give any candies.
You can eat the candies with flavors [2,4,5], and there are 3 unique flavors.


Constraints:

1 <= candies.length <= 10^5
1 <= candies[i] <= 10^5
0 <= k <= candies.length

"""

# V0
# IDEA : FIXED-SIZE WINDOW = THE GIVEN-AWAY CANDIES, COUNT WHAT IS LEFT
#
#   the sister's share is a window of exactly k consecutive candies, so
#   slide that window and ask how many distinct flavors survive OUTSIDE it.
#
#   keep a Counter of the whole array, then for the current window decrement
#   its flavors; a flavor disappears from the answer only when its count
#   drops to 0. the number of live keys in the Counter is the answer for
#   that window.
#
#   NOTE : k == 0 means giving nothing, so the answer is just the total
#          number of distinct flavors — the loop below handles it since the
#          window is empty.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def shareCandies(self, candies, k):
        cnt = Counter(candies)
        for x in candies[:k]:
            cnt[x] -= 1
            if cnt[x] == 0:
                del cnt[x]
        res = len(cnt)
        for i in range(k, len(candies)):
            cnt[candies[i]] -= 1                 # candy i joins the sister's share
            if cnt[candies[i]] == 0:
                del cnt[candies[i]]
            cnt[candies[i - k]] += 1             # candy i-k leaves it, back to us
            res = max(res, len(cnt))
        return res
