# V0 

# V1
# http://bookshadow.com/weblog/2017/02/19/leetcode-super-washing-machines/
class Solution(object):
    def findMinMoves(self, machines):
        """
        :type machines: List[int]
        :rtype: int
        """
        if sum(machines) % len(machines):
            return -1
        avg = sum(machines) / len(machines)
        ans = total = 0
        for m in machines:
            total += m - avg
            ans = max(ans, abs(total), m - avg)
        return ans

### Test case : dev 

# V1'
# http://bookshadow.com/weblog/2017/02/19/leetcode-super-washing-machines/
class Solution(object):
    def findMinMoves(self, machines):
        """
        :type machines: List[int]
        :rtype: int
        """
        total = sum(machines)
        size = len(machines)
        if total % size:
            return -1
        avg = total / size
        sums = [0] * size
        last = 0
        for i, m in enumerate(machines):
            last += m
            sums[i] = last
        ans = 0
        for i, m in enumerate(sums):
            left = i * avg - m + machines[i]
            right = (size - i - 1) * avg - sums[size - 1] + m
            if left > 0 and right > 0:
                ans = max(ans, left + right)
            else:
                ans = max(ans, abs(left), abs(right))
        return ans

# V1''
# https://leetcode.com/problems/super-washing-machines/discuss/99187/JavaC%2B%2BPython-O(1)-Space
class Solution:
    def findMinMoves(self, machines):
        total, n = sum(machines), len(machines)
        if total % n: return -1
        target, res, toRight = total / n, 0, 0
        for m in machines:
            toRight = m + toRight - target
            res = max(res, abs(toRight), m - target)
        return int(res)
        
# V2