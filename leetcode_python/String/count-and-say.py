# Time:  O(n * 2^n)
# Space: O(2^n)
#
# The count-and-say sequence is the sequence of integers beginning as follows:
# 1, 11, 21, 1211, 111221, ...
#
# 1 is read off as "one 1" or 11.
# 11 is read off as "two 1s" or 21.
# 21 is read off as "one 2, then one 1" or 1211.
# Given an integer n, generate the nth sequence.
#
# Note: The sequence of integers will be represented as a string.
#

"""

i.e. 

n = 1  ->  1   
n = 2  ->  11       ( there is 1 "1" when n=1 )
n = 3  ->  21       ( there is 2 "1" when n=2 )
n = 4  ->  1211     ( there is 1 "2" and 1 "1"  when n= 3 )
n = 5  ->  111221   ( there is 1 "1" and 1 "2" and  2 "1"  when n=4 )
n = 6  ->  312211   ( there is 3 "1" and 2 "2" and 1 "1" when n = 5  )
.
.
.

"""

# V0 


# V1 
# https://blog.csdn.net/XX_123_1_RJ/article/details/80957046
# IDEA : RECURSION  
class Solution(object):
    def countAndSay(self, n):  # recursion 
        if n == 1: return '1'  # end loop of recursion 
        s = self.countAndSay(n-1)
        res, count = '', 0
        for i in range(len(s)):
            count += 1
            if i == len(s) - 1 or s[i] != s[i+1]:  # end before meet the last string element 
                res += str(count)
                res += s[i]
                count = 0
        return res

# V1' 
# https://blog.csdn.net/XX_123_1_RJ/article/details/80957046
# IDEA : ITERATION 
class Solution(object):
    def countAndSay1(self, n):  # iteration 
        if n == 1: return '1'
        res = '1'
        while n > 1:
            s, res, count = res, '', 0
            for i in range(len(s)):
                count += 1
                if i == len(s) - 1 or s[i] != s[i + 1]:  # end before meet the last string element 
                    res += str(count)
                    res += s[i]
                    count = 0
            n -= 1
        return res

# V2 
class Solution:
    # @return a string
    def countAndSay(self, n):
        seq = "1"
        for i in range(n - 1):
            seq = self.getNext(seq)
        return seq

    def getNext(self, seq):
        i, next_seq = 0, ""
        while i < len(seq):
            cnt = 1
            while i < len(seq) - 1 and seq[i] == seq[i + 1]:
                cnt += 1
                i += 1
            next_seq += str(cnt) + seq[i]
            i += 1
        return next_seq
