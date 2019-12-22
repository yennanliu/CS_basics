# V0
# IDEA : BFS
class Solution(object):
    def depthSum(self, nestedList):
        """
        :type nestedList: List[NestedInteger]
        :rtype: int
        """
        depth, ans = 1, 0
        while nestedList:
            ans += depth*sum([i.getInteger() for i in nestedList if i.isInteger()])
            newList = []
            for i in nestedList:
                if not i.isInteger():
                    newList += i.getList()
            nestedList = newList            
            depth += 1
        return ans

# V0'
# IDEA : DFS
# class Solution(object):
#     def depthSum(self, nestedList):
#         d = 1 
#         r = 0 
#         for tmp in nestedList:
#             self.dfs(nestedList, d, r,)
#         return r 
#
#     def dfs(self, nestedList, d, r)
#         if len(nestedList) == 0:
#             return 0
#         if tmp is isInteger():
#             r += d*tmp
#         elif tmp == "[":
#             self.dfs(nestedList, d+1, r)
#         elif tmp == "]":
#             self.dfs(nestedList, d-1, r)

# V1
# https://blog.csdn.net/danspace1/article/details/87645153
# IDEA : BFS
# """
#class NestedInteger(object):
#    def __init__(self, value=None):
#        """
#        If value is not specified, initializes an empty list.
#        Otherwise initializes a single integer equal to value.
#        """
#
#    def isInteger(self):
#        """
#        @return True if this NestedInteger holds a single integer, rather than a nested list.
#        :rtype bool
#        """
#
#    def add(self, elem):
#        """
#        Set this NestedInteger to hold a nested list and adds a nested integer elem to it.
#        :rtype void
#        """
#
#    def setInteger(self, value):
#        """
#        Set this NestedInteger to hold a single integer equal to value.
#        :rtype void
#        """
#
#    def getInteger(self):
#        """
#        @return the single integer that this NestedInteger holds, if it holds a single integer
#        Return None if this NestedInteger holds a nested list
#        :rtype int
#        """
#
#    def getList(self):
#        """
#        @return the nested list that this NestedInteger holds, if it holds a nested list
#        Return None if this NestedInteger holds a single integer
#        :rtype List[NestedInteger]
#        """
class Solution(object):
    def depthSum(self, nestedList):
        """
        :type nestedList: List[NestedInteger]
        :rtype: int
        """
        depth, ans = 1, 0
        while nestedList:
            ans += depth*sum([i.getInteger() for i in nestedList if i.isInteger()])
            newList = []
            for i in nestedList:
                if not i.isInteger():
                    newList += i.getList()
            nestedList = newList            
            depth += 1
        return ans

# V1'
# https://www.jiuzhang.com/solution/nested-list-weight-sum/#tag-highlight-lang-python
class Solution(object):
    # @param {NestedInteger[]} nestedList a list of NestedInteger Object
    # @return {int} an integer
    def depthSum(self, nestedList):
        # Write your code here
        if len(nestedList) == 0:
            return 0
        stack = []
        sum = 0
        for n in nestedList:
            stack.append((n, 1))
        while stack:
            next, d = stack.pop(0)
            if next.isInteger():
               sum += d * next.getInteger()
            else:
                for i in next.getList():
                    stack.append((i, d+1))
        return sum

# V1''
# https://www.cnblogs.com/grandyang/p/5340305.html
# JAVA
# IDEA : DFS
# class Solution {
# public:
#     int depthSum(vector<NestedInteger>& nestedList) {
#         int res = 0;
#         for (auto a : nestedList) {
#             res += getSum(a, 1);
#         }
#         return res;
#     }
#     int getSum(NestedInteger ni, int level) {
#         int res = 0;
#         if (ni.isInteger()) return level * ni.getInteger();
#         for (auto a : ni.getList()) {
#             res += getSum(a, level + 1);
#         }
#         return res;
#     }
# };

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def depthSum(self, nestedList):
        """
        :type nestedList: List[NestedInteger]
        :rtype: int
        """
        def depthSumHelper(nestedList, depth):
            res = 0
            for l in nestedList:
                if l.isInteger():
                    res += l.getInteger() * depth
                else:
                    res += depthSumHelper(l.getList(), depth + 1)
            return res
        return depthSumHelper(nestedList, 1)