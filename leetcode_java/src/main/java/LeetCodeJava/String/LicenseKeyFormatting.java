package LeetCodeJava.String;

// https://leetcode.com/problems/license-key-formatting/description/

public class LicenseKeyFormatting {


    // V0
    // IDEA : STRING OP (strip "-", upper case, then re-group from the LEFT)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/license-key-formatting.py
    /**
     *  NOTE !!!
     *
     *   1) ALL "-" are removed first, and every char is upper cased
     *   2) EVERY group has exactly k chars, EXCEPT the FIRST one,
     *      which holds the `remainder` (n % k) chars
     *      -> and if (n % k == 0), the first group is a full k group as well
     *
     *   e.g. s = "2-5g-3-J", k = 2
     *        -> clean = "25G3J" (n = 5), n % k = 1
     *        -> "2" + "-" + "5G" + "-" + "3J"  =  "2-5G-3J"
     *
     * time = O(N)
     * space = O(N)
     */
    public String licenseKeyFormatting(String s, int k) {
        // edge
        if (s == null || s.isEmpty() || k <= 0) {
            return "";
        }

        // step 1) strip "-" + upper case
        StringBuilder clean = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '-') {
                clean.append(Character.toUpperCase(c));
            }
        }

        int n = clean.length();
        if (n == 0) {
            return "";
        }

        // step 2) re-group
        StringBuilder sb = new StringBuilder();
        /** NOTE !!! the FIRST group takes the `remainder` (n % k) chars */
        int firstGroupSize = n % k;
        int idx = 0;
        if (firstGroupSize > 0) {
            sb.append(clean, 0, firstGroupSize);
            idx = firstGroupSize;
        }

        while (idx < n) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append(clean, idx, idx + k);
            idx += k;
        }

        return sb.toString();
    }

    // V1
    // https://leetcode.com/problems/license-key-formatting/solutions/316752/clean-and-self-explanatory-11-ms-java-solution/
    /**
     * time = O(N)
     * space = O(N)
     */
    public String licenseKeyFormatting_1(String S, int K) {

        StringBuilder sb = new StringBuilder();

        for (int i = S.length() - 1, count = 0 ; i >= 0 ; --i) {

            char c = S.charAt(i);
            if (c == '-') continue;

            // put a '-' first if we already append K characters
            if (count == K) {
                sb.append('-');
                count = 0;
            }

            sb.append(Character.toUpperCase(c));
            ++count;
        }

        return sb.reverse().toString();
    }

    // V2
    // https://leetcode.com/problems/license-key-formatting/solutions/2087766/java-solution-16ms-runtime/
    /**
     * time = O(N)
     * space = O(N)
     */
    public String licenseKeyFormatting_2(String s, int k) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '-') continue;
            if (count == k) {
                sb.append('-');
                count = 0;
            }
            sb.append(s.charAt(i));
            count++;
        }
        return sb.reverse().toString().toUpperCase();
    }

}
