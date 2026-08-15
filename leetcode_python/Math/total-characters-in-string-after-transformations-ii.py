"""

3337. Total Characters in String After Transformations II
Hard

You are given a string s consisting of lowercase English letters, an integer t representing the number of transformations to perform, and an array nums of size 26. In one transformation, every character in s is replaced according to the following rules:

Replace s[i] with the next nums[s[i] - 'a'] consecutive characters in the alphabet. For example, if s[i] = 'a' and nums[0] = 3, the character 'a' transforms into the next 3 consecutive characters ahead of it, which results in "bcd".
The transformation wraps around the alphabet if it exceeds 'z'. For example, if s[i] = 'y' and nums[24] = 3, the character 'y' transforms into the next 3 consecutive characters ahead of it, which results in "zab".

Return the length of the resulting string after exactly t transformations.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: s = "abcyy", t = 2, nums = [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2]
Output: 7
Explanation:
First Transformation (t = 1):
'a' becomes 'b' as nums[0] == 1
'b' becomes 'c' as nums[1] == 1
'c' becomes 'd' as nums[2] == 1
'y' becomes 'z' as nums[24] == 1
'y' becomes 'z' as nums[24] == 1
String after the first transformation: "bcdzz"
Second Transformation (t = 2):
'b' becomes 'c' as nums[1] == 1
'c' becomes 'd' as nums[2] == 1
'd' becomes 'e' as nums[3] == 1
'z' becomes "ab" as nums[25] == 2
'z' becomes "ab" as nums[25] == 2
String after the second transformation: "cdeabab"
Final Length of the string: The string is "cdeabab", which has 7 characters.

Example 2:

Input: s = "azbk", t = 1, nums = [2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2]
Output: 8
Explanation:
First Transformation (t = 1):
'a' becomes "bc" as nums[0] == 2
'z' becomes "ab" as nums[25] == 2
'b' becomes "cd" as nums[1] == 2
'k' becomes "lm" as nums[10] == 2
String after the first transformation: "bccdablm"
Final Length of the string: The string is "bccdablm", which has 8 characters.


Constraints:

1 <= s.length <= 10^5
s consists only of lowercase English letters.
1 <= t <= 10^9
nums.length == 26
1 <= nums[i] <= 25

"""

# V0
# IDEA : THE ROUND IS A LINEAR MAP ON THE 26 COUNTS — SO EXPONENTIATE IT
#
#   the string never has to be built : only the histogram of letters matters,
#   and one transformation sends letter c to the nums[c] letters that follow
#   it cyclically. that is a fixed 26x26 matrix M with
#
#       M[(c + j) % 26][c] = 1   for j = 1 .. nums[c]
#
#   applying t rounds is M^t times the starting histogram, and t reaches
#   10^9, so binary exponentiation over 26x26 matrices does it in about 30
#   multiplications — LC 3335's per-round loop would be 10^9 iterations.
#
#   the answer is the sum of the resulting counts.
#
# time = O(26^3 log t + n), space = O(26^2)
class Solution(object):
    def lengthAfterTransformations(self, s, t, nums):
        MOD = 10 ** 9 + 7
        N = 26

        def mat_mult(A, B):
            C = [[0] * N for _ in range(N)]
            for i in range(N):
                Ai = A[i]
                Ci = C[i]
                for k in range(N):
                    a = Ai[k]
                    if a:
                        Bk = B[k]
                        for j in range(N):
                            Ci[j] = (Ci[j] + a * Bk[j]) % MOD
            return C

        M = [[0] * N for _ in range(N)]
        for c in range(N):
            for j in range(1, nums[c] + 1):
                M[(c + j) % N][c] += 1

        # result = M^t
        R = [[1 if i == j else 0 for j in range(N)] for i in range(N)]
        p = t
        base = M
        while p:
            if p & 1:
                R = mat_mult(R, base)
            base = mat_mult(base, base)
            p >>= 1

        cnt = [0] * N
        for ch in s:
            cnt[ord(ch) - 97] += 1

        total = 0
        for i in range(N):
            row = R[i]
            total += sum(row[j] * cnt[j] for j in range(N)) % MOD
        return total % MOD
