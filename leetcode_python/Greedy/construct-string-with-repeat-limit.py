"""

2182. Construct String With Repeat Limit
Medium

You are given a string s and an integer repeatLimit. Construct a new string repeatLimitedString using the characters of s such that no letter appears more than repeatLimit times in a row. You do not have to use all characters from s.

Return the lexicographically largest repeatLimitedString possible.

A string a is lexicographically larger than a string b if in the first position where a and b differ, string a has a letter that appears later in the alphabet than the corresponding letter in b. If the first min(a.length, b.length) characters do not differ, then the longer string is the lexicographically larger one.

Example 1:

Input: s = "cczazcc", repeatLimit = 3
Output: "zzcccac"
Explanation: We use all of the characters from s to construct the repeatLimitedString "zzcccac".
The letter 'a' appears at most 1 time in a row.
The letter 'c' appears at most 3 times in a row.
The letter 'z' appears at most 2 times in a row.
Hence, no letter appears more than repeatLimit times in a row and the string is a valid repeatLimitedString.
The string is the lexicographically largest repeatLimitedString possible so we return "zzcccac".
Note that the string "zzcccca" is lexicographically larger but the letter 'c' appears more than 3 times in a row, so it is not a valid repeatLimitedString.

Example 2:

Input: s = "aababab", repeatLimit = 2
Output: "bbabaa"
Explanation: We use only some of the characters from s to construct the repeatLimitedString "bbabaa".
The letter 'a' appears at most 2 times in a row.
The letter 'b' appears at most 2 times in a row.
Hence, no letter appears more than repeatLimit times in a row and the string is a valid repeatLimitedString.
The string is the lexicographically largest repeatLimitedString possible so we return "bbabaa".
Note that the string "bbabaaa" is lexicographically larger but the letter 'a' appears more than 2 times in a row, so it is not a valid repeatLimitedString.

Constraints:

1 <= repeatLimit <= s.length <= 10^5
s consists of lowercase English letters.

"""

# V0
# IDEA : GREEDY + COUNTING (emit the largest letter, break runs with the next largest)
#
#   to be lexicographically largest we always want the biggest remaining
#   letter.  emit at most repeatLimit copies of it; if some are left we
#   MUST insert one different letter, and the best filler is the largest
#   letter strictly smaller than it (a bigger one would have been used
#   already).  then continue with the same letter.
#   NOTE : the filler pointer j only moves downwards, so the whole scan
#          is linear in the alphabet, not quadratic.
#
# time = O(n + 26^2), space = O(n)
class Solution(object):
    def repeatLimitedString(self, s, repeatLimit):
        cnt = [0] * 26
        for ch in s:
            cnt[ord(ch) - 97] += 1

        res = []
        j = 25                       # candidate filler (smaller letter)
        for i in range(25, -1, -1):
            if cnt[i] == 0:
                continue
            if j >= i:
                j = i - 1
            while True:
                take = min(repeatLimit, cnt[i])
                cnt[i] -= take
                res.append(chr(97 + i) * take)
                if cnt[i] == 0:
                    break
                while j >= 0 and cnt[j] == 0:
                    j -= 1
                if j < 0:
                    break
                cnt[j] -= 1
                res.append(chr(97 + j))
            if j < 0:
                break
        return "".join(res)
