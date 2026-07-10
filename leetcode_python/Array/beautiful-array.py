"""

932. Beautiful Array
Solved
Medium
Topics
premium lock icon
Companies
An array nums of length n is beautiful if:

nums is a permutation of the integers in the range [1, n].
For every 0 <= i < j < n, there is no index k with i < k < j where 2 * nums[k] == nums[i] + nums[j].
Given the integer n, return any beautiful array nums of length n. There will be at least one valid answer for the given n.

 

Example 1:

Input: n = 4
Output: [2,1,4,3]
Example 2:

Input: n = 5
Output: [3,1,2,5,4]
 

Constraints:

1 <= n <= 1000


"""



# V0-1
# IDEA: Divide-and-Conquer (gpt)
# time = O(n)
# space = O(n)
class Solution(object):
    def beautifulArray(self, n):
        # Start with the smallest beautiful array.
        # A single element is always beautiful.
        res = [1]

        # Keep expanding the beautiful array until it contains n numbers.
        while len(res) < n:

            # Store the next generation of the beautiful array.
            tmp = []

            # -----------------------------
            # Generate all odd numbers first.
            # -----------------------------
            #
            # If x is in a beautiful array,
            # then (2*x - 1) is also beautiful.
            #
            # We only keep values that are <= n.
            #
            # Example:
            # res = [1, 2]
            #
            # x = 1 -> 1
            # x = 2 -> 3
            #
            # Result:
            # [1, 3]
            for x in res:
                if 2 * x - 1 <= n:
                    tmp.append(2 * x - 1)

            # ------------------------------
            # Generate all even numbers next.
            # ------------------------------
            #
            # Likewise,
            # if x is beautiful,
            # then (2*x) is also beautiful.
            #
            # Example:
            #
            # res = [1, 2]
            #
            # x = 1 -> 2
            # x = 2 -> 4
            #
            # Result:
            # [2, 4]
            for x in res:
                if 2 * x <= n:
                    tmp.append(2 * x)

            # The newly generated array becomes the current beautiful array.
            res = tmp

        # Return the final beautiful array.
        return res



# V0-2
# IDEA: Divide-and-Conquer (GEMINI)
# time = O(n log n)
# space = O(n)
class Solution(object):
    def __init__(self):
        # Cache to store previously computed beautiful arrays
        self.memo = {1: [1]}

    def beautifulArray(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        # If we've already solved for this size, return it from cache
        if n in self.memo:
            return self.memo[n]
            
        # 1. Map left side to the odd numbers needed for size n
        # (An odd sequence mapped from a smaller beautiful array remains beautiful)
        odds = self.beautifulArray((n + 1) // 2)
        left = [2 * x - 1 for x in odds]
        
        # 2. Map right side to the even numbers needed for size n
        evens = self.beautifulArray(n // 2)
        right = [2 * x for x in evens]
        
        # 3. Combine them: Odds + Evens
        self.memo[n] = left + right
        return self.memo[n]


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83539773
# time = O(n)
# space = O(n)
class Solution(object):
    def beautifulArray(self, N):
        """
        :type N: int
        :rtype: List[int]
        """
        res = [1]
        while len(res) < N:
            res = [2 * i - 1 for i in res] + [2 * i  for i in res]
        return [i for i in res if i <= N]
        
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83539773
# IDEA : ITERATION
# time = O(n log n)
# space = O(n)
class Solution(object):
    def beautifulArray(self, N):
        """
        :type N: int
        :rtype: List[int]
        """
        if N == 1: return [1]
        odd = [i * 2 - 1 for i in self.beautifulArray(N / 2 + N % 2)]
        even = [i * 2 for i in self.beautifulArray(N / 2)]
        return odd + even

# V2 
# time = O(n)
# space = O(n)
class Solution(object):
    def beautifulArray(self, N):
        """
        :type N: int
        :rtype: List[int]
        """
        result = [1]
        while len(result) < N:
            result = [i*2 - 1 for i in result] + [i*2 for i in result]
        return [i for i in result if i <= N]
