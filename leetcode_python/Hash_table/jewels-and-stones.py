"""

771. Jewels and Stones
Easy

You're given strings jewels representing the types of stones that are jewels, and stones representing the stones you have. Each character in stones is a type of stone you have. You want to know how many of the stones you have are also jewels.

Letters are case sensitive, so "a" is considered a different type of stone from "A".

 

Example 1:

Input: jewels = "aA", stones = "aAAbbbb"
Output: 3
Example 2:

Input: jewels = "z", stones = "ZZ"
Output: 0
 

Constraints:

1 <= jewels.length, stones.length <= 50
jewels and stones consist of only English letters.
All the characters of jewels are unique.

"""


# V0
class Solution(object):
    def numJewelsInStones(self, J, S):
        return len([ i for i in S if i in J])

# V0'
class Solution(object):
    def numJewelsInStones(self, J, S):
        lookup = set(J)
        return sum(s in lookup for s in S)

# V1 
# http://bookshadow.com/weblog/2018/01/28/leetcode-jewels-and-stones/
# IDEA : GREEDY 
class Solution(object):
    def numJewelsInStones(self, J, S):
        """
        :type J: str
        :type S: str
        :rtype: int
        """
        return sum(s in J for s in S)
        
# V1'   
class Solution(object):
    def numJewelsInStones(self, J, S):
        output = []
        J_list = list(J)
        for i in S:
            if i in J_list:
                output.append(i)
            else:
                pass
        return len(output)

# V2 
class Solution(object):
    def numJewelsInStones(self, J, S):
        """
        :type J: str
        :type S: str
        :rtype: int
        """
        lookup = set(J)
        return sum(s in lookup for s in S)
