# V0

# V1
# http://bookshadow.com/weblog/2016/10/09/leetcode-sentence-screen-fitting/
class Solution(object):
    def wordsTyping(self, sentence, rows, cols):
        """
        :type sentence: List[str]
        :type rows: int
        :type cols: int
        :rtype: int
        """
        wcount = len(sentence)
        wlens = map(len, sentence)
        slen = sum(wlens) + wcount
        dp = dict()
        pr = pc = pw = ans = 0
        while pr < rows:
            if (pc, pw) in dp:
                pr0, ans0 = dp[(pc, pw)]
                loop = (rows - pr0) / (pr - pr0 + 1)
                ans = ans0 + loop * (ans - ans0)
                pr = pr0 + loop * (pr - pr0)
            else:
                dp[(pc, pw)] = pr, ans
            scount = (cols - pc) / slen
            ans += scount
            pc += scount * slen + wlens[pw]
            if pc <= cols:
                pw += 1
                pc += 1
                if pw == wcount:
                    pw = 0
                    ans += 1
            if pc >= cols:
                pc = 0
                pr += 1
        return ans

# V1'
# https://www.jiuzhang.com/solution/sentence-screen-fitting/
# JAVA
# public class Solution {
#     /**
#      * @param sentence: a list of string
#      * @param rows: an integer
#      * @param cols: an integer
#      * @return: return an integer, denote times the given sentence can be fitted on the screen
#      */
    
#     public int wordsTyping(String[] sentence, int rows, int cols) {
#         // Write your code here
#         int n = sentence.length;
#         int[][] Dp = new int[105][1200];
#         int[] suffix = new int[105];
#         int[] slen = new int[105];

#         suffix[n] = 0;
#         for(int i = n - 1; i >= 0; i--) {
#             slen[i] = sentence[i].length();
#             suffix[i] = suffix[i + 1] + 1 + slen[i];
#         }
#         for(int j = 1; j <= 1100; j++ ) {
#             for(int i = n - 1; i >= 0; i--) {
#                 if( slen[i] > j) continue;
#                 else if(slen[i] == j || slen[i] == j - 1)
#                     Dp[i][j] = 1;
#                 else 
#                     Dp[i][j] = Dp[i + 1][j - 1 - slen[i]] + 1;
#             }
#         }
#         int ans = 0, start = 0;
#         for(int i = 0; i < rows; i++) {
#             int len = cols;
#             if(start + Dp[start][Math.min(len, 1100)] >= n) {
#                 ans ++;
#                 len -= suffix[start];
#                 start = 0;
#                 int t = (len + 1) / suffix[start];
#                 len -= suffix[start] * t;
#                 if(len == -1 ) len ++;
#                 ans += t;
#             }
#             start = (start + Dp[start][Math.min(len, 1100)]);
#         }
#         return ans;
#     }
# }

# V2
# Time:  O(r + n * c)
# Space: O(n)
class Solution(object):
    def wordsTyping(self, sentence, rows, cols):
        """
        :type sentence: List[str]
        :type rows: int
        :type cols: int
        :rtype: int
        """
        def words_fit(sentence, start, cols):
            if len(sentence[start]) > cols:
                return 0

            s, count = len(sentence[start]), 1
            i = (start + 1) % len(sentence)
            while s + 1 + len(sentence[i]) <= cols:
                s += 1 + len(sentence[i])
                count += 1
                i = (i + 1) % len(sentence)
            return count

        wc = [0] * len(sentence)
        for i in range(len(sentence)):
            wc[i] = words_fit(sentence, i, cols)

        words, start = 0, 0
        for i in range(rows):
            words += wc[start]
            start = (start + wc[start]) % len(sentence)
        return words / len(sentence)
