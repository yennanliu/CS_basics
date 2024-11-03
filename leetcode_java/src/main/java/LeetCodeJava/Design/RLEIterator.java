package LeetCodeJava.Design;

// https://leetcode.com/problems/rle-iterator/description/

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * 900. RLE Iterator
 * Medium
 * Topics
 * Companies
 * We can use run-length encoding (i.e., RLE) to encode a sequence of integers. In a run-length encoded array of even length encoding (0-indexed), for all even i, encoding[i] tells us the number of times that the non-negative integer value encoding[i + 1] is repeated in the sequence.
 *
 * For example, the sequence arr = [8,8,8,5,5] can be encoded to be encoding = [3,8,2,5]. encoding = [3,8,0,9,2,5] and encoding = [2,8,1,8,2,5] are also valid RLE of arr.
 * Given a run-length encoded array, design an iterator that iterates through it.
 *
 * Implement the RLEIterator class:
 *
 * RLEIterator(int[] encoded) Initializes the object with the encoded array encoded.
 * int next(int n) Exhausts the next n elements and returns the last element exhausted in this way. If there is no element left to exhaust, return -1 instead.
 *
 *
 * Example 1:
 *
 * Input
 * ["RLEIterator", "next", "next", "next", "next"]
 * [[[3, 8, 0, 9, 2, 5]], [2], [1], [1], [2]]
 * Output
 * [null, 8, 8, 5, -1]
 *
 * Explanation
 * RLEIterator rLEIterator = new RLEIterator([3, 8, 0, 9, 2, 5]); // This maps to the sequence [8,8,8,5,5].
 * rLEIterator.next(2); // exhausts 2 terms of the sequence, returning 8. The remaining sequence is now [8, 5, 5].
 * rLEIterator.next(1); // exhausts 1 term of the sequence, returning 8. The remaining sequence is now [5, 5].
 * rLEIterator.next(1); // exhausts 1 term of the sequence, returning 5. The remaining sequence is now [5].
 * rLEIterator.next(2); // exhausts 2 terms, returning -1. This is because the first term exhausted was 5,
 * but the second term did not exist. Since the last term exhausted does not exist, we return -1.
 *
 *
 * Constraints:
 *
 * 2 <= encoding.length <= 1000
 * encoding.length is even.
 * 0 <= encoding[i] <= 109
 * 1 <= n <= 109
 * At most 1000 calls will be made to next.
 *
 */

public class RLEIterator {
    /**
     * Your RLEIterator object will be instantiated and called as such:
     * RLEIterator obj = new RLEIterator(encoding);
     * int param_1 = obj.next(n);
     */
    // V0
    // TODO : implement
//    class RLEIterator {
//
//        public RLEIterator(int[] encoding) {
//
//        }
//
//        public int next(int n) {
//
//        }
//    }


    // V1-1
    // IDEA : TREEMAP
    // https://youtu.be/Y_x4H9nMps0?si=O83hIcI5zilPGiWK
    class RLEIterator_1 {
        private long next;
        private TreeMap<Long, Integer> tm;

        public RLEIterator_1(int[] encoding) {
            next = 0;
            tm = new TreeMap<>();
            long total = 0;
            for (int i = 0; i < encoding.length; i += 2) {
                if (encoding[i] == 0)
                    continue;
                total += encoding[i];
                tm.put(total, encoding[i + 1]);
            }
        }

        public int next(int n) {
            next += n;
            return tm.ceilingKey(next) != null ? tm.get(tm.ceilingKey(next)) : -1;
        }
    }

    // V1-2
    // IDEA : BRUTE FORCE (TLE)
    // https://youtu.be/Y_x4H9nMps0?si=O83hIcI5zilPGiWK
    class RLEIterator_1_1 {
        int index;
        List<Integer> list;

        public RLEIterator_1_1(int[] A) {
            index = 0;
            list = new ArrayList<>();
            for (int i = 0; i < A.length; i += 2) {
                for (int j = A[i]; j > 0; j--) {
                    list.add(A[i + 1]);
                }
            }
        }

        public int next(int n) {
            index += n;
            return index <= list.size() ? list.get(index - 1) : -1;
        }
    }

    // V2
    // https://leetcode.com/problems/rle-iterator/solutions/1418913/java-faster-than-100/
    class RLEIterator_2 {
        int index;
        int[] encoding;
        public RLEIterator_2(int[] encoding) {
            index = 0;
            this.encoding = encoding;
        }

        public int next(int n) {
            while (index < encoding.length && n > encoding[index]) {
                n -= encoding[index];
                index += 2;

            }
            if (index >= encoding.length) {
                return -1;
            }
            encoding[index] -= n;
            return encoding[index + 1];
        }
    }

    // V3
    // IDEA : RECURSION
    // https://leetcode.com/problems/rle-iterator/solutions/1139032/java-o-n-time-o-1-space-recursive-approach/
    class RLEIterator_3 {

        int index;
        int[] A;

        public RLEIterator_3(int[] A) {
            this.A = A;
            index = 0;
        }

        public int next(int n) {
            if (index >= A.length) {
                return -1;
            }

            if (n <= A[index]) {
                A[index] -= n;
                return A[index + 1];
            } else {
                index += 2;
                return next(n - A[index - 2]);
            }
        }
    }

    // V4
    // https://leetcode.com/problems/rle-iterator/solutions/4470485/short-concise-java-solution-beats-97/
    class RLEIterator_4 {
        int[] encodingArr;
        int cursor;
        public RLEIterator_4(int[] encoding) {
            this.encodingArr = new int[encoding.length];
            for(int i = 0;i<encoding.length;i++){
                encodingArr[i] = encoding[i];
            }
            this.cursor = 0;
        }

        public int next(int n) {
            while(cursor < encodingArr.length){
                if(encodingArr[cursor] >= n){
                    encodingArr[cursor] -= n;
                    return encodingArr[cursor+1];
                }else{
                    n -= encodingArr[cursor];
                    cursor +=2;
                }
            }
            return -1;

        }
    }

}
