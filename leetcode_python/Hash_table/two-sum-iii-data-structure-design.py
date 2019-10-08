"""
Design and implement a TwoSum class. It should support the following operations: add and find.

add - Add the number to an internal data structure.
find - Find if there exists any pair of numbers which sum is equal to the value.

For example,

add(1); add(3); add(5);
find(4) -> true
find(7) -> false

"""

# Time:  O(n)
# Space: O(n)

# Design and implement a TwoSum class. It should support the following operations: add and find.
#
# add - Add the number to an internal data structure.
# find - Find if there exists any pair of numbers which sum is equal to the value.
#
# For example,
# add(1); add(3); add(5);
# find(4) -> true
# find(7) -> false

# V0
class TwoSum(object):

    def __init__(self):
        self.dic = {}

    def add(self, number):
        if number not in self.dic:
            self.dic[number] = 1
        else:
            self.dic[number] += 1

    def find(self, value):
        dic = self.dic
        for num in dic:
            # Find if there exists any pair of numbers (in the dict) which sum is equal to the value.
            if value - num in dic and (value - num != num or dic[num] > 1): # 2 sum, so the sum can only be came from 2 number in the dict
                return True
        return False


# V1 
# http://www.voidcn.com/article/p-qhmcmxvf-qp.html
class TwoSum(object):

    def __init__(self):
        self.dic = {}

    def add(self, number):
        if number not in self.dic:
            self.dic[number] = 1
        else:
            self.dic[number] += 1

    def find(self, value):
        dic = self.dic
        for num in dic:
            """
            This is actually the "2 sum" problem 
            # 001 TWO SUM 
            # https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Hash_table/two-sum.py
            1.  Find if there exists any pair of numbers (in the dict) which sum is equal to the value.
            2.  Consinder two cases in below logic:
                GIVEN num + sth = value ->  sth = value - num
                case 1) sth ! = num --> value - num in dic and value - num != num
                case 2) sth == num --> value - num in dic and dic[num] > 1

                --> sum up case 1) & 2) : value - num in dic and (value - num != num or dic[num] > 1)
            """
            if value - num in dic and (value - num != num or dic[num] > 1): 
                return True
        return False

# V2 
from collections import defaultdict
class TwoSum(object):

    def __init__(self):
        """
        initialize your data structure here
        """
        self.lookup = defaultdict(int)



    def add(self, number):
        """
        Add the number to an internal data structure.
        :rtype: nothing
        """
        self.lookup[number] += 1


    def find(self, value):
        """
        Find if there exists any pair of numbers which sum is equal to the value.
        :type value: int
        :rtype: bool
        """
        for key in self.lookup:
            num = value - key
            if num in self.lookup and (num != key or self.lookup[key] > 1):
                return True
        return False

# V3 
# Time:  O(n)
# Space: O(n)
from collections import defaultdict
class TwoSum(object):

    def __init__(self):
        """
        initialize your data structure here
        """
        self.lookup = defaultdict(int)



    def add(self, number):
        """
        Add the number to an internal data structure.
        :rtype: nothing
        """
        self.lookup[number] += 1


    def find(self, value):
        """
        Find if there exists any pair of numbers which sum is equal to the value.
        :type value: int
        :rtype: bool
        """
        for key in self.lookup:
            num = value - key
            if num in self.lookup and (num != key or self.lookup[key] > 1):
                return True
        return False
