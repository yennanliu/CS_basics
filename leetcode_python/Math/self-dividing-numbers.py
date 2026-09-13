"""

728. Self Dividing Numbers
Easy

A self-dividing number is a number that is divisible by every digit it contains.

For example, 128 is a self-dividing number because 128 % 1 == 0, 128 % 2 == 0, and 128 % 8 == 0.

A self-dividing number is not allowed to contain the digit zero.

Given two integers left and right, return a list of all the self-dividing numbers in the range [left, right] (both inclusive).

Example 1:

Input: left = 1, right = 22
Output: [1,2,3,4,5,6,7,8,9,11,12,15,22]

Example 2:

Input: left = 47, right = 85
Output: [48,55,66,77]

Constraints:

1 <= left <= right <= 10^4

"""

# V0

# V1 : dev
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
# time = O(n * logr) = O(n)  # n = right - left + 1, r = right
# space = O(1)
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
# time = O(n * logr) = O(n)  # n = right - left + 1, r = right
# space = O(1)
class Solution(object):
    def selfDividingNumbers(self, left, right):
        is_self_dividing = lambda num: '0' not in str(num) and all([num % int(digit) == 0 for digit in str(num)])
        return list(filter(is_self_dividing, list(range(left, right + 1))))

# V3
# time = O(nlogr) = O(n)
# space = O(logr) = O(1)
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

