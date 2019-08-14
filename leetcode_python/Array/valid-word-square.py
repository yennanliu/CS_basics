"""
LeetCode 422. Valid Word Square

Given a sequence of words, check whether it forms a valid word square.

A sequence of words forms a valid word square if the kth row and column read the exact same string, where 0 ≤ k < max(numRows, numColumns).

Note:

The number of words given is at least 1 and does not exceed 500.
Word length will be at least 1 and does not exceed 500.
Each word contains only lowercase English alphabet a-z.

Given a sequence of words, check whether it forms a valid word square.

A sequence of words forms a valid word square if the kth row and column read the exact same string, where 0 ≤ k < max(numRows, numColumns).

Example 1:

Input:
[
  "abcd",
  "bnrt",
  "crmy",
  "dtye"
]

Output:
true

Explanation:
The first row and first column both read "abcd".
The second row and second column both read "bnrt".
The third row and third column both read "crmy".
The fourth row and fourth column both read "dtye".

Therefore, it is a valid word square.
Example 2:

Input:
[
  "abcd",
  "bnrt",
  "crm",
  "dt"
]

Output:
true

Explanation:
The first row and first column both read "abcd".
The second row and second column both read "bnrt".
The third row and third column both read "crm".
The fourth row and fourth column both read "dt".

Therefore, it is a valid word square.
Example 3:

Input:
[
  "ball",
  "area",
  "read",
  "lady"
]

Output:
false

Explanation:
The third row reads "read" while the third column reads "lead".

Therefore, it is NOT a valid word square.

"""

# V0

# V1
# http://us.jiuzhang.com/solution/valid-word-square/#tag-highlight-lang-python
class Solution:
    """
    @param words: a list of string
    @return: return a boolean
    """
    def validWordSquare(self, words):
        # write your code here
        n, m = len(words), len(words[0])
        if(n != m):
            return False
        for i in range(n):
        	for j in range(m):
        		if(j >= n or i >= m or not(words[i][j] == words[j][i])):
        			return False
        return True
        
# V1' 
# http://bookshadow.com/weblog/2016/10/16/leetcode-valid-word-square/
class Solution(object):
    def validWordSquare(self, words):
        """
        :type words: List[str]
        :rtype: bool
        """
        m = len(words)
        n = len(words[0]) if m else 0
        if m != n:
            return False
        for x in range(m):
            n = len(words[x])
            c = 0
            for y in range(m):
                if len(words[y]) < x + 1:
                    break
                c += 1
            if c != n:
                return False
            for y in range(n):
                if words[x][y] != words[y][x]:
                    return False
        return True

# V2 
# Time:  O(m * n)
# Space: O(1)
class Solution(object):
    def validWordSquare(self, words):
        """
        :type words: List[str]
        :rtype: bool
        """
        for i in range(len(words)):
            for j in range(len(words[i])):
                if j >= len(words) or i >= len(words[j]) or \
                   words[j][i] != words[i][j]:
                   return False
        return True