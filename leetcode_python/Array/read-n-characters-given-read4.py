# Time:  O(n)
# Space: O(1)
#
# The API: int read4(char *buf) reads 4 characters at a time from a file.
#
# The return value is the actual number of characters read. For example, it returns 3 if there is only 3 characters left in the file.
#
# By using the read4 API, implement the function int read(char *buf, int n) that reads n characters from the file.
#
# Note:
# The read function will only be called once for each test case.
#
# The read4 API is already defined for you.
# @param buf, a list of characters
# @return an integer

# V0 

# V1 
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
                
# V1'
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