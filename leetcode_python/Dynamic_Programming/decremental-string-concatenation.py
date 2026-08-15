"""

2746. Decremental String Concatenation
Medium

You are given a 0-indexed array words containing n strings.

Let's define a join operation join(x, y) between two strings x and y as concatenating them into xy. However, if the last character of x is equal to the first character of y, one of them is deleted.

For example join("ab", "ba") = "aba" and join("ab", "cde") = "abcde".

You are to perform n - 1 join operations. Let str0 = words[0]. Starting from i = 1 up to i = n - 1, for the ith operation, you can do one of the following:

Make stri = join(stri - 1, words[i])
Make stri = join(words[i], stri - 1)

Your task is to minimize the length of strn - 1.

Return an integer denoting the minimum possible length of strn - 1.


Example 1:

Input: words = ["aa","ab","bc"]
Output: 4
Explanation: In this example, we can perform join operations in the following order to minimize the length of str2:
str0 = "aa"
str1 = join(str0, "ab") = "aab"
str2 = join(str1, "bc") = "aabc"
It can be shown that the minimum possible length of str2 is 4.

Example 2:

Input: words = ["ab","b"]
Output: 2
Explanation: In this example, str0 = "ab", there are two ways to get str1:
join(str0, "b") = "ab" or join("b", str0) = "bab".
The first string, "ab", has the minimum length. Hence, the answer is 2.

Example 3:

Input: words = ["aaa","c","aba"]
Output: 6
Explanation: In this example, we can perform join operations in the following order to minimize the length of str2:
str0 = "aaa"
str1 = join(str0, "c") = "aaac"
str2 = join("aba", str1) = "abaaac"
It can be shown that the minimum possible length of str2 is 6.


Constraints:

1 <= words.length <= 1000
1 <= words[i].length <= 50
Each character in words[i] is an English lowercase letter

"""

# V0
# IDEA : DP OVER (FIRST CHAR, LAST CHAR) OF THE ACCUMULATED STRING
#
#   The only thing about the accumulated string that ever matters for a
#   FUTURE join is its first character and its last character — the middle is
#   never touched again. So the state is just (first, last), 26 * 26 = 676
#   possibilities, and we keep the minimum length reached for each state.
#
#   Processing words[i] (call it s, with head = s[0], tail = s[-1]) from a
#   state (a, b) of length L :
#     - append  : join(acc, s) -> new state (a, tail),
#                 length L + len(s) - (1 if head == b else 0)
#     - prepend : join(s, acc) -> new state (head, b),
#                 length L + len(s) - (1 if tail == a else 0)
#
#   NOTE : the saving is 1 character and only when the touching characters
#          match — that is the whole "decremental" part of the problem.
#
#   NOTE : for a SINGLE-character word, head == tail; appending it after a
#          string that ends with the same letter still just saves 1 (the
#          word itself is not consumed), which the formula already gives.
#
#   NOTE : this is written BOTTOM-UP on purpose. n can be 1000, so the
#          natural top-down dfs(i, a, b) would recurse 1000 deep and risk
#          hitting Python's default recursion limit.
#
# time = O(n * 26^2), space = O(26^2)
class Solution(object):
    def minimizeConcatenatedLength(self, words):
        INF = float('inf')
        # dp[a][b] = min length of an accumulated string starting with 'a'+a
        #            and ending with 'a'+b
        dp = [[INF] * 26 for _ in range(26)]
        first = ord(words[0][0]) - 97
        last = ord(words[0][-1]) - 97
        dp[first][last] = len(words[0])

        for s in words[1:]:
            head = ord(s[0]) - 97
            tail = ord(s[-1]) - 97
            size = len(s)
            nxt = [[INF] * 26 for _ in range(26)]
            for a in range(26):
                row = dp[a]
                for b in range(26):
                    cur = row[b]
                    if cur == INF:
                        continue
                    # acc + s
                    cand = cur + size - (1 if head == b else 0)
                    if cand < nxt[a][tail]:
                        nxt[a][tail] = cand
                    # s + acc
                    cand = cur + size - (1 if tail == a else 0)
                    if cand < nxt[head][b]:
                        nxt[head][b] = cand
            dp = nxt

        return min(min(row) for row in dp)
