"""

1839. Longest Substring Of All Vowels in Order

A string is considered beautiful if it satisfies the following conditions:

Each of the 5 English vowels ('a', 'e', 'i', 'o', 'u') must appear at least once in it.
The letters must be sorted in alphabetical order (i.e. all 'a's before 'e's, all 'e's before 'i's, etc.).
For example, strings "aeiou" and "aaaaaaeiiiioou" are considered beautiful, but "uaeio", "aeoiu", and "aaaeeeooo" are not beautiful.

Given a string word consisting of English vowels, return the length of the longest beautiful substring of word. If no such substring exists, return 0.

A substring is a contiguous sequence of characters in a string.

 

Example 1:

Input: word = "aeiaaioaaaaeiiiiouuuooaauuaeiu"
Output: 13
Explanation: The longest beautiful substring in word is "aaaaeiiiiouuu" of length 13.
Example 2:

Input: word = "aeeeiiiioooauuuaeiou"
Output: 5
Explanation: The longest beautiful substring in word is "aeiou" of length 5.
Example 3:

Input: word = "a"
Output: 0
Explanation: There is no beautiful substring, so return 0.
 

Constraints:

1 <= word.length <= 5 * 105
word consists of characters 'a', 'e', 'i', 'o', and 'u'.

"""


# V0
class Solution(object):
    def longestBeautifulSubstring(self, word):
        """
        :type word: str
        :rtype: int
        """
        


# V1
# IDEA: SLIDE WINDOW
# time = O(n)  # n = len(word); single pass
# space = O(1)
class Solution(object):
    def longestBeautifulSubstring(self, word):

        # Longest beautiful substring found so far
        max_len = 0

        # Current substring length
        curr_len = 1

        # Number of distinct vowels encountered
        # in increasing order
        distinct = 1

        # Start from the second character
        for i in range(1, len(word)):

            # Case 1:
            # We moved to a larger vowel
            # Example: a -> e, e -> i, i -> o, o -> u
            if word[i] > word[i - 1]:
                distinct += 1
                curr_len += 1

            # Case 2:
            # Same vowel
            # Example: a -> a
            elif word[i] == word[i - 1]:
                curr_len += 1

            # Case 3:
            # Order is broken
            # Example: o -> a
            else:
                curr_len = 1
                distinct = 1

            # Beautiful substring must contain:
            # a, e, i, o, u
            if distinct == 5:
                max_len = max(max_len, curr_len)

        return max_len





# V2
# IDEA: SLIDE WINDOW
# time = O(n)  # n = len(word); single pass
# space = O(1)
class Solution(object):
    def longestBeautifulSubstring(self, word):
        """
        :type word: str
        :rtype: int
        """
        if len(word) < 5:
            return 0
            
        max_len = 0
        current_len = 1
        unique_vowels = 1
        
        # Start scanning from the second character (index 1)
        for i in range(1, len(word)):
            # Rule 1: Alphabetical Order check
            # If the current character is greater than or equal to the previous one,
            # our valid beautiful streak continues!
            if word[i] >= word[i - 1]:
                current_len += 1
                # If it's a strictly greater character, we've discovered a new vowel type
                if word[i] > word[i - 1]:
                    unique_vowels += 1
            
            # Rule 2: Order Broken!
            # If the current character is smaller than the previous one (e.g., "e" followed by "a"),
            # the current streak is dead. Reset the window.
            else:
                current_len = 1
                unique_vowels = 1
                
            # If our current streak contains all 5 unique vowels, check if it's a new record
            if unique_vowels == 5:
                max_len = max(max_len, current_len)
                
        return max_len


