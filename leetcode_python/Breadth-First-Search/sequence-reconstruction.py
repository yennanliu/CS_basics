"""

444. Sequence Reconstruction
Medium

You are given an integer array nums of length n where nums is a permutation of the integers in the range [1, n]. You are also given a 2D integer array sequences where sequences[i] is a subsequence of nums.

Check if nums is the shortest possible and the only supersequence. The shortest supersequence is a sequence with the shortest length and has all sequences[i] as subsequences. There could be multiple valid supersequences for the given array sequences.

For example, for sequences = [[1,2],[1,3]], there are two shortest supersequences, [1,2,3] and [1,3,2].
While for sequences = [[1,2],[1,3],[1,2,3]], the only shortest supersequence possible is [1,2,3]. [1,2,3,4] is a possible supersequence but not the shortest.
Return true if nums is the only shortest supersequence for sequences, or false otherwise.

A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.

 

Example 1:

Input: nums = [1,2,3], sequences = [[1,2],[1,3]]
Output: false
Explanation: There are two possible supersequences: [1,2,3] and [1,3,2].
The sequence [1,2] is a subsequence of both: [1,2,3] and [1,3,2].
The sequence [1,3] is a subsequence of both: [1,2,3] and [1,3,2].
Since nums is not the only shortest supersequence, we return false.
Example 2:

Input: nums = [1,2,3], sequences = [[1,2]]
Output: false
Explanation: The shortest possible supersequence is [1,2].
The sequence [1,2] is a subsequence of it: [1,2].
Since nums is not the shortest supersequence, we return false.
Example 3:

Input: nums = [1,2,3], sequences = [[1,2],[1,3],[2,3]]
Output: true
Explanation: The shortest possible supersequence is [1,2,3].
The sequence [1,2] is a subsequence of it: [1,2,3].
The sequence [1,3] is a subsequence of it: [1,2,3].
The sequence [2,3] is a subsequence of it: [1,2,3].
Since nums is the only shortest supersequence, we return true.
 

Constraints:

n == nums.length
1 <= n <= 104
nums is a permutation of all the integers in the range [1, n].
1 <= sequences.length <= 104
1 <= sequences[i].length <= 104
1 <= sum(sequences[i].length) <= 105
1 <= sequences[i][j] <= n

All the arrays of sequences are unique.
sequences[i] is a subsequence of nums.

"""

# V0 

# V1 
# http://bookshadow.com/weblog/2016/10/30/leetcode-sequence-reconstruction/
class Solution(object):
    def sequenceReconstruction(self, org, seqs):
        """
        :type org: List[int]
        :type seqs: List[List[int]]
        :rtype: bool
        """
        size = len(org)
        indeg = [0] * size
        sucset = [set() for x in range(size)]
        if not seqs: return False
        for seq in seqs:
            if any(s > size or s < 1 for s in seq):
                return False
            for i in range(1, len(seq)):
                if seq[i] not in sucset[seq[i - 1] - 1]:
                    indeg[seq[i] - 1] += 1
                    sucset[seq[i - 1] - 1].add(seq[i])

        q = [i for i in org if not indeg[i - 1]]
        for x in range(size):
            if len(q) != 1 or q[0] != org[x]:
                return False
            nq = []
            for suc in sucset[q[0] - 1]:
                indeg[suc - 1] -= 1
                if not indeg[suc - 1]:
                    nq.append(suc)
            q = nq
        return True

# V1' 
# http://bookshadow.com/weblog/2016/10/30/leetcode-sequence-reconstruction/
class Solution(object):
    def sequenceReconstruction(self, org, seqs):
        """
        :type org: List[int]
        :type seqs: List[List[int]]
        :rtype: bool
        """
        indexes = {e : i for i, e in enumerate(org)}
        edges = set()

        if not seqs: return False
        for seq in seqs:
            for s in seq:
                if s not in indexes:
                    return False
            for i in range(1, len(seq)):
                pre, cur = seq[i - 1], seq[i]
                if indexes[pre] > indexes[cur]:
                    return False
                edges.add((pre, cur))

        for x in range(1, len(org)):
            if (org[x - 1], org[x]) not in edges:
                return False
        return True

# V1''
# https://www.jiuzhang.com/solution/sequence-reconstruction/#tag-highlight-lang-python
class Solution:
    """
    @param org: a permutation of the integers from 1 to n
    @param seqs: a list of sequences
    @return: true if it can be reconstructed only one or false
    """
    def sequenceReconstruction(self, org, seqs):
        graph = self.build_graph(seqs)
        topo_order = self.topological_sort(graph)
        return topo_order == org
            
    def build_graph(self, seqs):
        # initialize graph
        graph = {}
        for seq in seqs:
            for node in seq:
                if node not in graph:
                    graph[node] = set()
        
        for seq in seqs:
            for i in range(1, len(seq)):
                graph[seq[i - 1]].add(seq[i])

        return graph
    
    def get_indegrees(self, graph):
        indegrees = {
            node: 0
            for node in graph
        }
        
        for node in graph:
            for neighbor in graph[node]:
                indegrees[neighbor] += 1
                
        return indegrees
        
    def topological_sort(self, graph):
        indegrees = self.get_indegrees(graph)
        
        queue = []
        for node in graph:
            if indegrees[node] == 0:
                queue.append(node)
        
        topo_order = []
        while queue:
            if len(queue) > 1:
                # there must exist more than one topo orders
                return None
                
            node = queue.pop()
            topo_order.append(node)
            for neighbor in graph[node]:
                indegrees[neighbor] -= 1
                if indegrees[neighbor] == 0:
                    queue.append(neighbor)
                    
        if len(topo_order) == len(graph):
            return topo_order        
        return None

# V2 
# Time:  O(n * s), n is the size of org, s is the size of seqs
# Space: O(n)
import collections
class Solution(object):
    def sequenceReconstruction(self, org, seqs):
        """
        :type org: List[int]
        :type seqs: List[List[int]]
        :rtype: bool
        """
        if not seqs:
            return False
        pos = [0] * (len(org) + 1)
        for i in range(len(org)):
            pos[org[i]] = i

        is_matched = [False] * (len(org) + 1)
        cnt_to_match = len(org) - 1
        for seq in seqs:
            for i in range(len(seq)):
                if not 0 < seq[i] <= len(org):
                    return False
                if i == 0:
                    continue
                if pos[seq[i-1]] >= pos[seq[i]]:
                    return False
                if is_matched[seq[i-1]] == False and pos[seq[i-1]] + 1 == pos[seq[i]]:
                    is_matched[seq[i-1]] = True
                    cnt_to_match -= 1

        return cnt_to_match == 0

# Time:  O(|V| + |E|)
# Space: O(|E|)
class Solution2(object):
    def sequenceReconstruction(self, org, seqs):
        """
        :type org: List[int]
        :type seqs: List[List[int]]
        :rtype: bool
        """
        graph = collections.defaultdict(set)
        indegree = collections.defaultdict(int)
        integer_set = set()
        for seq in seqs:
            for i in seq:
                integer_set.add(i)
            if len(seq) == 1:
                if seq[0] not in indegree:
                    indegree[seq[0]] = 0
                continue
            for i in range(len(seq)-1):
                if seq[i] not in indegree:
                    indegree[seq[i]] = 0
                if seq[i+1] not in graph[seq[i]]:
                    graph[seq[i]].add(seq[i+1])
                    indegree[seq[i+1]] += 1

        cnt_of_zero_indegree = 0
        res = []
        q = []
        for i in indegree:
            if indegree[i] == 0:
                cnt_of_zero_indegree += 1
                if cnt_of_zero_indegree > 1:
                    return False
                q.append(i)

        while q:
            i = q.pop()
            res.append(i)
            cnt_of_zero_indegree = 0
            for j in graph[i]:
                indegree[j] -= 1
                if indegree[j] == 0:
                    cnt_of_zero_indegree += 1
                    if cnt_of_zero_indegree > 1:
                        return False
                    q.append(j)
        return res == org and len(org) == len(integer_set)