# Time:  O(m + n)
# Space: O(n)
# You're given strings J representing the types of stones that are jewels,
# and S representing the stones you have.
# Each character in S is a type of stone you have.
# You want to know how many of the stones you have are also jewels.
#
# The letters in J are guaranteed distinct, and all characters in J and S are letters.
# Letters are case sensitive, so "a" is considered a different type of stone from "A".
#
# Example 1:
# Input: J = "aA", S = "aAAbbbb"
# Output: 3
# Example 2:
#
# Input: J = "z", S = "ZZ"
# Output: 0
#
# Note:
# - S and J will consist of letters and have length at most 50.
# - The characters in J are distinct.

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
