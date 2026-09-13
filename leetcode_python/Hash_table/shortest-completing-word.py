"""

748. Shortest Completing Word
Easy

Given a string licensePlate and an array of strings words, find the shortest completing word in words.

A completing word is a word that contains all the letters in licensePlate. Ignore numbers and spaces in licensePlate, and treat letters as case insensitive. If a letter appears more than once in licensePlate, then it must appear in the word the same number of times or more.

For example, if licensePlate = "aBc 12c", then it contains letters 'a', 'b' (ignoring case), and 'c' twice. Possible completing words are "abccdef", "caaacab", and "cbca".

Return the shortest completing word in words. It is guaranteed an answer exists. If there are multiple shortest completing words, return the first one that occurs in words.

Example 1:

Input: licensePlate = "1s3 PSt", words = ["step","steps","stripe","stepple"]
Output: "steps"
Explanation: licensePlate contains letters 's', 'p', 's' (ignoring case), and 't'.
"step" contains 't' and 'p', but only contains 1 's'.
"steps" contains 't', 'p', and both 's' characters.
"stripe" is missing an 's'.
"stepple" is missing an 's'.
Since "steps" is the only word containing all the letters, that is the answer.

Example 2:

Input: licensePlate = "1s3 456", words = ["looks","pest","stew","show"]
Output: "pest"
Explanation: licensePlate only contains the letter 's'. All the words contain 's', but among these "pest", "stew", and "show" are shortest. The answer is "pest" because it is the word that appears earliest of the 3.

Constraints:

1 <= licensePlate.length <= 7
licensePlate contains digits, letters (uppercase or lowercase), or space ' '.
1 <= words.length <= 1000
1 <= words[i].length <= 15
words[i] consists of lower case English letters.

"""

# time = O(n)
# space = O(1)

# Find the minimum length word from a given dictionary words,
# which has all the letters from the string licensePlate.
# Such a word is said to complete the given string licensePlate
#
# Here, for letters we ignore case.
# For example, "P" on the licensePlate still matches "p" on the word.
#
# It is guaranteed an answer exists.
# If there are multiple answers, return the one that occurs first in the array.
#
# The license plate might have the same letter occurring multiple times.
# For example, given a licensePlate of "PP",
# the word "pair" does not complete the licensePlate, but the word "supper" does.
#
# Example 1:
# Input: licensePlate = "1s3 PSt", words = ["step", "steps", "stripe", "stepple"]
# Output: "steps"
# Explanation: The smallest length word that contains the letters "S", "P", "S", and "T".
# Note that the answer is not "step", because the letter "s" must occur in the word twice.
# Also note that we ignored case for the purposes of comparing whether a letter exists in the word.
# Example 2:
# Input: licensePlate = "1s3 456", words = ["looks", "pest", "stew", "show"]
# Output: "pest"
# Explanation: There are 3 smallest length words that contains the letters "s".
# We return the one that occurred first.
# Note:
# - licensePlate will be a string with length in range [1, 7].
# - licensePlate will contain digits, spaces, or letters (uppercase or lowercase).
# - words will have a length in the range [10, 1000].
# - Every words[i] will consist of lowercase letters, and have length in range [1, 15].


# V0
# time = O(n)  # n = total chars across words
# space = O(1)
import collections
import re
class Solution(object):
    def shortestCompletingWord(self, licensePlate, words):
        """
        :type licensePlate: str
        :type words: List[str]
        :rtype: str
        """
        clicense = collections.Counter(re.sub('[^a-z]','',licensePlate.lower()))
        ans = '#' * 1111
        for word in words:
            cword = collections.Counter(word)
            if all(clicense[k] <= cword[k] for k in clicense) and len(word) < len(ans):
                ans = word
        return ans

# V1
# http://bookshadow.com/weblog/2017/12/17/leetcode-shortest-completing-word/
# time = O(n)  # n = total chars across words
# space = O(1)
import collections
import re 
class Solution(object):
    def shortestCompletingWord(self, licensePlate, words):
        """
        :type licensePlate: str
        :type words: List[str]
        :rtype: str
        """
        clicense = collections.Counter(re.sub('[^a-z]','',licensePlate.lower()))
        ans = '#' * 1111
        for word in words:
            cword = collections.Counter(word)
            if all(clicense[k] <= cword[k] for k in clicense) and len(word) < len(ans):
                ans = word
        return ans

# V1'
# https://www.jiuzhang.com/solution/shortest-completing-word/#tag-highlight-lang-python
# time = O(n)  # n = total chars across words
# space = O(1)
import collections
class Solution:
    """
    @param licensePlate: a string
    @param words: List[str]
    @return: return a string
    """
    def shortestCompletingWord(self, licensePlate, words):
        # write your code here
        ans = ""
        d = collections.defaultdict(int)
        for c in licensePlate:
            if c.isalpha():
                d[c.lower()] += 1
        for w in words:
            for k, v in d.items():
                if w.count(k) < v:
                    break
            else:
                if not ans:
                    ans = w
                elif len(w) < len(ans):
                    ans = w
        return ans

# V2
# time = O(n)  # n = total chars across words
# space = O(1)
import collections
class Solution(object):
    def shortestCompletingWord(self, licensePlate, words):
        """
        :type licensePlate: str
        :type words: List[str]
        :rtype: str
        """
        def contains(counter1, w2):
            c2 = collections.Counter(w2.lower())
            c2.subtract(counter1)
            return all([x >= 0 for x in list(c2.values())])
        result = None
        counter = collections.Counter(c.lower() for c in licensePlate if c.isalpha())
        for word in words:
            if (result is None or (len(word) < len(result))) and \
               contains(counter, word):
                result = word
        return result