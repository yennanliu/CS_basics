"""

752. Open the Lock
Medium

You have a lock in front of you with 4 circular wheels. Each wheel has 10 slots: '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'. The wheels can rotate freely and wrap around: for example we can turn '9' to be '0', or '0' to be '9'. Each move consists of turning one wheel one slot.

The lock initially starts at '0000', a string representing the state of the 4 wheels.

You are given a list of deadends dead ends, meaning if the lock displays any of these codes, the wheels of the lock will stop turning and you will be unable to open it.

Given a target representing the value of the wheels that will unlock the lock, return the minimum total number of turns required to open the lock, or -1 if it is impossible.

Example 1:

Input: deadends = ["0201","0101","0102","1212","2002"], target = "0202"
Output: 6
Explanation:
A sequence of valid moves would be "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202".
Note that a sequence like "0000" -> "0001" -> "0002" -> "0102" -> "0202" would be invalid,
because the wheels of the lock become stuck after the display becomes the dead end "0102".

Example 2:

Input: deadends = ["8888"], target = "0009"
Output: 1
Explanation: We can turn the last wheel in reverse to move from "0000" -> "0009".

Example 3:

Input: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
Output: -1
Explanation: We cannot reach the target without getting stuck.

Constraints:

1 <= deadends.length <= 500
deadends[i].length == 4
target.length == 4
target will not be in the list deadends.
target and deadends[i] consist of digits only.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82703064
# time = O(10^4 + d)  # d = len(deadends); BFS over all lock states
# space = O(10^4 + d)
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
# time = O(10^4 + d)  # d = len(deadends); BFS over all lock states
# space = O(10^4 + d)
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
# time = O(k * n^k + d), n is the number of alphabets,
#                        k is the length of target,
#                        d is the size of deadends
# space = O(k * n^k + d)
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