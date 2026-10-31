"""

97. Interleaving String
Medium

Given strings s1, s2, and s3, find whether s3 is formed by an interleaving of s1 and s2.

An interleaving of two strings s and t is a configuration where s and t are divided into n and m substrings respectively, such that:

s = s1 + s2 + ... + sn
t = t1 + t2 + ... + tm
|n - m| <= 1
The interleaving is s1 + t1 + s2 + t2 + s3 + t3 + ... or t1 + s1 + t2 + s2 + t3 + s3 + ...
Note: a + b is the concatenation of strings a and b.

 

Example 1:


Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
Output: true
Explanation: One way to obtain s3 is:
Split s1 into s1 = "aa" + "bc" + "c", and s2 into s2 = "dbbc" + "a".
Interleaving the two splits, we get "aa" + "dbbc" + "bc" + "a" + "c" = "aadbbcbcac".
Since s3 can be obtained by interleaving s1 and s2, we return true.
Example 2:

Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
Output: false
Explanation: Notice how it is impossible to interleave s2 with any other string to obtain s3.
Example 3:

Input: s1 = "", s2 = "", s3 = ""
Output: true
 

Constraints:

0 <= s1.length, s2.length <= 100
0 <= s3.length <= 200
s1, s2, and s3 consist of lowercase English letters.
 

Follow up: Could you solve it using only O(s2.length) additional memory space?

"""


# V0
class Solution(object):
    def isInterleave(self, s1, s2, s3):
        """
        :type s1: str
        :type s2: str
        :type s3: str
        :rtype: bool
        """
        pass
        



# V0-1
# IDEA 1) 2D DP
"""
DP def:

	dp[i][j] = whether
	s3[0 : i+j]
	can be formed by interleaving

	s1[0 : i]
	and
	s2[0 : j]

	where

	i chars are taken from s1
	j chars are taken from s2




DP eq:

 The last character of s3[:i+j] must come from either:


 Case 1: last char comes from s1

	dp[i-1][j] == True
	and
	s1[i-1] == s3[i+j-1]


Case 2: last char comes from s2

	dp[i][j-1] == True
	and
	s2[j-1] == s3[i+j-1]



  
   -> so,


	   dp[i][j] =
	(
	    dp[i-1][j]
	    and
	    s1[i-1] == s3[i+j-1]
	)
	or
	(
	    dp[i][j-1]
	    and
	    s2[j-1] == s3[i+j-1]
	)

"""
# time = O(m*n)  # m = len(s1), n = len(s2)
# space = O(m*n)
class Solution(object):
    def isInterleave(self, s1, s2, s3):
        m, n = len(s1), len(s2)

        if m + n != len(s3):
            return False

        dp = [[False] * (n + 1) for _ in range(m + 1)]

        dp[0][0] = True

        for i in range(1, m + 1):
            dp[i][0] = (
            	# need to have matched `prev` indices
                dp[i - 1][0]
                and s1[i - 1] == s3[i - 1]
            )

        for j in range(1, n + 1):
            dp[0][j] = (
            	# need to have matched `prev` indices
                dp[0][j - 1]
                and s2[j - 1] == s3[j - 1]
            )

        for i in range(1, m + 1):
            for j in range(1, n + 1):

                dp[i][j] = (
                    # Case 1: last char comes from s1
                    (
                        dp[i - 1][j]
                        and s1[i - 1] == s3[i + j - 1]
                    )
                    # Case 2: last char comes from s2
                    or (
                        dp[i][j - 1]
                        and s2[j - 1] == s3[i + j - 1]
                    )
                )

        return dp[m][n]


# V0-0-1
# IDEA: 2D DP (GEMINI)
class Solution(object):

  def isInterleave(self, s1, s2, s3):
    """:type s1: str

    :type s2: str
    :type s3: str
    :rtype: bool
    """
    n1 = len(s1)
    n2 = len(s2)
    n3 = len(s3)

    # 1. 長度必須完全吻合
    if n1 + n2 != n3:
      return False

    # dp[i][j] 代表 s1 的前 i 個字元與 s2 的前 j 個字元，能否交錯組成 s3 的前 i+j 個字元
    dp = [[False] * (n2 + 1) for _ in range(n1 + 1)]

    # 基底狀態：空字串可以組成空字串
    dp[0][0] = True

    # 2. 初始化第一列（代表 s1 不提供字元，全由 s2 供應）
    for j in range(1, n2 + 1):
      dp[0][j] = dp[0][j - 1] and (s2[j - 1] == s3[j - 1])

    # 3. 初始化第一行（代表 s2 不提供字元，全由 s1 供應）
    for i in range(1, n1 + 1):
      dp[i][0] = dp[i - 1][0] and (s1[i - 1] == s3[i - 1])

    # 4. 填滿整個 DP 表格
    for i in range(1, n1 + 1):
      for j in range(1, n2 + 1):
        # 當前 s3 的字元位置對應到 s3[i + j - 1]
        # 情況 A：由 s1[i-1] 提供（前提是 dp[i-1][j] 為 True 且字元相符）
        match_s1 = dp[i - 1][j] and (s1[i - 1] == s3[i + j - 1])
        # 情況 B：由 s2[j-1] 提供（前提是 dp[i][j-1] 為 True 且字元相符）
        match_s2 = dp[i][j - 1] and (s2[j - 1] == s3[i + j - 1])

        dp[i][j] = match_s1 or match_s2

    return dp[n1][n2]


# V0-0-2
# IDEA: 2D DP (gpt)
class Solution(object):
    def isInterleave(self, s1, s2, s3):
        """
        :type s1: str
        :type s2: str
        :type s3: str
        :rtype: bool
        """
        n1 = len(s1)
        n2 = len(s2)
        n3 = len(s3)

        # Length must match exactly
        if n1 + n2 != n3:
            return False

        # dp[i][j] = whether s1[:i] and s2[:j]
        # can form s3[:i+j]
        dp = [[False] * (n2 + 1) for _ in range(n1 + 1)]

        # Base case
        dp[0][0] = True

        # Initialize first column: only use s1
        for i in range(1, n1 + 1):
            dp[i][0] = (
                dp[i - 1][0]
                and s1[i - 1] == s3[i - 1]
            )

        # Initialize first row: only use s2
        for j in range(1, n2 + 1):
            dp[0][j] = (
                dp[0][j - 1]
                and s2[j - 1] == s3[j - 1]
            )

        # DP transition
        for i in range(1, n1 + 1):
            for j in range(1, n2 + 1):
                k = i + j - 1

                # Take the next character from s1
                from_s1 = (
                    dp[i - 1][j]
                    and s1[i - 1] == s3[k]
                )

                # Take the next character from s2
                from_s2 = (
                    dp[i][j - 1]
                    and s2[j - 1] == s3[k]
                )

                dp[i][j] = from_s1 or from_s2

        return dp[n1][n2]



# V0-2
# IDEA 1) 2D DP
"""

DP def
    dp[i][j]: whether s3[0 : i+j] can be formed by INTERLEAVING

              s1[0 : i] and s2[0 : j]

              -> i chars taken from s1, j chars taken from s2

DP eq

     the last character of s3[:i+j] must come from ONE of the two:

     dp[i][j] = ( dp[i-1][j] and s1[i-1] == s3[i+j-1] )    # last char from s1

             or ( dp[i][j-1] and s2[j-1] == s3[i+j-1] )    # last char from s2


    -> e.g. edge: len(s1) + len(s2) != len(s3) -> False immediately

     init: dp[0][0] = True
           dp[i][0] = dp[i-1][0] and s1[i-1] == s3[i-1]
           dp[0][j] = dp[0][j-1] and s2[j-1] == s3[j-1]

     ans = dp[n1][n2]

"""
# time = O(m*n)  # m = len(s1), n = len(s2)
# space = O(m*n)
class Solution(object):
    def isInterleave(self, s1, s2, s3):
        """
        :type s1: str
        :type s2: str
        :type s3: str
        :rtype: bool
        """
        # EDGE CASE CHECK: If the combined lengths don't match s3, 
        # it is mathematically impossible to interleave them.
        if len(s1) + len(s2) != len(s3):
            return False
            
        m, n = len(s1), len(s2)
        
        # Initialize an (m + 1) x (n + 1) DP grid with False values
        dp = [[False] * (n + 1) for _ in range(m + 1)]
        
        # BASE CASE: Two empty strings can always interleave to form an empty s3
        dp[0][0] = True
        
        # 1. Initialize the first column: Using ONLY characters from s1 (s2 is empty)
        for i in range(1, m + 1):
            dp[i][0] = dp[i-1][0] and s1[i-1] == s3[i-1]
            
        # 2. Initialize the first row: Using ONLY characters from s2 (s1 is empty)
        for j in range(1, n + 1):
            dp[0][j] = dp[0][j-1] and s2[j-1] == s3[j-1]
            
        # 3. Fill the rest of the DP table grid
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                # We can form s3 up to here if:
                # - The previous char came from s1 AND s1's current char matches s3's current char
                # OR
                # - The previous char came from s2 AND s2's current char matches s3's current char
                dp[i][j] = (dp[i-1][j] and s1[i-1] == s3[i+j-1]) or \
                           (dp[i][j-1] and s2[j-1] == s3[i+j-1])
                           
        # The final bottom-right cell holds whether the full strings match completely
        return dp[m][n]




