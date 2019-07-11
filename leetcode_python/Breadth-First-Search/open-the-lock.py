# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82703064
class Solution:
    def openLock(self, deadends, target):
        """
        :type deadends: List[str]
        :type target: str
        :rtype: int
        """
        deadset = set(deadends)
        if (target in deadset) or ("0000" in deadset): return -1
        que = collections.deque()
        que.append("0000")
        visited = set(["0000"])
        step = 0
        while que:
            step += 1
            size = len(que)
            for i in range(size):
                point = que.popleft()
                for j in range(4):
                    for k in range(-1, 2, 2):
                        newPoint = [i for i in point]
                        newPoint[j] = chr((ord(newPoint[j]) - ord('0') + k + 10) % 10 + ord('0'))
                        newPoint = "".join(newPoint)
                        if newPoint == target:
                            return step
                        if (newPoint in deadset) or (newPoint in visited):
                            continue
                        que.append(newPoint)
                        visited.add(newPoint)
        return -1

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82703064
class Solution(object):
    def openLock(self, deadends, target):
        """
        :type deadends: List[str]
        :type target: str
        :rtype: int
        """
        que = collections.deque()
        que.append("0000")
        visited = set(deadends)
        step = 0
        while que:
            size = len(que)
            for _ in range(size):
                node = que.popleft()
                if node in visited:
                    continue
                visited.add(node)
                if node == target:
                    return step
                nodelist = map(int, list(node))
                que.append("".join(map(str, [(nodelist[0] + 1 + 10) % 10, nodelist[1], nodelist[2], nodelist[3]])))
                que.append("".join(map(str, [(nodelist[0] - 1 + 10) % 10, nodelist[1], nodelist[2], nodelist[3]])))
                que.append("".join(map(str, [nodelist[0], (nodelist[1] + 1 + 10) % 10, nodelist[2], nodelist[3]])))
                que.append("".join(map(str, [nodelist[0], (nodelist[1] - 1 + 10) % 10, nodelist[2], nodelist[3]])))
                que.append("".join(map(str, [nodelist[0], nodelist[1], (nodelist[2] + 1 + 10) % 10, nodelist[3]])))
                que.append("".join(map(str, [nodelist[0], nodelist[1], (nodelist[2] - 1 + 10) % 10, nodelist[3]])))
                que.append("".join(map(str, [nodelist[0], nodelist[1], nodelist[2], (nodelist[3] + 1 + 10) % 10])))
                que.append("".join(map(str, [nodelist[0], nodelist[1], nodelist[2], (nodelist[3] - 1 + 10) % 10])))
            step += 1
        return -1
        
# V2 
# Time:  O(k * n^k + d), n is the number of alphabets,
#                        k is the length of target,
#                        d is the size of deadends
# Space: O(k * n^k + d)
class Solution(object):
    def openLock(self, deadends, target):
        """
        :type deadends: List[str]
        :type target: str
        :rtype: int
        """
        dead = set(deadends)
        q = ["0000"]
        lookup = {"0000"}
        depth = 0
        while q:
            next_q = []
            for node in q:
                if node == target: return depth
                if node in dead: continue
                for i in range(4):
                    n = int(node[i])
                    for d in (-1, 1):
                        nn = (n+d) % 10
                        neighbor = node[:i] + str(nn) + node[i+1:]
                        if neighbor not in lookup:
                            lookup.add(neighbor)
                            next_q.append(neighbor)
            q = next_q
            depth += 1
        return -1