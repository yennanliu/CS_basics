"""

517. Super Washing Machines
Hard

You have n super washing machines on a line. Initially, each washing machine has some dresses or is empty.

For each move, you could choose any m (1 <= m <= n) washing machines, and pass one dress of each washing machine to one of its adjacent washing machines at the same time.

Given an integer array machines representing the number of dresses in each washing machine from left to right on the line, return the minimum number of moves to make all the washing machines have the same number of dresses. If it is not possible to do it, return -1.

 

Example 1:

Input: machines = [1,0,5]
Output: 3
Explanation:
1st move:    1     0 <-- 5    =>    1     1     4
2nd move:    1 <-- 1 <-- 4    =>    2     1     3
3rd move:    2     1 <-- 3    =>    2     2     2
Example 2:

Input: machines = [0,3,0]
Output: 2
Explanation:
1st move:    0 <-- 3     0    =>    1     2     0
2nd move:    1     2 --> 0    =>    1     1     1
Example 3:

Input: machines = [0,2,0]
Output: -1
Explanation:
It's impossible to make all three washing machines have the same number of dresses.
 

Constraints:

n == machines.length
1 <= n <= 104
0 <= machines[i] <= 105

"""

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