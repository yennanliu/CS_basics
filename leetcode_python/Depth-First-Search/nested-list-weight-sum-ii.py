# V0 

# V1 
# https://blog.csdn.net/qq508618087/article/details/51743408

# V1'
# https://www.jiuzhang.com/solution/nested-list-weight-sum-ii/#tag-highlight-lang-python
class Solution:
    """
    @param nestedList: a list of NestedInteger
    @return: the sum
    """
    def depthSumInverse(self, nestedList):
        # Write your code here.
        listq = nestedList
        qintq = [] # queue of integer queue
        
        while listq:
            intq = [] # this level integer queue
            newlistq = [] # next level list queue
            
            while listq:
                ni = listq.pop()
                if ni.isInteger():
                    intq.append(ni.getInteger())
                else:
                    newlistq.extend(ni.getList())
            
            qintq.append(intq)
            listq = newlistq
        
        wsum = 0 # weight sum
        w = 0 # weight
        while qintq:
            w += 1
            intq = qintq.pop()
            while intq:
                wsum += w * intq.pop()
        
        return wsum
        
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