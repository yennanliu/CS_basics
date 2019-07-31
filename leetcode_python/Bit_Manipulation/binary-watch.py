# V0

# V1 
# https://www.jianshu.com/p/7723d729da3b
# IDEA : GREEDY 
class Solution(object):
    def readBinaryWatch(self, num):
        return ['%d:%02d' % (h, m)
                for h in range(12) for m in range(60)
                if (bin(h) + bin(m)).count('1') == num]
                
# V1'
# https://www.jianshu.com/p/7723d729da3b
# IDEA : HASH MAP + GREEDY 
class Solution(object):
    # Binary Watch
    def readBinaryWatch(self, num):
        """
        :type num: int
        :rtype: List[str]
        """
        hourMap = {i: self.countOfBits(i) for i in range(12)}
        minutesMap = {i: self.countOfBits(i) for i in range(60)}
        res = []
        for hour in filter(lambda x: hourMap[x] <= num, range(12)):
            res += ["%s" % hour + ":" + ("%s" % minute).zfill(2) for minute in filter(lambda x: minutesMap[x] == num - hourMap[hour], range(60))]
        return res

    def countOfBits(self, num):
        counter = 0
        while num:
            counter += 1
            num &= num - 1
        return counter

# V1'' 
# https://www.jianshu.com/p/7723d729da3b
# IDEA : MATH 
class Solution(object):
    def readBinaryWatch(self, num):
        """
        :type num: int
        :rtype: List[str]
        """
        res = []
        if num > 10 or num < 0:
            return res
        for comb in itertools.combinations(range(10), num):
            h = int("".join(['1' if i in comb
                             else '0'
                             for i in range(4)]), 2)
            m = int("".join(['1' if i in comb
                             else '0'
                             for i in range(4, 10)]), 2)
            if h < 12 and m < 60:
                res.append(str(h) + ":" + str(m).zfill(2))
        return res 

# V1'''
# http://bookshadow.com/weblog/2016/09/18/leetcode-binary-watch/
# IDEA : BIT MANIPULATION 
class Solution(object):
    def readBinaryWatch(self, num):
        """
        :type num: int
        :rtype: List[str]
        """
        ans = []
        for x in range(1024):
            if bin(x).count('1') == num:
                h, m = x >> 6, x & 0x3F
                if h < 12 and m < 60:
                    ans.append('%d:%02d' % (h, m))
        return ans

# V1''''
# http://bookshadow.com/weblog/2016/09/18/leetcode-binary-watch/
# IDEA : GREEDY 
class Solution(object):
    def readBinaryWatch(self, num):
        """
        :type num: int
        :rtype: List[str]
        """
        ans = []
        for h in range(12):
            for m in range(60):
                if (bin(h)+ bin(m)).count('1') == num:
                    ans.append('%d:%02d' % (h, m))
        return ans

# V2 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def readBinaryWatch(self, num):
        """
        :type num: int
        :rtype: List[str]
        """
        def bit_count(bits):
            count = 0
            while bits:
                bits &= bits-1
                count += 1
            return count

        return ['%d:%02d' % (h, m) for h in range(12) for m in range(60)
                if bit_count(h) + bit_count(m) == num]

    def readBinaryWatch2(self, num):
        """
        :type num: int
        :rtype: List[str]
        """
        return ['{0}:{1}'.format(str(h), str(m).zfill(2))
                for h in range(12) for m in range(60)
                if (bin(h) + bin(m)).count('1') == num]
