# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80677194
class Solution(object):
    def ambiguousCoordinates(self, S):
        """
        :type S: str
        :rtype: List[str]
        """
        ans = []
        S = S[1:-1]
        for i in range(1, len(S)):
            left, right = S[:i], S[i:]
            left_list = self.get_number(left)
            right_list = self.get_number(right)
            if left_list and right_list:
                for left_number in left_list:
                    for right_number in right_list:
                        ans.append("(" + left_number + ", " + right_number + ")")
        return ans

    def get_number(self, num):
        decimal_list = []
        if len(num) == 1 or num[0] != '0':
            decimal_list.append(num)
        for i in range(1, len(num)):
            integer, fractor = num[:i], num[i:]
            print(integer, fractor)
            if (len(integer) > 1 and integer[0] == '0') or fractor[-1] == '0':
                continue
            decimal_list.append(integer + '.' + fractor)
        return decimal_list
        
# V2 
# Time:  O(n^4)
# Space: O(n)
import itertools
class Solution(object):
    def ambiguousCoordinates(self, S):
        """
        :type S: str
        :rtype: List[str]
        """
        def make(S, i, n):
            for d in range(1, n+1):
                left = S[i:i+d]
                right = S[i+d:i+n]
                if ((not left.startswith('0') or left == '0')
                        and (not right.endswith('0'))):
                    yield "".join([left, '.' if right else '', right])

        return ["({}, {})".format(*cand)
                for i in range(1, len(S)-2)
                for cand in itertools.product(make(S, 1, i),
                                              make(S, i+1, len(S)-2-i))]