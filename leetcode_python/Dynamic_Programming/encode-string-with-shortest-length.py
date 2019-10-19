# V0

# V1
# http://bookshadow.com/weblog/2016/12/11/leetcode-encode-string-with-shortest-length/
# IDEA : DP + BACKTRACKING
class Solution(object):
    def __init__(self):
        self.dp = dict()

    def encode(self, s):
        """
        :type s: str
        :rtype: str
        """
        size = len(s)
        if size <= 1: return s
        if s in self.dp: return self.dp[s]
        ans = s
        for p in range(1, size + 1):
            left, right = s[:p], s[p:]
            t = self.solve(left) + self.encode(right)
            if len(t) < len(ans): ans = t
        self.dp[s] = ans
        return ans

    def solve(self, s):
        ans = s
        size = len(s)
        for x in range(1, size / 2 + 1):
            if size % x or s[:x] * (size / x) != s: continue
            y = str(size / x) + '[' + self.encode(s[:x]) + ']'
            if len(y) < len(ans): ans = y
        return ans
        
# V2
# Time:  O(n^3) on average
# Space: O(n^2)
class Solution(object):
    def encode(self, s):
        """
        :type s: str
        :rtype: str
        """
        def encode_substr(dp, s, i, j):
            temp = s[i:j+1]
            pos = (temp + temp).find(temp, 1)  # O(n) on average
            if pos >= len(temp):
                return temp
            return str(len(temp)/pos) + '[' + dp[i][i + pos - 1] + ']'

        dp = [["" for _ in range(len(s))] for _ in range(len(s))]
        for length in range(1, len(s)+1):
            for i in range(len(s)+1-length):
                j = i+length-1
                dp[i][j] = s[i:i+length]
                for k in range(i, j):
                    if len(dp[i][k]) + len(dp[k+1][j]) < len(dp[i][j]):
                        dp[i][j] = dp[i][k] + dp[k+1][j]
                encoded_string = encode_substr(dp, s, i, j)
                if len(encoded_string) < len(dp[i][j]):
                    dp[i][j] = encoded_string
        return dp[0][len(s) - 1]