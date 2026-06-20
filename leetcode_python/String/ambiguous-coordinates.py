"""

816. Ambiguous Coordinates
Medium
Topics
premium lock icon
Companies
We had some 2-dimensional coordinates, like "(1, 3)" or "(2, 0.5)". Then, we removed all commas, decimal points, and spaces and ended up with the string s.

For example, "(1, 3)" becomes s = "(13)" and "(2, 0.5)" becomes s = "(205)".
Return a list of strings representing all possibilities for what our original coordinates could have been.

Our original representation never had extraneous zeroes, so we never started with numbers like "00", "0.0", "0.00", "1.0", "001", "00.01", or any other number that can be represented with fewer digits. Also, a decimal point within a number never occurs without at least one digit occurring before it, so we never started with numbers like ".1".

The final answer list can be returned in any order. All coordinates in the final answer have exactly one space between them (occurring after the comma.)

 

Example 1:

Input: s = "(123)"
Output: ["(1, 2.3)","(1, 23)","(1.2, 3)","(12, 3)"]
Example 2:

Input: s = "(0123)"
Output: ["(0, 1.23)","(0, 12.3)","(0, 123)","(0.1, 2.3)","(0.1, 23)","(0.12, 3)"]
Explanation: 0.0, 00, 0001 or 00.01 are not allowed.
Example 3:

Input: s = "(00011)"
Output: ["(0, 0.011)","(0.001, 1)"]
 

Constraints:

4 <= s.length <= 12
s[0] == '(' and s[s.length - 1] == ')'.
The rest of s are digits.
 
Seen this question in a real interview before?
1/6
Yes
No

"""


# V0
class Solution(object):
    def ambiguousCoordinates(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        # Step 1: Strip the outer parenthesis characters '(' and ')'
        digits = s[1:-1]
        res = []
        
        # Step 2: Try every possible split position to divide the digits into left and right halves
        # The left part needs at least 1 character, and the right part needs at least 1 character.
        for i in range(1, len(digits)):
            left_part = digits[:i]
            right_part = digits[i:]
            
            # Generate all legal number variations for both segments
            valid_lefts = self.get_valid_formats(left_part)
            valid_rights = self.get_valid_formats(right_part)
            
            # Step 4: Cross multiply the valid choices together
            for left in valid_lefts:
                for right in valid_rights:
                    res.append("({}, {})".format(left, right))
                    
        return res

    def get_valid_formats(self, sub_str):
        """
        Helper method that returns a list of all mathematically valid representations 
        of a string segment (either as a valid integer or a valid decimal).
        """
        ans = []
        n = len(sub_str)
        
        # Scenario A: Try treating the entire string slice as a whole integer (no decimal point)
        # Rule: It cannot have leading zeros unless the string is exactly "0"
        """
        NOTE !!!


        -> 


        When we look at strings like `"011"` or `"00011"`, 
        **any** string that has a length greater 
        than 1 (`len(integer_part) > 1`) and 
        starts with the character zero (`integer_part.startswith('0')`)
         is mathematically an `ILLEGAL` integer representation.



        By writing len(integer_part) > 1 and integer_part.startswith('0'),
        we are saying: "If you start with a zero,
        the only way you are allowed to survive is
        if you are exactly one character long (just '0').
        If you have any extra digits trailing after that leading zero, 
        you are illegal."



        =======================================================================
        | Integer Part | len > 1? | startswith('0')? | Combined Result | Status   |
        =======================================================================
        | "0"          | False    | True             | False           | ALLOWED  |
        | "01"         | True     | True             | True            | BLOCKED  |
        | "000"        | True     | True             | True            | BLOCKED  |
        | "105"        | True     | False            | False           | ALLOWED  |
        =======================================================================



        """
        if n == 1 or not sub_str.startswith('0'):
            ans.append(sub_str)
            
        # Scenario B: Inject a decimal point at every possible interior position
        for i in range(1, n):
            integer_part = sub_str[:i]
            decimal_part = sub_str[i:]
            
            # Rule 1: Integer part cannot have leading zeros unless it is exactly "0"
            if len(integer_part) > 1 and integer_part.startswith('0'):
                continue
            # Rule 2: Decimal part cannot have trailing zeros at the very end
            if decimal_part.endswith('0'):
                continue
                
            ans.append(integer_part + "." + decimal_part)
            
        return ans



# V0-2
# IDEA: GEMINI
class Solution(object):
    def ambiguousCoordinates(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        # Step 1: Strip the outer parenthesis characters '(' and ')'
        digits = s[1:-1]
        res = []
        
        # Step 2: Try every possible split position to divide the digits into left and right halves
        # The left part needs at least 1 character, and the right part needs at least 1 character.
        for i in range(1, len(digits)):
            left_part = digits[:i]
            right_part = digits[i:]
            
            # Generate all legal number variations for both segments
            valid_lefts = self.get_valid_formats(left_part)
            valid_rights = self.get_valid_formats(right_part)
            
            # Step 4: Cross multiply the valid choices together
            for left in valid_lefts:
                for right in valid_rights:
                    res.append("({}, {})".format(left, right))
                    
        return res

    def get_valid_formats(self, sub_str):
        """
        Helper method that returns a list of all mathematically valid representations 
        of a string segment (either as a valid integer or a valid decimal).
        """
        ans = []
        n = len(sub_str)
        
        # Scenario A: Try treating the entire string slice as a whole integer (no decimal point)
        # Rule: It cannot have leading zeros unless the string is exactly "0"
        if n == 1 or not sub_str.startswith('0'):
            ans.append(sub_str)
            
        # Scenario B: Inject a decimal point at every possible interior position
        for i in range(1, n):
            integer_part = sub_str[:i]
            decimal_part = sub_str[i:]
            
            # Rule 1: Integer part cannot have leading zeros unless it is exactly "0"
            if len(integer_part) > 1 and integer_part.startswith('0'):
                continue
            # Rule 2: Decimal part cannot have trailing zeros at the very end
            if decimal_part.endswith('0'):
                continue
                
            ans.append(integer_part + "." + decimal_part)
            
        return ans



# V0-2
# IDEA: GPT
class Solution(object):
    def ambiguousCoordinates(self, s):
        digits = s[1:-1]  # remove outer parentheses
        res = []

        def generate(part):
            n = len(part)

            # no decimal
            if n == 1 or part[0] != '0':
                yield part

            # decimal versions
            for i in range(1, n):
                left = part[:i]
                right = part[i:]

                # left side can't have leading zeros unless exactly "0"
                if len(left) > 1 and left[0] == '0':
                    continue

                # right side can't end with zero
                if right[-1] == '0':
                    continue

                yield left + "." + right

        for i in range(1, len(digits)):
            left = digits[:i]
            right = digits[i:]

            for l in generate(left):
                for r in generate(right):
                    res.append("(" + l + ", " + r + ")")

        return res




# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80677194
class Solution(object):
    def ambiguousCoordinates(self, S):
        """
        :type S: str
        :rtype: List[str]
        """
        ans = []
        S = S[1:-1]
        for i in range(1, len(S)):
            left, right = S[:i], S[i:]
            left_list = self.get_number(left)
            right_list = self.get_number(right)
            if left_list and right_list:
                for left_number in left_list:
                    for right_number in right_list:
                        ans.append("(" + left_number + ", " + right_number + ")")
        return ans

    def get_number(self, num):
        decimal_list = []
        if len(num) == 1 or num[0] != '0':
            decimal_list.append(num)
        for i in range(1, len(num)):
            integer, fractor = num[:i], num[i:]
            print(integer, fractor)
            if (len(integer) > 1 and integer[0] == '0') or fractor[-1] == '0':
                continue
            decimal_list.append(integer + '.' + fractor)
        return decimal_list
        
# V2 
# Time:  O(n^4)
# Space: O(n)
import itertools
class Solution(object):
    def ambiguousCoordinates(self, S):
        """
        :type S: str
        :rtype: List[str]
        """
        def make(S, i, n):
            for d in range(1, n+1):
                left = S[i:i+d]
                right = S[i+d:i+n]
                if ((not left.startswith('0') or left == '0')
                        and (not right.endswith('0'))):
                    yield "".join([left, '.' if right else '', right])

        return ["({}, {})".format(*cand)
                for i in range(1, len(S)-2)
                for cand in itertools.product(make(S, 1, i),
                                              make(S, i+1, len(S)-2-i))]