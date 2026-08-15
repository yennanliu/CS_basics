"""

2736. Maximum Sum Queries
Hard

You are given two 0-indexed integer arrays nums1 and nums2, each of length n, and a 1-indexed 2D array queries where queries[i] = [xi, yi].

For the ith query, find the maximum value of nums1[j] + nums2[j] among all indices j (0 <= j < n), where nums1[j] >= xi and nums2[j] >= yi, or -1 if there is no j satisfying the constraints.

Return an array answer where answer[i] is the answer to the ith query.


Example 1:

Input: nums1 = [4,3,1,2], nums2 = [2,4,9,5], queries = [[4,1],[1,3],[2,5]]
Output: [6,10,7]
Explanation:
For the 1st query xi = 4 and yi = 1, we can select index j = 0 since nums1[j] >= 4 and nums2[j] >= 1. The sum nums1[j] + nums2[j] is 6, and we can show that 6 is the maximum we can obtain.

For the 2nd query xi = 1 and yi = 3, we can select index j = 2 since nums1[j] >= 1 and nums2[j] >= 3. The sum nums1[j] + nums2[j] is 10, and we can show that 10 is the maximum we can obtain.

For the 3rd query xi = 2 and yi = 5, we can select index j = 3 since nums1[j] >= 2 and nums2[j] >= 5. The sum nums1[j] + nums2[j] is 7, and we can show that 7 is the maximum we can obtain.

Therefore, we return [6,10,7].

Example 2:

Input: nums1 = [3,2,5], nums2 = [2,3,4], queries = [[4,4],[3,2],[1,1]]
Output: [9,9,9]
Explanation: For this example, we can use index j = 2 for all the queries since it satisfies the constraints for each query.

Example 3:

Input: nums1 = [2,1], nums2 = [2,3], queries = [[3,3]]
Output: [-1]
Explanation: There is one query in this example with xi = 3 and yi = 3. For every index, j, either nums1[j] < xi or nums2[j] < yi. Hence, there is no solution.


Constraints:

nums1.length == nums2.length
n == nums1.length
1 <= n <= 10^5
1 <= nums1[i], nums2[i] <= 10^9
1 <= queries.length <= 10^5
queries[i].length == 2
xi == queries[i][1]
yi == queries[i][2]
1 <= xi, yi <= 10^9

"""

import bisect

# V0
# IDEA : OFFLINE QUERIES (SORT BY x) + BINARY INDEXED TREE (MAX) OVER nums2
#
#   This is a 2D dominance query: for each (x, y) we want
#       max{ nums1[j] + nums2[j] : nums1[j] >= x and nums2[j] >= y }.
#
#   Classic trick : peel off ONE dimension by sorting, handle the other with
#   a Fenwick / BIT.
#
#     1) sort the (nums1[j], nums2[j]) pairs by nums1 DESC
#     2) sort the queries by x DESC, remembering their original index
#     3) sweep the queries; before answering (x, y) push every pair whose
#        nums1 >= x into the BIT. From then on the "nums1 >= x" half of the
#        condition is automatic, and only "nums2 >= y" is left.
#
#   NOTE : a BIT natively answers PREFIX max, but we need a SUFFIX max over
#          nums2. So we index by the REVERSED rank
#               k(v) = n - bisect_left(sorted_nums2, v)
#                    = how many nums2 values are >= v
#          Bigger v  ->  smaller k, hence "nums2 >= y" becomes the prefix
#          1..k(y) and a plain prefix-max query works.
#
#   NOTE : k(v) >= 1 for any v that actually occurs in nums2 (so update never
#          loops forever on index 0), while a query y larger than every nums2
#          gives k = 0 and the `while k > 0` loop returns the -1 default.
#
#   NOTE : the answers must be written back at the ORIGINAL query index,
#          since we consumed the queries out of order.
#
# time = O((n + m) * log n + m * log m), space = O(n + m)
class Solution(object):
    def maximumSumQueries(self, nums1, nums2, queries):
        n = len(nums1)
        m = len(queries)

        # value list used for the (reversed) rank compression
        sorted_n2 = sorted(nums2)

        # pairs by nums1 descending
        pairs = sorted(zip(nums1, nums2), key=lambda p: -p[0])
        # query indices by x descending
        order = sorted(range(m), key=lambda i: -queries[i][0])

        tree = [-1] * (n + 1)   # BIT holding max(nums1 + nums2), -1 = empty
        res = [-1] * m
        j = 0

        for qi in order:
            x, y = queries[qi]

            # push every pair with nums1 >= x (pointer only moves forward)
            while j < n and pairs[j][0] >= x:
                a, b = pairs[j]
                k = n - bisect.bisect_left(sorted_n2, b)
                v = a + b
                while k <= n:
                    if tree[k] < v:
                        tree[k] = v
                    k += k & (-k)
                j += 1

            # prefix max over ranks 1..k  <=>  nums2 >= y
            k = n - bisect.bisect_left(sorted_n2, y)
            best = -1
            while k > 0:
                if tree[k] > best:
                    best = tree[k]
                k -= k & (-k)
            res[qi] = best

        return res
