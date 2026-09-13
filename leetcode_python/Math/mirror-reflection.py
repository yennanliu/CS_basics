"""

858. Mirror Reflection
Medium

There is a special square room with mirrors on each of the four walls. Except for the southwest corner, there are receptors on each of the remaining corners, numbered 0, 1, and 2.

The square room has walls of length p and a laser ray from the southwest corner first meets the east wall at a distance q from the 0^th receptor.

Given the two integers p and q, return the number of the receptor that the ray meets first.

The test cases are guaranteed so that the ray will meet a receptor eventually.

Example 1:

https://s3-lc-upload.s3.amazonaws.com/uploads/2018/06/18/reflection.png

Input: p = 2, q = 1
Output: 2
Explanation: The ray meets receptor 2 the first time it gets reflected back to the left wall.

Example 2:

Input: p = 3, q = 1
Output: 1

Constraints:

1 <= q <= p <= 1000

"""

# V0

# V1 : dev

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/82432579
# class Solution(object):
#     def mirrorReflection(self, p, q):
#         """
#         :type p: int
#         :type q: int
#         :rtype: int
#         """
#         m, n = q, p
#         while m % 2 == 0 and n % 2 == 0:
#             m, n = m / 2, n / 2
#             if m % 2 == 0 and n % 2 == 1:
#                 return 0
#             elif m % 2 == 1 and n % 2 == 1:
#                 return 1
#             else m % 2 == 1 and n % 2 == 0:
#                 return 2 

# V3
# time = O(1)
# space = O(1)
class Solution(object):
    def mirrorReflection(self, p, q):
        """
        :type p: int
        :type q: int
        :rtype: int
        """
        # explanation commented in the following solution
        return 2 if (p & -p) > (q & -q) else 0 if (p & -p) < (q & -q) else 1


# time = O(log(max(p, q))) = O(1) due to 32-bit integer
# space = O(1)
class Solution2(object):
    def mirrorReflection(self, p, q):
        """
        :type p: int
        :type q: int
        :rtype: int
        """
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        lcm = p*q // gcd(p, q)
        # let a = lcm / p, b = lcm / q
        if lcm // p % 2 == 1:
            if lcm // q % 2 == 1:
                return 1  # a is odd, b is odd <=> (p & -p) == (q & -q)
            return 2  # a is odd, b is even <=> (p & -p) > (q & -q)
        return 0  # a is even, b is odd <=> (p & -p) < (q & -q)
