# V0
# IDEA : SET
import random
class RandomizedSet(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.set = set()
        self.size = 0

    def insert(self, val):
        """
        Inserts a value to the set. Returns true if the set did not already contain the specified element.
        :type val: int
        :rtype: bool
        """
        if val not in self.set:
            self.set.add(val)
            self.size += 1
            return True
        return False

    def remove(self, val):
        """
        Removes a value from the set. Returns true if the set contained the specified element.
        :type val: int
        :rtype: bool
        """
        if val in self.set:
            self.set.remove(val)
            self.size -= 1
            return True
        return False

    def getRandom(self):
        """
        Get a random element from the set.
        :rtype: int
        """
        ind = random.randint(0, self.size - 1)
        return list(self.set)[ind]

# V0'
# IDEA : LIST + DICT
class RandomizedSet(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.nums, self.pos = list(), dict()

    def insert(self, val):
        """
        Inserts a value to the set. Returns true if the set did not already contain the specified element.
        :type val: int
        :rtype: bool
        """
        if val not in self.pos:
            self.nums.append(val)
            self.pos[val] = len(self.nums) - 1
            return True
        return False

    def remove(self, val):
        """
        Removes a value from the set. Returns true if the set contained the specified element.
        :type val: int
        :rtype: bool
        """
        if val in self.pos:
            ### NOTICE HERE 
            # IDEA : 
            # -> GET THE LAST IDX, ELEMENT IN THE LIST FIRST 
            # -> THEN SWAP LAST AND THE to-remove ELEMENT (val) 
            # -> SO ONCE REMOVE THE LAST ELEMENT IN THE LIST
            # -> WE CAN DO THE EXPECTED OP
            # -> UPDATE THE DICT ON ACCORDINGLY
            idx, last = self.pos[val], self.nums[-1]
            self.nums[idx] = last
            self.pos[last] = idx
            self.nums.pop()
            self.pos.pop(val, 0)
            return True
        return False

    def getRandom(self):
        """
        Get a random element from the set.
        :rtype: int
        """
        idx = random.randint(0, len(self.nums) - 1)
        return self.nums[idx]

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82732532
import random
class RandomizedSet(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.set = set()
        self.size = 0

    def insert(self, val):
        """
        Inserts a value to the set. Returns true if the set did not already contain the specified element.
        :type val: int
        :rtype: bool
        """
        if val not in self.set:
            self.set.add(val)
            self.size += 1
            return True
        return False

    def remove(self, val):
        """
        Removes a value from the set. Returns true if the set contained the specified element.
        :type val: int
        :rtype: bool
        """
        if val in self.set:
            self.set.remove(val)
            self.size -= 1
            return True
        return False

    def getRandom(self):
        """
        Get a random element from the set.
        :rtype: int
        """
        ind = random.randint(0, self.size - 1)
        return list(self.set)[ind]

### Test case 
r=RandomizedSet()
assert r.insert(1)==True
assert r.getRandom()==1
assert r.remove(1)==True
assert r.insert(2)==True
assert r.insert(3)==True
assert r.remove(100)==False

r=RandomizedSet()
assert r.remove(100)==False
assert r.insert(2)==True
assert r.remove(2)==True
assert r.remove(1)==False
assert r.insert(2)==True
assert r.insert(3)==True
assert r.getRandom() in [1,2,3]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82732532
class RandomizedSet(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.nums, self.pos = list(), dict()

    def insert(self, val):
        """
        Inserts a value to the set. Returns true if the set did not already contain the specified element.
        :type val: int
        :rtype: bool
        """
        if val not in self.pos:
            self.nums.append(val)
            self.pos[val] = len(self.nums) - 1
            return True
        return False

    def remove(self, val):
        """
        Removes a value from the set. Returns true if the set contained the specified element.
        :type val: int
        :rtype: bool
        """
        if val in self.pos:
            idx, last = self.pos[val], self.nums[-1]
            self.nums[idx] = last
            self.pos[last] = idx
            self.nums.pop()
            self.pos.pop(val, 0)
            return True
        return False

    def getRandom(self):
        """
        Get a random element from the set.
        :rtype: int
        """
        idx = random.randint(0, len(self.nums) - 1)
        return self.nums[idx]

# V1''
# http://bookshadow.com/weblog/2016/08/04/leetcode-insert-delete-getrandom-o1/
import random
class RandomizedSet(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.dataMap = {}
        self.dataList = []

    def insert(self, val):
        """
        Inserts a value to the set. Returns true if the set did not already contain the specified element.
        :type val: int
        :rtype: bool
        """
        if val in self.dataMap:
            return False
        self.dataMap[val] = len(self.dataList)
        self.dataList.append(val)
        return True

    def remove(self, val):
        """
        Removes a value from the set. Returns true if the set contained the specified element.
        :type val: int
        :rtype: bool
        """
        if val not in self.dataMap:
            return False
        idx = self.dataMap[val]
        tail = self.dataList.pop()
        if idx < len(self.dataList):
            self.dataList[idx] = tail
            self.dataMap[tail] = idx
        del self.dataMap[val]
        return True

    def getRandom(self):
        """
        Get a random element from the set.
        :rtype: int
        """
        return random.choice(self.dataList)

# V1'''
# https://www.hrwhisper.me/leetcode-insert-delete-getrandom-o1/
import random
class RandomizedSet(object):

    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.output = []
        self.index = {}
        

    def insert(self, val):
        """
        Inserts a value to the set. Returns true if the set did not already contain the specified element.
        :type val: int
        :rtype: bool
        """
        if val in self.index:
            return False
        self.index[val] = len(self.output)
        self.output.append(val)
        return True
        

    def remove(self, val):
        """
        Removes a value from the set. Returns true if the set contained the specified element.
        :type val: int
        :rtype: bool
        """
        if val not in self.index:
            return False
        index = self.index[val]
        last=self.output[-1]
        self.index[last] = index
        self.output[index] = last
        self.output.pop()
        del self.index[val]
        return True
        

    def getRandom(self):
        """
        Get a random element from the set.
        :rtype: int
        """
        index = random.randint(0,len(self.output)-1)
        return self.output[index]

# V2
