"""

696. Count Binary Substrings
Easy

Give a binary string s, return the number of non-empty substrings that have the same number of 0's and 1's, and all the 0's and all the 1's in these substrings are grouped consecutively.

Substrings that occur multiple times are counted the number of times they occur.

 

Example 1:

Input: s = "00110011"
Output: 6
Explanation: There are 6 substrings that have equal number of consecutive 1's and 0's: "0011", "01", "1100", "10", "0011", and "01".
Notice that some of these substrings repeat and are counted the number of times they occur.
Also, "00110011" is not a valid substring because all the 0's (and 1's) are not grouped together.
Example 2:

Input: s = "10101"
Output: 4
Explanation: There are 4 substrings: "10", "01", "10", "01" that have equal number of consecutive 1's and 0's.
 

Constraints:

1 <= s.length <= 105
s[i] is either '0' or '1'.
Accepted
132.8K
Submissions
206.2K

"""

# V0 
# IDEA :  Group By Character
# https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1

        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V0'
# IDEA : LINEAR SCAN
# # https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        ans, prev, cur = 0, 0, 1
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                ans += min(prev, cur)
                prev, cur = cur, 1
            else:
                cur += 1

        return ans + min(prev, cur)

# V0''
# IDEA : BRUTE FORCE (TLE)
# class Solution(object):
#     def countBinarySubstrings(self, s):
#         def check(x):
#             #print ("x = " + str(x))
#             _mid = len(x) // 2
#             if ("0" * _mid + "1" * _mid) == x or ("1" * _mid + "0" * _mid) == x:
#                 return True
#             else:
#                 return False  
#         # edge case
#         if not s:
#             return 0
#         #res = 0
#         res = []
#         for i in range(len(s)):
#             for j in range(i+1, len(s)):
#                 print ("s[i:j+1] = " + str(s[i:j+1]))
#                 if (j - i + 1) % 2 == 0:
#                     if check(s[i:j+1]):
#                         res.append(s[i:j+1])
#                         break
#         return res

# V1
# IDEA :  Group By Character
# https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1

        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V1'
# IDEA : Group By Character (Alternate Implentation as above (same idea))
# https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [len(list(v)) for _, v in itertools.groupby(s)]
        return sum(min(a, b) for a, b in zip(groups, groups[1:]))

# V1''
# IDEA :  Linear Scan
# https://leetcode.com/problems/count-binary-substrings/solution/
class Solution(object):
    def countBinarySubstrings(self, s):
        ans, prev, cur = 0, 0, 1
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                ans += min(prev, cur)
                prev, cur = cur, 1
            else:
                cur += 1

        return ans + min(prev, cur)

# V1'''
# https://leetcode.com/problems/count-binary-substrings/discuss/176153/Python-solution
class Solution(object):
    def countBinarySubstrings(self, s):
        res = 0
        prev = 0
        tmp = 1
        for i in range(1, len(s)):
            if s[i] != s[i-1]:
                res += min(prev, tmp)
                prev = tmp
                tmp = 1
            else:
                tmp += 1
        res += min(prev, tmp)
        return res

# V1''''  
# https://blog.csdn.net/wenqiwenqi123/article/details/78462141
# https://blog.csdn.net/fuxuemingzhu/article/details/79183556
class Solution(object):
    def countBinarySubstrings(self, s):
        groups = [1]
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                groups.append(1)
            else:
                groups[-1] += 1
 
        ans = 0
        for i in range(1, len(groups)):
            ans += min(groups[i-1], groups[i])
        return ans

# V2 
class Solution(object):
    def countBinarySubstrings(self, s):
        result, prev, curr = 0, 0, 1
        for i in range(1, len(s)):
            if s[i-1] != s[i]:
                result += min(prev, curr)
                prev, curr = curr, 1
            else:
                curr += 1
        result += min(prev, curr)
        return result