# V0 

# V1 
# https://blog.csdn.net/qq508618087/article/details/51743408

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def depthSumInverse(self, nestedList):
        """
        :type nestedList: List[NestedInteger]
        :rtype: int
        """
        def depthSumInverseHelper(list, depth, result):
            if len(result) < depth + 1:
                result.append(0)
            if list.isInteger():
                result[depth] += list.getInteger()
            else:
                for l in list.getList():
                    depthSumInverseHelper(l, depth + 1, result)

        result = []
        for list in nestedList:
            depthSumInverseHelper(list, 0, result)

        sum = 0
        for i in reversed(range(len(result))):
            sum += result[i] * (len(result) - i)
        return sum