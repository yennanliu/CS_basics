package LeetCodeJava.String;

// https://leetcode.com/problems/unique-email-addresses/

import java.util.HashSet;
import java.util.Set;

/**
 *  929. Unique Email Addresses
 *  Easy
 *
 *  Every valid email consists of a local name and a domain name, separated by
 *  the '@' sign. Besides lowercase letters, the email may contain one or more
 *  '.' or '+'.
 *
 *  If you add periods '.' between some characters in the local name part of
 *  an email address, mail sent there will be forwarded to the same address
 *  without dots in the local name.
 *
 *  If you add a plus '+' in the local name, everything after the first plus
 *  sign will be ignored.
 *
 *  Rules above do not apply to the domain name.
 *
 *  Given an array of strings emails, return the number of different addresses
 *  that actually receive mails.
 *
 *  Example 1:
 *  Input: emails = ["test.email+alex@leetcode.com",
 *                   "test.e.mail+bob.cathy@leetcode.com",
 *                   "testemail+david@lee.tcode.com"]
 *  Output: 2
 *
 *  Example 2:
 *  Input: emails = ["a@leetcode.com","b@leetcode.com","c@leetcode.com"]
 *  Output: 3
 *
 *  Constraints:
 *   - 1 <= emails.length <= 100
 *   - 1 <= emails[i].length <= 100
 */
public class UniqueEmailAddresses {

    // V0
    // IDEA: NORMALIZE each email (strip dots + everything after '+' in the
    //       local part) and count the distinct results in a HashSet.
    /**
     * time = O(n * l)
     * space = O(n * l)
     */
    public int numUniqueEmails(String[] emails) {
        Set<String> seen = new HashSet<>();
        for (String email : emails) {
            int at = email.indexOf('@');
            String local = email.substring(0, at);
            String domain = email.substring(at); // includes '@'

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < local.length(); i++) {
                char c = local.charAt(i);
                if (c == '+') {
                    break;
                }
                if (c != '.') {
                    sb.append(c);
                }
            }
            seen.add(sb.append(domain).toString());
        }
        return seen.size();
    }
}
