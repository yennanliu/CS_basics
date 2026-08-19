package LeetCodeJava.String;

// https://leetcode.com/problems/ip-to-cidr/

import java.util.ArrayList;
import java.util.List;

/**
 *  751. IP to CIDR
 *  Medium
 *
 *  An IP address is a formatted 32-bit unsigned integer where each group of 8
 *  bits is printed as a decimal number and the dot character '.' splits the groups.
 *  A CIDR block is a format used to denote a specific set of IP addresses. It is
 *  a string consisting of a base IP address, followed by a slash, followed by a
 *  prefix length k. The addresses it covers are all the IPs whose first k bits
 *  are the same as the base IP address.
 *
 *  Given a start IP address ip and the number of IPs we need to cover n, return
 *  the shortest list of CIDR blocks that covers the range of IP addresses
 *  [ip, ip + n). Nothing outside this range should be covered.
 *
 *  Example 1:
 *    Input:  ip = "255.0.0.7", n = 10
 *    Output: ["255.0.0.7/32","255.0.0.8/29","255.0.0.16/32"]
 *
 *  Example 2:
 *    Input:  ip = "117.145.102.62", n = 8
 *    Output: ["117.145.102.62/31","117.145.102.64/30","117.145.102.68/31"]
 *
 *  Constraints:
 *    7 <= ip.length <= 15
 *    ip is a valid IPv4 on the form "a.b.c.d" where a, b, c, d are integers in [0, 255].
 *    1 <= n <= 1000
 *    Every implied address ip + x (for x < n) will be a valid IPv4 address.
 */
public class IPToCIDR {

    // V0
    // IDEA: greedy — at each start take the largest block that (a) is aligned to
    //       the start's lowest set bit and (b) does not exceed the remaining count
    /**
     * time = O(log n * 32)  (at most ~2*log(n) blocks emitted)
     * space = O(1)  (excluding the output list)
     */
    public List<String> ipToCIDR(String ip, int n) {
        List<String> res = new ArrayList<>();
        long start = ipToLong(ip);
        long remain = n;

        while (remain > 0) {
            // largest block size allowed by the alignment of `start`
            int alignBits = (start == 0) ? 32 : Long.numberOfTrailingZeros(start);
            if (alignBits > 32) {
                alignBits = 32;
            }
            // largest block size not exceeding `remain`
            int countBits = 63 - Long.numberOfLeadingZeros(remain);
            int bits = Math.min(alignBits, countBits);

            int maskLen = 32 - bits;
            res.add(longToIp(start) + "/" + maskLen);

            long size = 1L << bits;
            start += size;
            remain -= size;
        }
        return res;
    }

    private long ipToLong(String ip) {
        long result = 0;
        for (String part : ip.split("\\.")) {
            result = result * 256 + Integer.parseInt(part);
        }
        return result;
    }

    private String longToIp(long x) {
        return ((x >> 24) & 255) + "." + ((x >> 16) & 255) + "."
                + ((x >> 8) & 255) + "." + (x & 255);
    }
}
