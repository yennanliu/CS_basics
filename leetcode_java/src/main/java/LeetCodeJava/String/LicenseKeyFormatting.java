package LeetCodeJava.String;

// https://leetcode.com/problems/license-key-formatting/description/

public class LicenseKeyFormatting {


    // V0
    // TODO : implement
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/license-key-formatting.py
//    public String licenseKeyFormatting(String s, int k) {
//
//    }

    // V1
    // https://leetcode.com/problems/license-key-formatting/solutions/316752/clean-and-self-explanatory-11-ms-java-solution/
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
