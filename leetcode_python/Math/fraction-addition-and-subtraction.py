
# V1  : dev 
# import re
# import operator
# class Solution(object):
# 	def fractionAddition(self, expression):
# 		# get multiplication product of list
# 		# https://stackoverflow.com/questions/595374/whats-the-function-like-sum-but-for-multiplication-product
# 		denominator_list =[]
# 		numerator_list = []
# 		[(denominator_list.append(int(i.split("/")[0])), numerator_list.append(int(expression.split("/")[1]) )) for i in  expression.split("+")  ]                      
# 		denominator_prod = reduce(operator.mul, denominator_list, 1)
# 		sum_numerator = 0 
# 		for i in numerator_list:
# 			sum_numerator = sum_numerator + denominator_prod/i 
# 		return "%d/%d".format(sum_numerator, denominator_prod/sum_numerator)



# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/80655459
class Solution(object):
    def fractionAddition(self, expression):
        """
        :type expression: str
        :rtype: str
        """
        if expression[0] == '-':
            expression = '0/1' + expression
        expression = expression.replace('-', '+-')
        son_moms = []
        for fractions in expression.split('+'):
            son_moms.append(list(map(int, fractions.split('/'))))
        ans = [0, 1]
        for son_mom in son_moms:
            ans[0] = ans[0] * son_mom[1] + son_mom[0] * ans[1]
            ans[1] = ans[1] * son_mom[1]
            for div in range(2, abs(min(ans))):
                while (ans[0] % div == 0) and (ans[1] % div == 0):
                    ans[0] /= div
                    ans[1] /= div
        if ans[0] == 0:
            return '0/1'
        return str(ans[0]) + "/" + str(ans[1])


# V3
# Time:  O(nlogx), x is the max denominator
# Space: O(n)
import re
class Solution(object):
    def fractionAddition(self, expression):
        """
        :type expression: str
        :rtype: str
        """
        def gcd(a, b):
            while b:
                a, b = b, a%b
            return a

        ints = list(map(int, re.findall('[+-]?\d+', expression)))
        A, B = 0, 1
        for i in range(0, len(ints), 2):
            a, b = ints[i], ints[i+1]
            A = A * b + a * B
            B *= b
            g = gcd(A, B)
            A //= g
            B //= g
        return '%d/%d' % (A, B)
