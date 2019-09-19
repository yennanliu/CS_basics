# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82903720
import collections
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
# Time:  O(n * b), n is the length of gene string, b is size of bank
# Space: O(b)
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
