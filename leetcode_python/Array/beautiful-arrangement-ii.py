"""

667. Beautiful Arrangement II
Medium

Given two integers n and k, construct a list answer that contains n different positive integers ranging from 1 to n and obeys the following requirement:

Suppose this list is answer = [a_1, a_2, a_3, ... , a_n], then the list [|a_1 - a_2|, |a_2 - a_3|, |a_3 - a_4|, ... , |a_n-1 - a_n|] has exactly k distinct integers.

Return the list answer. If there multiple valid answers, return any of them.

Example 1:

Input: n = 3, k = 1
Output: [1,2,3]
Explanation: The [1,2,3] has three different positive integers ranging from 1 to 3, and the [1,1] has exactly 1 distinct integer: 1

Example 2:

Input: n = 3, k = 2
Output: [1,3,2]
Explanation: The [1,3,2] has three different positive integers ranging from 1 to 3, and the [2,1] has exactly 2 distinct integers: 1 and 2.

Constraints:

1 <= k < n <= 10^4

"""

# V0 

# V1
# http://bookshadow.com/weblog/2017/08/30/leetcode-beautiful-arrangement-ii/
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(1)
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

