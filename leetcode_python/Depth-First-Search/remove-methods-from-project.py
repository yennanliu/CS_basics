"""

3310. Remove Methods From Project
Medium

You are maintaining a project that has n methods numbered from 0 to n - 1.

You are given two integers n and k, and a 2D integer array invocations, where invocations[i] = [a_i, b_i] indicates that method a_i invokes method b_i.

There is a known bug in method k. Method k, along with any method invoked by it, either directly or indirectly, are considered suspicious and we aim to remove them.

A group of methods can only be removed if no method outside the group invokes any methods within it.

Return an array containing all the remaining methods after removing all the suspicious methods. You may return the answer in any order. If it is not possible to remove all the suspicious methods, none should be removed.


Example 1:

Input: n = 4, k = 1, invocations = [[1,2],[0,1],[3,2]]
Output: [0,1,2,3]
Explanation:
Method 2 and method 1 are suspicious, but they are directly invoked by methods 3 and 0, which are not suspicious. We return all elements without removing anything.

Example 2:

Input: n = 3, k = 2, invocations = [[1,2],[0,1],[2,0]]
Output: []
Explanation:
All methods are suspicious. We can remove them.

Example 3:

Input: n = 1, k = 0, invocations = []
Output: []
Explanation:
Method 0 is suspicious. We can remove it.


Constraints:

1 <= n <= 10^5
0 <= k <= n - 1
0 <= invocations.length <= 2 * 10^5
invocations[i] == [a_i, b_i]
0 <= a_i, b_i <= n - 1
a_i != b_i
invocations[i] != invocations[j]

"""

# V0
# IDEA : MARK THE REACHABLE SET FROM k, THEN CHECK FOR ANY EDGE INTO IT
#
#   "suspicious" is exactly the set of methods reachable from k along the
#   invocation edges — one DFS finds them.
#
#   the group may only be removed if nothing OUTSIDE it calls into it, so
#   scan the edges once : an edge whose caller is clean and whose callee is
#   suspicious blocks the removal, and then everything stays.
#
#   the traversal is iterative — 10^5 methods could form a chain.
#
# time = O(n + E), space = O(n + E)
class Solution(object):
    def remainingMethods(self, n, k, invocations):
        adj = [[] for _ in range(n)]
        for a, b in invocations:
            adj[a].append(b)

        suspicious = [False] * n
        suspicious[k] = True
        stack = [k]
        while stack:
            u = stack.pop()
            for v in adj[u]:
                if not suspicious[v]:
                    suspicious[v] = True
                    stack.append(v)

        for a, b in invocations:
            if not suspicious[a] and suspicious[b]:
                return list(range(n))          # cannot remove anything

        return [i for i in range(n) if not suspicious[i]]
