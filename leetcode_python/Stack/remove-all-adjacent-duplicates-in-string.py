"""

1047. Remove All Adjacent Duplicates In String
Easy

2997

You are given a string s consisting of lowercase English letters. A duplicate removal consists of choosing two adjacent and equal letters and removing them.

We repeatedly make duplicate removals on s until we no longer can.

Return the final string after all such duplicate removals have been made. It can be proven that the answer is unique.

 

Example 1:

Input: s = "abbaca"
Output: "ca"
Explanation: 
For example, in "abbaca" we could remove "bb" since the letters are adjacent and equal, and this is the only possible move.  The result of this move is that the string is "aaca", of which only "aa" is possible, so the final string is "ca".
Example 2:

Input: s = "azxxzy"
Output: "ay"
 

Constraints:

1 <= s.length <= 105
s consists of lowercase English letters.

"""

# V0

# V1
# IDEA : STACK
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/discuss/294893/JavaC%2B%2BPython-Two-Pointers-and-Stack-Solution
class Solution:
    def removeDuplicates(self, S):
            res = []
            for c in S:
                if res and res[-1] == c:
                    res.pop()
                else:
                    res.append(c)
            return "".join(res)

# V1'
# IDEA : TWO POINTERS
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/discuss/294964/JavaPython-3-three-easy-iterative-codes-w-brief-explanation-analysis-and-follow-up.
class Solution:
     def removeDuplicates(self, S: str) -> str:
            end, a = -1, list(S)
            for c in a:
                if end >= 0 and a[end] == c:
                    end -= 1
                else:
                    end += 1
                    a[end] = c
            return ''.join(a[: end + 1])

# V1''
# IDEA : REPLACE
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/solution/
from string import ascii_lowercase
class Solution:
    def removeDuplicates(self, S: str) -> str:
        # generate 26 possible duplicates
        duplicates = [2 * ch for ch in ascii_lowercase]
        
        prev_length = -1
        while prev_length != len(S):
            prev_length = len(S)
            for d in duplicates:
                S = S.replace(d, '')
                
        return S

# V1'''
# IDEA : STACK
# https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/solution/
class Solution:
    def removeDuplicates(self, S: str) -> str:
        output = []
        for ch in S:
            if output and ch == output[-1]: 
                output.pop()
            else: 
                output.append(ch)
        return ''.join(output)

# V2