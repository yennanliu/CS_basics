# V0 

# V1 
# http://bookshadow.com/weblog/2018/01/14/leetcode-partition-labels/
class Solution(object):
    def partitionLabels(self, S):
        """
        :type S: str
        :rtype: List[int]
        """
        rangeDict = {}
        for i, c in enumerate(S):
            if c not in rangeDict: rangeDict[c] = [i, i]
            else: rangeDict[c][1] = i
        rangeList = sorted(rangeDict.values(), cmp = lambda x, y: x[0] - y[0] or y[1] - x[1])
        ans = []
        cmin = cmax = 0
        for start, end in rangeList:
            if start > cmax:
                ans.append(cmax - cmin + 1)
                cmin, cmax = start, end
            else: cmax = max(cmax, end)
        ans.append(cmax - cmin + 1)
        return ans

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79265829
class Solution(object):
    def partitionLabels(self, S):
        """
        :type S: str
        :rtype: List[int]
        """
        lindex = {c: i for i, c in enumerate(S)}
        j = anchor = 0
        ans = []
        for i, c in enumerate(S):
            j = max(j, lindex[c])
            if i == j:
                ans.append(j - anchor + 1)
                anchor = j + 1
        return ans
        
# V3 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def partitionLabels(self, S):
        """
        :type S: str
        :rtype: List[int]
        """
        lookup = {c: i for i, c in enumerate(S)}
        first, last = 0, 0
        result = []
        for i, c in enumerate(S):
            last = max(last, lookup[c])
            if i == last:
                result.append(i-first+1)
                first = i+1
        return result
