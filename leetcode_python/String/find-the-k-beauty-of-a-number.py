"""

2269. Find the K-Beauty of a Number
Easy

The k-beauty of an integer num is defined as the number of substrings of num when it is read as a string that meet the following conditions:

It has a length of k.
It is a divisor of num.

Given integers num and k, return the k-beauty of num.

Note:

Leading zeros are allowed.
0 is not a divisor of any value.

A substring is a contiguous sequence of characters in a string.


Example 1:

Input: num = 240, k = 2
Output: 2
Explanation: The following are the substrings of num of length k:
- "24" from "240": 24 is a divisor of 240.
- "40" from "240": 40 is a divisor of 240.
Therefore, the k-beauty is 2.

Example 2:

Input: num = 430043, k = 2
Output: 2
Explanation: The following are the substrings of num of length k:
- "43" from "430043": 43 is a divisor of 430043.
- "30" from "430043": 30 is not a divisor of 430043.
- "00" from "430043": 0 is not a divisor of 430043.
- "04" from "430043": 4 is not a divisor of 430043.
- "43" from "430043": 43 is a divisor of 430043.
Therefore, the k-beauty is 2.


Constraints:

1 <= num <= 10^9
1 <= k <= num.length (taking num as a string)

"""

# V0
# IDEA : SLIDING WINDOW OVER THE DECIMAL STRING
#
#   num has at most 10 digits, so just slide a length-k window across str(num),
#   parse it and test divisibility.
#
#   NOTE : leading zeros are allowed ("04" -> 4), and a window that parses to
#          0 must NOT be counted - 0 divides nothing.
#
# time = O(d * k), d = number of digits (<= 10), space = O(d)
class Solution(object):
    def divisorSubstrings(self, num, k):
        s = str(num)
        res = 0
        for i in range(len(s) - k + 1):
            v = int(s[i:i + k])
            if v != 0 and num % v == 0:
                res += 1
        return res
