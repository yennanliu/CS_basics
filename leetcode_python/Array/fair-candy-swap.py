# V0 

# V1 
# https://www.jiuzhang.com/solution/fair-candy-swap/#tag-highlight-lang-python
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
# Time:  O(m + n)
# Space: O(m + n)
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