"""

1233. Remove Sub-Folders from the Filesystem
Medium

Given a list of folders folder, return the folders after removing all sub-folders in
those folders. You may return the answer in any order.

If a folder[i] is located within another folder[j], it is called a sub-folder of it.
A sub-folder of folder[j] must start with folder[j], followed by a "/".
For example, "/a/b" is a sub-folder of "/a", but "/b" is not a sub-folder of "/a/b/c".

The format of a path is one or more concatenated strings of the form: '/' followed by one
or more lowercase English letters.

For example, "/leetcode" and "/leetcode/problems" are valid paths while an empty string
and "/" are not.

Example 1:

Input: folder = ["/a","/a/b","/c/d","/c/d/e","/c/f"]
Output: ["/a","/c/d","/c/f"]
Explanation: Folders "/a/b" is a subfolder of "/a" and "/c/d/e" is inside of folder
"/c/d" in our filesystem.

Example 2:

Input: folder = ["/a","/a/b/c","/a/b/d"]
Output: ["/a"]
Explanation: Folders "/a/b/c" and "/a/b/d" will be removed because they are subfolders
of "/a".

Example 3:

Input: folder = ["/a/b/c","/a/b/ca","/a/b/d"]
Output: ["/a/b/c","/a/b/ca","/a/b/d"]


Constraints:

1 <= folder.length <= 4 * 10^4
2 <= folder[i].length <= 100
folder[i] contains only lowercase letters and '/'.
folder[i] always starts with the character '/'.
Each folder name is unique.

"""

# V0
# IDEA : SORT + PREFIX CHECK
#        after lexicographic sorting, a parent always comes right before all of
#        its sub-folders, so it is enough to compare each path against the LAST
#        kept folder.
#        NOTE !!! the `+ "/"` guard is what stops "/a/b/ca" from being treated
#        as a sub-folder of "/a/b/c"
# time = O(n * L log n), L = max path length
# space = O(n)
class Solution(object):
    def removeSubfolders(self, folder):
        folder.sort()
        res = []
        for f in folder:
            if not res or not f.startswith(res[-1] + "/"):
                res.append(f)
        return res


# V1
# IDEA : TRIE over path components
#        insert every folder split by "/"; a path is a sub-folder iff we walk
#        through a node already marked as the end of another folder
# time = O(n * L)
# space = O(n * L)
class Solution_1(object):
    def removeSubfolders(self, folder):
        trie = {}
        for f in folder:
            node = trie
            for part in f.split("/")[1:]:
                node = node.setdefault(part, {})
            node["#"] = f  # mark end of a real folder

        res = []

        def dfs(node):
            if "#" in node:
                res.append(node["#"])
                return  # everything deeper is a sub-folder -> prune
            for k, child in node.items():
                dfs(child)

        dfs(trie)
        return res
