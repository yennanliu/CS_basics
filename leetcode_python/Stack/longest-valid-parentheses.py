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

# V1
# IDEA : STACK
# https://leetcode.com/problems/longest-valid-parentheses/discuss/582820/Java-and-Python-using-stack
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

# V1'
# IDEA : STACK
# https://leetcode.com/problems/longest-valid-parentheses/discuss/14180/Python-Stack-Solution
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

# V1'''''''
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/longest-valid-parentheses/solution/
# JAVA
# public class Solution {
#     public boolean isValid(String s) {
#         Stack<Character> stack = new Stack<Character>();
#         for (int i = 0; i < s.length(); i++) {
#             if (s.charAt(i) == '(') {
#                 stack.push('(');
#             } else if (!stack.empty() && stack.peek() == '(') {
#                 stack.pop();
#             } else {
#                 return false;
#             }
#         }
#         return stack.empty();
#     }
#     public int longestValidParentheses(String s) {
#         int maxlen = 0;
#         for (int i = 0; i < s.length(); i++) {
#             for (int j = i + 2; j <= s.length(); j+=2) {
#                 if (isValid(s.substring(i, j))) {
#                     maxlen = Math.max(maxlen, j - i);
#                 }
#             }
#         }
#         return maxlen;
#     }
# }

# V1''''''''''
# IDEA : DP
# https://leetcode.com/problems/longest-valid-parentheses/solution/
# JAVA
# public class Solution {
#     public int longestValidParentheses(String s) {
#         int maxans = 0;
#         int dp[] = new int[s.length()];
#         for (int i = 1; i < s.length(); i++) {
#             if (s.charAt(i) == ')') {
#                 if (s.charAt(i - 1) == '(') {
#                     dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
#                 } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
#                     dp[i] = dp[i - 1] + ((i - dp[i - 1]) >= 2 ? dp[i - dp[i - 1] - 2] : 0) + 2;
#                 }
#                 maxans = Math.max(maxans, dp[i]);
#             }
#         }
#         return maxans;
#     }
# }

# V1'''''''''''
# IDEA : STACK
# https://leetcode.com/problems/longest-valid-parentheses/solution/
# JAVA
# public class Solution {
#
#     public int longestValidParentheses(String s) {
#         int maxans = 0;
#         Stack<Integer> stack = new Stack<>();
#         stack.push(-1);
#         for (int i = 0; i < s.length(); i++) {
#             if (s.charAt(i) == '(') {
#                 stack.push(i);
#             } else {
#                 stack.pop();
#                 if (stack.empty()) {
#                     stack.push(i);
#                 } else {
#                     maxans = Math.max(maxans, i - stack.peek());
#                 }
#             }
#         }
#         return maxans;
#     }
# }


# V1''''''''''
# IDEA : WITHOUT EXTRA SPACE
# https://leetcode.com/problems/longest-valid-parentheses/solution/
# JAVA
# public class Solution {
#     public int longestValidParentheses(String s) {
#         int left = 0, right = 0, maxlength = 0;
#         for (int i = 0; i < s.length(); i++) {
#             if (s.charAt(i) == '(') {
#                 left++;
#             } else {
#                 right++;
#             }
#             if (left == right) {
#                 maxlength = Math.max(maxlength, 2 * right);
#             } else if (right >= left) {
#                 left = right = 0;
#             }
#         }
#         left = right = 0;
#         for (int i = s.length() - 1; i >= 0; i--) {
#             if (s.charAt(i) == '(') {
#                 left++;
#             } else {
#                 right++;
#             }
#             if (left == right) {
#                 maxlength = Math.max(maxlength, 2 * left);
#             } else if (left >= right) {
#                 left = right = 0;
#             }
#         }
#         return maxlength;
#     }
# }

# V2