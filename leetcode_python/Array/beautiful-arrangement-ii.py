# V0 

# V1 
# http://bookshadow.com/weblog/2017/08/30/leetcode-beautiful-arrangement-ii/
class Solution(object):
    def constructArray(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[int]
        """
        ans = [1]
        for x in range(k):
            m = ans[-1] + [1, -1][x % 2] * (k - x)
            ans.append(m)
        return ans + range(k + 2, n + 1)

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def constructArray(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[int]
        """
        result = []
        left, right = 1, n
        while left <= right:
            if k % 2:
                result.append(left)
                left += 1
            else:
                result.append(right)
                right -= 1
            if k > 1:
                k -= 1
        return result

