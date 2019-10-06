# Time:  O(n)
# Space: O(10) = O(1)
# You are playing the following Bulls and Cows game with your friend:
# You write a 4-digit secret number and ask your friend to guess it,
# each time your friend guesses a number, you give a hint, the hint
# tells your friend how many digits are in the correct positions
# (called "bulls") and how many digits are in the wrong positions
# (called "cows"), your friend will use those hints to find out the
# secret number.
#
# For example:
#
# Secret number:  1807
# Friend's guess: 7810
# Hint: 1 bull and 3 cows. (The bull is 8, the cows are 0, 1 and 7.)
# According to Wikipedia: "Bulls and Cows (also known as Cows and Bulls
# or Pigs and Bulls or Bulls and Cleots) is an old code-breaking mind or
# paper and pencil game for two or more players, predating the similar
# commercially marketed board game Mastermind. The numerical version of
# the game is usually played with 4 digits, but can also be played with
# 3 or any other number of digits."
#
# Write a function to return a hint according to the secret number and
# friend's guess, use A to indicate the bulls and B to indicate the cows,
# in the above example, your function should return 1A3B.
#
# You may assume that the secret number and your friend's guess only contain
# digits, and their lengths are always equal.
#

# V0

# V1 
# http://bookshadow.com/weblog/2015/10/31/leetcode-bulls-and-cows/
# IDEA :  OPERATOR.eq()
# https://docs.python.org/3.0/library/operator.html
# In [19]: operator.eq(1,1)
# Out[19]: True
# In [20]: operator.eq(1,2)
# Out[20]: False
import collections, operator
class Solution(object):
    def getHint(self, secret, guess):
        """
        :type secret: str
        :type guess: str
        :rtype: str
        """
        bull = sum(map(operator.eq, secret, guess))  # find the number of digits with the same index in secret, and guess
        sa = collections.Counter(secret)
        sb = collections.Counter(guess)
        cow = sum((sa & sb).values()) - bull         # (find the number of digits that exist in both secret, and guess) - bull 
        return str(bull) + 'A' + str(cow) + 'B'

# V1' 
# http://bookshadow.com/weblog/2015/10/31/leetcode-bulls-and-cows/
def getHint(self, secret, guess):
    bulls = sum(map(operator.eq, secret, guess))
    both = sum(min(secret.count(x), guess.count(x)) for x in '0123456789')
    return '%dA%dB' % (bulls, both - bulls)

# V1''
# https://www.jiuzhang.com/solution/bulls-and-cows/#tag-highlight-lang-python
class Solution:
    def getHint(self, secret, guess):
        a , b , n = 0 , 0 , len(secret)
        ds = [0 for i in range(10)]
        dg = [0 for i in range(10)]
        for i in range (n) :
            x = ord(secret[i]) - ord('0')
            y = ord(guess[i]) - ord('0')
            if x== y:
                a += 1
            ds[x] += 1
            dg[y] += 1
        for i in range (10) :
            b += min(ds[i],dg[i])
        return str(a) + 'A' + str(b - a) + 'B'

# V2 
# Time:  O(n)
# Space: O(10) = O(1)
import operator
# One pass solution.
from collections import defaultdict, Counter
from itertools import izip, imap
class Solution(object):
    def getHint(self, secret, guess):
        """
        :type secret: str
        :type guess: str
        :rtype: str
        """
        A, B = 0, 0
        s_lookup, g_lookup = defaultdict(int), defaultdict(int)
        for s, g in izip(secret, guess):
            if s == g:
                A += 1
            else:
                if s_lookup[g]:
                    s_lookup[g] -= 1
                    B += 1
                else:
                    g_lookup[g] += 1
                if g_lookup[s]:
                    g_lookup[s] -= 1
                    B += 1
                else:
                    s_lookup[s] += 1

        return "%dA%dB" % (A, B)

# Two pass solution.
class Solution2(object):
    def getHint(self, secret, guess):
        """
        :type secret: str
        :type guess: str
        :rtype: str
        """
        A = sum(imap(operator.eq, secret, guess))
        B = sum((Counter(secret) & Counter(guess)).values()) - A
        return "%dA%dB" % (A, B)
