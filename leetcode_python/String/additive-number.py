"""

306. Additive Number
Medium
Topics
premium lock icon
Companies
An additive number is a string whose digits can form an additive sequence.

A valid additive sequence should contain at least three numbers. Except for the first two numbers, each subsequent number in the sequence must be the sum of the preceding two.

Given a string containing only digits, return true if it is an additive number or false otherwise.

Note: Numbers in the additive sequence cannot have leading zeros, so sequence 1, 2, 03 or 1, 02, 3 is invalid.

 

Example 1:

Input: "112358"
Output: true
Explanation: 
The digits can form an additive sequence: 1, 1, 2, 3, 5, 8. 
1 + 1 = 2, 1 + 2 = 3, 2 + 3 = 5, 3 + 5 = 8
Example 2:

Input: "199100199"
Output: true
Explanation: 
The additive sequence is: 1, 99, 100, 199. 
1 + 99 = 100, 99 + 100 = 199
 

Constraints:

1 <= num.length <= 35
num consists only of digits.
 

Follow up: How would you handle overflow for very large input integers?

 


"""

# V0
# IDEA (GPT)
# time = O(1)  # unimplemented stub
# space = O(1)
class Solution(object):
    def isAdditiveNumber(self, num):
        """
        :type num: str
        :rtype: bool
        """
        pass


# V0-1
# time = O(n^3)  # O(n^2) splits, each check() scan is O(n)
# space = O(n)
class Solution:
    def isAdditiveNumber(self, num: str) -> bool:
        n = len(num)

        # length of first number
        for i in range(1, n):
            # no leading zeros
            if num[0] == "0" and i > 1:
                break

            # length of second number
            for j in range(i + 1, n):
                # no leading zeros
                if num[i] == "0" and j - i > 1:
                    break

                a = int(num[:i])
                b = int(num[i:j])

                if self.check(num, j, a, b):
                    return True

        return False

    def check(self, num, idx, a, b):
        while idx < len(num):
            c = a + b
            s = str(c)

            if not num.startswith(s, idx):
                return False

            idx += len(s)
            a, b = b, c

        return True


# V0-2
# IDEA: Backtracking / Brute Force (GEMINI)
# time = O(n^3)  # O(n^2) splits, each isValidSequence recursion is O(n)
# space = O(n)
class Solution(object):
    def isAdditiveNumber(self, num):
        """
        :type num: str
        :rtype: bool
        """
        n = len(num)
        if n < 3:
            return False
            
        # i represents the end index of the first number
        for i in range(1, n // 2 + 1):
            # Leading zero check for the first number
            if num[0] == '0' and i > 1:
                break
                
            # j represents the end index of the second number
            # The remaining string must be at least as long as max(i, j-i)
            for j in range(i + 1, n):
                # Leading zero check for the second number
                if num[i] == '0' and j - i > 1:
                    break
                    
                # Extract our first two numbers
                n1 = int(num[:i])
                n2 = int(num[i:j])
                
                # Check if these two numbers can form a valid sequence
                if self.isValidSequence(n1, n2, j, num):
                    return True
                    
        return False

    def isValidSequence(self, n1, n2, k, num):
        # If we successfully parsed the entire string to the end, it's valid
        if k == len(num):
            return True
            
        # Calculate what the next number MUST be
        next_sum = n1 + n2
        next_sum_str = str(next_sum)
        
        # Check if the remaining string starts with this expected sum
        if not num.startswith(next_sum_str, k):
            return False
            
        # Cascade down the line recursively: 
        # n2 becomes the new n1, next_sum becomes the new n2
        return self.isValidSequence(n2, next_sum, k + len(next_sum_str), num)


# V1
# http://bookshadow.com/weblog/2015/11/18/leetcode-additive-number/
# IDEA : ITERATION
# time = O(n^3)  # O(n^2) combinations, each inner while scan is O(n)
# space = O(n)
class Solution(object):
    def isAdditiveNumber(self, num):
        """
        :type num: str
        :rtype: bool
        """
        n = len(num)
        for i, j in itertools.combinations(range(1, n), 2):
            a, b = num[:i], num[i:j]
            if a != str(int(a)) or b != str(int(b)):
                continue
            while j < n:
                c = str(int(a) + int(b))
                if not num.startswith(c, j):
                    break
                j += len(c)
                a, b = b, c
            if j == n:
                return True
        return False


# V1'
# http://bookshadow.com/weblog/2015/11/18/leetcode-additive-number/
# IDEA : RECURSION
# time = O(n^3)  # O(n^2) splits, each recursive search scan is O(n)
# space = O(n)
class Solution(object):
    def isAdditiveNumber(self, num):
        """
        :type num: str
        :rtype: bool
        """
        def isValid(num):
            return len(num) == 1 or num[0] != '0'

        def search(a, b, c):
            d = str(int(a) + int(b))
            if not isValid(d) or not c.startswith(d):
                return False
            if c == d:
                return True
            return search(b, d, c[len(d):])

        size = len(num)
        for x in range(1, size / 2 + 1):
            for y in range(x + 1, size):
                a, b, c = num[:x], num[x:y], num[y:]
                if not isValid(a) or not isValid(b):
                    continue
                if search(a, b, c):
                    return True
        return False

# V2
# time = O(n^3)
# space = O(n)
class Solution(object):
    def isAdditiveNumber(self, num):
        """
        :type num: str
        :rtype: bool
        """
        def add(a, b):
            res, carry, val = "", 0, 0
            for i in range(max(len(a), len(b))):
                val = carry
                if i < len(a):
                    val += int(a[-(i + 1)])
                if i < len(b):
                    val += int(b[-(i + 1)])
                carry, val = val / 10, val % 10
                res += str(val)
            if carry:
                res += str(carry)
            return res[::-1]

        for i in range(1, len(num)):
            for j in range(i + 1, len(num)):
                s1, s2 = num[0:i], num[i:j]
                if (len(s1) > 1 and s1[0] == '0') or \
                   (len(s2) > 1 and s2[0] == '0'):
                    continue

                expected = add(s1, s2)
                cur = s1 + s2 + expected
                while len(cur) < len(num):
                    s1, s2, expected = s2, expected, add(s2, expected)
                    cur += expected
                if cur == num:
                    return True
        return False
