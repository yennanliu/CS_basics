# V0 

# V1 
# http://bookshadow.com/weblog/2017/05/15/leetcode-kill-process/
# IDEA : DFS (stack)
class Solution(object):
    def killProcess(self, pid, ppid, kill):
        """
        :type pid: List[int]
        :type ppid: List[int]
        :type kill: int
        :rtype: List[int]
        """
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