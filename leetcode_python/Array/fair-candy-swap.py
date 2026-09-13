"""

888. Fair Candy Swap
Easy

Alice and Bob have a different total number of candies. You are given two integer arrays aliceSizes and bobSizes where aliceSizes[i] is the number of candies of the i^th box of candy that Alice has and bobSizes[j] is the number of candies of the j^th box of candy that Bob has.

Since they are friends, they would like to exchange one candy box each so that after the exchange, they both have the same total amount of candy. The total amount of candy a person has is the sum of the number of candies in each box they have.

Return an integer array answer where answer[0] is the number of candies in the box that Alice must exchange, and answer[1] is the number of candies in the box that Bob must exchange. If there are multiple answers, you may return any one of them. It is guaranteed that at least one answer exists.

Example 1:

Input: aliceSizes = [1,1], bobSizes = [2,2]
Output: [1,2]

Example 2:

Input: aliceSizes = [1,2], bobSizes = [2,3]
Output: [1,2]

Example 3:

Input: aliceSizes = [2], bobSizes = [1,3]
Output: [2,3]

Constraints:

1 <= aliceSizes.length, bobSizes.length <= 10^4
1 <= aliceSizes[i], bobSizes[j] <= 10^5
Alice and Bob have a different total number of candies.
There will be at least one valid answer for the given input.

"""

# V0 

# V1 
# https://www.jiuzhang.com/solution/fair-candy-swap/#tag-highlight-lang-python
# time = O(n log n)
# space = O(1)
class Solution:
    """
    @param A: an array
    @param B: an array
    @return: an integer array
    """
    def fairCandySwap(self, A, B):
        # Write your code here.
        ans = []
        sumA = sum(A)
        sumB = sum(B)
        A.sort()
        B.sort()
        tmp = sumA - (sumA + sumB) / 2
        i = 0
        j = 0
        while i < len(A) and j < len(B):
            if A[i] - B[j] == tmp:
                ans.append(A[i])
                ans.append(B[j])
                break
            elif A[i] - B[j] > tmp:
                j += 1
            elif A[i] - B[j] < tmp:
                i += 1
        return ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82013077
# time = O(m + n)
# space = O(n)
class Solution(object):
    def fairCandySwap(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        sum_A, sum_B, set_B = sum(A), sum(B), set(B)
        target = (sum_A + sum_B) / 2
        for a in A:
            b = target - (sum_A - a)
            if b >= 1 and b <= 100000 and b in set_B:
                return [a, b]

# V2
# time = O(m + n)
# space = O(m + n)
class Solution(object):
    def fairCandySwap(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        diff = (sum(A)-sum(B))//2
        setA = set(A)
        for b in set(B):
            if diff+b in setA:
                return [diff+b, b]
        return []