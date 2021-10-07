"""

388. Longest Absolute File Path
Medium

Suppose we have a file system that stores both files and directories. An example of one system is represented in the following picture:



Here, we have dir as the only directory in the root. dir contains two subdirectories, subdir1 and subdir2. subdir1 contains a file file1.ext and subdirectory subsubdir1. subdir2 contains a subdirectory subsubdir2, which contains a file file2.ext.

In text form, it looks like this (with ⟶ representing the tab character):

dir
⟶ subdir1
⟶ ⟶ file1.ext
⟶ ⟶ subsubdir1
⟶ subdir2
⟶ ⟶ subsubdir2
⟶ ⟶ ⟶ file2.ext
If we were to write this representation in code, it will look like this: "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext". Note that the '\n' and '\t' are the new-line and tab characters.

Every file and directory has a unique absolute path in the file system, which is the order of directories that must be opened to reach the file/directory itself, all concatenated by '/'s. Using the above example, the absolute path to file2.ext is "dir/subdir2/subsubdir2/file2.ext". Each directory name consists of letters, digits, and/or spaces. Each file name is of the form name.extension, where name and extension consist of letters, digits, and/or spaces.

Given a string input representing the file system in the explained format, return the length of the longest absolute path to a file in the abstracted file system. If there is no file in the system, return 0.

 

Example 1:


Input: input = "dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext"
Output: 20
Explanation: We have only one file, and the absolute path is "dir/subdir2/file.ext" of length 20.
Example 2:


Input: input = "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext"
Output: 32
Explanation: We have two files:
"dir/subdir1/file1.ext" of length 21
"dir/subdir2/subsubdir2/file2.ext" of length 32.
We return 32 since it is the longest absolute path to a file.
Example 3:

Input: input = "a"
Output: 0
Explanation: We do not have any files, just a single directory named "a".
Example 4:

Input: input = "file1.txt\nfile2.txt\nlongfile.txt"
Output: 12
Explanation: There are 3 files at the root directory.
Since the absolute path for anything at the root directory is just the name itself, the answer is "longfile.txt" with length 12.
 

Constraints:

1 <= input.length <= 104
input may contain lowercase or uppercase English letters, a new line character '\n', a tab character '\t', a dot '.', a space ' ', and digits.

"""

# V0
# IDEA : dict + replae, split
class Solution(object):
    def lengthLongestPath(self, input):
        # NOTE : we maintain a dict for collecting key and the length till now
        dict={}
        longest=0
        fileList=input.split("\n")
        for i in fileList:
            # directory
            if "." not in i:
                key = i.count("\t") # level of directory
                value = len(i.replace("\t","")) # length after removing '\t'
                dict[key]=value
            # file
            else:
                key=i.count("\t")
                ### NOTE :　length of doc (all directory length + doc length + count of '\') 
                length = sum([dict[j] for j in dict.keys() if j<key]) + len(i.replace("\t","")) + key
                longest=max(longest,length)
        return longest

# V0'
class Solution(object):
    def lengthLongestPath(self, input):
        maxlen = 0
        pathlen = {0: 0}
        #for line in input.splitlines():
        for line in input.split('\n'):
            name = line.lstrip('\t')
            depth = len(line) - len(name)
            if '.' in name:
                maxlen = max(maxlen, pathlen[depth] + len(name))
            else:
                pathlen[depth + 1] = pathlen[depth] + len(name) + 1
        return maxlen

# V1
# IDEA :STACK
# https://leetcode.com/problems/longest-absolute-file-path/discuss/812407/Python-3-or-Stack-or-Explanation
class Solution:
    def lengthLongestPath(self, s: str) -> int:
        paths, stack, ans = s.split('\n'), [], 0
        for path in paths:
            p = path.split('\t')
            depth, name = len(p) - 1, p[-1]
            l = len(name)
            while stack and stack[-1][1] >= depth: 
                stack.pop()
            if not stack: 
                stack.append((l, depth))
            else: 
                stack.append((l+stack[-1][0], depth))
            if '.' in name: 
                ans = max(ans, stack[-1][0] + stack[-1][1])   
        return ans

# V1
# IDEA : dict + replae, split
# https://leetcode.com/problems/longest-absolute-file-path/discuss/86640/python-solution-easy-to-understand
class Solution(object):
    def lengthLongestPath(self, input):
        dict={}
        longest=0
        fileList=input.split("\n")
        for i in fileList:
            if "." not in i:  # directory
                key = i.count("\t") # level of directory
                value = len(i.replace("\t","")) # length after removing '\t'
                dict[key]=value
            else: # doc
                key=i.count("\t")
                #　length of doc (all directory length + doc length + count of '\') 
                length = sum([dict[j] for j in dict.keys() if j<key]) + len(i.replace("\t","")) + key
                longest=max(longest,length)
        return longest

# V1'
# http://bookshadow.com/weblog/2016/08/21/leetcode-longest-absolute-file-path/
class Solution(object):
    def lengthLongestPath(self, input):
        """
        :type input: str
        :rtype: int
        """
        ans = lengthSum = 0
        stack = [(-1, 0)]
        for p in input.split('\n'):
            depth = p.count('\t')
            name = p.replace('\t', '')
            topDepth, topLength = stack[-1]
            while depth <= topDepth:
                stack.pop()
                lengthSum -= topLength
                topDepth, topLength = stack[-1]
            length = len(name) + (depth > 0)
            lengthSum += length
            stack.append((depth, length))
            if name.count('.'):
                ans = max(ans, lengthSum)
        return ans

# V1''
# https://leetcode.com/problems/longest-absolute-file-path/discuss/86619/Simple-Python-solution
# IDEA : lstrip() : Remove spaces to the left of the string:
# https://www.w3schools.com/python/ref_string_lstrip.asp
class Solution(object):
    def lengthLongestPath(self, input):
        maxlen = 0
        pathlen = {0: 0}
        for line in input.splitlines():
            name = line.lstrip('\t')
            depth = len(line) - len(name)
            if '.' in name:
                maxlen = max(maxlen, pathlen[depth] + len(name))
            else:
                pathlen[depth + 1] = pathlen[depth] + len(name) + 1
        return maxlen

### Test case : dev

# V1'''
# http://bookshadow.com/weblog/2016/08/21/leetcode-longest-absolute-file-path/
class Solution(object):
    def lengthLongestPath(self, input):
        """
        :type input: str
        :rtype: int
        """
        maxlen = 0
        pathlen = {0: 0}
        for line in input.splitlines():
            name = line.lstrip('\t')
            depth = len(line) - len(name)
            if '.' in name:
                maxlen = max(maxlen, pathlen[depth] + len(name))
            else:
                pathlen[depth + 1] = pathlen[depth] + len(name) + 1
        return maxlen

# V1''''
# https://www.jiuzhang.com/solution/longest-absolute-file-path/#tag-highlight-lang-python
import re, collections
class Solution:
    # @param {string} input an abstract file system
    # @return {int} return the length of the longest absolute path to file
    def lengthLongestPath(self, input):
        # Write your code here
        dict = collections.defaultdict(lambda: "")
        lines = input.split("\n")

        n = len(lines)
        result = 0
        for i in xrange(n):
            count = lines[i].count("\t")

            lines[i] = dict[count - 1] + re.sub("\\t+","/", lines[i])
            if "." in lines[i]:
                result = max(result, len(lines[i]))
            dict[count] = lines[i]

        return result

# V1'''''
# https://leetcode.com/problems/longest-absolute-file-path/discuss/266896/Simple-(Python)-solution-with-detailed-explanation
class Solution:
    def lengthLongestPath(self, input: str) -> int:
        # return value
        maxlen = 0
        
        # map each level (0-indexed) to the current
        # maximum path length through that level;
        #
        # begin with a "-1 level" of 0 length, to simply
        # index the "level before the 0th level", which
        # is a common practice in dynamic programming
        levels = {-1:0}
        
        # split up the string so that each file/directory (token) is an
        # entry in the list; this preserves the tabs in each entry
        for token in input.split('\n'):
            # the 0-indexed level of this token is the number of tabs
            ''' only tabs contribute to the level; spaces are regular characters'''
            level = token.count('\t')
            
            # update the current level to be the length of the path
            # through the previous level + length of this token;
            # subtracting the level removes the tabs from the count
            #
            # think of this like reading through the structure with your eyes
            # from top to bottom; for each token you look at on a new line,
            # you update the line's level to be the path through the current token,
            # disregarding the remainder.  we never "look back up" when reading
            # the directory from top to bottom, and the string structure is
            # given to be consistent, so one pass updates the levels as deisred
            levels[level] = levels[level - 1] + len(token) - level
            
            # if we just processed a filename, overwrite the maximum length
            # if the current path + the number of backslashes is longer;
            # we store the raw token lengths without any separator,
            # so adding the 0-indexed level provides the correct number
            # of separators; if there is 1 item, the level is 0 (so no '/'),
            # if there are two items, the level is 1 (so one '/' between
            # the items) and so on
            if '.' in token:
                maxlen = max(maxlen, levels[level] + level)

        return maxlen

# V1'''''''
# https://leetcode.com/problems/longest-absolute-file-path/discuss/328592/python3-beats-99
class Solution:
    def lengthLongestPath(self, input: str) -> int:
        parts = input.split('\n')
        ans = 0
        stack = []
        L = 0
        for s in parts:
            cs = s.lstrip('\t')
            level = len(s) - len(cs)
            while stack and stack[-1][1] >= level:
                L -= len(stack.pop()[0])
            stack.append((cs, level))
            L += len(cs)
            if "." in cs:
                ans = max(ans, len(stack) + L - 1)                
        return ans

# V2
# Time:  O(n)
# Space: O(d), d is the max depth of the paths
class Solution(object):
    def lengthLongestPath(self, input):
        """
        :type input: str
        :rtype: int
        """
        def split_iter(s, tok):
            start = 0
            for i in range(len(s)):
                if s[i] == tok:
                    yield s[start:i]
                    start = i + 1
            yield s[start:]

        max_len = 0
        path_len = {0: 0}
        for line in split_iter(input, '\n'):
            name = line.lstrip('\t')
            depth = len(line) - len(name)
            if '.' in name:
                max_len = max(max_len, path_len[depth] + len(name))
            else:
                path_len[depth + 1] = path_len[depth] + len(name) + 1
        return max_len