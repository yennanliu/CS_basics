package LeetCodeJava.Design;

// https://leetcode.com/problems/fancy-sequence/

import java.util.ArrayList;
import java.util.List;

/**
 *  1622. Fancy Sequence
 *  Hard
 *
 *  Write an API that generates fancy sequences using the append, addAll and
 *  multAll operations.
 *
 *  Implement the Fancy class:
 *    Fancy() Initializes the object with an empty sequence.
 *    void append(int val) Appends an integer val to the end of the sequence.
 *    void addAll(int inc) Increments all existing values in the sequence by an
 *      integer inc.
 *    void multAll(int m) Multiplies all existing values in the sequence by an
 *      integer m.
 *    int getIndex(int idx) Gets the current value at index idx (0-indexed) of the
 *      sequence modulo 10^9 + 7. If the index is greater or equal than the length
 *      of the sequence, return -1.
 *
 *  Example 1:
 *    Input
 *      ["Fancy","append","addAll","append","multAll","getIndex","addAll",
 *       "append","multAll","getIndex","getIndex","getIndex"]
 *      [[],[2],[3],[7],[2],[0],[3],[10],[2],[0],[1],[2]]
 *    Output
 *      [null,null,null,null,null,10,null,null,null,26,34,20]
 *    Explanation
 *      append(2)   -> [2]
 *      addAll(3)   -> [5]
 *      append(7)   -> [5,7]
 *      multAll(2)  -> [10,14]
 *      getIndex(0) -> 10
 *      addAll(3)   -> [13,17]
 *      append(10)  -> [13,17,10]
 *      multAll(2)  -> [26,34,20]
 *      getIndex(0) -> 26, getIndex(1) -> 34, getIndex(2) -> 20
 *
 *  Constraints:
 *    1 <= val, inc, m <= 100
 *    0 <= idx <= 10^5
 *    At most 10^5 calls total will be made to append, addAll, multAll and getIndex.
 */
public class FancySequence {

    // V0
    // IDEA: LAZY AFFINE TRANSFORM + MODULAR INVERSE (Fermat)
    //
    //       every stored element reads back as  v = mul * base + add, where
    //       (mul, add) is ONE global affine transform accumulated so far:
    //         addAll(inc) -> add = add + inc
    //         multAll(m)  -> mul = mul * m ; add = add * m
    //       so addAll / multAll are O(1) instead of touching the whole sequence.
    //
    //       append(val) must store a `base` s.t. applying the CURRENT (mul, add)
    //       gives val back, i.e. base = (val - add) / mul (mod 1e9+7).
    //       dividing == multiplying by the modular inverse; MOD is prime, so
    //       inv(mul) = mul^(MOD-2) by Fermat's little theorem.
    /**
     * time = O(1) for append / addAll / multAll, O(log MOD) for getIndex
     * space = O(n), n = number of appended values
     */
    private static final long MOD = 1_000_000_007L;

    private final List<Long> base;
    private long mul;
    private long add;

    public FancySequence() {
        this.base = new ArrayList<>();
        this.mul = 1L;
        this.add = 0L;
    }

    public void append(int val) {
        // base = (val - add) * inv(mul)
        long v = ((val - add) % MOD + MOD) % MOD;
        base.add(v * modPow(mul, MOD - 2) % MOD);
    }

    public void addAll(int inc) {
        add = (add + inc) % MOD;
    }

    public void multAll(int m) {
        mul = mul * m % MOD;
        add = add * m % MOD;
    }

    public int getIndex(int idx) {
        if (idx < 0 || idx >= base.size()) {
            return -1;
        }
        return (int) ((base.get(idx) * mul + add) % MOD);
    }

    /** x^e mod MOD, fast exponentiation */
    private long modPow(long x, long e) {
        long res = 1L;
        x %= MOD;
        while (e > 0) {
            if ((e & 1L) == 1L) {
                res = res * x % MOD;
            }
            x = x * x % MOD;
            e >>= 1;
        }
        return res;
    }
}
