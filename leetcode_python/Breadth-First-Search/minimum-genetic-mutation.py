"""

433. Minimum Genetic Mutation
Medium
Topics
premium lock icon
Companies
A gene string can be represented by an 8-character long string, with choices from 'A', 'C', 'G', and 'T'.

Suppose we need to investigate a mutation from a gene string startGene to a gene string endGene where one mutation is defined as one single character changed in the gene string.

For example, "AACCGGTT" --> "AACCGGTA" is one mutation.
There is also a gene bank bank that records all the valid gene mutations. A gene must be in bank to make it a valid gene string.

Given the two gene strings startGene and endGene and the gene bank bank, return the minimum number of mutations needed to mutate from startGene to endGene. If there is no such a mutation, return -1.

Note that the starting point is assumed to be valid, so it might not be included in the bank.

 

Example 1:

Input: startGene = "AACCGGTT", endGene = "AACCGGTA", bank = ["AACCGGTA"]
Output: 1
Example 2:

Input: startGene = "AACCGGTT", endGene = "AAACGGTA", bank = ["AACCGGTA","AACCGCTA","AAACGGTA"]
Output: 2
 

Constraints:

0 <= bank.length <= 10
startGene.length == endGene.length == bank[i].length == 8
startGene, endGene, and bank[i] consist of only the characters ['A', 'C', 'G', 'T'].
 


"""

# V0 
class Solution(object):
    def minMutation(self, startGene, endGene, bank):
        """
        :type startGene: str
        :type endGene: str
        :type bank: List[str]
        :rtype: int
        """
        pass


# V0-1
# IDEA: BFS (gpt)
from collections import deque

class Solution(object):
    def minMutation(self, startGene, endGene, bank):
        if startGene == endGene:
            return 0

        # Only endGene must exist in the bank.
        if endGene not in bank:
            return -1

        bank = set(bank)          # O(1) lookup
        q = deque([(startGene, 0)])
        visited = {startGene}

        genes = ['A', 'C', 'G', 'T']

        while q:
            cur_gene, steps = q.popleft()

            if cur_gene == endGene:
                return steps

            for i in range(len(cur_gene)):
                for ch in genes:
                    if ch == cur_gene[i]:
                        continue

                    """
                    NOTE !!!


                    how we update string by idx
                    """
                    new_gene = (
                        cur_gene[:i] +
                        ch +
                        cur_gene[i + 1:]
                    )

                    if new_gene in bank and new_gene not in visited:
                        """
                        NOTE !!!


                        we need to update `visited` immediately,
                        or another bfs call will visit the same `new_gene` again


                        (e.g. Otherwise multiple parents may enqueue the same gene.)
                        """
                        visited.add(new_gene)
                        q.append((new_gene, steps + 1))

        return -1


# V0-2
# IDEA: BFS (GEMINI)
import collections

class Solution(object):
    def minMutation(self, startGene, endGene, bank):
        """
        :type startGene: str
        :type endGene: str
        :type bank: List[str]
        :rtype: int
        """
        # Convert bank to a set for O(1) lookups
        bank_set = set(bank)
        
        # Edge case: if endGene isn't in the bank, it's impossible
        if endGene not in bank_set:
            return -1
            
        # Queue stores: [current_gene, mutation_count]
        q = collections.deque([[startGene, 0]])
        
        # Visited set prevents infinite loops. Add startGene immediately.
        visited = set([startGene])
        
        # Renamed to avoid overwriting variables
        chars = ["A", "C", "G", "T"]
        
        # Because we store 'steps' in the queue, we don't need the `for _ in range(size)` layer loop!
        while q:
            cur_node, steps = q.popleft()
            
            if cur_node == endGene:
                return steps 
                
            # Try mutating each of the 8 characters
            for i in range(len(cur_node)):
                for c in chars:
                    # Minor optimization: skip if the character is the same
                    if c == cur_node[i]:
                        continue
                        
                    # Create the new mutated gene
                    new_node = cur_node[:i] + c + cur_node[i+1:]
                    
                    # If valid and unvisited, queue it up
                    if new_node in bank_set and new_node not in visited:
                        visited.add(new_node)
                        q.append([new_node, steps + 1])
                        
        return -1


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82903720
import collections
# time = O(b * L), b = size of bank, L = length of gene string
# space = O(b)
class Solution(object):
    def minMutation(self, start, end, bank):
        """
        :type start: str
        :type end: str
        :type bank: List[str]
        :rtype: int
        """
        bfs = collections.deque()
        bfs.append((start, 0))
        bankset = set(bank)
        while bfs:
            gene, step = bfs.popleft()
            if gene == end:
                return step
            for i in range(len(gene)):
                for x in "ACGT":
                    newGene = gene[:i] + x + gene[i+1:]
                    if newGene in bank and newGene != gene:
                        bfs.append((newGene, step + 1))
                        bank.remove(newGene)
        return -1

# V1'
# https://www.jiuzhang.com/solution/minimum-genetic-mutation/#tag-highlight-lang-python
from collections import deque
# time = O(b * L), b = size of bank, L = length of gene string
# space = O(b)
class Solution:
    """
    @param start: 
    @param end: 
    @param bank: 
    @return: the minimum number of mutations needed to mutate from "start" to "end"
    """
    def minMutation(self, start, end, bank):
        # Write your code here
        if not bank:
            return -1
        bank = set(bank)
        h = deque()
        h.append((start, 0))
        while h:
            seq, step = h.popleft()
            if seq == end:
                return step
            for c in "ACGT":
                for i in range(len(seq)):
                    new_seq = seq[:i] + c + seq[i + 1:]
                    if new_seq in bank:
                        h.append((new_seq, step + 1))
                        bank.remove(new_seq)
        return -1

# V2
# time = O(n * b), n is the length of gene string, b is size of bank
# space = O(b)
from collections import deque
class Solution(object):
    def minMutation(self, start, end, bank):
        """
        :type start: str
        :type end: str
        :type bank: List[str]
        :rtype: int
        """
        lookup = {}
        for b in bank:
            lookup[b] = False

        q = deque([(start, 0)])
        while q:
            cur, level = q.popleft()
            if cur == end:
                return level

            for i in range(len(cur)):
                for c in ['A', 'T', 'C', 'G']:
                    if cur[i] == c:
                        continue

                    next_str = cur[:i] + c + cur[i+1:]
                    if next_str in lookup and lookup[next_str] == False:
                        q.append((next_str, level+1))
                        lookup[next_str] = True
        return -1
