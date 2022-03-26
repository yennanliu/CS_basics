"""

1166. Design File System
Medium

You are asked to design a file system that allows you to create new paths and associate them with different values.

The format of a path is one or more concatenated strings of the form: / followed by one or more lowercase English letters. For example, "/leetcode" and "/leetcode/problems" are valid paths while an empty string "" and "/" are not.

Implement the FileSystem class:

bool createPath(string path, int value) Creates a new path and associates a value to it if possible and returns true. Returns false if the path already exists or its parent path doesn't exist.
int get(string path) Returns the value associated with path or returns -1 if the path doesn't exist.
 

Example 1:

Input: 
["FileSystem","createPath","get"]
[[],["/a",1],["/a"]]
Output: 
[null,true,1]
Explanation: 
FileSystem fileSystem = new FileSystem();

fileSystem.createPath("/a", 1); // return true
fileSystem.get("/a"); // return 1
Example 2:

Input: 
["FileSystem","createPath","createPath","get","createPath","get"]
[[],["/leet",1],["/leet/code",2],["/leet/code"],["/c/d",1],["/c"]]
Output: 
[null,true,true,2,false,-1]
Explanation: 
FileSystem fileSystem = new FileSystem();

fileSystem.createPath("/leet", 1); // return true
fileSystem.createPath("/leet/code", 2); // return true
fileSystem.get("/leet/code"); // return 2
fileSystem.createPath("/c/d", 1); // return false because the parent path "/c" doesn't exist.
fileSystem.get("/c"); // return -1 because this path doesn't exist.
 

Constraints:

The number of calls to the two functions is less than or equal to 104 in total.
2 <= path.length <= 100
1 <= value <= 109

"""

# V0

# V1
# IDEA : dict
# https://leetcode.com/problems/design-file-system/discuss/365925/Python-dict-solution
class FileSystem:

    def __init__(self):
        self.d = {}

    def createPath(self, path: str, value: int) -> bool:
        if path in self.d: return False
        if len(path) == 1: return False
        idx = len(path) - 1
        while path[idx] != '/': idx -= 1
        if idx == 0 or path[:idx] in self.d: 
            self.d[path] = value
            return True
        return False
        
    def get(self, path: str) -> int:
        return self.d.get(path, -1)

# V1'
# IDEA : DICT
# https://leetcode.com/problems/design-file-system/discuss/827613/python-simple-solution
class FileSystem:

    def __init__(self):
        from collections import defaultdict 
        T = lambda : defaultdict(T)
        self.T = T
        self.env = self.T()
        

    def createPath(self, path: str, value: int) -> bool:
        tokens = list(filter(lambda x : x, path.split('/')))
        m = self.env 
        for token in tokens[:-1]:
            if token not in m:
                return False 
            m = m[token][0]
        if tokens[-1] in m:
            return False
        m[tokens[-1]] = (self.T(),value)
        return True 

    def get(self, path: str) -> int:
        tokens = list(filter(lambda x : x, path.split('/')))
        m = self.env 
        for token in tokens[:-1]:
            if token not in m:
                return -1 
            m = m[token][0]
        if tokens[-1] not in m:
            return -1
        return m[tokens[-1]][1]

# V1''
# IDEA : trie
# https://leetcode.com/problems/design-file-system/discuss/1615602/python-multi-way-tree
class Node:
    def __init__(self, val=None):
        '''multi-way node to implement file system'''
        self.children = dict()
        self.val = val
        
class FileSystem:
    def __init__(self):
        self.root = Node()

    def createPath(self, path: str, value: int) -> bool:
        path, node = path[1:].split('/'), self.root
        for p in path[:-1]:
            if p not in node.children:  # parent path does not exist
                return False
            node = node.children[p]
        if path[-1] in node.children:  # path exists
            return False
        node.children[path[-1]] = Node(value)
        return True

    def get(self, path: str) -> int:
        node = self.root
        for p in path[1:].split('/'):
            if p not in node.children:
                return -1
            node = node.children[p]
        return node.val

# V1'''
# IDEA :  Dictionary for storing paths
# https://leetcode.com/problems/design-file-system/solution/
class FileSystem:

    def __init__(self):
        self.paths = defaultdict()

    def createPath(self, path: str, value: int) -> bool:
        
        # Step-1: basic path validations
        if path == "/" or len(path) == 0 or path in self.paths:
            return False
        
        # Step-2: if the parent doesn't exist. Note that "/" is a valid parent.
        parent = path[:path.rfind('/')]
        if len(parent) > 1 and parent not in self.paths:
            return False
        
        # Step-3: add this new path and return true.
        self.paths[path] = value
        return True

    def get(self, path: str) -> int:
        return self.paths.get(path, -1)

# V1''''
# IDEA :  Trie based approach
# https://leetcode.com/problems/design-file-system/solution/
# The TrieNode data structure.
class TrieNode(object):
    def __init__(self, name):
        self.map = defaultdict(TrieNode)
        self.name = name
        self.value = -1

class FileSystem:

    def __init__(self):
        
        # Root node contains the empty string.
        self.root = TrieNode("")

    def createPath(self, path: str, value: int) -> bool:
        
        # Obtain all the components
        components = path.split("/")
        
        # Start "curr" from the root node.
        cur = self.root
        
        # Iterate over all the components.
        for i in range(1, len(components)):
            name = components[i]
            
            # For each component, we check if it exists in the current node's dictionary.
            if name not in cur.map:
                
                # If it doesn't and it is the last node, add it to the Trie.
                if i == len(components) - 1:
                    cur.map[name] = TrieNode(name)
                else:
                    return False
            cur = cur.map[name]
        
        # Value not equal to -1 means the path already exists in the trie. 
        if cur.value!=-1:
            return False
        
        cur.value = value
        return True

    def get(self, path: str) -> int:
        
        # Obtain all the components
        cur = self.root
        
        # Start "curr" from the root node.
        components = path.split("/")
        
        # Iterate over all the components.
        for i in range(1, len(components)):
            
            # For each component, we check if it exists in the current node's dictionary.
            name = components[i]
            if name not in cur.map:
                return -1
            cur = cur.map[name]
        return cur.value

# V2