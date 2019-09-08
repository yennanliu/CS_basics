# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/84728534
# IDEA : GREEDY 
class Solution(object):
    def largestTimeFromDigits(self, A):
        """
        :type A: List[int]
        :rtype: str
        """
        A.sort()
        for h in range(23, -1, -1):
            for m in range(59, -1, -1):
                t = [h / 10, h % 10, m / 10, m % 10]
                ts = sorted(t)
                if ts == A:
                    return str(t[0]) + str(t[1]) + ":" + str(t[2]) + str(t[3])
        return ""

# V1'
# https://www.cnblogs.com/seyjs/p/10058403.html
class Solution(object):
    def largestTimeFromDigits(self, A):
        """
        :type A: List[int]
        :rtype: str
        """
        import itertools
        res = -1
        for i in itertools.permutations(A,4):
            if i[0]*10 + i[1] < 24 and i[2]*10 + i[3] < 60:
                res = max(res,(i[0]*1000+i[1]*100+i[2]*10+i[3]))
        if res == -1:
            return  ''
        res = str(res).zfill(4)
        return res[0:2] + ':' + res[2:]
        
# V2 
# Time:  O(1)
# Space: O(1)
import itertools
class Solution(object):
    def largestTimeFromDigits(self, A):
        """
        :type A: List[int]
        :rtype: str
        """
        result = ""
        for i in range(len(A)):
            A[i] *= -1
        A.sort()
        for h1, h2, m1, m2 in itertools.permutations(A):
            hours = -(10*h1 + h2)
            mins = -(10*m1 + m2)
            if 0 <= hours < 24 and 0 <= mins < 60:
                result = "{:02}:{:02}".format(hours, mins)
                break
        return result