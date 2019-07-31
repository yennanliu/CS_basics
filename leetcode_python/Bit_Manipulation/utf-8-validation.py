# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82963310
class Solution(object):
    def validUtf8(self, data):
        """
        :type data: List[int]
        :rtype: bool
        """
        cnt = 0
        for d in data:
            if cnt == 0:
                if (d >> 5) == 0b110:
                    cnt = 1
                elif (d >> 4) == 0b1110:
                    cnt = 2
                elif (d >> 3) == 0b11110:
                    cnt = 3
                elif (d >> 7):
                    return False
            else:
                if (d >> 6) != 0b10:
                    return False
                cnt -= 1
        return cnt == 0

# V1' 
# http://bookshadow.com/weblog/2016/09/04/leetcode-utf-8-validation/
class Solution(object):
    def validUtf8(self, data):
        """
        :type data: List[int]
        :rtype: bool
        """
        masks = [0x0, 0x80, 0xE0, 0xF0, 0xF8]
        bits = [0x0, 0x0, 0xC0, 0xE0, 0xF0]
        while data:
            for x in (4, 3, 2, 1, 0):
                if data[0] & masks[x] == bits[x]:
                    break
            if x == 0 or len(data) < x:
                return False
            for y in range(1, x):
                if data[y] & 0xC0 != 0x80:
                    return False
            data = data[x:]
        return True
               
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def validUtf8(self, data):
        """
        :type data: List[int]
        :rtype: bool
        """
        count = 0
        for c in data:
            if count == 0:
                if (c >> 5) == 0b110:
                    count = 1
                elif (c >> 4) == 0b1110:
                    count = 2
                elif (c >> 3) == 0b11110:
                    count = 3
                elif (c >> 7):
                    return False
            else:
                if (c >> 6) != 0b10:
                    return False
                count -= 1
        return count == 0