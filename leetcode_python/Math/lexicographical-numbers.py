# V1  : dev 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/81776094
# e.g. 
# n = 13   --> 1,10,11,12,13,2,3,4,5,6,7,8,9 
# n = 300  --> 1,10,100,101,102...198,199,2,20,200,201...299,3,30,300
class Solution:
    def lexicalOrder(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        cur = 1
        ans = []
        for i in range(n):
            ans.append(cur)
            if cur * 10 <= n:
                cur *= 10
            else:
                if cur >= n:
                    cur //= 10
                cur += 1
                while cur % 10 == 0:
                    cur //= 10
        return ans


# V3 
# Space: O(1)
class Solution(object):
    def lexicalOrder(self, n):
        result = []

        i = 1
        while len(result) < n:
            k = 0
            while i * 10**k <= n:
                result.append(i * 10**k)
                k += 1

            num = result[-1] + 1
            while num <= n and num % 10:
                result.append(num)
                num += 1

            if not num % 10:
                num -= 1
            else:
                num /= 10

            while num % 10 == 9:
                num /= 10

            i = num+1

        return result
