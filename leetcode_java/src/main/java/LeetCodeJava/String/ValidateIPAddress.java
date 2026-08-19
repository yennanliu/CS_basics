package LeetCodeJava.String;

// https://leetcode.com/problems/validate-ip-address/

/**
 *  468. Validate IP Address
 *  Medium
 *
 *  Given a string queryIP, return "IPv4" if IP is a valid IPv4 address,
 *  "IPv6" if IP is a valid IPv6 address, or "Neither" if IP is not a correct
 *  IP of any type.
 *
 *  A valid IPv4 address is "x1.x2.x3.x4" where 0 <= xi <= 255 and xi cannot
 *  contain leading zeros.
 *
 *  A valid IPv6 address is "x1:x2:x3:x4:x5:x6:x7:x8" where
 *    - 1 <= xi.length <= 4
 *    - xi is a hexadecimal string (digits, 'a'-'f', 'A'-'F')
 *    - leading zeros are allowed.
 *
 *  Example 1:
 *    Input: queryIP = "172.16.254.1"                        Output: "IPv4"
 *  Example 2:
 *    Input: queryIP = "2001:0db8:85a3:0:0:8A2E:0370:7334"   Output: "IPv6"
 *  Example 3:
 *    Input: queryIP = "256.256.256.256"                     Output: "Neither"
 *
 *  Constraints:
 *    queryIP consists only of English letters, digits and the characters
 *    '.' and ':'.
 */
public class ValidateIPAddress {

    // V0
    // IDEA: split on '.' / ':' and validate every chunk by hand (no regex)
    /**
     * time = O(n)
     * space = O(n)
     */
    public String validIPAddress(String queryIP) {
        if (queryIP == null) {
            return "Neither";
        }

        if (queryIP.indexOf('.') >= 0 && queryIP.indexOf(':') < 0) {
            return isIPv4(queryIP) ? "IPv4" : "Neither";
        }
        if (queryIP.indexOf(':') >= 0 && queryIP.indexOf('.') < 0) {
            return isIPv6(queryIP) ? "IPv6" : "Neither";
        }
        return "Neither";
    }

    private boolean isIPv4(String ip) {
        // -1 limit keeps trailing empty chunks, so "1.2.3." is rejected
        String[] parts = ip.split("\\.", -1);
        if (parts.length != 4) {
            return false;
        }
        for (String p : parts) {
            if (p.length() == 0 || p.length() > 3) {
                return false;
            }
            for (int i = 0; i < p.length(); i++) {
                if (!Character.isDigit(p.charAt(i))) {
                    return false;
                }
            }
            // no leading zeros
            if (p.length() > 1 && p.charAt(0) == '0') {
                return false;
            }
            if (Integer.parseInt(p) > 255) {
                return false;
            }
        }
        return true;
    }

    private boolean isIPv6(String ip) {
        String[] parts = ip.split(":", -1);
        if (parts.length != 8) {
            return false;
        }
        for (String p : parts) {
            if (p.length() == 0 || p.length() > 4) {
                return false;
            }
            for (int i = 0; i < p.length(); i++) {
                char c = p.charAt(i);
                boolean hex = (c >= '0' && c <= '9')
                        || (c >= 'a' && c <= 'f')
                        || (c >= 'A' && c <= 'F');
                if (!hex) {
                    return false;
                }
            }
        }
        return true;
    }
}
