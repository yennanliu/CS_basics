
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



[
  "abcd",
  "bnrt",
  "crm",
  "dt"
]


# V1  : dev 
"""
class Solution(object):
	def validWordSquare(self, words):
		equal_or_not=[]
		for col, row in zip(x,y):
			for k,j in zip(col, row):
				if k==j:
					equal_or_not.append(1)
				else:
					equal_or_not.append(0)
		if sum(equal_or_not) == max(col,row):
			return True 
		else:
			return False 
"""







# V2 
# Time:  O(m * n)
# Space: O(1)

class Solution(object):
    def validWordSquare(self, words):
        """
        :type words: List[str]
        :rtype: bool
        """
        for i in xrange(len(words)):
            for j in xrange(len(words[i])):
            	"""
            	A sequence of words forms a valid word square if the kth row and column read the exact same string, 
            	where 0 ≤ k < max(numRows, numColumns).
				"""
                if j >= len(words) or i >= len(words[j]) or \
                   words[j][i] != words[i][j]:
                   return False
        return True




