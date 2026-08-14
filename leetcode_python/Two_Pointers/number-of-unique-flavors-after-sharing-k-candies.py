"""

2107. Number of Unique Flavors After Sharing K Candies
Medium
(premium / locked problem)

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

Example 3:

Input: candies = [2,4,5], k = 0
Output: 3
Explanation:
You do not have to give any candies.
You can eat the candies with flavors [2,4,5].
There are 3 unique flavors, so return 3.


Constraints:

1 <= candies.length <= 10^5
1 <= candies[i] <= 10^5
0 <= k <= candies.length

"""

# V0
# IDEA : FIXED-SIZE SLIDING WINDOW OF THE *REMOVED* CANDIES
#
#   the sister takes a window of exactly k consecutive candies, so what you
#   keep is "everything outside that window". slide the window across the
#   array and count the distinct flavors OUTSIDE it.
#
#   keeping a Counter of the whole array and decrementing the window's
#   contents makes that count cheap : start from the full multiset, remove
#   the first window, then for each shift add back the flavor leaving the
#   window and remove the one entering it. the answer is the number of keys
#   whose count is still > 0 — tracked incrementally as `unique`.
#
# time = O(n), space = O(n)
from collections import Counter


class Solution(object):
    def shareCandies(self, candies, k):
        outside = Counter(candies)
        if k == 0:
            return len(outside)

        # remove the first window [0, k-1]
        for i in range(k):
            outside[candies[i]] -= 1
            if outside[candies[i]] == 0:
                del outside[candies[i]]

        res = len(outside)
        for i in range(k, len(candies)):
            # candies[i - k] leaves the window -> it is kept again
            outside[candies[i - k]] += 1
            # candies[i] enters the window -> it is given away
            outside[candies[i]] -= 1
            if outside[candies[i]] == 0:
                del outside[candies[i]]
            res = max(res, len(outside))
        return res
