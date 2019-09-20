# V0 

# V1 
# http://bookshadow.com/weblog/2017/08/30/leetcode-path-sum-iv/
class Solution(object):
    def pathSum(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        dmap = {1 : 0}
        leaves = set([1])
        for num in nums:
            path, val = num / 10, num % 10
            lvl, seq = path / 10, path % 10
            parent = (lvl - 1) * 10 + (seq + 1) / 2
            dmap[path] = dmap[parent] + val
            leaves.add(path)
            if parent in leaves: leaves.remove(parent)
        return sum(dmap[v] for v in leaves)
        
# V1'
# https://www.jiuzhang.com/solution/path-sum-iv/#tag-highlight-lang-python
class Solution:
    """
    @param nums: the list
    @return: the sum of all paths from the root towards the leaves
    """
    def pathSumIV(self, nums):
        # Write your code here.
        if len(nums) == 1:    
            return nums[0] % 10;        
        a = [-1 for i in range(100)];
        for i in nums:
            a[i / 10] = i % 10;
        ret = 0;
        for i in range(2,5):
            for j in range(1,9):
                idx = i * 10 + j;
                pre = (i - 1) * 10 + (j + 1)/2;
                next1 = (i + 1) * 10 + 2 * j;
                next2 = (i + 1) * 10 + 2 * j - 1;
                if a[idx] != -1 and a[pre] != -1: 
                    a[idx] = a[idx] + a[pre];               
                if a[next1] == -1 and a[next2] == -1 and a[idx] != -1:
                    ret = ret + a[idx];         
        return ret;

# V2 
# Time:  O(n)
# Space: O(p), p is the number of paths
import collections
class Solution(object):
    def pathSum(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        class Node(object):
            def __init__(self, num):
                self.level = num/100 - 1
                self.i = (num%100)/10 - 1
                self.val = num%10
                self.leaf = True

            def isParent(self, other):
                return self.level == other.level-1 and \
                       self.i == other.i/2

        if not nums:
            return 0
        result = 0
        q = collections.deque()
        dummy = Node(10)
        parent = dummy
        for num in nums:
            child = Node(num)
            while not parent.isParent(child):
                result += parent.val if parent.leaf else 0
                parent = q.popleft()
            parent.leaf = False
            child.val += parent.val
            q.append(child)
        while q:
            result += q.pop().val
        return result