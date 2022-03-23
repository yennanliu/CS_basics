"""

1963. Minimum Number of Swaps to Make the String Balanced
Medium

You are given a 0-indexed string s of even length n. The string consists of exactly n / 2 opening brackets '[' and n / 2 closing brackets ']'.

A string is called balanced if and only if:

It is the empty string, or
It can be written as AB, where both A and B are balanced strings, or
It can be written as [C], where C is a balanced string.
You may swap the brackets at any two indices any number of times.

Return the minimum number of swaps to make s balanced.

 

Example 1:

Input: s = "][]["
Output: 1
Explanation: You can make the string balanced by swapping index 0 with index 3.
The resulting string is "[[]]".
Example 2:

Input: s = "]]][[["
Output: 2
Explanation: You can do the following to make the string balanced:
- Swap index 0 with index 4. s = "[]][][".
- Swap index 1 with index 5. s = "[[][]]".
The resulting string is "[[][]]".
Example 3:

Input: s = "[]"
Output: 0
Explanation: The string is already balanced.
 

Constraints:

n == s.length
2 <= n <= 106
n is even.
s[i] is either '[' or ']'.
The number of opening brackets '[' equals n / 2, and the number of closing brackets ']' equals n / 2.

"""

# V0

# V1
# IDEA : greedy
# https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/discuss/1390249/JavaPython-3-Space-O(1)-time-O(n)-codes-w-brief-explanation.
# IDEA :
# Traverse the input s, remove all matched []'s. Since the number of ['s and ]'s are same, the remaining must be ]]]...[[[. Otherwise, if left half had any [, there would be at least a matched [] among them.
#
# For every 2 pairs of square brackets, a swap will make them matched;
# If only 1 pair not matched, we need a swap.
# Therefore, we need at least (pairs + 1) / 2 swaps.
class Solution:
    def minSwaps(self, s: str) -> int:
            stk = []
            for c in s:
                if stk and c == ']':
                    stk.pop()
                elif c == '[':
                    stk.append(c)
            return (len(stk) + 1) // 2

# V1'
# IDEA : greedy
# https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/discuss/1390249/JavaPython-3-Space-O(1)-time-O(n)-codes-w-brief-explanation.
# IDEA :
# There are only 2 types of characters in s, so we can get rid of stack in above codes and use two counters, openBracket and closeBracket, to memorize the unmatched open and close square brackets: - Credit to @saksham66 for suggestion of variables naming.
class Solution:
    def minSwaps(self, s: str) -> int:
            open_bracket = close_bracket = 0
            for c in s:
                if open_bracket > 0 and c == ']':
                    open_bracket -= 1
                elif c == '[':
                    open_bracket += 1
                else:
                    close_bracket += 1    
            return (open_bracket + 1) // 2

# V1''
# IDEA : greedy
# https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/discuss/1390249/JavaPython-3-Space-O(1)-time-O(n)-codes-w-brief-explanation.
# IDEA :
# Since the occurrences of ['s and ]'s are same, we actually only need to count openBracket, and closeBracket is redundant. Credit to @fishballLin
def minSwaps(self, s: str) -> int:
        open_bracket = 0
        for c in s:
            if open_bracket > 0 and c == ']':
                open_bracket -= 1
            elif c == '[':
                open_bracket += 1
        return (open_bracket + 1) // 2

# V1'''
# IDEA : GREEDY
# https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/discuss/1390193/Python-Greedy-O(n)-explained
# IDEA :
# The idea is to traverse our string and check the biggest possible disbalance. For example if we look at ]]][]]][[[[[, for traversals we have:
# -1, -2, -3, -2, -3, -4, -5, -4, -3, -2, -1, 0 and the biggest disbalance is equal to 5. What we can do in one swap of brackets is to decreas it at most by 2: for this we need to take the leftest ] with negative balance and the rightest [ with negative balance and change them. For example in our case we have:
#
# []][]]][[[[] : [1, 0, -1, 0, -1, -2, -3, -2, -1, 0, 1, 0]
#
# [][[]]][][[] : [1, 0, 1, 2, 1, 0, -1, 0, -1, 0, 1, 0]
#
# [][[]][[]][] : [1, 0, 1, 2, 1, 0, 1, 2, 1, 0, 1, 0]
#
# Complexity
# Time complexity is O(n) to traverse our string, space is O(1).
class Solution:
    def minSwaps(self, s):
        bal, ans = 0, 0
        for symb in s:
            if symb == "[": bal += 1
            else: bal -= 1
            ans = min(bal, ans)
        return (-ans + 1)//2

# V1''''
# https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/discuss/1393727/Golang-and-Python-Solutions
class Solution:
    def minSwaps(self, s: str) -> int:
        extraClose, close = 0, 0
        for char in s:
            if char == "[":
                close -= 1
            else:
                close += 1
            extraClose = max(close, extraClose)
        return (extraClose + 1) // 2

# V1''''
# IDEA : TWO POINTERS
# https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/discuss/1444539/Python3-solution-or-O(n)-or-explained-pattern
class Solution:
    def minSwaps(self, s: str) -> int:
        l = 0
        r = 0
        for i in range(len(s)):
            if s[i] == ']':
                if l == 0:
                    r += 1
                else:
                    l -= 1
            else:
                l += 1
        if l % 2 == 0:
            res = l // 2
        else:
            res = (l+1)//2
        return res

# V1'''''
# https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-string-balanced/discuss/1390394/Easy-Python
class Solution:
    def minSwaps(self, s: str) -> int:
        lks=[]
        for x in s:
            if len(lks)==0:
                lks.append(x)
            elif lks[-1]=='[' and x==']':
                lks.pop()
            else:
                lks.append(x)
        k=len(lks)//2 + 1
        return k//2

# V2