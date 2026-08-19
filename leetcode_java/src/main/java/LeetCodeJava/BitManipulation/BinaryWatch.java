package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/binary-watch/

import java.util.ArrayList;
import java.util.List;

/**
 *  401. Binary Watch
 *  Easy
 *
 *  A binary watch has 4 LEDs on the top to represent the hours (0-11), and 6
 *  LEDs on the bottom to represent the minutes (0-59). Each LED represents a
 *  zero or one, with the least significant bit on the right.
 *
 *  Given an integer turnedOn which represents the number of LEDs that are
 *  currently on (ignoring the PM), return all possible times the watch could
 *  represent. You may return the answer in any order.
 *
 *  The hour must not contain a leading zero, e.g. "01:00" is not valid.
 *  The minute must be consist of two digits, e.g. "10:2" is not valid.
 *
 *  Example 1:
 *  Input: turnedOn = 1
 *  Output: ["0:01","0:02","0:04","0:08","0:16","0:32","1:00","2:00","4:00","8:00"]
 *
 *  Example 2:
 *  Input: turnedOn = 9
 *  Output: []
 *
 *  Constraints:
 *  0 <= turnedOn <= 10
 */
public class BinaryWatch {

    // V0
    // IDEA: brute force over all 12 * 60 valid times, keep those whose total
    //       popcount equals turnedOn
    /**
     * time = O(12 * 60) = O(1)
     * space = O(1) (excluding output)
     */
    public List<String> readBinaryWatch(int turnedOn) {
        List<String> res = new ArrayList<>();
        for (int h = 0; h < 12; h++) {
            for (int m = 0; m < 60; m++) {
                if (Integer.bitCount(h) + Integer.bitCount(m) == turnedOn) {
                    res.add(h + ":" + (m < 10 ? "0" + m : String.valueOf(m)));
                }
            }
        }
        return res;
    }
}
