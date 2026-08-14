"""

1948. Delete Duplicate Folders in System
Hard

Due to a bug, there are many duplicate folders in a file system. You are given a 2D array paths, where paths[i] is an array representing an absolute path to the ith folder in the file system.

For example, ["one", "two", "three"] represents the path "/one/two/three".

Two folders (not necessarily on the same level) are identical if they contain the same non-empty set of identical subfolders and underlying subfolder structure. The folders do not need to be at the root level to be identical. If two or more folders are identical, then mark the folders as well as all their subfolders.

For example, folders "/a" and "/b" in the file structure below are identical. They (as well as their subfolders) should all be marked:
/a
/a/x
/a/x/y
/a/z
/b
/b/x
/b/x/y
/b/z
However, if the file structure also included the path "/b/w", then the folders "/a" and "/b" would not be identical. Note that "/a/x" and "/b/x" would still be considered identical even with the added folder.

Once all the identical folders and their subfolders have been marked, the file system will delete all of them. The file system only runs the deletion once, so any folders that become identical after the initial deletion are not deleted.

Return the 2D array ans containing the paths of the remaining folders after deleting all the marked folders. The paths may be returned in any order.


Example 1:

Input: paths = [["a"],["c"],["d"],["a","b"],["c","b"],["d","a"]]
Output: [["d"],["d","a"]]
Explanation: The file structure is as shown.
Folders "/a" and "/c" (and their subfolders) are marked for deletion because they both contain an empty folder named "b".

Example 2:

Input: paths = [["a"],["c"],["a","b"],["c","b"],["a","b","x"],["a","b","x","y"],["w"],["w","y"]]
Output: [["c"],["c","b"],["a"],["a","b"]]
Explanation: The file structure is as shown.
Folders "/a/b/x" and "/w" (and their subfolders) are marked for deletion because they both contain an empty folder named "y".
Note that folders "/a" and "/c" are identical after the deletion, but they are not deleted because they were not marked beforehand.

Example 3:

Input: paths = [["a","b"],["c","d"],["c"],["a"]]
Output: [["c"],["c","d"],["a"],["a","b"]]
Explanation: All folders are unique in the file system.
Note that the returned array can be in a different order as the order does not matter.


Constraints:

1 <= paths.length <= 2 * 10^4
1 <= paths[i].length <= 500
1 <= paths[i][j].length <= 10
1 <= sum(paths[i][j].length) <= 2 * 10^5
path[i][j] consists of lowercase English letters.
No two paths lead to the same folder.
For any folder not at the root level, its parent folder will also be in the input.

"""

# V0
# IDEA : TRIE + SUBTREE SERIALIZATION (canonical string per folder = its identity)
#
#   1) insert every path into a trie, so one trie node == one folder.
#   2) post-order, give each node a canonical signature :
#          sig(node) = concat of sorted( name + "(" + sig(child) + ")" )
#      sorting makes the signature independent of insertion order, and a LEAF
#      gets the empty signature - which is exactly why empty folders are never
#      considered duplicates of each other.
#   3) two nodes with the same non-empty signature are identical folders ->
#      mark BOTH deleted (the first one is remembered in a dict).
#   4) walk the trie again, skipping whole deleted subtrees, and emit paths.
#
#   NOTE : marking happens on the ORIGINAL tree, all at once, which is what
#          "the file system only runs the deletion once" means.
#   NOTE : paths can be 500 deep -> both traversals are ITERATIVE.
#
# time = O(total signature length), space = O(same)
class Solution(object):
    def deleteDuplicateFolder(self, paths):
        children = [{}]                 # node id -> {name: child id}
        deleted = [False]

        for path in paths:
            cur = 0
            for name in path:
                if name not in children[cur]:
                    children.append({})
                    deleted.append(False)
                    children[cur][name] = len(children) - 1
                cur = children[cur][name]

        # pre-order list, reversed -> a valid post-order (children before parent)
        order = []
        stack = [0]
        while stack:
            u = stack.pop()
            order.append(u)
            for v in children[u].values():
                stack.append(v)

        sig = [""] * len(children)
        seen = {}
        for u in reversed(order):
            if not children[u]:
                continue                # leaf -> empty signature, never a dup
            s = "".join(sorted(name + "(" + sig[v] + ")"
                               for name, v in children[u].items()))
            sig[u] = s
            if s in seen:
                deleted[u] = True
                deleted[seen[s]] = True
            else:
                seen[s] = u

        res = []
        stack = [(0, [])]
        while stack:
            u, path = stack.pop()
            if deleted[u]:
                continue
            if path:
                res.append(path)
            for name, v in children[u].items():
                stack.append((v, path + [name]))
        return res
