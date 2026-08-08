"""

953. Verifying an Alien Dictionary
Solved
Easy
Topics
premium lock icon
Companies
In an alien language, surprisingly, they also use English lowercase letters, but possibly in a different order. The order of the alphabet is some permutation of lowercase letters.

Given a sequence of words written in the alien language, and the order of the alphabet, return true if and only if the given words are sorted lexicographically in this alien language.

 

Example 1:

Input: words = ["hello","leetcode"], order = "hlabcdefgijkmnopqrstuvwxyz"
Output: true
Explanation: As 'h' comes before 'l' in this language, then the sequence is sorted.
Example 2:

Input: words = ["word","world","row"], order = "worldabcefghijkmnpqstuvxyz"
Output: false
Explanation: As 'd' comes after 'l' in this language, then words[0] > words[1], hence the sequence is unsorted.
Example 3:

Input: words = ["apple","app"], order = "abcdefghijklmnopqrstuvwxyz"
Output: false
Explanation: The first three characters "app" match, and the second string is shorter (in size.) According to lexicographical rules "apple" > "app", because 'l' > '∅', where '∅' is defined as the blank character which is less than any other character (More info).
 

Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 20
order.length == 26
All characters in words[i] and order are English lowercase letters.
 


"""


# V0
class Solution(object):
    def isAlienSorted(self, words, order):
        """
        :type words: List[str]
        :type order: str
        :rtype: bool
        """
        pass


# V0-1
# IDEA: ordering MAPPING (gemini)
class Solution(object):
    def isAlienSorted(self, words, order):
        """
        :type words: List[str]
        :type order: str
        :rtype: bool
        """
        # 1. Build the priority mapping
        order_arr = [0] * 26
        for i in range(len(order)):
            # Use ord() to get ASCII integer values in Python
            order_arr[ord(order[i]) - ord('a')] = i

        # 2. Compare adjacent words
        for i in range(1, len(words)):
            prev = words[i-1]
            cur = words[i]
            
            _len = min(len(prev), len(cur))
            
            # 3. FIX: Use 'j' so we don't overwrite 'i'
            for j in range(_len):
                p_val = prev[j]
                c_val = cur[j]
                
                # Compare their alien dictionary index
                if order_arr[ord(p_val) - ord('a')] > order_arr[ord(c_val) - ord('a')]:
                    # Out of order! 
                    return False
                    
                elif order_arr[ord(p_val) - ord('a')] < order_arr[ord(c_val) - ord('a')]:
                    # They are in the correct order. Stop comparing this pair.
                    break
            
            # 4. FIX: The 'for-else' block in Python
            # The 'else' attached to a 'for' loop ONLY runs if the loop 
            # finished naturally without hitting a 'break'.
            # This perfectly handles the prefix edge case (e.g., "apple" vs "app")
            else:
                if len(prev) > len(cur):
                    return False

        return True


# V0-2

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/84924672
# https://blog.csdn.net/GQxxxxxl/article/details/84927823
# time = O(n * l), l is the average length of words
# space = O(1)
class Solution:
    def isAlienSorted(self, words, order):
        """
        :type words: List[str]
        :type order: str
        :rtype: bool
        """

        for i in range(len(words)-1):
            for j in range(len(words[i])):
                if j>len(words[i+1])-1:
                    return(False)
                elif order.index(words[i][j])<order.index(words[i+1][j]):
                    break
                elif order.index(words[i][j])==order.index(words[i+1][j]):
                    continue
                else:
                    return(False)
        return(True)
        
# V2
# time = O(n * l), l is the average length of words
# space = O(1)
class Solution(object):
    def isAlienSorted(self, words, order):
        """
        :type words: List[str]
        :type order: str
        :rtype: bool
        """
        lookup = {c: i for i, c in enumerate(order)}
        for i in range(len(words)-1):
            word1 = words[i]
            word2 = words[i+1]
            for k in range(min(len(word1), len(word2))):
                if word1[k] != word2[k]:
                    if lookup[word1[k]] > lookup[word2[k]]:
                        return False
                    break
            else:
                if len(word1) > len(word2):
                    return False
        return True
