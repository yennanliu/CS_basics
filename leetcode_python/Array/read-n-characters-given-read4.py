"""

157. Read N Characters Given Read4
Easy

Given a file and assume that you can only read the file using a given method read4, implement a method to read n characters.

Method read4:

The API read4 reads four consecutive characters from file, then writes those characters into the buffer array buf4.

The return value is the number of actual characters read.

Note that read4() has its own file pointer, much like FILE *fp in C.

Definition of read4:

    Parameter:  char[] buf4
    Returns:    int

buf4[] is a destination, not a source. The results from read4 will be copied to buf4[].
Below is a high-level example of how read4 works:


File file("abcde"); // File is "abcde", initially file pointer (fp) points to 'a'
char[] buf4 = new char[4]; // Create buffer with enough space to store characters
read4(buf4); // read4 returns 4. Now buf4 = "abcd", fp points to 'e'
read4(buf4); // read4 returns 1. Now buf4 = "e", fp points to end of file
read4(buf4); // read4 returns 0. Now buf4 = "", fp points to end of file
 

Method read:

By using the read4 method, implement the method read that reads n characters from file and store it in the buffer array buf. Consider that you cannot manipulate file directly.

The return value is the number of actual characters read.

Definition of read:

    Parameters: char[] buf, int n
    Returns:    int

buf[] is a destination, not a source. You will need to write the results to buf[].
Note:

Consider that you cannot manipulate the file directly. The file is only accessible for read4 but not for read.
The read function will only be called once for each test case.
You may assume the destination buffer array, buf, is guaranteed to have enough space for storing n characters.
 

Example 1:

Input: file = "abc", n = 4
Output: 3
Explanation: After calling your read method, buf should contain "abc". We read a total of 3 characters from the file, so return 3.
Note that "abc" is the file's content, not buf. buf is the destination buffer that you will have to write the results to.
Example 2:

Input: file = "abcde", n = 5
Output: 5
Explanation: After calling your read method, buf should contain "abcde". We read a total of 5 characters from the file, so return 5.
Example 3:

Input: file = "abcdABCD1234", n = 12
Output: 12
Explanation: After calling your read method, buf should contain "abcdABCD1234". We read a total of 12 characters from the file, so return 12.
 

Constraints:

1 <= file.length <= 500
file consist of English letters and digits.
1 <= n <= 1000

"""

# V0 
class Solution:
    def read(self, buf, n):
        copied_chars = 0
        read_chars = 4
        buf4 = [''] * 4
        
        while copied_chars < n and read_chars == 4:
            read_chars = read4(buf4)
            
            for i in range(read_chars):
                if copied_chars == n:
                    return copied_chars
                buf[copied_chars] = buf4[i]
                copied_chars += 1
        
        return copied_chars

# V1
# IDEA : Use Internal Buffer of 4 Characters
#     -> progress :  File -> Internal Buffer of 4 Characters -> Buffer of N Characters.
# https://leetcode.com/problems/read-n-characters-given-read4/solution/
class Solution:
    def read(self, buf: List[str], n: int) -> int:
        copied_chars = 0
        read_chars = 4
        buf4 = [''] * 4
        
        while copied_chars < n and read_chars == 4:
            read_chars = read4(buf4)
            
            for i in range(read_chars):
                if copied_chars == n:
                    return copied_chars
                buf[copied_chars] = buf4[i]
                copied_chars += 1
        
        return copied_chars

# V1'
# IDEA : Speed Up: No Internal Buffer
# https://leetcode.com/problems/read-n-characters-given-read4/solution/
# C++
# class Solution {
# public:
#     int read(char *buf, int n) {
#         int copiedChars = 0;
#         int readChars = 4;
#         int remainingChars = n;
#        
#         // While there are at least 4 characters remaining to be read and the last
#         // call to readChars returned 4 characters, write directly to buf.
#         while (remainingChars >= 4 && readChars == 4) {
#             readChars = read4(buf + copiedChars);
#             copiedChars += readChars;
#         }
#        
#         // If there are between 1 and 3 characters that we still want to read and
#         // readChars was not 0 last time we called read4, then create a buffer
#         // for just this one call so we can ensure buf does not overflow.
#         if (remainingChars && readChars) {
#             char buf4[4];
#             readChars = read4(buf4);
#             for (int i = 0; i < min(remainingChars, readChars); i++) {
#                 buf[copiedChars++] = buf4[i];
#             }
#         }
#        
#         return min(n, copiedChars);
#     }
# };

# V1'
# http://www.voidcn.com/article/p-pfdpmnvw-qp.html
# https://www.cnblogs.com/yrbbest/p/4489710.html
# The read4 API is already defined for you.
# @param buf, a list of characters
# @return an integer
# def read4(buf):
class Solution(object):
    def read(self, buf, n):
        """
        :type buf: Destination buffer (List[str])
        :type n: Maximum number of characters to read (int)
        :rtype: The number of characters read (int)
        """
        index = 0
        while True:
            buf4 = [""]*4
            current = min(read4(buf4), index) # use read4 method, save the read data in bur4 
            for i in range(current):
                buf[index] = buf4[i] # send value to buf, test case may need to check whether buf read the necessary characters 
                index += 1
            if current!=4:
                return index 

# V1''
# https://www.jiuzhang.com/solution/read-n-characters-given-read4-ii-call-multiple-times/#tag-highlight-lang-python
class Solution:
    def __init__(self):
        self.buf4, self.i4, self.n4 = [None] * 4, 0, 0

    # @param {char[]} buf destination buffer
    # @param {int} n maximum number of characters to read
    # @return {int} the number of characters read
    def read(self, buf, n):
        # Write your code here
        i = 0
        while i < n:
            if self.i4 == self.n4:
                self.i4, self.n4 = 0, Reader.read4(self.buf4)
                if not self.n4:
                    break
            buf[i], i, self.i4 = self.buf4[self.i4], i + 1, self.i4 + 1
        return i

# V2
# Time:  O(n)
# Space: O(1)
def read4(buf):
    global file_content
    i = 0
    while i < len(file_content) and i < 4:
        buf[i] = file_content[i]
        i += 1

    if len(file_content) > 4:
        file_content = file_content[4:]
    else:
        file_content = ""
    return i

class Solution(object):
    def read(self, buf, n):
        """
        :type buf: Destination buffer (List[str])
        :type n: Maximum number of characters to read (int)
        :rtype: The number of characters read (int)
        """
        read_bytes = 0
        buffer = [''] * 4
        for i in range(n / 4 + 1):
            size = read4(buffer)
            if size:
                size = min(size, n-read_bytes)
                buf[read_bytes:read_bytes+size] = buffer[:size]
                read_bytes += size
            else:
                break
        return read_bytes