"""

2813. Maximum Elegance of a K-Length Subsequence
Hard

You are given a 0-indexed 2D integer array items of length n and an integer k.

items[i] = [profit_i, category_i], where profit_i and category_i denote the profit and category of the ith item respectively.

Let's define the elegance of a subsequence of items as total_profit + distinct_categories^2, where total_profit is the sum of all profits in the subsequence, and distinct_categories is the number of distinct categories from all the categories in the selected subsequence.

Your task is to find the maximum elegance from all subsequences of size k in items.

Return an integer denoting the maximum elegance of a subsequence of items with size exactly k.

Note: A subsequence of an array is a new array generated from the original array by deleting some elements (possibly none) without changing the remaining elements' relative order.


Example 1:

Input: items = [[3,2],[5,1],[10,1]], k = 2
Output: 17
Explanation: In this example, we have to select a subsequence of size 2.
We can select items[0] = [3,2] and items[2] = [10,1].
The total profit in this subsequence is 3 + 10 = 13, and the subsequence contains 2 distinct categories [2,1].
Hence, the elegance is 13 + 2^2 = 17, and we can show that it is the maximum achievable elegance.

Example 2:

Input: items = [[3,1],[3,1],[2,2],[5,3]], k = 3
Output: 19
Explanation: In this example, we have to select a subsequence of size 3.
We can select items[0] = [3,1], items[2] = [2,2], and items[3] = [5,3].
The total profit in this subsequence is 3 + 2 + 5 = 10, and the subsequence contains 3 distinct categories [1,2,3].
Hence, the elegance is 10 + 3^2 = 19, and we can show that it is the maximum achievable elegance.

Example 3:

Input: items = [[1,1],[2,1],[3,1]], k = 3
Output: 7
Explanation: In this example, we have to select a subsequence of size 3.
We should select all the items.
The total profit will be 1 + 2 + 3 = 6, and the subsequence contains 1 distinct category [1].
Hence, the maximum elegance is 6 + 1^2 = 7.


Constraints:

1 <= items.length == n <= 10^5
items[i].length == 2
items[i][0] == profit_i
items[i][1] == category_i
1 <= profit_i <= 10^9
1 <= category_i <= n
1 <= k <= n

"""

# V0
# IDEA : GREEDY (SORT BY PROFIT DESC) + STACK OF "SACRIFICEABLE" DUPLICATES
#
#   Sort items by profit descending and take the top k as the starting pick:
#   that maximises total_profit, so it is optimal for any fixed number of
#   distinct categories we might end up with.
#
#   From there the only useful move is to trade profit for one extra distinct
#   category. Swapping in an item of a BRAND NEW category means kicking out one
#   of the currently selected items, and the only item we may safely drop is
#   one whose category is DUPLICATED inside the selection (otherwise we lose a
#   category while gaining one - a pure profit loss). Among the duplicates we
#   drop the cheapest one, i.e. the last duplicate seen while scanning the
#   sorted prefix -> a LIFO stack `dup` holds exactly those profits in
#   decreasing order, so `dup.pop()` is the cheapest droppable item.
#
#   Scanning the remaining items in profit-descending order, each swap raises
#   distinct_categories by 1 and lowers total_profit; we record the running
#   maximum of tot + len(vis)^2 after every swap.
#
#   NOTE : skip an item whose category is already selected (no gain), and stop
#          swapping when `dup` is empty (nothing left we are allowed to drop).
#   NOTE : the answer is monotone-checked at EVERY step, not only at the end -
#          the profit loss may outweigh the +(2c+1) category gain at some
#          point, so the best answer can occur mid-way.
#
# time = O(n * log n), space = O(n)
class Solution(object):
    def findMaximumElegance(self, items, k):
        items = sorted(items, key=lambda x: -x[0])

        tot = 0
        vis = set()
        dup = []
        for p, c in items[:k]:
            tot += p
            if c in vis:
                dup.append(p)
            else:
                vis.add(c)

        ans = tot + len(vis) ** 2
        for p, c in items[k:]:
            if c in vis or not dup:
                continue
            vis.add(c)
            tot += p - dup.pop()
            cur = tot + len(vis) ** 2
            if cur > ans:
                ans = cur
        return ans
