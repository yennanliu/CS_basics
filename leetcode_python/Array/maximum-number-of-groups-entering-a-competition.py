"""

2358. Maximum Number of Groups Entering a Competition
Medium

You are given a positive integer array grades which represents the grades of students in a university. You would like to enter all these students into a competition in ordered non-empty groups, such that the ordering meets the following conditions:

The sum of the grades of students in the ith group is less than the sum of the grades of students in the (i + 1)th group, for all groups (except the last).
The total number of students in the ith group is less than the total number of students in the (i + 1)th group, for all groups (except the last).

Return the maximum number of groups that can be formed.


Example 1:

Input: grades = [10,6,12,7,3,5]
Output: 3
Explanation: The following is a possible way to form 3 groups of students:
- 1st group has the students with grades = [12]. Sum of grades: 12. Student count: 1
- 2nd group has the students with grades = [6,7]. Sum of grades: 6 + 7 = 13. Student count: 2
- 3rd group has the students with grades = [10,3,5]. Sum of grades: 10 + 3 + 5 = 18. Student count: 3
It can be shown that it is not possible to form more than 3 groups.

Example 2:

Input: grades = [8,8]
Output: 1
Explanation: We can only form 1 group, since forming 2 groups would lead to an equal number of students in both groups.


Constraints:

1 <= grades.length <= 10^5
1 <= grades[i] <= 10^5

"""

# V0
# IDEA : MATH / GREEDY (only the group SIZES matter)
#
#   Sort grades ascending and hand out 1, 2, 3, ... k students in order.
#   Then group i has both fewer students AND a smaller sum than group i+1:
#     - sizes are 1 < 2 < ... < k by construction
#     - every grade in group i is <= every grade in group i+1, and group i+1
#       has one extra student, so its sum is strictly larger.
#   Leftover students are just dumped into the last group (still the largest).
#
#   So the answer is the largest k with 1 + 2 + ... + k = k(k+1)/2 <= n.
#
#   NOTE : we never actually need to sort -- only n matters.
#
# time = O(1), space = O(1)
class Solution(object):
    def maximumGroups(self, grades):
        n = len(grades)
        k = int(((8 * n + 1) ** 0.5 - 1) // 2)
        # guard against float rounding at the boundary
        while (k + 1) * (k + 2) // 2 <= n:
            k += 1
        while k * (k + 1) // 2 > n:
            k -= 1
        return k
