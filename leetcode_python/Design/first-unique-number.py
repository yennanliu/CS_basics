"""

1429. First Unique Number

# https://leetcode.ca/all/1429.html

You have a queue of integers, you need to retrieve the first unique integer in the queue.

Implement the FirstUnique class:

FirstUnique(int[] nums) Initializes the object with the numbers in the queue.
int showFirstUnique() returns the value of the first unique integer of the queue, and returns -1 if there is no such integer.
void add(int value) insert value to the queue.
 

Example 1:

Input: 
["FirstUnique","showFirstUnique","add","showFirstUnique","add","showFirstUnique","add","showFirstUnique"]
[[[2,3,5]],[],[5],[],[2],[],[3],[]]
Output: 
[null,2,null,2,null,3,null,-1]
Explanation: 
FirstUnique firstUnique = new FirstUnique([2,3,5]);
firstUnique.showFirstUnique(); // return 2
firstUnique.add(5);            // the queue is now [2,3,5,5]
firstUnique.showFirstUnique(); // return 2
firstUnique.add(2);            // the queue is now [2,3,5,5,2]
firstUnique.showFirstUnique(); // return 3
firstUnique.add(3);            // the queue is now [2,3,5,5,2,3]
firstUnique.showFirstUnique(); // return -1

Example 2:

Input: 
["FirstUnique","showFirstUnique","add","add","add","add","add","showFirstUnique"]
[[[7,7,7,7,7,7]],[],[7],[3],[3],[7],[17],[]]
Output: 
[null,-1,null,null,null,null,null,17]
Explanation: 
FirstUnique firstUnique = new FirstUnique([7,7,7,7,7,7]);
firstUnique.showFirstUnique(); // return -1
firstUnique.add(7);            // the queue is now [7,7,7,7,7,7,7]
firstUnique.add(3);            // the queue is now [7,7,7,7,7,7,7,3]
firstUnique.add(3);            // the queue is now [7,7,7,7,7,7,7,3,3]
firstUnique.add(7);            // the queue is now [7,7,7,7,7,7,7,3,3,7]
firstUnique.add(17);           // the queue is now [7,7,7,7,7,7,7,3,3,7,17]
firstUnique.showFirstUnique(); // return 17

Example 3:

Input: 
["FirstUnique","showFirstUnique","add","showFirstUnique"]
[[[809]],[],[809],[]]
Output: 
[null,809,null,-1]
Explanation: 
FirstUnique firstUnique = new FirstUnique([809]);
firstUnique.showFirstUnique(); // return 809
firstUnique.add(809);          // the queue is now [809,809]
firstUnique.showFirstUnique(); // return -1
 

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^8
1 <= value <= 10^8
At most 50000 calls will be made to showFirstUnique and add.


"""

# V0

# V1
# https://blog.csdn.net/Changxing_J/article/details/109854442
# IDEA : dequeue
from collections import Counter, deque
class FirstUnique:
    
    def __init__(self, nums):
        self.count = Counter(nums)
        self.array = deque(nums)
        self.first = -1
        self._find()

    def showFirstUnique(self):
        return self.first

    def add(self, value):
        self.count[value] += 1
        self.array.append(value)
        if self.first == -1 or value == self.first:
            self._find()

    def _find(self):
        self.first = - 
        while self.array:
            first = self.array.popleft()
            if self.count[first] == 1:
                self.first = first
                break

# V1'
# IDEA : dequeue
# https://blog.csdn.net/sinat_30403031/article/details/116664368
from collections import Counter, deque, defaultdict
from heapq import heappush, heappop
class FirstUnique:

    def __init__(self, nums):
        self.nums = nums
        self.dic = []
        self.pos = set()
        self.Counter = defaultdict(int)
        for index, num in enumerate(self.nums):
            self.counter[num] += 1
            if self.counter[num] == 1:
                heappush(self.pic, (index, num))
                self.pos.add(num)
            elif num in self.pos and self.counter[num] == 2:
                self.pos.remove(num)

    def showFirstUnique(self):
        while len(self.dic):
            index, number = heappop(self.dic)
            if number in self.pos:
                heappush(self.dic, (index, number))
                return number
        return -1

    def add(self, value):
        self.nums.append(value)
        self.Counter[value] += 1
        if value in self.pos:
            self.pos.remove(value)
        elif self.Counter[value] == 1:
            self.pos.add(value)
            heappush(self.dic, (len(self.nums)-1, value))

# V1''
# https://github.com/jyj407/leetcode/blob/master/1429.md
class FirstUnique:

    def __init__(self, nums):
        self.data = []
        self.mp = dict()
        
        for num in nums :
            self.data.append(num);
            self.mp[num] = self.mp.setdefault(num, 0) + 1

    def showFirstUnique(self):
        for num in self.data :
            if (self.mp[num] == 1) :
                return num;

        return -1;


    def add(self, value):
        self.data.append(value)
        self.mp[value] =  self.mp.setdefault(value, 0) + 1

# V1'''
# https://github.com/jyj407/leetcode/blob/master/1429.md
class FirstUnique:

    def __init__(self, nums):
        self.allDict = dict()
        self.unique = list()
        for  num in nums :
            self.allDict[num] = self.allDict.setdefault(num, 0) + 1
            self.updateUniqueSet(num)

    def updateUniqueSet(self, num) :
        if (self.allDict[num] == 1) :
            self.unique.append(num)
        elif (self.allDict[num] > 1) :
            if (num in self.unique):
                self.unique.remove(num)

    def showFirstUnique(self):
        if (not self.unique) :
            return -1

        return self.unique[0]

    def add(self, value):
        self.allDict[value] = self.allDict.setdefault(value, 0) + 1
        self.updateUniqueSet(value)

# V1''''
# https://github.com/jyj407/leetcode/blob/master/1429.md
class FirstUnique2 :

    def __init__(self, nums):
        self.allDict = dict()
        self.unique = list()
        for  num in nums :
            self.allDict[num] = self.allDict.setdefault(num, 0) + 1
            self.updateUniqueSet(num)

    def updateUniqueSet(self, num) :
        if (self.allDict[num] == 1) :
            self.unique.append(num)
        elif (self.allDict[num] > 1) :
            if (num in self.unique):
                self.unique.remove(num)
    
    def showFirstUnique(self):
        if (not self.unique) :
            return -1

        return self.unique[0]

    def add(self, value):
        self.allDict[value] = self.allDict.setdefault(value, 0) + 1
        self.updateUniqueSet(value)

# V1'''''
# https://leetcode.ca/2019-10-29-1429-First-Unique-Number/
# C++
# / OJ: https://leetcode.com/problems/first-unique-number/
#
# // Time:
# //    FirstUnique: O(N)
# //    showFirstUnique: O(1)
# //    add: O(1)
# // Space: O(N)
# class FirstUnique {
#     list<int> data;
#     typedef list<int>::iterator iter;
#     unordered_map<int, iter> m;
#     unordered_set<int> s;
# public:
#     FirstUnique(vector<int>& nums) {
#         for (int n : nums) add(n);
#     }
#    
#     int showFirstUnique() {
#         return data.size() ? data.front() : -1;
#     }
#    
#     void add(int value) {
#         if (s.count(value)) return;
#         if (m.count(value)) {
#             data.erase(m[value]);
#             s.insert(value);
#         } else {
#             data.push_back(value);
#             m[value] = prev(data.end());
#         }
#     }
# };

# V2