"""

640. Solve the Equation
Medium

Solve a given equation and return the value of 'x' in the form of a string "x=#value". The equation contains only '+', '-' operation, the variable 'x' and its coefficient. You should return "No solution" if there is no solution for the equation, or "Infinite solutions" if there are infinite solutions for the equation.

If there is exactly one solution for the equation, we ensure that the value of 'x' is an integer.

 

Example 1:

Input: equation = "x+5-3+x=6+x-2"
Output: "x=2"
Example 2:

Input: equation = "x=x"
Output: "Infinite solutions"
Example 3:

Input: equation = "2x=x"
Output: "x=0"
Example 4:

Input: equation = "2x+3x-6x=x+2"
Output: "x=-1"
Example 5:

Input: equation = "x=x+2"
Output: "No solution"
 

Constraints:

3 <= equation.length <= 1000
equation has exactly one '='.
equation consists of integers with an absolute value in the range [0, 100] without any leading zeros, and the variable 'x'.

"""

# V0
# IDEA : replace + eval + math
# https://leetcode.com/problems/solve-the-equation/discuss/105362/Simple-2-liner-(and-more)
# eval : The eval() method parses the expression passed to this method and runs python expression (code) within the program.
# -> https://www.runoob.com/python/python-func-eval.html
class Solution(object):
    def solveEquation(self, equation):
        tmp = equation.replace('x', 'j').replace('=', '-(')
        z = eval(tmp + ")" , {'j':1j})
        # print ("equation = " + str(equation))
        # print ("tmp = " + str(tmp))
        # print ("z = " + str(z))
        a, x = z.real, -z.imag
        return 'x=%d' % (a / x) if x else 'No solution' if a else 'Infinite solutions'

# V0'
# IDEA : REGULAR EXPRESSION
import re
class Solution(object):
    def solveEquation(self, equation):
        a, b, side = 0, 0, 1
        for eq, sign, num, isx in re.findall('(=)|([-+]?)(\d*)(x?)', equation):
            #print ("eq : {}, sign : {}, num : {}, isx : {}".format(eq, sign, num, isx))
            
            # case : "="
            if eq:
                side = -1
            # case : "x"
            elif isx:
                a += side * int(sign + '1') * int(num or 1)
            # case : number
            elif num:
                b -= side * int(sign + num)
        return 'x=%d' % (b / a) if a else 'No solution' if b else 'Infinite solutions'

# V0'
class Solution(object):
    def solveEquation(self, equation):
        z = eval(equation.replace('x', 'j').replace('=', '-(') + ')', {'j': 1j})
        a, x = z.real, -z.imag
        return 'x=%d' % (a / x) if x else 'No solution' if a else 'Infinite solutions'

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/80656224
class Solution(object):
    def solveEquation(self, equation):
        """
        :type equation: str
        :rtype: str
        """
        left, right = equation.split('=')
        if left[0] == '-':
            left = '0' + left
        if right[0] == '-':
            right = '0' + right
        left = left.replace('-', '+-')
        right = right.replace('-', '+-')
        left_x, left_val, right_x, right_val = 0, 0, 0, 0
        for num in left.split('+'):
            if 'x' in num:
                if num == 'x':
                    left_x += 1
                elif num == '-x':
                    left_x -= 1
                else:
                    left_x += int(num[:-1])
            else:
                left_val += int(num)
        for num in right.split('+'):
            if 'x' in num:
                if num == 'x':
                    right_x += 1
                elif num == '-x':
                    right_x -= 1
                else:
                    right_x += int(num[:-1])
            else:
                right_val += int(num)
        left_x -= right_x
        right_val -= left_val
        if left_x != 0 and right_val == 0:
            return "x=0"
        elif left_x != 0 and right_val != 0:
            return 'x=' + str(right_val / left_x)
        elif left_x == 0 and right_val == 0:
            return "Infinite solutions"
        elif left_x == 0 and right_val != 0:
            return "No solution"

### Test case : dev

# V1'
# https://leetcode.com/problems/solve-the-equation/discuss/105362/Simple-2-liner-(and-more)
# IDEA : eval
class Solution(object):
    def solveEquation(self, equation):
        z = eval(equation.replace('x', 'j').replace('=', '-(') + ')', {'j': 1j})
        a, x = z.real, -z.imag
        return 'x=%d' % (a / x) if x else 'No solution' if a else 'Infinite solutions'

# V2
# Time:  O(n)
# Space: O(n)
import re
class Solution(object):
    def solveEquation(self, equation):
        """
        :type equation: str
        :rtype: str
        """
        a, b, side = 0, 0, 1
        for eq, sign, num, isx in re.findall('(=)|([-+]?)(\d*)(x?)', equation):
            if eq:
                side = -1
            elif isx:
                a += side * int(sign + '1') * int(num or 1)
            elif num:
                b -= side * int(sign + num)
        return 'x=%d' % (b / a) if a else 'No solution' if b else 'Infinite solutions'
