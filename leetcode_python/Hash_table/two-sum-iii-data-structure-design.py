"""

170. Two Sum III - Data structure design
Easy

Design a data structure that accepts a stream of integers and checks if it has a pair of integers that sum up to a particular value.

Implement the TwoSum class:

TwoSum() Initializes the TwoSum object, with an empty array initially.
void add(int number) Adds number to the data structure.
boolean find(int value) Returns true if there exists any pair of numbers whose sum is equal to value, otherwise, it returns false.
 

Example 1:

Input
["TwoSum", "add", "add", "add", "find", "find"]
[[], [1], [3], [5], [4], [7]]
Output
[null, null, null, null, true, false]

Explanation
TwoSum twoSum = new TwoSum();
twoSum.add(1);   // [] --> [1]
twoSum.add(3);   // [1] --> [1,3]
twoSum.add(5);   // [1,3] --> [1,3,5]
twoSum.find(4);  // 1 + 3 = 4, return true
twoSum.find(7);  // No two integers sum up to 7, return false
 

Constraints:

-105 <= number <= 105
-231 <= value <= 231 - 1
At most 104 calls will be made to add and find.

"""

# V0
# IDEA : hashmap + 2 sum
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
# IDEA : SORTED LIST
# https://leetcode.com/problems/two-sum-iii-data-structure-design/solution/
class TwoSum(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.nums = []
        self.is_sorted = False


    def add(self, number):
        """
        Add the number to an internal data structure..
        :type number: int
        :rtype: None
        """
        # Inserting while maintaining the ascending order.
        # for index, num in enumerate(self.nums):
        #     if number <= num:
        #         self.nums.insert(index, number)
        #         return
        ## larger than any number
        #self.nums.append(number)

        self.nums.append(number)
        self.is_sorted = False
    

    def find(self, value):
        """
        Find if there exists any pair of numbers which sum is equal to the value.
        :type value: int
        :rtype: bool
        """
        if not self.is_sorted:
            self.nums.sort()
            self.is_sorted = True

        low, high = 0, len(self.nums)-1
        while low < high:
            currSum = self.nums[low] + self.nums[high]
            if currSum < value:
                low += 1
            elif currSum > value:
                high -= 1
            else: # currSum == value
                return True
        
        return False


# V1'
# IDEA : HASHMAP
# https://leetcode.com/problems/two-sum-iii-data-structure-design/solution/
class TwoSum(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.num_counts = {}


    def add(self, number):
        """
        Add the number to an internal data structure..
        :type number: int
        :rtype: None
        """
        if number in self.num_counts:
            self.num_counts[number] += 1
        else:
            self.num_counts[number] = 1

    def find(self, value):
        """
        Find if there exists any pair of numbers which sum is equal to the value.
        :type value: int
        :rtype: bool
        """
        for num in self.num_counts.keys():
            comple = value - num
            if num != comple:
                if comple in self.num_counts:
                    return True
            elif self.num_counts[num] > 1:
                return True
        
        return False

# V1''
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
