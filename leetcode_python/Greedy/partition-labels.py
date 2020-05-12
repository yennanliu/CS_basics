# V0 
# IDEA : GREEDY
class Solution(object):
    def partitionLabels(self, S):
        lindex = {c: i for i, c in enumerate(S)}
        j = anchor = 0
        ans = []
        for i, c in enumerate(S):
            j = max(j, lindex[c])
            if i == j:
                ans.append(j - anchor + 1)
                anchor = j + 1
        return ans

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79265829
# https://leetcode.com/problems/partition-labels/discuss/298474/Python-two-pointer-solution-with-explanation
# IDEA : GREEDY
class Solution(object):
    def partitionLabels(self, S):
        # NOTICE HERE 
        # lindex WILL COLLECT "THE MAX IDX" FOR EACH ELEMENTS IN S (consider each element may exists multi times)
        lindex = {c: i for i, c in enumerate(S)}
        j = anchor = 0
        ans = []
        for i, c in enumerate(S):
            j = max(j, lindex[c])
            if i == j:
                ans.append(j - anchor + 1)
                anchor = j + 1
        return ans

### Test case
s=Solution()
assert s.partitionLabels("ababcbacadefegdehijhklij") == [9,7,8]
assert s.partitionLabels("") == []
assert s.partitionLabels("aaa") == [3]
assert s.partitionLabels("a") == [1]
assert s.partitionLabels("aaannnnwefwrwnodiclwefoiw") == [3,22]
assert s.partitionLabels("aaabbbccc") == [3,3,3]
assert s.partitionLabels("aaabbabccac") == [11]
assert s.partitionLabels("wfrw34nekmvlsoixhaWDOIQFIOQASCADQ") ==  [4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 14]

# V1'
# https://leetcode.com/problems/partition-labels/solution/
# IDEA : GREEDY 
class Solution(object):
    def partitionLabels(self, S):
        last = {c: i for i, c in enumerate(S)}
        j = anchor = 0
        ans = []
        for i, c in enumerate(S):
            j = max(j, last[c])
            if i == j:
                ans.append(i - anchor + 1)
                anchor = i + 1           
        return ans

# V1''
# https://leetcode.com/problems/partition-labels/discuss/193371/Python-solution-28ms
class Solution(object):
    def partitionLabels(self, S):
        """
        :type S: str
        :rtype: List[int]
        """
        dic = {}
        for i, c in enumerate(S):
            dic[c] = i
        
        cur_max = 0
        res = []
        count = 0
        
        for i, c in enumerate(S):
            count += 1
            cur_max = max(cur_max, dic[c])
            
            if cur_max == i:
                res.append(count)
                count = 0
        return res

# V1'''
# https://leetcode.com/problems/partition-labels/discuss/390697/Python-easy-two-pass-with-explanation.-O(N)-time-O(1)-space
class Solution(object):
    def partitionLabels(self, S):
        result, last_seen, max_last_seen, count = [], {}, 0, 0
        for i, char in enumerate(S):
            last_seen[char] = i
        for i, char in enumerate(S):
            max_last_seen = max(max_last_seen, last_seen[char])
            count += 1
            if i == max_last_seen:
                result.append(count)
                count = 0
        return result

# V1''''
# https://leetcode.com/problems/partition-labels/discuss/113258/Short-easy-Python
class Solution(object):
    def partitionLabels(self, S):
        sizes = []
        while S:
            i = 1
            while set(S[:i]) & set(S[i:]):
                i += 1
            sizes.append(i)
            S = S[i:]
        return sizes

# V1''''' 
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
