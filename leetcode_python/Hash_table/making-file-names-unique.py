"""

1487. Making File Names Unique
Medium

Given an array of strings names of size n. You will create n folders in your file system such that, at the ith minute, you will create a folder with the name names[i].

Since two files cannot have the same name, if you enter a folder name that was previously used, the system will have a suffix addition to its name in the form of (k), where, k is the smallest positive integer such that the obtained name remains unique.

Return an array of strings of length n where ans[i] is the actual name the system will assign to the ith folder when you create it.


Example 1:

Input: names = ["pes","fifa","gta","pes(2019)"]
Output: ["pes","fifa","gta","pes(2019)"]
Explanation: Let's see how the file system creates folder names:
"pes" --> not assigned before, remains "pes"
"fifa" --> not assigned before, remains "fifa"
"gta" --> not assigned before, remains "gta"
"pes(2019)" --> not assigned before, remains "pes(2019)"

Example 2:

Input: names = ["gta","gta(1)","gta","avalon"]
Output: ["gta","gta(1)","gta(2)","avalon"]
Explanation: Let's see how the file system creates folder names:
"gta" --> not assigned before, remains "gta"
"gta(1)" --> not assigned before, remains "gta(1)"
"gta" --> the name is reserved, system adds (k), since "gta(1)" is also reserved, systems put k = 2. it becomes "gta(2)"
"avalon" --> not assigned before, remains "avalon"

Example 3:

Input: names = ["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece"]
Output: ["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece(4)"]
Explanation: When the last folder is created, the smallest positive valid k is 4, and it becomes "onepiece(4)".


Constraints:

1 <= names.length <= 5 * 10^4
1 <= names[i].length <= 20
names[i] consists of lowercase English letters, digits, and/or round brackets.

"""

# V0
# IDEA : HASH TABLE OF "NEXT SUFFIX TO TRY" (never restart k from 1)
#
#   used[name] = the smallest k worth trying for this base name.
#   on a collision, walk k upward until name(k) is free, then remember
#   k + 1 as the next starting point.
#   NOTE : without that memory, a list of 5*10^4 identical names would
#          rescan 1..k every time -> quadratic. with it, each k is visited
#          at most once per base name, so the whole thing is linear.
#   NOTE : a literal input like "gta(1)" also occupies the map, which is
#          why the k walk still has to test membership.
#
# time = O(L) amortised, space = O(L), L = total length of all names
class Solution(object):
    def getFolderNames(self, names):
        used = {}                 # taken name -> next suffix to try
        res = []
        for name in names:
            if name not in used:
                used[name] = 1
                res.append(name)
                continue
            k = used[name]
            cand = name + "(" + str(k) + ")"
            while cand in used:
                k += 1
                cand = name + "(" + str(k) + ")"
            used[name] = k + 1
            used[cand] = 1
            res.append(cand)
        return res
