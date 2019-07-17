# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79137225
# IDEA : LINEAR SEARCH 
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
# Time:  O(logn)
# Space: O(1)
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
