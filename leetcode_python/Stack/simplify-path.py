# Given an absolute path for a file (Unix-style), simplify it.
# For example,
# path = "/home/", => "/home"
# path = "/a/./b/../../c/", => "/c"
# 1
# 2
# Corner Cases:
# Did you consider the case where path = “/../”? 
# In this case, you should return “/”.
# Another corner case is the path might contain multiple slashes ‘/’ together, such as “/home//foo/”. 
# In this case, you should ignore redundant slashes and return “/home/foo”.


# e.g. :
# path  = "/../a/b/c/./.. "
# -- > output = "/a/b"
# TRICK :  
# IF  ".." , then stack pop 
# IF  "."  , then do nothing 
# ELSE     , push to stack  

# STEP 1. "/" : root directory 

# STEP 2. ".." : to the upper directory, since it is blank, so still stay at root directory 

# STEP 3. "a"  : to sub directory a, now at "/a"

# STEP 4. "b"  :  to sub directory a, now at "/a/b"

# STEP 5. "c"  : to sub directory a, now at  "/a/b/c"

# STEP 6. "."  : current directory, do nothing. still at "/a/b/c"

# STEP 7. ".." : back to the upper directory, stay at "/a/b" finally 

# V0 
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

# V1 
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
