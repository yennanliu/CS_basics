"""

8. String to Integer (atoi)
Medium

Implement the myAtoi(string s) function, which converts a string to a 32-bit signed integer (similar to C/C++'s atoi function).

The algorithm for myAtoi(string s) is as follows:

Read in and ignore any leading whitespace.
Check if the next character (if not already at the end of the string) is '-' or '+'. Read this character in if it is either. This determines if the final result is negative or positive respectively. Assume the result is positive if neither is present.
Read in next the characters until the next non-digit character or the end of the input is reached. The rest of the string is ignored.
Convert these digits into an integer (i.e. "123" -> 123, "0032" -> 32). If no digits were read, then the integer is 0. Change the sign as necessary (from step 2).
If the integer is out of the 32-bit signed integer range [-231, 231 - 1], then clamp the integer so that it remains in the range. Specifically, integers less than -231 should be clamped to -231, and integers greater than 231 - 1 should be clamped to 231 - 1.
Return the integer as the final result.
Note:

Only the space character ' ' is considered a whitespace character.
Do not ignore any characters other than the leading whitespace or the rest of the string after the digits.
 

Example 1:

Input: s = "42"
Output: 42
Explanation: The underlined characters are what is read in, the caret is the current reader position.
Step 1: "42" (no characters read because there is no leading whitespace)
         ^
Step 2: "42" (no characters read because there is neither a '-' nor '+')
         ^
Step 3: "42" ("42" is read in)
           ^
The parsed integer is 42.
Since 42 is in the range [-231, 231 - 1], the final result is 42.
Example 2:

Input: s = "   -42"
Output: -42
Explanation:
Step 1: "   -42" (leading whitespace is read and ignored)
            ^
Step 2: "   -42" ('-' is read, so the result should be negative)
             ^
Step 3: "   -42" ("42" is read in)
               ^
The parsed integer is -42.
Since -42 is in the range [-231, 231 - 1], the final result is -42.
Example 3:

Input: s = "4193 with words"
Output: 4193
Explanation:
Step 1: "4193 with words" (no characters read because there is no leading whitespace)
         ^
Step 2: "4193 with words" (no characters read because there is neither a '-' nor '+')
         ^
Step 3: "4193 with words" ("4193" is read in; reading stops because the next character is a non-digit)
             ^
The parsed integer is 4193.
Since 4193 is in the range [-231, 231 - 1], the final result is 4193.
 

Constraints:

0 <= s.length <= 200
s consists of English letters (lower-case and upper-case), digits (0-9), ' ', '+', '-', and '.'.

"""

# V0
# IDEA : REGEX
class Solution(object):
    def myAtoi(self, str):
        str = str.strip()
        try:
            ### NOTE : this trick (regex)
            res = re.search('(^[\+\-]?\d+)', str).group()
            # res = re.search(r"\d+", s).group()
            res = int(res)
            res = res if res <= 2**31 - 1 else 2**31 - 1    # 2**31 == 2147483648
            res = res if res >= -1 * 2**31  else -1 * 2**31   # -(1)*(2**31) == - 2147483648
        except:
            res = 0
        return res

# V0'
# IDEA : string op
class Solution(object):
    def myAtoi(self, _str):
        _str = _str.strip()
        number = 0
        flag = 1
        print ("_str = " + str(_str))
        if not _str:
            return 0
        if _str[0] == '-':
            _str = _str[1:]
            flag = -1
        elif _str[0] == '+':
            _str = _str[1:]
        for c in _str:
            #if c >= '0' and c <= '9':  # '3' > '2' -> True
            if c in [str(x) for x in range(10)]:
                """
                str(int) -> ord demo
                             
                Example 1 :
                In [55]: for i in range(10):
                        ...: print (str(i) + " ord = " + str(ord(str(i))))
                        ...:
                                0 ord = 48
                                1 ord = 49
                                2 ord = 50
                                3 ord = 51
                                4 ord = 52
                                5 ord = 53
                                6 ord = 54
                                7 ord = 55
                                8 ord = 56
                                9 ord = 57

                Example 2 :

                            In [62]: z
                            Out[62]: '5634'

                            In [63]: ans = 0

                            In [64]: for i in z:
                                ...:     ans = 10 * ans + int(i)
                                ...:

                            In [65]: ans
                            Out[65]: 5634
                """
                #number = 10*number + ord(c) - ord('0')  # _string to integer 
                number = 10*number + int(c)  # _string to integer , above is OK as well
            else:
                break
        res = flag * number
        res = res if res <= 2**31 - 1 else 2**31 - 1    # 2**31 == 2147483648
        res = res if res >= -1 * 2**31  else -1 * 2**31   # -(1)*(2**31) == - 2147483648
        return res

# V1
# IDEA : Follow the Rules
# https://leetcode.com/problems/string-to-integer-atoi/solution/
class Solution:
    def myAtoi(self, input: str) -> int:
        sign = 1 
        result = 0
        index = 0
        n = len(input)
        
        INT_MAX = pow(2,31) - 1 
        INT_MIN = -pow(2,31)
        
        # Discard all spaces from the beginning of the input string.
        while index < n and input[index] == ' ':
            index += 1
        
        # sign = +1, if it's positive number, otherwise sign = -1. 
        if index < n and input[index] == '+':
            sign = 1
            index += 1
        elif index < n and input[index] == '-':
            sign = -1
            index += 1
        
        # Traverse next digits of input and stop if it is not a digit. 
        # End of string is also non-digit character.
        while index < n and input[index].isdigit():
            digit = int(input[index])
            
            # Check overflow and underflow conditions. 
            if ((result > INT_MAX // 10) or (result == INT_MAX // 10 and digit > INT_MAX % 10)):
                # If integer overflowed return 2^31-1, otherwise if underflowed return -2^31.    
                return INT_MAX if sign == 1 else INT_MIN
            
            # Append current digit to the result.
            result = 10 * result + digit
            index += 1
        
        # We have formed a valid number without any overflow/underflow.
        # Return it after multiplying it with its sign.
        return sign * result

# V1
# IDEA : Deterministic Finite Automaton (DFA)
# https://leetcode.com/problems/string-to-integer-atoi/solution/
class StateMachine:
    def __init__(self):
        self.State = { "q0": 1, "q1": 2, "q2": 3, "qd": 4 }
        self.INT_MAX, self.INT_MIN = pow(2, 31) - 1, -pow(2, 31)
        
        # Store current state value.
        self.__current_state = self.State["q0"]
        # Store result formed and its sign.
        self.__result = 0
        self.__sign = 1

    def to_state_q1(self, ch: chr) -> None:
        """Transition to state q1."""
        self.__sign = -1 if (ch == '-') else 1
        self.__current_state = self.State["q1"]
    
    def to_state_q2(self, digit: int) -> None:
        """Transition to state q2."""
        self.__current_state = self.State["q2"]
        self.append_digit(digit)
    
    def to_state_qd(self) -> None:
        """Transition to dead state qd."""
        self.__current_state = self.State["qd"]
    
    def append_digit(self, digit: int) -> None:
        """Append digit to result, if out of range return clamped value."""
        if ((self.__result > self.INT_MAX // 10) or 
            (self.__result == self.INT_MAX // 10 and digit > self.INT_MAX % 10)):
            if self.__sign == 1:
                # If sign is 1, clamp result to INT_MAX.
                self.__result = self.INT_MAX
            else:
                # If sign is -1, clamp result to INT_MIN.
                self.__result = self.INT_MIN
                self.__sign = 1
            
            # When the 32-bit int range is exceeded, a dead state is reached.
            self.to_state_qd()
        else:
            # Append current digit to the result. 
            self.__result = (self.__result * 10) + digit

    def transition(self, ch: chr) -> None:
        """Change state based on current input character."""
        if self.__current_state == self.State["q0"]:
            # Beginning state of the string (or some whitespaces are skipped).
            if ch == ' ':
                # Current character is a whitespaces.
                # We stay in same state. 
                return
            elif ch == '-' or ch == '+':
                # Current character is a sign.
                self.to_state_q1(ch)
            elif ch.isdigit():
                # Current character is a digit.
                self.to_state_q2(int(ch))
            else:
                # Current character is not a space/sign/digit.
                # Reached a dead state.
                self.to_state_qd()
        
        elif self.__current_state == self.State["q1"] or self.__current_state == self.State["q2"]:
            # Previous character was a sign or digit.
            if ch.isdigit():
                # Current character is a digit.
                self.to_state_q2(int(ch))
            else:
                # Current character is not a digit.
                # Reached a dead state.
                self.to_state_qd()
    
    def get_integer(self) -> None:
        """Return the final result formed with it's sign."""
        return self.__sign * self.__result
    
    def get_state(self) -> None:
        """Get current state."""
        return self.__current_state

class Solution:
    def myAtoi(self, input: str) -> int:
        q = StateMachine()
        
        for ch in input:
            q.transition(ch)
            if q.get_state() == q.State["qd"]:
                break

        return q.get_integer()

# V1 
# https://blog.csdn.net/coder_orz/article/details/52053932
class Solution(object):
    def myAtoi(self, _str):
        _str = _str.strip()
        number, flag = 0, 1
        print ("_str = " + str(_str))
        if not _str:
            return 0
        if _str[0] == '-':
            _str = _str[1:]
            flag = -1
        elif _str[0] == '+':
            _str = _str[1:]
        for c in _str:
            if c >= '0' and c <= '9':  # '3' > '2' -> True 
                number = 10*number + ord(c) - ord('0')  # _string to integer 
            else:
                break
        number = flag * number
        number = number if number <= 2147483647 else 2147483647
        number = number if number >= -2147483648 else -2147483648
        return number

# V1'
# https://blog.csdn.net/coder_orz/article/details/52053932
# IDEA : REGULAR EXPRESSION 
class Solution(object):
    def myAtoi(self, str):
        """
        :type str: str
        :rtype: int
        """
        str = str.strip()
        try:
            res = re.search('(^[\+\-]?\d+)', str).group()
            res = int(res)
            res = res if res <= 2147483647 else 2147483647
            res = res if res >= -2147483648 else -2147483648
        except:
            res = 0
        return res

# V1''
# https://www.jiuzhang.com/solution/string-to-integer-atoi/#tag-highlight-lang-python
class Solution(object):
    # string -> int, remove prefix and +, -. Please note the max, min interval of int when transform
    def atoi(self, str):
        str = str.strip()
        if str == "" :
            return 0
        i = 0
        sign = 1
        ret = 0
        length = len(str)
        MaxInt = (1 << 31) - 1
        if str[i] == '+':
            i += 1
        elif str[i] == '-' :
            i += 1
            sign = -1
        
        for i in range(i, length) :
            if str[i] < '0' or str[i] > '9' :
                break
            ret = ret * 10 + int(str[i])
            if ret > sys.maxint:
                break
        ret *= sign
        if ret >= MaxInt:
            return MaxInt
        if ret < MaxInt * -1 :
            return MaxInt * - 1 - 1 
        return ret

# V2 
class Solution(object):
    def myAtoi(self, str):
        """
        :type str: str
        :rtype: int
        """
        INT_MAX =  2147483647
        INT_MIN = -2147483648
        result = 0

        if not str:
            return result

        i = 0
        while i < len(str) and str[i].isspace():
            i += 1

        if len(str) == i:
            return result

        sign = 1
        if str[i] == "+":
            i += 1
        elif str[i] == "-":
            sign = -1
            i += 1

        while i < len(str) and '0' <= str[i] <= '9':
            if result > (INT_MAX - int(str[i])) / 10:
                return INT_MAX if sign > 0 else INT_MIN
            result = result * 10 + int(str[i])
            i += 1

        return sign * result