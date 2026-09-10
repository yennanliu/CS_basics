"""

131. Palindrome Partitioning
Medium

Given a string s, partition s such that every substring of the partition is a palindrome. Return all possible palindrome partitioning of s.

A palindrome string is a string that reads the same backward as forward.

 

Example 1:

Input: s = "aab"
Output: [["a","a","b"],["aa","b"]]
Example 2:

Input: s = "a"
Output: [["a"]]
 

Constraints:

1 <= s.length <= 16
s contains only lowercase English letters.

"""

# V0
# IDEA : BACKTRCK (GPT)
# time = O(n * 2^n)
# space = O(n)
class Solution(object):
    def partition(self, s):
        """
        :type s: str
        :rtype: List[List[str]]
        """

        self.res = []

        self.helper(s, 0, [])

        return self.res


    """
    NOTE !!!


    `start_idx` as param
    """
    def helper(self, s, start_idx, tmp):

        # Reached the end
        if start_idx == len(s):
            self.res.append(tmp[:])
            return

        """
        NOTE !!!


        1.  we loop over `end_idx`,

           (but NOT start_idx)


        2. end idx is in range(tart_idx + 1, len(s) + 1)

            -> since in py, `end` idx is actual idx + 1

            -> e.g.

                 s = [1,2,3]

                 s[2:3] = ?


        3. ONLY move forward (run recursion)
            if `part == part[::-1]`

            -> e.g. current sub str is palindrome
        """
        # Try every possible end position
        for end_idx in range(start_idx + 1, len(s) + 1):

            # Current substring
            part = s[start_idx:end_idx]

            # Only choose palindrome
            if part == part[::-1]:

                # Choose
                tmp.append(part)

                """
                NOTE !!!

                the `next` `start_idx` is `end_idx`
                """
                # Explore
                self.helper(s, end_idx, tmp)

                # Undo
                tmp.pop()



# V0
# IDEA : BACKTRCK
# time = O(n * 2^n)
# space = O(n)
class Solution(object):
    def partition(self, s):
        # help func
        def help(s, tmp):
            # NOTE !! if not s, means there is NO remaining s, then append tmp to res, and return (end the recursion)
            if not s:
                res.append(tmp[:])
                return
            """

            NOTE !!! we have to start from 1, end at len(s) + 1

            example :

                    In [75]: x = "aab"
                        ...: for i in range(1, len(x)+1):
                        ...:     print (x[:i])
                        ...:
                    a
                    aa
                    aab

                    In [76]:

                    In [76]: x = "aab"
                        ...: for i in range(1, len(x)):
                        ...:     print (x[:i])
                        ...:
                    a
                    aa

            """
            for i in range(1, len(s)+1):
                if s[:i] == s[:i][::-1]:
                    """
                    NOTE here !!!!
                        -> we subset s by "s[i:]"
                        -> we update current tmp by "tmp + [s[:i]]"
                    """

                    help(s[i:], tmp + [s[:i]])
        # edge case
        if not s:
            return [[""]]
        if len(s) == 1:
            return [[s]]
        res = []
        tmp = []
        help(s, tmp)
        return res


# V0-1
# IDEA: BACKTRACK (GPT)
class Solution(object):
    def partition(self, s):
        """
        :type s: str
        :rtype: List[List[str]]
        """
        res = []

        def backtrack(start_idx, path):
            # Base Case: Reached the end of the string -> valid partition complete
            if start_idx == len(s):
                res.append(list(path))
                return

            # Explore all candidate partitions s[start_idx : end_idx]
            """
            Q: why  `start_idx + 1, len(s) + 1` ?

            ->

            Python 字串切片的「左閉右開」特性 與 range() 函數「不包含終點」的語法規則


            1. why start_idx + 1 ?

                -> - Python 的字串切片 s[start_idx : end_idx] 取出的字元範圍是
                   [start_idx, end_idx -1] (包含起點，不包含終點） 


                   - 遞迴切割時，每次切出的子字串長度至少要為 1
                     （不能切出空字串造成死迴圈）。

                
                    - 當 end_idx = start_idx + 1 時，s[start_idx : start_idx + 1]
                    恰好能取出長度為 1 的單一字元。


            2. why len(s) + 1 ?

                ->  - range(A, B) 產生的數值範圍是從 A 到 B - 1

                    - 我們希望子字串最遠可以一路切到字串末端，也就是讓 end_idx 
                      能夠取到 len(s)（對應切片 s[start_idx : len(s)]）。


                    - 為了讓迴圈中的 end_idx 能夠順利到達 len(s)，range 
                      的第二個參數（stop）就必須設定為 len(s) + 1。

            """
            for end_idx in range(start_idx + 1, len(s) + 1):
                substring = s[start_idx:end_idx]
                
                if self.is_palindrome(substring):
                    path.append(substring)           # Choose
                    backtrack(end_idx, path)         # Explore
                    path.pop()                       # Backtrack

        backtrack(0, [])
        return res

    def is_palindrome(self, x):
        l, r = 0, len(x) - 1
        while l < r:
            if x[l] != x[r]:
                return False
            l += 1
            r -= 1
        return True


# V0-2
# IDEA: BACKTRACK (gemini)
class Solution(object):
    def partition(self, s):
        """
        :type s: str
        :rtype: List[List[str]]
        """
        res = []

        def backtrack(start_idx, path):
            # Base Case: Reached the end of the string -> valid partition complete
            if start_idx == len(s):
                res.append(list(path))
                return

            # Explore all candidate partitions s[start_idx : end_idx]
            for end_idx in range(start_idx + 1, len(s) + 1):
                substring = s[start_idx:end_idx]
                
                if self.is_palindrome(substring):
                    path.append(substring)           # Choose
                    backtrack(end_idx, path)         # Explore
                    path.pop()                       # Backtrack

        backtrack(0, [])
        return res

    def is_palindrome(self, x):
        l, r = 0, len(x) - 1
        while l < r:
            if x[l] != x[r]:
                return False
            l += 1
            r -= 1
        return True


# V0-3
# IDEA: 2D DP (gemini)
class Solution(object):
    def partition(self, s):
        n = len(s)
        res = []

        # 1. DP Precomputation: dp[i][j] is True if s[i..j] is a palindrome
        dp = [[False] * n for _ in range(n)]
        for right in range(n):
            for left in range(right + 1):
                if s[left] == s[right] and (right - left <= 2 or dp[left + 1][right - 1]):
                    dp[left][right] = True

        # 2. Backtracking using O(1) DP lookup
        def backtrack(start, path):
            if start == n:
                res.append(list(path))
                return

            for end in range(start, n):
                if dp[start][end]:
                    path.append(s[start : end + 1])
                    backtrack(end + 1, path)
                    path.pop()

        backtrack(0, [])
        return res


# V0'
# IDEA : BACKTRCK, similar as LC 046 permutations
# time = O(n * 2^n)
# space = O(n)
class Solution(object):
    def partition(self, s):
        def help(s, res, path):
            if not s:
                res.append(path)
                return
            for i in range(1, len(s)+1):
                if s[:i] == s[:i][::-1]:
                    help(s[i:], res, path + [s[:i]])
        # edge case
        if not s:
            return
        res = []
        path = []
        help(s, res, path)
        return res

# V0''
# IDEA : BACKTRCK, similar as LC 046 permutations
# time = O(n * 2^n)
# space = O(n)
class Solution(object):
    def partition(self, s):
        res = []
        self.helper(s, res, [])
        return res
        
    def helper(self, s, res, path):
        if not s:
            res.append(path)
            return
        # beware of the start and the end index
        for i in range(1, len(s) + 1): 
            if self.isPalindrome(s[:i]):
                """
                ### backtrcking 
                if s[:i] is palindrome, then check if there is palindrome in s[i:] as well
                e.g.  
                    a a b b a 
                  => 
                    if 'aa' (<-) is palindrome, then check a b b a (->)
                """
                self.helper(s[i:], res, path + [s[:i]])

    def isPalindrome(self, x):
        return x == x[::-1]

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79574462
# IDEA : BACKTRACKING 
# NOTE :
# In [12]: x=[1,2,3]
# In [13]: for i in range(1,len(x)+1):
#     ...:     print (i)
#     ...:     
# 1
# 2
# 3
# time = O(n * 2^n)
# space = O(n)
class Solution(object):
    def partition(self, s):
        """
        :type s: str
        :rtype: List[List[str]]
        """
        self.isPalindrome = lambda s : s == s[::-1]
        res = []
        self.helper(s, res, [])
        return res
        
    def helper(self, s, res, path):
        if not s:
            res.append(path)
            return
        # beware of the start and the end index
        for i in range(1, len(s) + 1): 
            if self.isPalindrome(s[:i]):
                self.helper(s[i:], res, path + [s[:i]])

### Test case:
s=Solution()
assert s.partition("aab")==[['a', 'a', 'b'], ['aa', 'b']]
assert s.partition("")==[[]]
assert s.partition("a")==[['a']]
assert s.partition("ab")==[['a', 'b']]
assert s.partition(" b")==[[' ', 'b']]
assert s.partition("abc")==[['a', 'b', 'c']]
assert s.partition("abcbc")==[['a', 'b', 'c', 'b', 'c'], ['a', 'b', 'cbc'], ['a', 'bcb', 'c']]
assert s.partition("aaa")==[['a', 'a', 'a'], ['a', 'aa'], ['aa', 'a'], ['aaa']]

# V1'
# https://leetcode.com/problems/palindrome-partitioning/discuss/42100/Python-easy-to-understand-backtracking-solution.
# IDEA : DFS + backtracking
# time = O(n * 2^n)
# space = O(n)
class Solution(object):
    def partition(self, s):
        res = []
        self.dfs(s, [], res)
        return res
        
    def dfs(self, s, path, res):
        if not s: # backtracking
            res.append(path)
        for i in range(1, len(s)+1):
            if self.isPar(s[:i]):
                self.dfs(s[i:], path+[s[:i]], res)
                
    def isPar(self, s):
        return s == s[::-1]

# V1''
# https://leetcode.com/problems/palindrome-partitioning/discuss/441529/backtracking-python
# IDEA : BACKTRACKING + DFS + DP
# time = O(n * 2^n)
# space = O(n^2)
class Solution(object):
    def partition(self, s):
        """
        :type s: str
        :rtype: List[List[str]]
        """
        dp = [[False for _ in range(len(s))] for _ in range(len(s))]
        for j in range(len(s)):
            dp[j][j] = True
            for i in range(j):
                if s[i]==s[j]:
                    dp[i][j] = (j-i==1) or (dp[i+1][j-1])
    
        res = []
        def dfs(start, curpath):
            if start>=len(s):
                res.append(curpath)
            for i in range(start, len(s)):
                if dp[start][i] is True:
                    dfs(i+1, curpath+[s[start:i+1]])
        dfs(0, [])
        return res

# V1'''
# https://leetcode.com/problems/palindrome-partitioning/discuss/42025/1-liner-Python-Ruby
# time = O(n * 2^n)
# space = O(n * 2^n)
class Solution(object):
    def partition(self, s):
        return [[s[:i]] + rest
            for i in range(1, len(s)+1)
            if s[:i] == s[i-1::-1]
            for rest in self.partition(s[i:])] or [[]]

# V1
# IDEA : Backtracking
# https://leetcode.com/problems/palindrome-partitioning/solution/
# JAVA
# class Solution {
#     public List<List<String>> partition(String s) {
#         List<List<String>> result = new ArrayList<List<String>>();
#         dfs(0, result, new ArrayList<String>(), s);
#         return result;
#     }
#
#     void dfs(int start, List<List<String>> result, List<String> currentList, String s) {
#         if (start >= s.length()) result.add(new ArrayList<String>(currentList));
#         for (int end = start; end < s.length(); end++) {
#             if (isPalindrome(s, start, end)) {
#                 // add current substring in the currentList
#                 currentList.add(s.substring(start, end + 1));
#                 dfs(end + 1, result, currentList, s);
#                 // backtrack and remove the current substring from currentList
#                 currentList.remove(currentList.size() - 1);
#             }
#         }
#     }
#
#     boolean isPalindrome(String s, int low, int high) {
#         while (low < high) {
#             if (s.charAt(low++) != s.charAt(high--)) return false;
#         }
#         return true;
#     }
# }

# V1
# IDEA : Backtracking with Dynamic Programming
# https://leetcode.com/problems/palindrome-partitioning/solution/
# JAVA
# class Solution {
#     public List<List<String>> partition(String s) {
#         int len = s.length();
#         boolean[][] dp = new boolean[len][len];
#         List<List<String>> result = new ArrayList<>();
#         dfs(result, s, 0, new ArrayList<>(), dp);
#         return result;
#     }
#
#     void dfs(List<List<String>> result, String s, int start, List<String> currentList, boolean[][] dp) {
#         if (start >= s.length()) result.add(new ArrayList<>(currentList));
#         for (int end = start; end < s.length(); end++) {
#             if (s.charAt(start) == s.charAt(end) && (end - start <= 2 || dp[start + 1][end - 1])) {
#                 dp[start][end] = true;
#                 currentList.add(s.substring(start, end + 1));
#                 dfs(result, s, end + 1, currentList, dp);
#                 currentList.remove(currentList.size() - 1);
#             }
#         }
#     }
# }

# V2 
# time = O(n^2 ~ 2^n)
# space = O(n^2)
class Solution(object):
    # @param s, a string
    # @return a list of lists of string
    def partition(self, s):
        n = len(s)

        is_palindrome = [[0 for j in range(n)] for i in range(n)]
        for i in reversed(range(0, n)):
            for j in range(i, n):
                is_palindrome[i][j] = s[i] == s[j] and ((j - i < 2 ) or is_palindrome[i + 1][j - 1])

        sub_partition = [[] for i in range(n)]
        for i in reversed(range(n)):
            for j in range(i, n):
                if is_palindrome[i][j]:
                    if j + 1 < n:
                        for p in sub_partition[j + 1]:
                            sub_partition[i].append([s[i:j + 1]] + p)
                    else:
                        sub_partition[i].append([s[i:j + 1]])

        return sub_partition[0]

# time = O(2^n)
# space = O(n)
# recursive solution
class Solution2(object):
    # @param s, a string
    # @return a list of lists of string
    def partition(self, s):
        result = []
        self.partitionRecu(result, [], s, 0)
        return result

    def partitionRecu(self, result, cur, s, i):
        if i == len(s):
            result.append(list(cur))
        else:
            for j in range(i, len(s)):
                if self.isPalindrome(s[i: j + 1]):
                    cur.append(s[i: j + 1])
                    self.partitionRecu(result, cur, s, j + 1)
                    cur.pop()

    def isPalindrome(self, s):
        for i in range(len(s) / 2):
            if s[i] != s[-(i + 1)]:
                return False
        return True