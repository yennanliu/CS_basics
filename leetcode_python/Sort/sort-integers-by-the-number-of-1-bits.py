"""

1356. Sort Integers by The Number of 1 Bits
Easy

You are given an integer array arr. Sort the integers in the array in ascending order by the number of 1's in their binary representation and in case of two or more integers have the same number of 1's you have to sort them in ascending order.

Return the array after sorting it.


Example 1:

Input: arr = [0,1,2,3,4,5,6,7,8]
Output: [0,1,2,4,8,3,5,6,7]
Explantion: [0] is the only integer with 0 bits.
[1,2,4,8] all have 1 bit.
[3,5,6] have 2 bits.
[7] has 3 bits.
The sorted array by bits is [0,1,2,4,8,3,5,6,7]

Example 2:

Input: arr = [1024,512,256,128,64,32,16,8,4,2,1]
Output: [1,2,4,8,16,32,64,128,256,512,1024]
Explantion: All integers have 1 bit in the binary representation, you should just sort them in ascending order.


Constraints:

1 <= arr.length <= 500
0 <= arr[i] <= 10^4

"""

# V0
# IDEA : CUSTOM SORT KEY (popcount, value)
# time = O(n log n)
# space = O(n)
class Solution(object):
    def sortByBits(self, arr):
        return sorted(arr, key=lambda x: (bin(x).count("1"), x))


# V1
# IDEA : ENCODE popcount INTO THE VALUE, THEN PLAIN SORT
#        since arr[i] <= 10^4, `popcount * 100000 + x` keeps the
#        two sort keys in separate "digit ranges" -> one numeric sort
#        gives the same order, no comparator / tuple needed
# time = O(n log n)
# space = O(n)
class Solution2(object):
    def sortByBits(self, arr):
        encoded = [bin(x).count("1") * 100000 + x for x in arr]
        encoded.sort()
        return [x % 100000 for x in encoded]
