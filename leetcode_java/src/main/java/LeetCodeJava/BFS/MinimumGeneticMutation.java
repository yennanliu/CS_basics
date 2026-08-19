package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-genetic-mutation/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 *  433. Minimum Genetic Mutation
 *  Medium
 *
 *  A gene string can be represented by an 8-character long string, with choices
 *  from 'A', 'C', 'G', and 'T'.
 *
 *  Suppose we need to investigate a mutation from a gene string startGene to a
 *  gene string endGene where one mutation is defined as one single character
 *  changed in the gene string.
 *
 *  For example, "AACCGGTT" --> "AACCGGTA" is one mutation.
 *
 *  There is also a gene bank bank that records all the valid gene mutations.
 *  A gene must be in bank to make it a valid gene string.
 *
 *  Given the two gene strings startGene and endGene and the gene bank bank,
 *  return the minimum number of mutations needed to mutate from startGene to
 *  endGene. If there is no such a mutation, return -1.
 *
 *  Note that the starting point is assumed to be valid, so it might not be
 *  included in the bank.
 *
 *
 *  Example 1:
 *
 *  Input: startGene = "AACCGGTT", endGene = "AACCGGTA", bank = ["AACCGGTA"]
 *  Output: 1
 *
 *  Example 2:
 *
 *  Input: startGene = "AACCGGTT", endGene = "AAACGGTA",
 *         bank = ["AACCGGTA","AACCGCTA","AAACGGTA"]
 *  Output: 2
 *
 *
 *  Constraints:
 *
 *  0 <= bank.length <= 10
 *  startGene.length == endGene.length == bank[i].length == 8
 *  startGene, endGene, and bank[i] consist of only the characters
 *  ['A', 'C', 'G', 'T'].
 */
public class MinimumGeneticMutation {

    // V0
    // IDEA: BFS over gene strings — each step flips 1 char, the result must be in bank
    /**
     * time = O(B * L * 4), B = bank size, L = gene length (8)
     * space = O(B)
     */
    public int minMutation(String startGene, String endGene, String[] bank) {
        if (startGene == null || endGene == null) {
            return -1;
        }
        if (startGene.equals(endGene)) {
            return 0;
        }

        Set<String> valid = new HashSet<>();
        if (bank != null) {
            for (String b : bank) {
                valid.add(b);
            }
        }
        if (!valid.contains(endGene)) {
            return -1;
        }

        char[] genes = new char[] { 'A', 'C', 'G', 'T' };
        Set<String> visited = new HashSet<>();
        visited.add(startGene);

        Deque<String> q = new ArrayDeque<>();
        q.add(startGene);
        int step = 0;

        while (!q.isEmpty()) {
            int size = q.size();
            step++;
            for (int s = 0; s < size; s++) {
                String cur = q.poll();
                char[] arr = cur.toCharArray();
                for (int i = 0; i < arr.length; i++) {
                    char origin = arr[i];
                    for (char g : genes) {
                        if (g == origin) {
                            continue;
                        }
                        arr[i] = g;
                        String next = new String(arr);
                        if (valid.contains(next) && !visited.contains(next)) {
                            if (next.equals(endGene)) {
                                return step;
                            }
                            visited.add(next);
                            q.add(next);
                        }
                    }
                    arr[i] = origin;
                }
            }
        }
        return -1;
    }
}
