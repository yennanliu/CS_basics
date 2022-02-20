"""

828. Count Unique Characters of All Substrings of a Given String
Hard

Let's define a function countUniqueChars(s) that returns the number of unique characters on s.

For example, calling countUniqueChars(s) if s = "LEETCODE" then "L", "T", "C", "O", "D" are the unique characters since they appear only once in s, therefore countUniqueChars(s) = 5.
Given a string s, return the sum of countUniqueChars(t) where t is a substring of s.

Notice that some substrings can be repeated so in this case you have to count the repeated ones too.

 

Example 1:

Input: s = "ABC"
Output: 10
Explanation: All possible substrings are: "A","B","C","AB","BC" and "ABC".
Evey substring is composed with only unique letters.
Sum of lengths of all substring is 1 + 1 + 1 + 2 + 2 + 3 = 10
Example 2:

Input: s = "ABA"
Output: 8
Explanation: The same as example 1, except countUniqueChars("ABA") = 1.
Example 3:

Input: s = "LEETCODE"
Output: 92
 

Constraints:

1 <= s.length <= 105
s consists of uppercase English letters only.

"""

# V0

# V1
# https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/discuss/128952/C%2B%2BJavaPython-One-pass-O(N)
# IDEA :
# Let's think about how a character can be found as a unique character.
# Think about string "XAXAXXAX" and focus on making the second "A" a unique character.
# We can take "XA(XAXX)AX" and between "()" is our substring.
# We can see here, to make the second "A" counted as a uniq character, we need to:
# insert "(" somewhere between the first and second A
# insert ")" somewhere between the second and third A
# For step 1 we have "A(XA" and "AX(A", 2 possibility.
# For step 2 we have "A)XXA", "AX)XA" and "AXX)A", 3 possibilities.
# So there are in total 2 * 3 = 6 ways to make the second A a unique character in a substring.
# In other words, there are only 6 substring, in which this A contribute 1 point as unique string.
# Instead of counting all unique characters and struggling with all possible substrings,
# we can count for every char in S, how many ways to be found as a unique char.
# We count and sum, and it will be out answer.
class Solution(object):
     def uniqueLetterString(self, S):
            index = {c: [-1, -1] for c in string.ascii_uppercase}
            res = 0
            for i, c in enumerate(S):
                k, j = index[c]
                res += (i - j) * (j - k)
                index[c] = [j, i]
            for c in index:
                k, j = index[c]
                res += (len(S) - j) * (j - k)
            return res % (10**9 + 7)

# V1'
# https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/discuss/128974/10-line-Python-Solution
class Solution:
    def uniqueLetterString(self, S):
        N, S, ret = len(S), [ord(_)-ord('a') for _ in S.lower()], 0
        for c in range(26):
            idxs = [i for i in range(N) if S[i]==c]
            for i in range(len(idxs)):
                idx = idxs[i]
                prev = idxs[i-1] if i-1>=0 else -1
                succ = idxs[i+1] if i+1<len(idxs) else N
                ret += (idx-prev)*(succ-idx)
        return ret%(10**9+7)

# V1''
# https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/discuss/177144/python-concise-100
class Solution(object):
    def uniqueLetterString(self, S):
        a,b,s,n=[-1]*26,[-1]*26,0,ord('A')
        for i in range(len(S)):
            t=ord(S[i])-n
            s+=(b[t]-a[t])*(i-b[t])
            a[t],b[t]=b[t],i
        for i in range(26): s+=(b[i]-a[i])*(len(S)-b[i])
        return s%(10**9+7)

# V1'''
# https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/discuss/1562768/Python-O(n)
# IDEA : 
#For any char c find closest equal chars in front and in back. c...c...c. Then initial char c is counted in substrings for which it is unique character, so that substring cannot contain closest front and back chars c and needs to contain original c. To compute such substrings multiply possibilities for left and right endings of the substring (c..left...c...right...c). And sum these values for all chars in s. This will count in a different way the value we want.
class Solution:
    def uniqueLetterString(self, s):
        n = len(s)
        nb_front, nb_back = [n] * n, [-1] * n
        ch2index_front, ch2index_back = {}, {}
        for b in range(n):
            f = n - 1 - b
            if s[f] in ch2index_front:
                nb_front[f] = ch2index_front[s[f]]
            if s[b] in ch2index_back:
                nb_back[b] = ch2index_back[s[b]]
            ch2index_front[s[f]] = f
            ch2index_back[s[b]] = b

        return sum((nb_front[i] - i) * (i - nb_back[i]) for i in range(n))

# V1''''
# https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/discuss/423812/Python-detailed-explanation
class Solution:
    def uniqueLetterString(self, S):
        res=[0]*(len(S)+1)
        idxs=[[-1,-1]]*26
        for i,c in enumerate(S):
            code=ord(c)-ord('A')
            first,second=idxs[code]
            res[i+1]=1+res[i]+(i-1-second)-(second-first)
            idxs[code]=[second,i]
        return sum(res)%(10**9+7)

# V1'''''
# IDEA : DP
# https://blog.csdn.net/qq_17550379/article/details/103064459
class Solution(object):
    def uniqueLetterString(self, s):
        res = 0
        mod = 10**9 + 7
        f = 0
        m, n = [0]*26, [0]*26
        for i, v in enumerate(s):
            t = ord(v) - 65
            f = f + 1 + i - 2 * m[t] + n[t]
            res = (res + f) % mod
            n[t], m[t] = m[t], i+1
        return res
        
# V2