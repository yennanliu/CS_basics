# https://leetcode.com/problems/string-without-aaa-or-bbb/description/

"""

984. String Without AAA or BBB
Solved
Medium
Topics
premium lock icon
Companies
Given two integers a and b, return any string s such that:

s has length a + b and contains exactly a 'a' letters, and exactly b 'b' letters,
The substring 'aaa' does not occur in s, and
The substring 'bbb' does not occur in s.
 

Example 1:

Input: a = 1, b = 2
Output: "abb"
Explanation: "abb", "bab" and "bba" are all correct answers.
Example 2:

Input: a = 4, b = 1
Output: "aabaa"
 

Constraints:

0 <= a, b <= 100
It is guaranteed such an s exists for the given a and b.


"""

# V0
class Solution(object):
    def strWithout3a3b(self, a, b):
    	pass


# V1-1
# IDEA: GREEDY (GPT)
class Solution(object):
    def strWithout3a3b(self, a, b):
        ans = []

        while a > 0 or b > 0:

            # Last two chars are "aa"
            if len(ans) >= 2 and ans[-1] == ans[-2] == 'a':
                ans.append('b')
                b -= 1

            # Last two chars are "bb"
            elif len(ans) >= 2 and ans[-1] == ans[-2] == 'b':
                ans.append('a')
                a -= 1

            # Prefer the character with more remaining count
            elif a >= b and a > 0:
                ans.append('a')
                a -= 1

            elif b > 0:
                ans.append('b')
                b -= 1

        return "".join(ans)



# V1-2
# IDEA: GREEDY (GPT)
class Solution(object):
    def strWithout3a3b(self, a, b):
        """
        :type a: int
        :type b: int
        :rtype: str
        """
        ans = []
        
        # Keep looping until BOTH a and b are fully used up
        while a > 0 or b > 0:
            write_a = False
            
            # Check the last two characters to see if we are forced to write 'a' or 'b'
            if len(ans) >= 2 and ans[-1] == ans[-2]:
                if ans[-1] == 'b':
                    write_a = True  # Forced to write 'a' because we have "bb"
                else:
                    write_a = False # Forced to write 'b' because we have "aa"
            else:
                # No danger of 3-in-a-row! Greedily pick the character with more remaining counts
                if a >= b:
                    write_a = True
                else:
                    write_a = False
            
            # Append the chosen character and decrement its count
            if write_a:
                ans.append('a')
                a -= 1
            else:
                ans.append('b')
                b -= 1
                
        return "".join(ans)



# V2
# IDEA: GREEDY
# https://leetcode.com/problems/string-without-aaa-or-bbb/editorial/
class Solution(object):
    def strWithout3a3b(self, A, B):
        ans = []

        while A or B:
            if len(ans) >= 2 and ans[-1] == ans[-2]:
                writeA = ans[-1] == 'b'
            else:
                writeA = A >= B

            if writeA:
                A -= 1
                ans.append('a')
            else:
                B -= 1
                ans.append('b')

        return "".join(ans)

