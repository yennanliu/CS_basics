"""

1573. Number of Ways to Split a String
Medium

Given a binary string s, you can split s into 3 non-empty strings s1, s2, and s3 where s1 + s2 + s3 = s.

Return the number of ways s can be split such that the number of ones is the same in s1, s2, and s3. Since the answer may be too large, return it modulo 109 + 7.

 

Example 1:

Input: s = "10101"
Output: 4
Explanation: There are four ways to split s in 3 parts where each part contain the same number of letters '1'.
"1|010|1"
"1|01|01"
"10|10|1"
"10|1|01"
Example 2:

Input: s = "1001"
Output: 0
Example 3:

Input: s = "0000"
Output: 3
Explanation: There are three ways to split s in 3 parts.
"0|0|00"
"0|00|0"
"00|0|0"
 

Constraints:

3 <= s.length <= 105
s[i] is either '0' or '1'.
Accepted
16.9K

"""

# V0

# V1
# IDEA : MATH + problem understanding
# https://leetcode.com/problems/number-of-ways-to-split-a-string/discuss/830601/Python-O(n)-%2B-Explanation
class Solution(object):
    def numWays(self, s):
        mod = 10**9+7
        cnt = s.count('1')
        if cnt == 0: return (len(s)-1)*(len(s)-2)//2 % mod
        if cnt % 3 != 0: return 0
        ones = []
        for i,x in enumerate(s):
            if x == '1': ones.append(i)
        return (ones[cnt//3] - ones[cnt//3-1]) * (ones[2*cnt//3]- ones[2*cnt//3-1]) % mod

# V1'
# https://leetcode.com/problems/number-of-ways-to-split-a-string/discuss/830485/python-solution
class Solution:
	def numWays(self, s):
		idx = []
		res = 0
		for i in range(len(s)):
			if s[i] == "1":
				idx.append(i)
		if len(idx) % 3 != 0:
			return 0
		if "1" not in s:
			return sum([i for i in range(1, len(s) - 1)]) % (10 ** 9 + 7)
		n = len(idx) // 3
		cnt1 = idx[n] - idx[n - 1] - 1
		cnt2 = idx[-n] - idx[-n - 1] - 1
		res = (cnt2 + 1) * (cnt1 + 1)
		return res % (10 ** 9 + 7)

# V1''
# https://leetcode.com/problems/number-of-ways-to-split-a-string/discuss/1202856/Python-detail-explanation
class Solution:
    def numWays(self, s):
        oneCount = 0
        for c in s:
            if c == '1':
                oneCount += 1
        
        res = 0        
        if oneCount % 3 != 0: 
            return 0
        elif oneCount == 0:
            res = (len(s) - 2) * (len(s) - 1) // 2
        else:
            count, k =0,  oneCount // 3			
            segArr = [[0, 0], [0, 0]]
            for index, c in enumerate(s):
                if c == '1':
                    count += 1
                    if count == k:
                        segArr[0][0] = index
                    if count == k + 1:
                        segArr[0][1] = index
                    if count == 2 * k:
                        segArr[1][0] = index
                    if count == 2 * k + 1:
                        segArr[1][1] = index

            res = (segArr[0][1] - segArr[0][0]) * (segArr[1][1] - segArr[1][0])

        return res % (10 ** 9 + 7)

# V1''
# https://leetcode.com/problems/number-of-ways-to-split-a-string/discuss/830431/Solution-with-python-Code
class Solution:
    def numWays(self, s):
            n = len(s)
            mod = 10**9 + 7
            ones = s.count('1')
            if ones % 3 != 0:
                return 0
            if ones == 0:
                # n-1 choose 2
                n -= 1
                return (n * (n-1)//2) % mod
            rc = ones//3

            s = [int(i) for i in s]
            i = -1

            lc = 0
            while lc < rc:
                i += 1
                lc += s[i]
            left1 = i
            right1 = i+1
            while right1 < n and s[right1] == 0:
                right1 += 1
            right1 -= 1

            i = n
            rr = 0
            while rr < rc:
                i -= 1
                rr += s[i]
            right2 = i
            left2 = i - 1
            while left2 >= 0 and s[left2] == 0:
                left2 -= 1
            left2 += 1
            a = right1 - left1 + 1
            b = right2 - left2 + 1
            return (a * b) % mod
            return res

# V2