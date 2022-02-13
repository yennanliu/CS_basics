
"""

190. Reverse Bits
Easy

Reverse bits of a given 32 bits unsigned integer.

Note:

Note that in some languages, such as Java, there is no unsigned integer type. In this case, both input and output will be given as a signed integer type. They should not affect your implementation, as the integer's internal binary representation is the same, whether it is signed or unsigned.
In Java, the compiler represents the signed integers using 2's complement notation. Therefore, in Example 2 above, the input represents the signed integer -3 and the output represents the signed integer -1073741825.


# https://blog.csdn.net/coder_orz/article/details/51705094

翻轉一個給定的32位無符號數的位。
比如，給定輸入整數43261596（二進製表示為00000010100101000001111010011100）
，返回964176192（二進製表示為00111001011110000010100101000000）。
進一步：如果該函數被多次調用，你該如何優化它？

Example 1:

Input: n = 00000010100101000001111010011100
Output:    964176192 (00111001011110000010100101000000)
Explanation: The input binary string 00000010100101000001111010011100 represents the unsigned integer 43261596, so return 964176192 which its binary representation is 00111001011110000010100101000000.
Example 2:

Input: n = 11111111111111111111111111111101
Output:   3221225471 (10111111111111111111111111111111)
Explanation: The input binary string 11111111111111111111111111111101 represents the unsigned integer 4294967293, so return 3221225471 which its binary representation is 10111111111111111111111111111111.
 

Constraints:

The input must be a binary string of length 32
 

Follow up: If this function is called many times, how would you optimize it?
"""

# V0
class Solution:
    def reverseBits(self, n):
        s = bin(n)[2:]
        s = "0"*(32 - len(s)) + s
        t = s[::-1]
        return int(t,2)

# V0'
# DEMO
# n = 10100101000001111010011100
# n =       10100101000001111010011100
class Solution:
    def reverseBits(self, n):
        n = bin(n)[2:]         # convert to binary, and remove the usual 0b prefix
        print ("n = " + str(n))
        n = '%32s' % n         # print number into a pre-formatted string with space-padding
        print ("n = " + str(n))
        n = n.replace(' ','0') # Convert the useful space-padding into zeros
        # Now we have a  proper binary representation, so we can make the final transformation
        return int(n[::-1],2)

# V0'' 
class Solution(object):
    def reverseBits(self, n):
        #b = bin(n)[:1:-1]
        b = bin(n)[2:][::-1]
        return int(b + '0'*(32-len(b)), 2)

# V1
# https://leetcode.com/problems/reverse-bits/discuss/176539/Python-solution
class Solution:
    def reverseBits(self, n):
        s = bin(n)[2:]
        s = "0"*(32 - len(s)) + s
        t = s[::-1]
        return int(t,2)

# V1'
# https://leetcode.com/problems/reverse-bits/discuss/732541/Python-Solution-with-explanation
class Solution:
    def reverseBits(self, n: int):
        s = bin(n).replace("0b", "") # convert given number to binary representation
        """
         if we want bin(3) in python, it returns "11" and not "000...11"
         then if we reverse it ie, reverse of "11" is "11" but expected answer 
         is "110000... "(reverse of 000...11)
        """
        if (len(s) != 32): # Check if the binary string is 32 bits
            s = "0"*(32 - len(s)) + s
        s = s[::-1] # reverse the string
        return int(s, 2) # convert it into its binary form

# V1''
# https://blog.csdn.net/coder_orz/article/details/51705094
# IDEA : transform str to binary 2 -> extend to 32 digit -> complete this 32 digit
class Solution(object):
    def reverseBits(self, n):
        """
        :type n: int
        :rtype: int
        """
        b = bin(n)[:1:-1]
        return int(b + '0'*(32-len(b)), 2)

# V1'''
# IDEA : string op
# https://leetcode.com/problems/reverse-bits/discuss/744794/Easy-Python-or-100-Speed
class Solution:
    def reverseBits(self, n):
        n = bin(n)[2:]         # convert to binary, and remove the usual 0b prefix
        n = '%32s' % n         # print number into a pre-formatted string with space-padding
        n = n.replace(' ','0') # Convert the useful space-padding into zeros
        # Now we have a  proper binary representation, so we can make the final transformation
        return int(n[::-1],2)

# V1''''
# https://leetcode.com/problems/reverse-bits/discuss/744794/Easy-Python-or-100-Speed
class Solution:
    def reverseBits(self, n):
        return int( ('%32s' % bin(n)[2:]).replace(' ','0')[::-1] ,2)

# V1'''''
# https://leetcode.com/problems/reverse-bits/discuss/744794/Easy-Python-or-100-Speed
class Solution:
    def reverseBits(self, n):
        n = bin(n)[2:]
        n = '0'*( 32-len(n) ) + n
        return int( n[::-1] ,2)

# V1''''''
# https://blog.csdn.net/coder_orz/article/details/51705094
class Solution(object):
    def reverseBits(self, n):
        """
        :type n: int
        :rtype: int
        """
        res = 0
        for i in range(32):
            res <<= 1
            res |= ((n >> i) & 1)
        return res

# V1'''''''
# https://blog.csdn.net/coder_orz/article/details/51705094
class Solution(object):
    def reverseBits(self, n):
        """
        :type n: int
        :rtype: int
        """
        n = (n >> 16) | (n << 16);
        n = ((n & 0xff00ff00) >> 8) | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >> 4) | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >> 2) | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >> 1) | ((n & 0x55555555) << 1);
        return n

# V2 
# Time : O(logn) = O(32)
# Space: O(1)
class Solution(object):
    # @param n, an integer
    # @return an integer
    def reverseBits(self, n):
        result = 0
        for i in range(32):
            result <<= 1
            result |= n & 1
            n >>= 1
        return result

    def reverseBits2(self, n):
        string = bin(n)
        if '-' in string:
            string = string[:3] + string[3:].zfill(32)[::-1]
        else:
            string = string[:2] + string[2:].zfill(32)[::-1]
        return int(string, 2)