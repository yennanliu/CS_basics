"""

393. UTF-8 Validation
Medium

Given an integer array data representing the data, return whether it is a valid UTF-8 encoding.

A character in UTF8 can be from 1 to 4 bytes long, subjected to the following rules:

For a 1-byte character, the first bit is a 0, followed by its Unicode code.
For an n-bytes character, the first n bits are all one's, the n + 1 bit is 0, followed by n - 1 bytes with the most significant 2 bits being 10.
This is how the UTF-8 encoding would work:

   Char. number range  |        UTF-8 octet sequence
      (hexadecimal)    |              (binary)
   --------------------+---------------------------------------------
   0000 0000-0000 007F | 0xxxxxxx
   0000 0080-0000 07FF | 110xxxxx 10xxxxxx
   0000 0800-0000 FFFF | 1110xxxx 10xxxxxx 10xxxxxx
   0001 0000-0010 FFFF | 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
Note: The input is an array of integers. Only the least significant 8 bits of each integer is used to store the data. This means each integer represents only 1 byte of data.

 

Example 1:

Input: data = [197,130,1]
Output: true
Explanation: data represents the octet sequence: 11000101 10000010 00000001.
It is a valid utf-8 encoding for a 2-bytes character followed by a 1-byte character.
Example 2:

Input: data = [235,140,4]
Output: false
Explanation: data represented the octet sequence: 11101011 10001100 00000100.
The first 3 bits are all one's and the 4th bit is 0 means it is a 3-bytes character.
The next byte is a continuation byte which starts with 10 and that's correct.
But the second continuation byte does not start with 10, so it is invalid.
 

Constraints:

1 <= data.length <= 2 * 104
0 <= data[i] <= 255

"""

# V0 
# https://leetcode.com/problems/utf-8-validation/discuss/400575/case

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

### Test case : dev

# V1'
# https://leetcode.com/problems/utf-8-validation/discuss/87478/Simplest-Python-Solution
class Solution(object):
    def validUtf8(self, data):
        """
        :type data: List[int]
        :rtype: bool
        """
        count = 0
        
        for byte in data:
            if byte >= 128 and byte <= 191:
                if not count:
                    return False
                count -= 1
            else:
                if count:
                    return False
                if byte < 128:
                    continue
                elif byte < 224:
                    count = 1
                elif byte < 240:
                    count = 2
                elif byte < 248:
                    count = 3
                else:
                    return False                  
        return count == 0

# V1''
# https://leetcode.com/problems/utf-8-validation/discuss/156588/My-easy-to-understand-Python-solution
class Solution:
    def validUtf8(self, data):
        
        def rest(i):
            if len(data) < i: return False
            for _ in range(i):
                if not data.pop().startswith("10"): return False
            return True
        
        data = [str(bin(seq)[2:].zfill(8)) for seq in data[::-1]]
        while data:
            seq = data.pop()
            if seq.startswith("0"): continue
            if seq.startswith("110"):
                if not rest(1): return False
            elif seq.startswith("1110"):
                if not rest(2): return False
            elif seq.startswith("11110"):
                if not rest(3): return False
            else: return False
        return True

# V1'''
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

# V1'''''
# https://leetcode.com/problems/utf-8-validation/solution/
# IDEA : String Manipulation
# time complexity : O(N)
# space complexity : O(N)
class Solution:
    def validUtf8(self, data):
        """
        :type data: List[int]
        :rtype: bool
        """

        # Number of bytes in the current UTF-8 character
        n_bytes = 0

        # For each integer in the data array.
        for num in data:

            # Get the binary representation. We only need the least significant 8 bits
            # for any given number.
            bin_rep = format(num, '#010b')[-8:]

            # If this is the case then we are to start processing a new UTF-8 character.
            if n_bytes == 0:

                # Get the number of 1s in the beginning of the string.
                for bit in bin_rep:
                    if bit == '0': break
                    n_bytes += 1

                # 1 byte characters
                if n_bytes == 0:
                    continue

                # Invalid scenarios according to the rules of the problem.
                if n_bytes == 1 or n_bytes > 4:
                    return False
            else:
                # Else, we are processing integers which represent bytes which are a part of
                # a UTF-8 character. So, they must adhere to the pattern `10xxxxxx`.
                if not (bin_rep[0] == '1' and bin_rep[1] == '0'):
                    return False

            # We reduce the number of bytes to process by 1 after each integer.
            n_bytes -= 1

        # This is for the case where we might not have the complete data for
        # a particular UTF-8 character.
        return n_bytes == 0     

# V1'''''''
# https://leetcode.com/problems/utf-8-validation/solution/
# Bit Manipulation
# time complexity : O(N)
# space complexity : O(1)
class Solution:
    def validUtf8(self, data):
        """
        :type data: List[int]
        :rtype: bool
        """

        # Number of bytes in the current UTF-8 character
        n_bytes = 0

        # Mask to check if the most significant bit (8th bit from the left) is set or not
        mask1 = 1 << 7

        # Mask to check if the second most significant bit is set or not
        mask2 = 1 << 6
        for num in data:

            # Get the number of set most significant bits in the byte if
            # this is the starting byte of an UTF-8 character.
            mask = 1 << 7
            if n_bytes == 0:
                while mask & num:
                    n_bytes += 1
                    mask = mask >> 1

                # 1 byte characters
                if n_bytes == 0:
                    continue

                # Invalid scenarios according to the rules of the problem.
                if n_bytes == 1 or n_bytes > 4:
                    return False
            else:

                # If this byte is a part of an existing UTF-8 character, then we
                # simply have to look at the two most significant bits and we make
                # use of the masks we defined before.
                if not (num & mask1 and not (num & mask2)):
                    return False
            n_bytes -= 1
        return n_bytes == 0     

# V1''''''''''
# https://github.com/xieqilu/Bloomberg/blob/master/UTF-8Decoding.cs
# JAVA
# bool valid_utf8(const vector<unsigned char>& data) {
#     int size = 0;
#     for(auto c : data) {
#         if(size == 0) {
#     //size is the number of rest bytes for this specific
#     //possible UTF-8 sequence
#             if((c >> 5) == 0b110) size = 1; //0b means binary number
#             else if((c >> 4) == 0b1110) size = 2;
#             else if((c >> 3) == 0b11110) size = 3;
#             else if(c >> 7) return false;
#         } else {
#             if((c >> 6) != 0b10) return false;
#             --size;
#         }
#     }
#     return size == 0;
# }

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