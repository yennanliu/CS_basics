# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80812350
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
