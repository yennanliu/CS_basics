# V0 

# V1 
# http://bookshadow.com/weblog/2017/05/15/leetcode-kill-process/
# IDEA : BFS 
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