"""

1010. Pairs of Songs With Total Durations Divisible by 60
Medium

You are given a list of songs where the ith song has a duration of time[i] seconds.

Return the number of pairs of songs for which their total duration in seconds is divisible by 60. Formally, we want the number of indices i, j such that i < j with (time[i] + time[j]) % 60 == 0.

 

Example 1:

Input: time = [30,20,150,100,40]
Output: 3
Explanation: Three pairs have a total duration divisible by 60:
(time[0] = 30, time[2] = 150): total duration 180
(time[1] = 20, time[3] = 100): total duration 120
(time[1] = 20, time[4] = 40): total duration 60
Example 2:

Input: time = [60,60,60]
Output: 3
Explanation: All three pairs have a total duration of 120, which is divisible by 60.
 

Constraints:

1 <= time.length <= 6 * 104
1 <= time[i] <= 500

"""

# V0
# IDEA : dict
# IDEA : NOTE : we only count "NUMBER OF PAIRS", instead get all pairs indexes
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        d = {}
        pairs = 0
        for t in time:
            #print ("d = " + str(d))
            t %= 60
            ### NOTE this : (60 - t) % 60
            if (60 - t) % 60 in d:
                """
                NOTE : this trick
                -> we append "all 60 duration combinations count" via the existing times of element "(60 - t) % 60" 
                """
                ### NOTE this : (60 - t) % 60
                pairs += d[(60 - t) % 60]
            ### NOTE : we need to make d[t] = 1 if t not in d
            if t not in d:
                d[t] = 1
            else:
                ### NOTE : here : we plus 1 when an element already exist
                d[t] += 1
        return pairs

# V1
# IDEA : HASH TABLE
# https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/discuss/964284/Python-single-pass
from collections import defaultdict
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        rem = defaultdict(int)
        pairs = 0
        for t in time:
            t %= 60
            pairs += rem[(60 - t) % 60]
            rem[t] += 1
        return pairs

# V1'
# IDEA : HASH TABLE
# https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/discuss/847195/Python-hash-table
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        table = defaultdict(int)
        c = 0
        for t in time:
            if (60-(t%60))%60 in table:
                c+=table[(60-(t%60))%60]
            table[(t%60)] +=1
        return c

# V1''
# IDEA : HASH TABLE
# https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/discuss/257165/2-Sum-Python
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        d = {}
        
        for t in time:
            rem  =  t % 60
            if rem in d:
                d[rem] = d[rem] + 1
            else:
                d[rem] = 1
        
        res = 0
        
        for i in range(1,30):
            if i in d:
                a = d[i]
                j = 60 - i
                if j in d:
                    b = d[j]
                    res += a*b
        if 0 in d:
            a = d[0]
            res += a*(a-1)/2
        if 30 in d:
            a = d[30]
            res += a*(a-1)/2  
        
        return res

# V1'''
# https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/discuss/915768/Python-Solution
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        import operator as op
        from functools import reduce

        def ncr(n, r):
            r = min(r, n-r)
            numer = reduce(op.mul, range(n, n-r, -1), 1)
            denom = reduce(op.mul, range(1, r+1), 1)
            return numer // denom  
        a = [0]*60
        for i in time:
            i %= 60
            a[i] +=1
        ret = 0
        ret+= ncr(a[0],2) if a[0] > 1 else 0
        i = 1
        while i < 30:
            ret+= a[i]*a[60-i]
            i+=1
        ret+=ncr(a[30],2) if a[30] > 1 else 0
        return ret

# V1'''''
# IDEA : collections.Counter
# https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/discuss/313790/Python-3
class Solution:
    def numPairsDivisibleBy60(self, time):
        di = collections.Counter([60 if t % 60 == 0 else t % 60 for t in time])
        return sum(di[key] * di[60 - key] if key < 30 else 0 if key % 30 else sum(range(1, di[key])) for key in di)

# V2