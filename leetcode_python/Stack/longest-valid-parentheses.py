"""

32. Longest Valid Parentheses
Hard

Given a string containing just the characters '(' and ')', find the length of the longest valid (well-formed) parentheses substring.

 

Example 1:

Input: s = "(()"
Output: 2
Explanation: The longest valid parentheses substring is "()".
Example 2:

Input: s = ")()())"
Output: 4
Explanation: The longest valid parentheses substring is "()()".
Example 3:

Input: s = ""
Output: 0
 

Constraints:

0 <= s.length <= 3 * 104
s[i] is '(', or ')'.

"""

# V0
class Solution(object):
    def longestValidParentheses(self, s):
        """
        :type s: str
        :rtype: int
        """
        pass


# V0-1
# IDEA: STACK (gpt)
class Solution(object):

    def longestValidParentheses(self, s):
        """
        :type s: str
        :rtype: int
        """

        max_len = 0

        # Store indices.
        # Start with -1 as the base index.
        st = [-1]

        for i in range(len(s)):

            if s[i] == "(":
                st.append(i)

            else:
                # Try to match this ')'
                st.pop()

                # No matching '('.
                if not st:
                    # Current ')' becomes the new boundary.
                    st.append(i)
                else:
                    # Valid substring length.
                    max_len = max(max_len, i - st[-1])

        return max_len


# V0-2
# IDEA: STACK (gemini)
class Solution(object):

  def longestValidParentheses(self, s):
    """:type s: str :rtype: int"""
    if not s:
      return 0

    # 堆疊用於存放索引，初始放入 -1 作為邊界哨兵
    stack = [-1]
    max_len = 0

    for i, char in enumerate(s):
      if char == '(':
        stack.append(i)
      else:
        stack.pop()
        if not stack:
          # 堆疊為空，代表當前的 ')' 無法配對，將其作為新的邊界基準點
          stack.append(i)
        else:
          # 當前合法長度 = 當前位置 - 堆疊頂端的最新邊界
          max_len = max(max_len, i - stack[-1])

    return max_len


# V0-4
# IDEA: 1D DP (gemini)
class Solution(object):

  def longestValidParentheses(self, s):
    """:type s: str :rtype: int"""
    if not s or len(s) <= 1:
      return 0

    n = len(s)
    dp = [0] * n
    max_len = 0

    for i in range(1, n):
      if s[i] == ')':
        # 情況 1：形如 "...()"
        if s[i - 1] == '(':
          dp[i] = (dp[i - 2] if i >= 2 else 0) + 2

        # 情況 2：形如 "...))"
        elif i - dp[i - 1] > 0 and s[i - dp[i - 1] - 1] == '(':
          # 外層長度 (dp[i-1] + 2) + 跨過左括號後前面的合法長度
          prev_valid = dp[i - dp[i - 1] - 2] if i - dp[i - 1] >= 2 else 0
          dp[i] = dp[i - 1] + 2 + prev_valid

        max_len = max(max_len, dp[i])

    return max_len


# V0-5
# IDEA: 1D DP (GPT)
class Solution(object):

    def longestValidParentheses(self, s):
        """
        :type s: str
        :rtype: int
        """

        # Edge case
        if not s or len(s) <= 1:
            return 0

        n = len(s)

        # dp[i] = longest valid parentheses substring
        # ending at index i
        dp = [0] * n

        max_len = 0

        for i in range(1, n):

            # A valid parentheses substring must end with ')'.
            if s[i] == ")":

                # Case 1:
                # "...()"
                #
                # Example:
                # s = "(()"
                #          i
                #
                # If s[i-1] == '(',
                # we have a new pair "()".
                if s[i - 1] == "(":
                    dp[i] = 2

                    # There may be a valid substring
                    # before this "()", so connect it.
                    if i >= 2:
                        dp[i] += dp[i - 2]

                # Case 2:
                # "...))"
                #
                # We need to find the '(' that matches
                # the current ')'.
                elif s[i - 1] == ")":

                    # dp[i-1] is the length of the valid
                    # parentheses substring immediately
                    # before i.
                    prev_len = dp[i - 1]

                    # Index of the character just before
                    # that valid substring.
                    open_idx = i - prev_len - 1

                    # Make sure that index is valid
                    # and that it is '('.
                    if open_idx >= 0 and s[open_idx] == "(":

                        # Add the matching pair "()".
                        dp[i] = dp[i - 1] + 2

                        # There may also be another valid
                        # substring before the matching '('.
                        if open_idx >= 1:
                            dp[i] += dp[open_idx - 1]

            max_len = max(max_len, dp[i])

        return max_len


# V0-6
# IDEA: 2 SCAN (left -> right, right -> left) (gemini)
class Solution(object):

  def longestValidParentheses(self, s):
    """:type s: str :rtype: int"""
    if not s:
      return 0

    left = right = 0
    max_len = 0

    # 第一遍：從左到右掃描
    for char in s:
      if char == '(':
        left += 1
      else:
        right += 1

      if left == right:
        max_len = max(max_len, 2 * right)
      elif right > left:  # 右括號過多，重置
        left = right = 0

    left = right = 0

    # 第二遍：從右到左掃描
    for char in reversed(s):
      if char == '(':
        left += 1
      else:
        right += 1

      if left == right:
        max_len = max(max_len, 2 * left)
      elif left > right:  # 左括號過多，重置
        left = right = 0

    return max_len


# V0-7
# Idea 1: Brute force (TLE) (gpt)
class Solution(object):

    def longestValidParentheses(self, s):
        """
        :type s: str
        :rtype: int
        """

        # Edge case
        if not s or len(s) <= 1:
            return 0

        n = len(s)
        max_len = 0

        # Try every substring s[j:i+1].
        for i in range(n):
            for j in range(i):

                # A valid parentheses string must have even length.
                cur_len = i - j + 1

                if cur_len % 2 != 0:
                    continue

                if self.helper(s[j:i + 1]):
                    max_len = max(max_len, cur_len)

        return max_len

    def helper(self, s):
        """
        Return True if the whole string is valid parentheses.
        """

        if not s:
            return False

        st = []

        for x in s:

            if x == "(":
                st.append(x)

            else:
                # No '(' available to match this ')'.
                if not st:
                    return False

                st.pop()

        # All '(' must be matched.
        return len(st) == 0


# V0-8
# Idea 1: Brute force (TLE) (GEMINI)
class Solution(object):

  def longestValidParentheses(self, s):
    """:type s: str :rtype: int"""
    n = len(s)
    max_len = 0

    # 1. 窮舉所有偶數長度的子字串區間 [i, j)
    for i in range(n):
      for j in range(i + 2, n + 1, 2):  # 合法括號字串長度必定為偶數
        if self.is_valid(s[i:j]):
          max_len = max(max_len, j - i)

    return max_len

  def is_valid(self, sub):
    balance = 0
    for char in sub:
      if char == '(':
        balance += 1
      else:
        balance -= 1
      # 過程中右括號比左括號多，立刻判定無效
      if balance < 0:
        return False
    # 最終必須剛好平衡
    return balance == 0


# V1
# IDEA : STACK
# https://leetcode.com/problems/longest-valid-parentheses/discuss/582820/Java-and-Python-using-stack
# time = O(n)
# space = O(n)
class Solution(object):
    def longestValidParentheses(self, s):
            stack = []
            ls = len(s)
            ml = 0
            last = -1
            for i in range(ls):
                if s[i]=="(":
                    stack.append(i)
                elif s[i] == ")":
                    if stack == []:
                        last = i
                    else:
                        stack.pop()
                        if stack ==[]:
                            ml = max(ml,i - last)
                        else:
                            ml = max(ml, i - stack[len(stack)-1])

            return ml


# V2
# IDEA: 1D DP (gpt)
"""
DP def:

    dp[i] = 以 s[i] 結尾的最長 valid parentheses 長度


DP eq:


"""
class Solution(object):
    def longestValidParentheses(self, s):
        """
        :type s: str
        :rtype: int
        """
        # edge
        if not s or len(s) <= 1:
            return 0

        n = len(s)

        # dp[i] = longest valid parentheses
        #         ending at index i
        dp = [0] * n

        res = 0

        for i in range(1, n):
            # Current character must be ')'
            if s[i] == ')':

                # Case 1:
                # "()"
                if s[i - 1] == '(':
                    dp[i] = 2

                    if i >= 2:
                        dp[i] += dp[i - 2]

                # Case 2:
                # "...))"
                else:
                    # Find the character that may match s[i]
                    prev = i - dp[i - 1] - 1

                    if prev >= 0 and s[prev] == '(':
                        dp[i] = dp[i - 1] + 2

                        if prev >= 1:
                            dp[i] += dp[prev - 1]

            res = max(res, dp[i])

        return res


# V3
# IDEA: STACK (gemini)
class Solution(object):

    def longestValidParentheses(self, s):
        """
        :type s: str
        :rtype: int
        """
        if not s:
            return 0

        # 初始化堆疊放入一個基底 -1，用來計算從字串開頭起算的長度
        stack = [-1]
        max_len = 0

        for i, char in enumerate(s):
            if char == "(":
                stack.append(i)
            else:
                stack.pop()
                if not stack:
                    # 若堆疊空了，代表當前的 ')' 是多餘的，將其當作新的基準點
                    stack.append(i)
                else:
                    # 計算當前有效括號長度：當前索引減去堆疊頂端的起始基準
                    max_len = max(max_len, i - stack[-1])

        return max_len


# V1'
# IDEA : STACK
# https://leetcode.com/problems/longest-valid-parentheses/discuss/14180/Python-Stack-Solution
# time = O(n)
# space = O(n)
class Solution(object):
    def longestValidParentheses(self, s):
        stack = []
        for i in range(len(s)):
            if s[i] == '(':
                stack.append(i)
            elif stack and s[stack[-1]] == '(':
                stack.pop()
            else:
                stack.append(i)
        stack = [-1] + stack + [len(s)]
        ans = 0
        for i in range(len(stack)-1):
            ans = max(ans, stack[i+1]-stack[i]-1)
        return ans

# V1''
# IDEA : STACK
# https://leetcode.com/problems/longest-valid-parentheses/discuss/1503685/Python-or-Stack
# time = O(n)
# space = O(n)
class Solution(object):
    def longestValidParentheses(self, s):

        if s =="":
            return 0
        max_ = 0
        stck = []
        stck.append(-1)
        
        for i in range(len(s)):
            if s[i]=="(":
                stck.append(i)
            if s[i]==")":
                if len(stck)==1:
                    stck.pop()
                    stck.append(i)
                    continue
                stck.pop()
                max_ = max(max_, i - stck[-1])
        return max_

# V1'''
# IDEA : STACK
# https://leetcode.com/problems/longest-valid-parentheses/discuss/1139974/PythonGo-O(n)-by-stack-w-Comment
# time = O(n)
# space = O(n)
class Solution:
    def longestValidParentheses(self, s):

        # stack, used to record index of parenthesis
        # initialized to -1 as dummy head for valid parentheses length computation
        stack = [-1]
        
        max_length = 0
        
		# linear scan each index and character in input string s
        for cur_idx, char in enumerate(s):
            
            if char == '(':
                
                # push when current char is (
                stack.append( cur_idx )
                
            else:
                
                # pop when current char is )
                stack.pop()
                
                if not stack:
                    
                    # stack is empty, push current index into stack
                    stack.append( cur_idx )
                else:
                    # stack is non-empty, update maximal valid parentheses length
                    max_length = max(max_length, cur_idx - stack[-1])
                
        return max_length

# V1''''
# IDEA : DEQUE
# https://leetcode.com/problems/longest-valid-parentheses/discuss/14186/Python-solution-with-detailed-explanation
# time = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def longestValidParentheses(self, s):
        """
        :type s: str
        :rtype: int
        """
        if len(s) == 0:
            return 0
        st, max_run = deque(), 0
        for idx, x in enumerate(s):
            if x == ")" and len(st) > 0 and s[st[-1]] == "(":
                st.pop()
            else:
                st.append(idx)
        st.appendleft(-1)
        st.append(len(s))
        for i in range(1, len(st)):
            max_run = max(max_run, st[i]-st[i-1]-1)
        return max_run

# V1'''''
# IDEA : DP
# https://leetcode.com/problems/longest-valid-parentheses/discuss/350422/Simple-Python-DP-solution
# time = O(n)
# space = O(n)
class Solution(object):
    def longestValidParentheses(self, s):
        if not s:
            return 0
        ending_here = len(s) * [0]
        res, x = 0, 0
        for i in range(len(s)):
            if s[i] == '(':
                x += 1
            else:
                if x > 0:
                    x -= 1
                    j = i - 1 - ending_here[i-1]
                    if s[j] == '(':
                        ending_here[i] = ending_here[i-1] + 2
                        if j-1 >= 0:
                            ending_here[i] += ending_here[j-1]
                        res = max(res, ending_here[i])
        return res


# V2
