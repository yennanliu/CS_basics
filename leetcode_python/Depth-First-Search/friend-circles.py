# V0 

# V1 
# https://blog.csdn.net/XX_123_1_RJ/article/details/82656277
# http://bookshadow.com/weblog/2017/04/03/leetcode-friend-circles/
# IDEA : disjoint- set 
class Solution(object):
    def findCircleNum(self, M):
        """
        :type M: List[List[int]]
        :rtype: int
        """
        N = len(M)
        f = range(N)

        def find(x):
            while f[x] != x: x = f[x]
            return x

        for x in range(N):
            for y in range(x + 1, N):
                if M[x][y]: f[find(x)] = find(y)
        return sum(f[x] == x for x in range(N))

# V1' 
# https://blog.csdn.net/XX_123_1_RJ/article/details/82656277
# http://bookshadow.com/weblog/2017/04/03/leetcode-friend-circles/
# IDEA : DFS  (stack)
class Solution(object):
    def findCircleNum(self, M):
        """
        :type M: List[List[int]]
        :rtype: int
        """
        cnt, N = 0, len(M)
        vset = set()
        def dfs(n):
            for x in range(N):
                if M[n][x] and x not in vset:
                    vset.add(x)
                    dfs(x)
        for x in range(N):
            if x not in vset:
                cnt += 1
                dfs(x)
        return cnt

# V1'' 
# https://blog.csdn.net/XX_123_1_RJ/article/details/82656277
# http://bookshadow.com/weblog/2017/04/03/leetcode-friend-circles/
# IDEA : BFS (queue)
lass Solution(object):
    def findCircleNum(self, M):
        """
        :type M: List[List[int]]
        :rtype: int
        """
        cnt, N = 0, len(M)
        vset = set()
        def bfs(n):
            q = [n]
            while q:
                n = q.pop(0)
                for x in range(N):
                    if M[n][x] and x not in vset:
                        vset.add(x)
                        q.append(x)
        for x in range(N):
            if x not in vset:
                cnt += 1
                bfs(x)
        return cnt

# V1'''
# https://www.jiuzhang.com/solution/friend-circles/#tag-highlight-lang-python
# IDEA : UNION FIND 
class Solution:
    """
    @param M: a matrix
    @return: the total number of friend circles among all the students
    """
    def BFS(self, student, M):
        queue = []
        queue.append(student)
        while len(queue) :
            size = len(queue)
            for i in range(0, size) :    # MAKE SURE THE DISTANCE BETWEEN START, END POINT ARE THE SAME DURING EVERY SRARCH 
                j = queue[0]
                del queue[0]
                M[j][j] = 2
                for k in range(0, len(M[0])):   # GO THROUGH ALL FRIENDSHIPS 
                    if M[j][k] == 1 and M[k][k] == 1:   # IF M[k][k]==1 -> k is not searched yet, KEEP SEARCHING  
                        queue.append(k)
    def findCircleNum(self, M):
        # Write your code here
        count = 0
        for i in range(0, len(M)):
            if M[i][i] == 1 :   # IF CURRENT diagonal =1 -> THIS PERSON IS IN THE NEW CIRCLE 
                count += 1  # COUNT + 1 
                self.BFS(i, M) # START SEARCH 
        return count

# V2 
# Time:  O(n^2)
# Space: O(n)
class Solution(object):
    def findCircleNum(self, M):
        """
        :type M: List[List[int]]
        :rtype: int
        """

        class UnionFind(object):
            def __init__(self, n):
                self.set = range(n)
                self.count = n

            def find_set(self, x):
               if self.set[x] != x:
                   self.set[x] = self.find_set(self.set[x])  # path compression.
               return self.set[x]

            def union_set(self, x, y):
                x_root, y_root = map(self.find_set, (x, y))
                if x_root != y_root:
                    self.set[min(x_root, y_root)] = max(x_root, y_root)
                    self.count -= 1

        circles = UnionFind(len(M))
        for i in range(len(M)):
            for j in range(len(M)):
                if M[i][j] and i != j:
                    circles.union_set(i, j)
        return circles.count