package LeetCodeJava.Design;

// https://leetcode.com/problems/design-an-ordered-stream/

import java.util.ArrayList;
import java.util.List;

/**
 *  1656. Design an Ordered Stream
 *  Easy
 *
 *  There is a stream of n (idKey, value) pairs arriving in an arbitrary order, where idKey is
 *  an integer between 1 and n and value is a string. No two pairs have the same id.
 *
 *  Design a stream that returns the values in increasing order of their IDs by returning a
 *  chunk (list) of values after each insertion. The concatenation of all the chunks should
 *  result in a list of the sorted values.
 *
 *  Implement the OrderedStream class:
 *
 *   - OrderedStream(int n) Constructs the stream to take n values.
 *   - String[] insert(int idKey, String value) Inserts the pair (idKey, value) into the
 *     stream, then returns the largest possible chunk of currently inserted values that
 *     appear next in the order.
 *
 *  Example 1:
 *
 *  Input
 *  ["OrderedStream", "insert", "insert", "insert", "insert", "insert"]
 *  [[5], [3, "ccccc"], [1, "aaaaa"], [2, "bbbbb"], [5, "eeeee"], [4, "ddddd"]]
 *  Output
 *  [null, [], ["aaaaa"], ["bbbbb", "ccccc"], [], ["ddddd", "eeeee"]]
 *
 *  Constraints:
 *
 *   1 <= n <= 1000
 *   1 <= id <= n
 *   value.length == 5, value consists only of lowercase letters
 *   Each call to insert will have a unique id, exactly n calls will be made to insert
 */
public class DesignAnOrderedStream {

    // V0
    // IDEA: BUCKET BY ID + MONOTONE POINTER
    //       store value at slot idKey, then flush the contiguous filled prefix from ptr.
    //       ptr never moves backwards -> amortized O(1) per insert.
    /**
     * time = O(1) amortized per insert (O(n) total over all n inserts)
     * space = O(n)
     */
    private final String[] data;
    private int ptr;

    public DesignAnOrderedStream(int n) {
        this.data = new String[n + 1];
        this.ptr = 1;
    }

    public String[] insert(int idKey, String value) {
        data[idKey] = value;
        List<String> res = new ArrayList<>();
        while (ptr < data.length && data[ptr] != null) {
            res.add(data[ptr]);
            ptr++;
        }
        return res.toArray(new String[0]);
    }
}
