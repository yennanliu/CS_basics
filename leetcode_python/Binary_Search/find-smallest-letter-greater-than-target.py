"""

744. Find Smallest Letter Greater Than Target
Easy

You are given an array of characters letters that is sorted in non-decreasing order, and a character target. There are at least two different characters in letters.

Return the smallest character in letters that is lexicographically greater than target. If such a character does not exist, return the first character in letters.

Example 1:

Input: letters = ["c","f","j"], target = "a"
Output: "c"
Explanation: The smallest character that is lexicographically greater than 'a' in letters is 'c'.

Example 2:

Input: letters = ["c","f","j"], target = "c"
Output: "f"
Explanation: The smallest character that is lexicographically greater than 'c' in letters is 'f'.

Example 3:

Input: letters = ["x","x","y","y"], target = "z"
Output: "x"
Explanation: There are no characters in letters that is lexicographically greater than 'z' so we return letters[0].

Constraints:

2 <= letters.length <= 10^4
letters[i] is a lowercase English letter.
letters is sorted in non-decreasing order.
letters contains at least two different characters.
target is a lowercase English letter.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79137225
# IDEA : LINEAR SEARCH
# time = O(n)
# space = O(1)
class Solution(object):
    def nextGreatestLetter(self, letters, target):
        """
        :type letters: List[str]
        :type target: str
        :rtype: str
        """
        for letter in letters:
        # can just do the alphabet ordering check via "<", ">"
        # i.e. 'a' > 'c' ;  'b' < 'd'
            if ord(letter) > ord(target):
                return letter
        return letters[0]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79137225
# IDEA : BINARY SEARCH
# time = O(log n)
# space = O(1)
class Solution(object):
    def nextGreatestLetter(self, letters, target):
        """
        :type letters: List[str]
        :type target: str
        :rtype: str
        """
        index = bisect.bisect_right(letters, target)
        return letters[index % len(letters)]
       
# V2
# time = O(log n)
# space = O(1)
import bisect
class Solution(object):
    def nextGreatestLetter(self, letters, target):
        """
        :type letters: List[str]
        :type target: str
        :rtype: str
        """
        i = bisect.bisect_right(letters, target)
        return letters[0] if i == len(letters) else letters[i]
