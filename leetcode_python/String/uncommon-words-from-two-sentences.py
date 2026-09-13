"""

884. Uncommon Words from Two Sentences
Easy

A sentence is a string of single-space separated words where each word consists only of lowercase letters.

A word is uncommon if it appears exactly once in one of the sentences, and does not appear in the other sentence.

Given two sentences s1 and s2, return a list of all the uncommon words. You may return the answer in any order.

Example 1:

Input: s1 = "this apple is sweet", s2 = "this apple is sour"
Output: ["sweet","sour"]
Explanation:

The word "sweet" appears only in s1, while the word "sour" appears only in s2.

Example 2:

Input: s1 = "apple apple", s2 = "banana"
Output: ["banana"]

Constraints:

1 <= s1.length, s2.length <= 200
s1 and s2 consist of lowercase English letters and spaces.
s1 and s2 do not have leading or trailing spaces.
All the words in s1 and s2 are separated by a single space.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/81749170
# time = O(m + n)
# space = O(m + n)
class Solution:
    def uncommonFromSentences(self, A, B):
        """
        :type A: str
        :type B: str
        :rtype: List[str]
        """
        count_A = collections.Counter(A.split(' '))
        count_B = collections.Counter(B.split(' '))
        words = list((count_A.keys() | count_B.keys()) - (count_A.keys() & count_B.keys()))
        ans = []
        for word in words:
            if count_A[word] == 1 or count_B[word] == 1:
                ans.append(word)
        return ans

# V2
# time = O(m + n)
# space = O(m + n)
import collections
class Solution(object):
    def uncommonFromSentences(self, A, B):
        """
        :type A: str
        :type B: str
        :rtype: List[str]
        """
        count = collections.Counter(A.split())
        count += collections.Counter(B.split())
        return [word for word in count if count[word] == 1]