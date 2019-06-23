# V1  : dev 
# class Solution(object):
#     def selfDividingNumbers(self, left, right):
#         output = []
#         for i in range(left, right+1):
#             digit_str =  str(i)
#             if '0' in digit_str:
#                 break
#             print (digit_str)
#             for j in digit_str:
#                 print (j)
#                 if i % int(j) != 0:
#                     break 
#                 else:
#                     pass 
#                 output.append(i) 
#         return [ i for i in set(output)] 


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79053113
class Solution:
    def isDividingNumber(self, num):
        if '0' in str(num):
            return False
        return 0 == sum(num % int(i) for i in str(num))
    def selfDividingNumbers(self, left, right):
        """
        :type left: int
        :type right: int
        :rtype: List[int]
        """
        answer = []
        for num in range(left, right+1):
            print(num)
            if self.isDividingNumber(num):
                answer.append(num)
        return answer


# V2' 
class Solution(object):
    def selfDividingNumbers(self, left, right):
        is_self_dividing = lambda num: '0' not in str(num) and all([num % int(digit) == 0 for digit in str(num)])
        return list(filter(is_self_dividing, list(range(left, right + 1))))

# V3 
# Time:  O(nlogr) = O(n)
# Space: O(logr) = O(1)
class Solution(object):
    def selfDividingNumbers(self, left, right):
        """
        :type left: int
        :type right: int
        :rtype: List[int]
        """
        def isDividingNumber(num):
            n = num
            while n > 0:
                if (n%10) == 0 or (num%(n%10)) != 0:
                    return False
                n /= 10
            return True

        result = []
        for num in range(left, right+1):
            if isDividingNumber(num):
                result.append(num)
        return result

