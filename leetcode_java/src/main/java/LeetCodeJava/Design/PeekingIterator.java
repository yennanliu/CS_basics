package LeetCodeJava.Design;

// https://leetcode.com/problems/peeking-iterator/

import java.util.Iterator;

/**
 *  284. Peeking Iterator
 *  Medium
 *
 *  Design an iterator that supports the peek operation on an existing iterator in
 *  addition to the hasNext and the next operations.
 *
 *  Implement the PeekingIterator class:
 *   - PeekingIterator(Iterator<int> nums) Initializes the object with the given
 *     integer iterator iterator.
 *   - int next() Returns the next element in the array and moves the pointer to the
 *     next element.
 *   - boolean hasNext() Returns true if there are still elements in the array.
 *   - int peek() Returns the next element in the array without moving the pointer.
 *
 *  Example 1:
 *    Input
 *      ["PeekingIterator", "next", "peek", "next", "next", "hasNext"]
 *      [[[1, 2, 3]], [], [], [], [], []]
 *    Output
 *      [null, 1, 2, 2, 3, false]
 *
 *  Constraints:
 *    1 <= nums.length <= 1000
 *    1 <= nums[i] <= 1000
 *    All the calls to next and peek are valid.
 *    At most 1000 calls will be made to next, hasNext, and peek.
 */
public class PeekingIterator implements Iterator<Integer> {

    // V0
    // IDEA: keep ONE element pre-fetched from the underlying iterator.
    //       peek() hands back the cached element, next() hands it back and refills.
    /**
     * time = O(1) per op
     * space = O(1)
     */
    private final Iterator<Integer> iterator;
    private Integer cached; // null => underlying iterator is exhausted

    public PeekingIterator(Iterator<Integer> iterator) {
        this.iterator = iterator;
        this.cached = iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public Integer peek() {
        return this.cached;
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    @Override
    public Integer next() {
        Integer cur = this.cached;
        this.cached = this.iterator.hasNext() ? this.iterator.next() : null;
        return cur;
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    @Override
    public boolean hasNext() {
        return this.cached != null;
    }
}
