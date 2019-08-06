# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79434941
# http://bookshadow.com/weblog/2017/06/04/leetcode-find-duplicate-file-in-system/
# IDEA : collections.defaultdict 
class Solution(object):
    def findDuplicate(self, paths):
        """
        :type paths: List[str]
        :rtype: List[List[str]]
        """
        contentDict = collections.defaultdict(list)
        for path in paths:
            parts = path.split()
            dir, files = parts[0], parts[1:]
            for file in files:
                tokens = file.split('.')
                name, content = tokens[0], tokens[1][4:-1]
                contentDict[content].append(dir + '/' + name + '.txt')
        return [g for g in contentDict.values() if len(g) > 1]

# V2 
# Time:  O(n * l), l is the average length of file content
# Space: O(n * l)
import collections
class Solution(object):
    def findDuplicate(self, paths):
        """
        :type paths: List[str]
        :rtype: List[List[str]]
        """
        files = collections.defaultdict(list)
        for path in paths:
           s = path.split(" ")
           for i in range(1,len(s)):
               file_name = s[0] + "/" + s[i][0:s[i].find("(")]
               file_content = s[i][s[i].find("(")+1:s[i].find(")")]
               files[file_content].append(file_name)

        result = []
        for file_content, file_names in files.iteritems():
            if len(file_names)>1:
                result.append(file_names)
        return result
