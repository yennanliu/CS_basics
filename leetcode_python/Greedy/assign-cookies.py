"""

455. Assign Cookies
Easy

Assume you are an awesome parent and want to give your children some cookies. But, you should give each child at most one cookie.

Each child i has a greed factor g[i], which is the minimum size of a cookie that the child will be content with; and each cookie j has a size s[j]. If s[j] >= g[i], we can assign the cookie j to the child i, and the child i will be content. Your goal is to maximize the number of your content children and output the maximum number.

Example 1:

Input: g = [1,2,3], s = [1,1]
Output: 1
Explanation: You have 3 children and 2 cookies. The greed factors of 3 children are 1, 2, 3.
And even though you have 2 cookies, since their size is both 1, you could only make the child whose greed factor is 1 content.
You need to output 1.

Example 2:

Input: g = [1,2], s = [1,2,3]
Output: 2
Explanation: You have 2 children and 3 cookies. The greed factors of 2 children are 1, 2.
You have 3 cookies and their sizes are big enough to gratify all of the children,
You need to output 2.

Constraints:

1 <= g.length <= 3 * 10^4
0 <= s.length <= 3 * 10^4
1 <= g[i], s[j] <= 2^31 - 1

Note: This question is the same as  2410: Maximum Matching of Players With Trainers.

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/54177627
# IDEA : GREEDY 
# sort two list firstly, then for loop greed list 
# and move both index -> index + 1 if cookie > greed
# only move greed's index -> index + 1 if cookie < greed
# time = O(n log n)
# space = O(1)
class Solution:
    def findContentChildren(self, g, s):
        """
        :type g: List[int]
        :type s: List[int]
        :rtype: int
        """
        g.sort() # g : greed
        s.sort() # s : size  (cookie)
        sp = 0
        res = 0
        for gi in g:
            while sp < len(s) and s[sp] < gi: # if cookie < greed -> s.index += 1 
                sp += 1
            if sp < len(s) and s[sp] >= gi:   # if cookie > greed ->  g.index += 1 and s.index += 1 
                res += 1
                sp += 1
        return res

# V2
# time = O(n log n)
# space = O(1)
class Solution(object):
    def findContentChildren(self, g, s):
        """
        :type g: List[int]
        :type s: List[int]
        :rtype: int
        """
        g.sort()
        s.sort()

        result, i = 0, 0
        for j in range(len(s)):
            if i == len(g):
                break
            if s[j] >= g[i]:
                result += 1
                i += 1
        return result
