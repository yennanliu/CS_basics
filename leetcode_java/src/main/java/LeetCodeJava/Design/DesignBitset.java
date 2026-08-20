package LeetCodeJava.Design;

// https://leetcode.com/problems/design-bitset/

/**
 *  2166. Design Bitset
 *  Medium
 *
 *  A Bitset is a data structure that compactly stores bits.
 *
 *  Implement the Bitset class:
 *
 *   - Bitset(int size) Initializes the Bitset with size bits, all of which are 0.
 *   - void fix(int idx) Updates the value of the bit at the index idx to 1. If the value
 *     was already 1, no change occurs.
 *   - void unfix(int idx) Updates the value of the bit at the index idx to 0. If the
 *     value was already 0, no change occurs.
 *   - void flip() Flips the values of each bit in the Bitset.
 *   - boolean all() Returns true if the value of each bit in the Bitset is 1.
 *   - boolean one() Returns true if there is at least one bit with value 1.
 *   - int count() Returns the total number of bits in the Bitset which have value 1.
 *   - String toString() Returns the current composition of the Bitset.
 *
 *  Example 1:
 *    Input
 *      ["Bitset","fix","fix","flip","all","unfix","flip","one","unfix","count","toString"]
 *      [[5],[3],[1],[],[],[0],[],[],[0],[],[]]
 *    Output
 *      [null,null,null,null,false,null,null,true,null,2,"01010"]
 *    Explanation
 *      Bitset bs = new Bitset(5); // "00000"
 *      fix(3);  -> "00010"      fix(1);  -> "01010"
 *      flip();  -> "10101"      all();   -> false
 *      unfix(0);-> "00101"      flip();  -> "11010"
 *      one();   -> true         unfix(0);-> "01010"
 *      count(); -> 2            toString(); -> "01010"
 *
 *  Constraints:
 *    1 <= size <= 10^5
 *    0 <= idx < size
 *    At most 10^5 calls will be made in total to fix, unfix, flip, all, and toString.
 *    At least one call will be made to all, one, count, or toString.
 *    At most 5 * 10^4 calls will be made to toString.
 */
public class DesignBitset {

    // V0
    // IDEA: STORE THE RAW BITS *PLUS* A LAZY "INVERTED" FLAG SO flip() IS O(1)
    //
    //   physically rewriting every bit on flip() would make the operation O(size),
    //   and there can be 10^5 of them. instead keep the raw array plus a boolean
    //   `inverted`; the LOGICAL value of bit i is
    //       bits[i] ^ inverted
    //
    //   with `ones` tracking the LOGICAL count of set bits:
    //       flip()  -> inverted ^= 1 ; ones = size - ones          (O(1))
    //       fix     -> only touch the array when the logical bit is 0
    //       unfix   -> only touch it when the logical bit is 1
    //   toString() is the one unavoidable O(size) call.
    /**
     * time = O(1) per op except toString(), which is O(size)
     * space = O(size)
     */
    private final int size;
    private final byte[] bits;
    private int inverted = 0;
    private int ones = 0;        // number of LOGICAL 1 bits

    public DesignBitset(int size) {
        this.size = size;
        this.bits = new byte[size];
    }

    private int get(int idx) {
        return this.bits[idx] ^ this.inverted;
    }

    public void fix(int idx) {
        if (get(idx) == 0) {
            this.bits[idx] ^= 1;
            this.ones++;
        }
    }

    public void unfix(int idx) {
        if (get(idx) == 1) {
            this.bits[idx] ^= 1;
            this.ones--;
        }
    }

    public void flip() {
        this.inverted ^= 1;
        this.ones = this.size - this.ones;
    }

    public boolean all() {
        return this.ones == this.size;
    }

    public boolean one() {
        return this.ones > 0;
    }

    public int count() {
        return this.ones;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.size);
        for (int i = 0; i < this.size; i++) {
            sb.append((char) ('0' + (this.bits[i] ^ this.inverted)));
        }
        return sb.toString();
    }
}
