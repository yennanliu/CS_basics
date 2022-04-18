"""

71. Simplify Path
Medium

Given a string path, which is an absolute path (starting with a slash '/') to a file or directory in a Unix-style file system, convert it to the simplified canonical path.

In a Unix-style file system, a period '.' refers to the current directory, a double period '..' refers to the directory up a level, and any multiple consecutive slashes (i.e. '//') are treated as a single slash '/'. For this problem, any other format of periods such as '...' are treated as file/directory names.

The canonical path should have the following format:

The path starts with a single slash '/'.
Any two directories are separated by a single slash '/'.
The path does not end with a trailing '/'.
The path only contains the directories on the path from the root directory to the target file or directory (i.e., no period '.' or double period '..')
Return the simplified canonical path.

 

Example 1:

Input: path = "/home/"
Output: "/home"
Explanation: Note that there is no trailing slash after the last directory name.
Example 2:

Input: path = "/../"
Output: "/"
Explanation: Going one level up from the root directory is a no-op, as the root level is the highest level you can go.
Example 3:

Input: path = "/home//foo/"
Output: "/home/foo"
Explanation: In the canonical path, multiple consecutive slashes are replaced by a single one.
 

Constraints:

1 <= path.length <= 3000
path consists of English letters, digits, period '.', slash '/' or '_'.
path is a valid absolute Unix path.

"""

# V0
# IDEA : STACK
class Solution:
    def simplifyPath(self, path: str) -> str:
        s = path.split('/')
        result = []
        for i in range(len(s)):
            if s[i] and s[i] != '.' and s[i]!='/' and s[i]!='..':
                result.append(s[i])
            elif s[i] == '..':
                if result:
                    result.pop()
        
        return "/"+"/".join(result)

# V0'
# IDEA : STACK
class Solution(object):
    def simplifyPath(self, path):
        stack = []
        dirs = path.split('/')
        for dir in dirs:
            if not dir or dir == '.':
                continue
            if dir == '..':
                if stack:
                    stack.pop()
            else:                
                stack.append(dir)
        return '/' + '/'.join(stack)

# V0''
class Solution(object):
    def simplifyPath(self, path):
        s_final = []
        # note this trick
        _path = path.split("/")
        for p in _path:
            if p == "." or p == "":
                ## NOTE : use continue here
                # the continue will SKIP REST OF THE CODE IN THE LOOP
                # https://www.programiz.com/python-programming/break-continue
                continue
            elif p == "..":
                if s_final:
                    # attay has this method
                    s_final.pop()
            else:
                s_final.append(p)
        return "/" + "/".join(s_final)

# V1
# IDEA : STACK
# https://leetcode.com/problems/simplify-path/solution/
class Solution:
    def simplifyPath(self, path: str) -> str:

        # Initialize a stack
        stack = []

        # Split the input string on "/" as the delimiter
        # and process each portion one by one
        for portion in path.split("/"):

            # If the current component is a "..", then
            # we pop an entry from the stack if it's non-empty
            if portion == "..":
                if stack:
                    stack.pop()
            elif portion == "." or not portion:
                # A no-op for a "." or an empty string
                continue
            else:
                # Finally, a legitimate directory name, so we add it
                # to our stack
                stack.append(portion)

        # Stich together all the directory names together
        final_str = "/" + "/".join(stack)
        return final_str

# V1'
# IDEA : STACK
# https://leetcode.com/problems/simplify-path/discuss/1958861/Python-3
class Solution:
    def simplifyPath(self, path: str) -> str:
        s = path.split('/')
        result = []
        for i in range(len(s)):
            if s[i] and s[i] != '.' and s[i]!='/' and s[i]!='..':
                result.append(s[i])
            elif s[i] == '..':
                if result:
                    result.pop()
        
        return "/"+"/".join(result)

# V1''
# IDEA : STACK
# https://leetcode.com/problems/simplify-path/discuss/1050588/Python-stack
class Solution:
    def simplifyPath(self, path):
        s = []
        for x in path.split('/'):
            if not x: 
                continue
            elif x == '.':
                continue
            elif x == '..':
                if s:
                    s.pop()
            else:
                s.append(x)
        return '/' + '/'.join(s)

# V1'''
# IDEA : STACK
# https://leetcode.com/problems/simplify-path/discuss/1050624/Python-by-stack-w-Comment
class Solution:
    def simplifyPath(self, path: str) -> str:
        
        # stack to store directory name
        stack = []
        
        # check each directory name in given path, split by '/'
        for dir_name in path.split('/'):
        
            if dir_name == '' or dir_name == '.':
                
                # do nothing if directory name is either empty string or '.'
                continue
                
            elif dir_name == '..':
                
                # go back to parnet level and pop stack if stack is not empty
                if stack:
                    stack.pop()
                else:
                    continue
            
            else:
                # push current directory name into stack
                stack.append(dir_name)
                        
        return '/' + '/'.join(stack)

# V1'''''
# https://blog.csdn.net/fuxuemingzhu/article/details/80812350
# https://www.cnblogs.com/zuoyuan/p/3777289.html
# IDEA  :  SIMULATE THE PROCESS 
class Solution(object):
    def simplifyPath(self, path):
        """
        :type path: str
        :rtype: str
        """
        stack = list()
        dirs = path.split('/')
        for dir in dirs:
            if not dir or dir == '.':
                continue
            if dir == '..':
                if stack:
                    stack.pop()
            else:                
                stack.append(dir)
        return '/' + '/'.join(stack)

# output : 
# In [54]: Solution().simplifyPath(path)
#     ...: 
# token : 
# stack : []
# token : a
# stack : ['a']
# token : .
# stack : ['a']
# token : b
# stack : ['a', 'b']
# token : ..
# stack : ['a']
# token : ..
# stack : []
# token : c
# stack : ['c']
# token : 
# stack : ['c']
# Out[54]: '/c'

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    # @param path, a string
    # @return a string
    def simplifyPath(self, path):
        stack, tokens = [], path.split("/")
        for token in tokens:
            if token == ".." and stack:
                stack.pop()
            elif token != ".." and token != "." and token:
                stack.append(token)
        return "/" + "/".join(stack)