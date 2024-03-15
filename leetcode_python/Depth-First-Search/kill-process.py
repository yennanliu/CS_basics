"""

582. Kill Process
Medium

You have n processes forming a rooted tree structure. You are given two integer arrays pid and ppid, where pid[i] is the ID of the ith process and ppid[i] is the ID of the ith process's parent process.

Each process has only one parent process but may have multiple children processes. Only one process has ppid[i] = 0, which means this process has no parent process (the root of the tree).

When a process is killed, all of its children processes will also be killed.

Given an integer kill representing the ID of a process you want to kill, return a list of the IDs of the processes that will be killed. You may return the answer in any order.

 

Example 1:


Input: pid = [1,3,10,5], ppid = [3,0,5,3], kill = 5
Output: [5,10]
Explanation: The processes colored in red are the processes that should be killed.
Example 2:

Input: pid = [1], ppid = [0], kill = 1
Output: [1]
 

Constraints:

n == pid.length
n == ppid.length
1 <= n <= 5 * 104
1 <= pid[i] <= 5 * 104
0 <= ppid[i] <= 5 * 104
Only one process has no parent.
All the values of pid are unique.
kill is guaranteed to be in pid.

"""

# V0
# IDEA : BFS + defaultdict
from collections import defaultdict
class Solution(object):
    def killProcess(self, pid, ppid, kill):
        d = defaultdict(set)
        for a, b in zip(ppid, pid):
            d[a].add(b)
        q = [kill]
        res = []
        while q:
            for i in range(len(q)):
                tmp = q.pop(-1) # TODO check : should be pop(0) ??? 
                res.append(tmp)
                for _ in d[tmp]:
                    q.append(_)
        return res

# V0'
# IDEA : DFS
class Solution:
    def killProcess(self, pid, ppid, kill):
        n = len(pid)
        child = {}
        for i in range(n):
            if ppid[i] not in child:
                child[ppid[i]] = []
            child[ppid[i]].append(pid[i])

        ans = []
        self.dfs(kill, ans, child)
        return ans

    def dfs(self, now, ans, child):
        ans.append(now)
        if now in child:
            for nxt in child[now]:
                self.dfs(nxt, ans, child)

# V1
# IDEA : BFS
# http://bookshadow.com/weblog/2017/05/15/leetcode-kill-process/
class Solution(object):
    def killProcess(self, pid, ppid, kill):
        dic = collections.defaultdict(set)
        for child, parent in zip(pid, ppid):
            dic[parent].add(child)
        queue = [kill]
        victims = []
        while queue:
            first = queue.pop(0)
            victims.append(first)
            for child in dic[first]:
                queue.append(child)
        return victims

# V1'
# https://www.jiuzhang.com/solution/582-kill-process/#tag-highlight-lang-python
# IDEA : DFS 
class Solution:
    """
    @param pid: the process id
    @param ppid: the parent process id
    @param kill: a PID you want to kill
    @return: a list of PIDs of processes that will be killed in the end
    """

    def killProcess(self, pid, ppid, kill):
        n = len(pid)
        child = {}
        for i in range(n):
            if ppid[i] not in child:
                child[ppid[i]] = []
            child[ppid[i]].append(pid[i])

        ans = []
        self.dfs(kill, ans, child)
        return ans

    def dfs(self, now, ans, child):
        ans.append(now)
        if now in child:
            for nxt in child[now]:
                self.dfs(nxt, ans, child)

# V1''
# https://www.jiuzhang.com/solution/582-kill-process/#tag-highlight-lang-python
# IDEA : BFS 
class Solution:
    """
    @param pid: the process id
    @param ppid: the parent process id
    @param kill: a PID you want to kill
    @return: a list of PIDs of processes that will be killed in the end
    """

    def killProcess(self, pid, ppid, kill):
        m = {}
        for i, parent in enumerate(ppid):
            if parent not in m:
                m[parent] = []
            m[parent].append(pid[i])

        queue = [kill]
        answer = []
        while queue:
            curr = queue.pop(0)
            answer.append(curr)
            if curr in m:
                queue += m[curr]
        return answer

# V1
# IDEA : DFS (TLE)
# https://leetcode.com/problems/kill-process/solution/
# JAVA
# public class Solution {
#
#     public List < Integer > killProcess(List < Integer > pid, List < Integer > ppid, int kill) {
#         List < Integer > l = new ArrayList < > ();
#         if (kill == 0)
#             return l;
#         l.add(kill);
#         for (int i = 0; i < ppid.size(); i++)
#             if (ppid.get(i) == kill)
#                 l.addAll(killProcess(pid, ppid, pid.get(i)));
#         return l;
#     }
# }

# V1
# IDEA : Tree Simulation
# https://leetcode.com/problems/kill-process/solution/
# public class Solution {
#     class Node {
#         int val;
#         List < Node > children = new ArrayList < > ();
#     }
#     public List < Integer > killProcess(List < Integer > pid, List < Integer > ppid, int kill) {
#         HashMap < Integer, Node > map = new HashMap < > ();
#         for (int id: pid) {
#             Node node = new Node();
#             node.val = id;
#             map.put(id, node);
#         }
#         for (int i = 0; i < ppid.size(); i++) {
#             if (ppid.get(i) > 0) {
#                 Node par = map.get(ppid.get(i));
#                 par.children.add(map.get(pid.get(i)));
#             }
#         }
#         List < Integer > l = new ArrayList < > ();
#         l.add(kill);
#         getAllChildren(map.get(kill), l);
#         return l;
#     }
#     public void getAllChildren(Node pn, List < Integer > l) {
#         for (Node n: pn.children) {
#             l.add(n.val);
#             getAllChildren(n, l);
#         }
#     }
# }


# V1
# IDEA : HashMap + Depth First Search 
# https://leetcode.com/problems/kill-process/solution/
# JAVA
# public class Solution {
#     public List < Integer > killProcess(List < Integer > pid, List < Integer > ppid, int kill) {
#         HashMap < Integer, List < Integer >> map = new HashMap < > ();
#         for (int i = 0; i < ppid.size(); i++) {
#             if (ppid.get(i) > 0) {
#                 List < Integer > l = map.getOrDefault(ppid.get(i), new ArrayList < Integer > ());
#                 l.add(pid.get(i));
#                 map.put(ppid.get(i), l);
#             }
#         }
#         List < Integer > l = new ArrayList < > ();
#         l.add(kill);
#         getAllChildren(map, l, kill);
#         return l;
#     }
#     public void getAllChildren(HashMap < Integer, List < Integer >> map, List < Integer > l, int kill) {
#         if (map.containsKey(kill))
#             for (int id: map.get(kill)) {
#                 l.add(id);
#                 getAllChildren(map, l, id);
#             }
#     }
# }

# V1
# IDEA : HashMap + Breadth First Search
# https://leetcode.com/problems/kill-process/solution/
# JAVA
# public class Solution {
#
#     public List < Integer > killProcess(List < Integer > pid, List < Integer > ppid, int kill) {
#         HashMap < Integer, List < Integer >> map = new HashMap < > ();
#         for (int i = 0; i < ppid.size(); i++) {
#             if (ppid.get(i) > 0) {
#                 List < Integer > l = map.getOrDefault(ppid.get(i), new ArrayList < Integer > ());
#                 l.add(pid.get(i));
#                 map.put(ppid.get(i), l);
#             }
#         }
#         Queue < Integer > queue = new LinkedList < > ();
#         List < Integer > l = new ArrayList < > ();
#         queue.add(kill);
#         while (!queue.isEmpty()) {
#             int r = queue.remove();
#             l.add(r);
#             if (map.containsKey(r))
#                 for (int id: map.get(r))
#                     queue.add(id);
#         }
#         return l;
#     }
# }

# V2 
# Time:  O(n)
# Space: O(n)
import collections
# DFS solution.
class Solution(object):
    def killProcess(self, pid, ppid, kill):
        """
        :type pid: List[int]
        :type ppid: List[int]
        :type kill: int
        :rtype: List[int]
        """
        def killAll(pid, children, killed):
            killed.append(pid)
            for child in children[pid]:
                killAll(child, children, killed)

        result = []
        children = collections.defaultdict(set)
        for i in range(len(pid)):
            children[ppid[i]].add(pid[i])
        killAll(kill, children, result)
        return result


# Time:  O(n)
# Space: O(n)
# BFS solution.
class Solution2(object):
    def killProcess(self, pid, ppid, kill):
        """
        :type pid: List[int]
        :type ppid: List[int]
        :type kill: int
        :rtype: List[int]
        """
        def killAll(pid, children, killed):
            killed.append(pid)
            for child in children[pid]:
                killAll(child, children, killed)

        result = []
        children = collections.defaultdict(set)
        for i in range(len(pid)):
            children[ppid[i]].add(pid[i])
        q = collections.deque()
        q.append(kill)
        while q:
            p = q.popleft()
            result.append(p)
            for child in children[p]:
                q.append(child)
        return result