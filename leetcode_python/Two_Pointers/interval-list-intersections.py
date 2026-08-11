# https://leetcode.com/problems/interval-list-intersections/description/

"""

986. Interval List Intersections
Solved
Medium
Topics
premium lock icon
Companies
You are given two lists of closed intervals, firstList and secondList, where firstList[i] = [starti, endi] and secondList[j] = [startj, endj]. Each list of intervals is pairwise disjoint and in sorted order.

Return the intersection of these two interval lists.

A closed interval [a, b] (with a <= b) denotes the set of real numbers x with a <= x <= b.

The intersection of two closed intervals is a set of real numbers that are either empty or represented as a closed interval. For example, the intersection of [1, 3] and [2, 4] is [2, 3].

 

Example 1:


Input: firstList = [[0,2],[5,10],[13,23],[24,25]], secondList = [[1,5],[8,12],[15,24],[25,26]]
Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
Example 2:

Input: firstList = [[1,3],[5,9]], secondList = []
Output: []
 

Constraints:

0 <= firstList.length, secondList.length <= 1000
firstList.length + secondList.length >= 1
0 <= starti < endi <= 109
endi < starti+1
0 <= startj < endj <= 109 
endj < startj+1


"""


# V0
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        """
        :type firstList: List[List[int]]
        :type secondList: List[List[int]]
        :rtype: List[List[int]]
        """
        pass


# V1-1
# IDEA: interval + 2 pointers (gemini)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        """
        :type firstList: List[List[int]]
        :type secondList: List[List[int]]
        :rtype: List[List[int]]
        """
        # 1. FIX: Intersection with an empty list is always empty []
        if not firstList or not secondList:
            return []

        ans = []
        i, j = 0, 0
        len_f, len_s = len(firstList), len(secondList)

        while i < len_f and j < len_s:
            s1, e1 = firstList[i]
            s2, e2 = secondList[j]

            # 2. Check for overlap: max(start) <= min(end)
            start = max(s1, s2)
            end = min(e1, e2)

            if start <= end:
                ans.append([start, end])

            # 3. FIX: Move pointer of interval that ends first to prevent infinite loop
            if e1 < e2:
                i += 1
            else:
                j += 1

        return ans


# V1-2
# IDEA: interval + 2 pointers (GPT)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        """
        :type firstList: List[List[int]]
        :type secondList: List[List[int]]
        :rtype: List[List[int]]
        """

        # Edge case
        if not firstList or not secondList:
            return []

        ans = []

        i = 0
        j = 0

        len_f = len(firstList)
        len_s = len(secondList)

        while i < len_f and j < len_s:
            s1, e1 = firstList[i]
            s2, e2 = secondList[j]

            # Check if intervals overlap
            if not (e1 < s2 or s1 > e2):
                ans.append([max(s1, s2), min(e1, e2)])

            # Move the interval that ends first
            if e1 < e2:
                i += 1
            else:
                j += 1

        return ans

# V2
