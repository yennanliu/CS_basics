# V0 

# V1 
# https://blog.csdn.net/coder_orz/article/details/51317748
# IDEA : "unly number" :  a number is an ugly number 
# if all its prime factors are within [2, 3, 5].
# e.g. 6, 8 are ugly number ; while 14 is not 
# please note that 1 is ugly number as well 
# IDEA : ITERATION  
class Solution(object):
    def isUgly(self, num):
        """
        :type num: int
        :rtype: bool
        """
        if num <= 0:
            return False

        for i in [2, 3, 5]:
            while num%i == 0:
                num = num / i
        return True if num == 1 else False

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51317748
# IDEA : RECURSION 
class Solution(object):
    def isUgly(self, num):
        """
        :type num: int
        :rtype: bool
        """
        if num <= 0:
            return False
        if num == 1:
            return True

        if num % 2 == 0:
            return self.isUgly(num/2)
        elif num % 3 == 0:
            return self.isUgly(num/3)
        elif num % 5 == 0:
            return self.isUgly(num/5)
        else:
            return False

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51317748
class Solution(object):
    def isUgly(self, num):
        """
        :type num: int
        :rtype: bool
        """
        return num > 0 == 30**30 % num

# V2 
# Time:  O(n)
# Space: O(1)
import heapq
class Solution(object):
    # @param {integer} n
    # @return {integer}
    def nthUglyNumber(self, n):
        ugly_number = 0

        heap = []
        heapq.heappush(heap, 1)
        for _ in range(n):
            ugly_number = heapq.heappop(heap)
            if ugly_number % 2 == 0:
                heapq.heappush(heap, ugly_number * 2)
            elif ugly_number % 3 == 0:
                heapq.heappush(heap, ugly_number * 2)
                heapq.heappush(heap, ugly_number * 3)
            else:
                heapq.heappush(heap, ugly_number * 2)
                heapq.heappush(heap, ugly_number * 3)
                heapq.heappush(heap, ugly_number * 5)

        return ugly_number

    def nthUglyNumber2(self, n):
        ugly = [1]
        i2 = i3 = i5 = 0
        while len(ugly) < n:
            while ugly[i2] * 2 <= ugly[-1]: i2 += 1
            while ugly[i3] * 3 <= ugly[-1]: i3 += 1
            while ugly[i5] * 5 <= ugly[-1]: i5 += 1
            ugly.append(min(ugly[i2] * 2, ugly[i3] * 3, ugly[i5] * 5))
        return ugly[-1]

    def nthUglyNumber3(self, n):
        q2, q3, q5 = [2], [3], [5]
        ugly = 1
        for u in heapq.merge(q2, q3, q5):
            if n == 1:
                return ugly
            if u > ugly:
                ugly = u
                n -= 1
                q2 += 2 * u,
                q3 += 3 * u,
                q5 += 5 * u,


class Solution2(object):
    ugly = sorted(2**a * 3**b * 5**c
                  for a in range(32) for b in range(20) for c in range(14))

    def nthUglyNumber(self, n):
        return self.ugly[n-1]
