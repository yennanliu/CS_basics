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