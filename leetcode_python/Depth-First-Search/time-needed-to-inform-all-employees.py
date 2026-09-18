"""

1376. Time Needed to Inform All Employees
Medium

A company has n employees with a unique ID for each employee from 0 to n - 1.
The head of the company is the one with headID.

Each employee has one direct manager given in the manager array where manager[i]
is the direct manager of the i-th employee, manager[headID] = -1. Also, it is
guaranteed that the subordination relationships have a tree structure.

The head of the company wants to inform all the company employees of an urgent
piece of news. He will inform his direct subordinates, and they will inform their
subordinates, and so on until all employees know about the urgent news.

The i-th employee needs informTime[i] minutes to inform all of his direct
subordinates (i.e., After informTime[i] minutes, all his direct subordinates can
start spreading the news).

Return the number of minutes needed to inform all the employees about the urgent news.


Example 1:

Input: n = 1, headID = 0, manager = [-1], informTime = [0]
Output: 0
Explanation: The head of the company is the only employee in the company.

Example 2:

Input: n = 6, headID = 2, manager = [2,2,-1,2,2,2], informTime = [0,0,1,0,0,0]
Output: 1
Explanation: The head of the company with id = 2 is the direct manager of all the
employees in the company and needs 1 minute to inform them all.


Constraints:

1 <= n <= 10^5
0 <= headID < n
manager.length == n
0 <= manager[i] < n
manager[headID] == -1
informTime.length == n
0 <= informTime[i] <= 1000
informTime[i] == 0 if employee i has no subordinates.

"""

# V0
# IDEA : DFS ON THE MANAGER TREE (answer = the SLOWEST root-to-leaf path)
#
#   a manager tells all of their reports at once, so sibling branches run in
#   parallel : the company is informed when the longest chain finishes, not the
#   sum of the chains.
#
#     total(i) = informTime[i] + max(total(child)) over children
#     answer   = total(headID)
#
#   which is the same thing as "the deepest node, weighted by informTime".
#
#   NOTE !!! `manager` points UP, so it has to be inverted into a children list
#            first -- walking it upward per employee is O(n) per node, O(n^2)
#            overall, and n is 10^5 here.
#   NOTE !!! iterative, not recursive : the tree can be a 10^5-long chain and
#            Python's default recursion limit is 1000.
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def numOfMinutes(self, n, headID, manager, informTime):
        """
        :type n: int
        :type headID: int
        :type manager: List[int]
        :type informTime: List[int]
        :rtype: int
        """
        # edge
        if n <= 1:
            return 0

        children = defaultdict(list)
        for i, m in enumerate(manager):
            if m != -1:
                children[m].append(i)

        res = 0
        # (employee, minutes elapsed before THIS employee hears the news)
        stack = [(headID, 0)]
        while stack:
            node, t = stack.pop()
            if t > res:
                res = t
            for c in children[node]:
                stack.append((c, t + informTime[node]))

        return res
