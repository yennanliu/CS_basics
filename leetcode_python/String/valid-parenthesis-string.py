"""

678. Valid Parenthesis String
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given a string s containing only three types of characters: '(', ')' and '*', return true if s is valid.

The following rules define a valid string:

Any left parenthesis '(' must have a corresponding right parenthesis ')'.
Any right parenthesis ')' must have a corresponding left parenthesis '('.
Left parenthesis '(' must go before the corresponding right parenthesis ')'.
'*' could be treated as a single right parenthesis ')' or a single left parenthesis '(' or an empty string "".
 

Example 1:

Input: s = "()"
Output: true
Example 2:

Input: s = "(*)"
Output: true
Example 3:

Input: s = "(*))"
Output: true
Example 4:

Input: s = "("
Output: false
 

Constraints:

1 <= s.length <= 100
s[i] is '(', ')' or '*'.

"""


# V0
class Solution(object):
    def checkValidString(self, s):
        """
        :type s: str
        :rtype: bool
        """
        pass

# V0-1
# IDEA: MIN, MAX parenthesis CNT
class Solution(object):
    def checkValidString(self, s):
        """
        :type s: str
        :rtype: bool
        """
        # edge case
        if not s:
            return True

        min_left_paren_cnt = 0
        max_left_paren_cnt = 0

        for x in s:

            if x == "(":
                # Must be an opening parenthesis
                min_left_paren_cnt += 1
                max_left_paren_cnt += 1

            elif x == ")":
                # Must close one opening parenthesis
                min_left_paren_cnt -= 1
                max_left_paren_cnt -= 1

            else:
                # '*' can be:
                # 1. ')'
                # 2. empty
                # 3. '('
                #
                # Minimum: treat '*' as ')'
                min_left_paren_cnt -= 1

                # Maximum: treat '*' as '('
                max_left_paren_cnt += 1

            # We cannot have negative minimum.
            #
            # Example:
            # s = "(*"
            # '*' can simply be empty,
            # so minimum unmatched '(' is 0.
            min_left_paren_cnt = max(0, min_left_paren_cnt)

            # Even if every '*' is treated as '(',
            # we still have too many ')'.
            if max_left_paren_cnt < 0:
                return False

        # If minimum unmatched '(' is 0,
        # there exists a valid interpretation.
        return min_left_paren_cnt == 0


# V0-2


# V1-1
# IDEA: TOP DOWN DP
# https://leetcode.com/problems/valid-parenthesis-string/editorial/
class Solution:
    def checkValidString(self, s: str) -> bool:
        n = len(s)
        memo = [[-1] * n for _ in range(n)]
        return self.is_valid_string(0, 0, s, memo)

    def is_valid_string(self, index: int, open_count: int, s: str, memo: List[List[int]]) -> bool:
        # If reached end of the string, check if all brackets are balanced
        if index == len(s):
            return open_count == 0

        # If already computed, return memoized result
        if memo[index][open_count] != -1:
            return memo[index][open_count] == 1

        is_valid = False
        # If encountering '*', try all possibilities
        if s[index] == '*':
            is_valid |= self.is_valid_string(index + 1, open_count + 1, s, memo)  # Treat '*' as '('
            if open_count > 0:
                is_valid |= self.is_valid_string(index + 1, open_count - 1, s, memo)  # Treat '*' as ')'
            is_valid |= self.is_valid_string(index + 1, open_count, s, memo)  # Treat '*' as empty
        else:
            # Handle '(' and ')'
            if s[index] == '(':
                is_valid = self.is_valid_string(index + 1, open_count + 1, s, memo)  # Increment count for '('
            elif open_count > 0:
                is_valid = self.is_valid_string(index + 1, open_count - 1, s, memo)  # Decrement count for ')'

        # Memoize and return the result
        memo[index][open_count] = 1 if is_valid else 0
        return is_valid


# V1-2
# IDEA: BOTTOM UP DP
# https://leetcode.com/problems/valid-parenthesis-string/editorial/
class Solution:
    def checkValidString(self, s: str) -> bool:
        n = len(s)
        # dp[i][j] represents if the substring starting from index i is valid with j opening brackets
        dp = [[False] * (n + 1) for _ in range(n + 1)]

        # base case: an empty string with 0 opening brackets is valid
        dp[n][0] = True

        for index in range(n - 1, -1, -1):
            for open_bracket in range(n):
                is_valid = False

                # '*' can represent '(' or ')' or '' (empty)
                if s[index] == '*':
                    if open_bracket < n:
                        is_valid |= dp[index + 1][open_bracket + 1]  # try '*' as '('
                    # opening brackets to use '*' as ')'
                    if open_bracket > 0:
                        is_valid |= dp[index + 1][open_bracket - 1]  # try '*' as ')'
                    is_valid |= dp[index + 1][open_bracket]  # ignore '*'
                else:
                    # If the character is not '*', it can be '(' or ')'
                    if s[index] == '(':
                        is_valid |= dp[index + 1][open_bracket + 1]  # try '('
                    elif open_bracket > 0:
                        is_valid |= dp[index + 1][open_bracket - 1]  # try ')'
                dp[index][open_bracket] = is_valid

        return dp[0][0]  # check if the entire string is valid with no excess opening brackets


# V1-3
# IDEA: 2 STACK
# https://leetcode.com/problems/valid-parenthesis-string/editorial/
class Solution:
    def checkValidString(self, s: str) -> bool:
        # Stacks to store indices of open brackets and asterisks
        open_brackets = []
        asterisks = []

        for i, ch in enumerate(s):
            # If current character is an open bracket, push its index onto the stack
            if ch == "(":
                open_brackets.append(i)
            # If current character is an asterisk, push its index onto the stack
            elif ch == "*":
                asterisks.append(i)
            # current character is a closing bracket ')'
            else:
                # If there are open brackets available, use them to balance the closing bracket
                if open_brackets:
                    open_brackets.pop()
                elif asterisks:
                    # If no open brackets are available, use an asterisk to balance the closing bracket
                    asterisks.pop()
                else:
                    # nnmatched ')' and no '*' to balance it.
                    return False

        # Check if there are remaining open brackets and asterisks that can balance them
        while open_brackets and asterisks:
            # If an open bracket appears after an asterisk, it cannot be balanced, return false
            if open_brackets.pop() > asterisks.pop():
                return False  # '*' before '(' which cannot be balanced.

        # If all open brackets are matched and there are no unmatched open brackets left, return true
        return not open_brackets


# V1-4
# IDEA: 2 POINTERS
# https://leetcode.com/problems/valid-parenthesis-string/editorial/
class Solution:
    def checkValidString(self, s: str) -> bool:
        open_count = 0
        close_count = 0
        length = len(s) - 1
        
        # Traverse the string from both ends simultaneously
        for i in range(length + 1):
            # Count open parentheses or asterisks
            if s[i] == '(' or s[i] == '*':
                open_count += 1
            else:
                open_count -= 1
            
            # Count close parentheses or asterisks
            if s[length - i] == ')' or s[length - i] == '*':
                close_count += 1
            else:
                close_count -= 1
            
            # If at any point open count or close count goes negative, the string is invalid
            if open_count < 0 or close_count < 0:
                return False
        
        # If open count and close count are both non-negative, the string is valid
        return True
