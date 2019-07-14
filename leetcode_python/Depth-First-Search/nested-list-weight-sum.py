# V0 

# V1 
# https://www.cnblogs.com/grandyang/p/5340305.html

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