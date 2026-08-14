"""

1051. Height Checker
Easy

A school is trying to take an annual photo of all the students. The students are
asked to stand in a single file line in non-decreasing order by height.
Let this ordering be represented by the integer array expected where expected[i]
is the expected height of the ith student in line.

You are given an integer array heights representing the current order that the
students are standing in. Each heights[i] is the height of the ith student in line
(0-indexed).

Return the number of indices where heights[i] != expected[i].


Example 1:

Input: heights = [1,1,4,2,1,3]
Output: 3
Explanation:
heights:  [1,1,4,2,1,3]
expected: [1,1,1,2,3,4]
Indices 2, 4, and 5 do not match.

Example 2:

Input: heights = [5,1,2,3,4]
Output: 5
Explanation:
heights:  [5,1,2,3,4]
expected: [1,2,3,4,5]
All indices do not match.

Example 3:

Input: heights = [1,2,3,4,5]
Output: 0
Explanation:
heights:  [1,2,3,4,5]
expected: [1,2,3,4,5]
All indices match.


Constraints:

1 <= heights.length <= 100
1 <= heights[i] <= 100

"""

# V0
# IDEA : SORT A COPY + COMPARE INDEX BY INDEX
# time = O(n log n)
# space = O(n)
class Solution(object):
    def heightChecker(self, heights):
        expected = sorted(heights)
        return sum(1 for a, b in zip(heights, expected) if a != b)


# V1
# IDEA : COUNTING SORT (heights are bounded by 100)
# time = O(n + k), k = 100
# space = O(k)
class Solution2(object):
    def heightChecker(self, heights):
        MAX_H = 100
        cnt = [0] * (MAX_H + 1)
        for h in heights:
            cnt[h] += 1

        res = 0
        h = 1
        for x in heights:
            # advance to the next height that still has students left
            while cnt[h] == 0:
                h += 1
            if h != x:
                res += 1
            cnt[h] -= 1
        return res
