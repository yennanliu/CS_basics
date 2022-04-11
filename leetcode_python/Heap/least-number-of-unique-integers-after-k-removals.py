"""

1481. Least Number of Unique Integers after K Removals
Medium

Given an array of integers arr and an integer k. Find the least number of unique integers after removing exactly k elements.

 

Example 1:

Input: arr = [5,5,4], k = 1
Output: 1
Explanation: Remove the single 4, only 5 is left.
Example 2:
Input: arr = [4,3,1,1,3,3,2], k = 3
Output: 2
Explanation: Remove 4, 2 and either one of the two 1s or three 3s. 1 and 3 will be left.
 

Constraints:

1 <= arr.length <= 10^5
1 <= arr[i] <= 10^9
0 <= k <= arr.length

"""

# V0
# IDEA : Counter
from collections import Counter
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        # edge case
        if not arr:
            return 0
        cnt = dict(Counter(arr))
        cnt_sorted = sorted(cnt.items(), key = lambda x : x[1])
        #print ("cnt_sorted = " + str(cnt_sorted))
        removed = 0
        for key, freq in cnt_sorted:
            """
            NOTE !!!
                -> we need to remove exactly k elements and make remain unique integers as less as possible
                -> since we ALREADY sort num_counter,
                -> so the elements NOW are ordering with their count
                    -> so we need to remove ALL element while k still > 0
                    -> so k -= freq, since for element key, there are freq count for it in arr
            """
            if freq <= k:
                k -= freq
                removed += 1

        return len(cnt.keys()) - removed

# V0
# IDEA : Counter
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        num_counter = Counter(arr)
        remove_ele = []
        _num_counter = sorted(num_counter.items(), key = lambda x : x[1])
        for key, freq in _num_counter:
            """
            NOTE !!!
                -> we need to remove exactly k elements and make remain unique integers as less as possible
                -> since we ALREADY sort num_counter,
                -> so the elements NOW are ordering with their count
                    -> so we need to remove ALL element while k still > 0
                    -> so k -= freq, since for element key, there are freq count for it in arr
            """
            if freq <= k:
                k -= freq
                remove_ele.append(key)
        return len(num_counter) - len(remove_ele)

# V0'
# IDEA : Counter
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        num_counter = Counter(arr)
        remove_ele = []
        for key, freq in sorted(num_counter.items(), key = lambda x : x[1]):
            if freq <= k:
                k -= freq
                remove_ele.append(key)
        return len(num_counter) - len(remove_ele)

# V0''
# IDEA : Counter + heapq
class Solution(object):
    def findLeastNumOfUniqueInts(self, arr, k):
            # use counter, and heap (priority queue)
            from collections import Counter
            import heapq
            h = []
            for key, val in Counter(arr).items():
                heapq.heappush(h,(val,key))

            while k > 0:
                item = heapq.heappop(h)    
                if item[0] != 1:
                    heapq.heappush(h, (item[0]-1, item[1]))      
                k -=1

            return len(h)

# V1
# IDEA : Counter
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/979896/Python-Solution
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        num_counter = Counter(arr)
        remove_ele = []
        for key, freq in sorted(num_counter.items(), key = lambda x : x[1]):
            if freq <= k:
                k -= freq
                remove_ele.append(key)
        return len(num_counter) - len(remove_ele)

# V1'
# IDEA : Counter
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/686293/Python-solution-with-Counter
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        from collections import Counter
        count = sorted(Counter(arr).items(), key=lambda x: x[1])  # sort dictionary by value increasing and get it as tuple
        removed = 0  # number of removed items
        for key, val in count: 
            if k >= val:  
                k -= val
                removed += 1
            else:
                break
        return len(count)-removed  # number of remained elements

# V1''
# IDEA : Counter
from collections import Counter
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        """
        Given an array of integers arr and number of removals k, this
        program uses the Python Counter to help it determine the least
        number of unique integers in arr after k removals.

        :param arr: array of integers
        :type arr: list[int]
        :param k: number of integer removals from arr
        :type k: int
        :return: least number of unique integers in arr after k removals
        :rtype: int
        """
        """
        Remove the integers with the lowest counts to get the least number
        of unique integers in arr
        """
        counts = Counter( arr ).most_common()
        unique = len( counts )
        while k > 0 and unique > 0:
            k -= counts[unique - 1][1]
            if k >= 0:
                unique -= 1
        return unique

# V1'''
# IDEA : Counter + heapq
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/686429/Python-heaps
from collections import Counter
from heapq import *
class Solution(object):
    def findLeastNumOfUniqueInts(self, arr, k):
        c = Counter(arr)
        heap = []
        for key, value in c.items():
            heappush(heap, (value, key))
        while heap and k > 0:
            value, key = heappop(heap)
            if value > 1:
                heappush(heap, (value-1, key))
            k -= 1
        return len(heap)

# V1''''
# IDEA : Counter + heapq
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/688829/Python-heap-solution
class Solution(object):
    def findLeastNumOfUniqueInts(self, arr, k):
        heap = []
        count = collections.Counter(arr)
        for key in count:
            heapq.heappush(heap, (count[key], key))
        
        while(heap and k):
            count, key = heapq.heappop(heap)
            if k >= count:
                k -= count
            else:
                return len(heap) + 1
        return len(heap)

# V1'''''
# IDEA : Counter + heapq
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/704179/python-solution%3A-Counter-and-Priority-Queue
# IDEA
# -> Count the occurence of each number.
# -> We want to delete the number with lowest occurence thus we can use minimum steps to reduce the total unique numbers in the list. For example,[4,3,1,1,3,3,2]. The Counter of this array will be: {3:3, 1:2, 4:1, 2:1}. Given k = 3, the greedy approach is to delete 2 and 4 first because both of them are appearing once. We need an ordering data structure to give us the lowest occurence of number each time. As you may know, Priority Queue comes to play
# -> Use heap to build PQ for the counter. We store each member as a tuple: (count, number) Python heap module will sort it based on the first member of the tuple.
# -> loop through k times to pop member out of heap and check if we need to push it back
class Solution(object):
    def findLeastNumOfUniqueInts(self, arr, k):
            # use counter, and heap (priority queue)
            from collections import Counter
            import heapq
            h = []
            for key, val in Counter(arr).items():
                heapq.heappush(h,(val,key))

            while k > 0:
                item = heapq.heappop(h)    
                if item[0] != 1:
                    heapq.heappush(h, (item[0]-1, item[1]))      
                k -=1

            return len(h)

# V1'''''''
# IDEA : Counter + heapq
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/1542356/Python-MinHeap-Solution
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        counter = collections.Counter(arr)
        minHeap = []
        for key, val in counter.items():
            heapq.heappush(minHeap, val)
        
        while k:
            minHeap[0] -= 1
            if minHeap[0] == 0:
                heapq.heappop(minHeap)
            k -= 1
        
        return len(minHeap)

# V1'''''''
# IDEA : GREEDY
# https://zxi.mytechroad.com/blog/hashtable/leetcode-1481-least-number-of-unique-integers-after-k-removals/
# C++
# class Solution {
# public:
#   int findLeastNumOfUniqueInts(vector<int>& arr, int k) {    
#     unordered_map<int, int> c;
#     for (int x : arr) ++c[x];
#     vector<int> m; // freq
#     for (const auto [x, f] : c)
#       m.push_back(f);
#     sort(begin(m), end(m));
#     int ans = m.size();    
#     int i = 0;
#     while (k--) {
#       if (--m[i] == 0) {
#         ++i;
#         --ans;
#       }
#     }
#     return ans;
#   }
# };

# V2