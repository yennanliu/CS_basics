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


# V1  : dev 



# V2 
class Solution:
    # @return a string
    def countAndSay(self, n):
        seq = "1"
        for i in xrange(n - 1):
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



