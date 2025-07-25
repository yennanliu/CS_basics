package LeetCodeJava.String;

// https://leetcode.com/problems/encode-and-decode-strings/
// https://leetcode.ca/all/271.html

/**
 * 271. Encode and Decode Strings
 * Design an algorithm to encode a list of strings to a string. The encoded string is then sent over the network and is decoded back to the original list of strings.
 *
 * Machine 1 (sender) has the function:
 *
 * string encode(vector<string> strs) {
 *   // ... your code
 *   return encoded_string;
 * }
 * Machine 2 (receiver) has the function:
 * vector<string> decode(string s) {
 *   //... your code
 *   return strs;
 * }
 * So Machine 1 does:
 *
 * string encoded_string = encode(strs);
 * and Machine 2 does:
 *
 * vector<string> strs2 = decode(encoded_string);
 * strs2 in Machine 2 should be the same as strs in Machine 1.
 *
 * Implement the encode and decode methods.
 *
 *
 *
 * Note:
 *
 * The string may contain any possible characters out of 256 valid ascii characters. Your algorithm should be generalized enough to work on any possible characters.
 * Do not use class member/global/static variables to store states. Your encode and decode algorithms should be stateless.
 * Do not rely on any library method such as eval or serialize methods. You should implement your own encode/decode algorithm.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Bloomberg Google Microsoft Square Twitter
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Your Codec object will be instantiated and called as such:
// Codec codec = new Codec();
// codec.decode(codec.encode(strs));

public class EncodeAndDecodeStrings {

    // V0
    // TODO : implement it
    // Encodes a list of strings to a single string.
//    public String encode(List<String> strs) {
//
//    }
//    public List<String> decode(String s) {
//
//    }

    // V0-0-1
    // IDEA: STRING OP
    // TODO: validate below:
    public String encode_0_0_1(List<String> strs) {
        // edge
        if(strs == null || strs.isEmpty()){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(String x: strs){
            sb.append(x);
            sb.append("#");  // NOTE !!! handle cases if strs has special character
        }

        return sb.toString();
    }

    public List<String> decode_0_0_1(String s) {
        // edge
        if(s.isEmpty()){
            return null;
        }

        List<String> res = new ArrayList<>();

        //String[] str_arr = s.split("#");
        String[] str_arr = s.split("#", -1); // NOTE: We remove the last element because it's an empty string resulting from the final delimiter
        for(String x: str_arr){
            if(!x.isEmpty()){
                res.add(x);
            }
        }

        return res;
    }

    // V0-1
    // IDEA: STRING OP (fixed by gpt)
    // Encodes a list of strings to a single string.
    public String encode_0_1(List<String> strs) {
        if (strs == null || strs.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str.length()).append(':').append(str);
        }
        return sb.toString();
    }

    // Decodes a single string to a list of strings.
    public List<String> decode_0_1(String s) {
        List<String> res = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return res;
        }

        int i = 0;
        while (i < s.length()) {
            int colon = s.indexOf(':', i);
            int len = Integer.parseInt(s.substring(i, colon));
            i = colon + 1;
            res.add(s.substring(i, i + len));
            i += len;
        }

        return res;
    }

    // V0-2
    // IDEA : STRING, ARRAY OP
    public String encode_0_2(List<String> strs) {
        StringBuilder encodedString = new StringBuilder();
        // Iterate through the list of strings
        for (String s : strs) {
            // Append each string to the StringBuilder followed by the delimiter
            encodedString.append(s);
            encodedString.append("π");
        }
        // Return the entire encoded string
        return encodedString.toString();
    }

    // Decodes a single string to a list of strings.
    public List<String> decode_0_2(String s) {
        // Split the encoded string at each occurrence of the delimiter
        // Note: We use -1 as the limit parameter to ensure trailing empty strings are included
        /**
         *
         *  In the given code, split("π", -1) is splitting a
         *  string s using the delimiter "π". The second argument, -1,
         *  is used to control the behavior of the split operation.
         *
         *  When you use -1 as the second argument in the split method,
         *  it indicates that you want to include all trailing empty strings
         *  in the resulting array. -> `This means that if there are consecutive
         *  delimiters at the end of the input string, the split method
         *  will include empty strings for each of those delimiters.`
         *
         *
         *  Example :
         *
         *   String s = "appleπbananaπ";
         *   String[] decodedStrings = s.split("π", -1);
         *   System.out.println(Arrays.toString(decodedStrings));
         *
         *   # result:
         *
         *   [apple, banana, ]
         *
         *
         */
        String[] decodedStrings = s.split("π", -1);
        // Convert the array to a list and return it
        // Note: We remove the last element because it's an empty string resulting from the final delimiter
        return new ArrayList<>(Arrays.asList(decodedStrings).subList(0, decodedStrings.length - 1));
    }

    // V1
    // IDEA :  Non-ASCII delimiter
    // https://leetcode.com/problems/encode-and-decode-strings/editorial/
    // Encodes a list of strings to a single string.
    public String encode_1(List<String> strs) {
        StringBuilder encodedString = new StringBuilder();
        // Iterate through the list of strings
        for (String s : strs) {
            // Append each string to the StringBuilder followed by the delimiter
            encodedString.append(s);
            encodedString.append("π");
        }
        // Return the entire encoded string
        return encodedString.toString();
    }

    // Decodes a single string to a list of strings.
    public List<String> decode_1(String s) {
        // Split the encoded string at each occurrence of the delimiter
        // Note: We use -1 as the limit parameter to ensure trailing empty strings are included
        /**
         *
         *  In the given code, split("π", -1) is splitting a
         *  string s using the delimiter "π". The second argument, -1,
         *  is used to control the behavior of the split operation.
         *
         *  When you use -1 as the second argument in the split method,
         *  it indicates that you want to include all trailing empty strings
         *  in the resulting array. -> `This means that if there are consecutive
         *  delimiters at the end of the input string, the split method
         *  will include empty strings for each of those delimiters.`
         *
         *
         *  Example :
         *
         *   String s = "appleπbananaπ";
         *   String[] decodedStrings = s.split("π", -1);
         *   System.out.println(Arrays.toString(decodedStrings));
         *
         *   # result:
         *
         *   [apple, banana, ]
         *
         *
         */
        String[] decodedStrings = s.split("π", -1);
        // Convert the array to a list and return it
        // Note: We remove the last element because it's an empty string resulting from the final delimiter
        return new ArrayList<>(Arrays.asList(decodedStrings).subList(0, decodedStrings.length - 1));
    }

    // V2
    // IDEA : Escaping
    // https://leetcode.com/problems/encode-and-decode-strings/editorial/
    // Encodes a list of strings to a single string.
    public String encode_2(List<String> strs) {
        // Initialize a StringBuilder to hold the encoded strings
        StringBuilder encodedString = new StringBuilder();

        // Iterate over each string in the input list
        for (String s : strs) {
            // Replace each occurrence of '/' with '//'
            // This is our way of "escaping" the slash character
            // Then add our delimiter '/:' to the end
            encodedString.append(s.replace("/", "//")).append("/:");
        }

        // Return the final encoded string
        return encodedString.toString();
    }

    // Decodes a single string to a list of strings.
    public List<String> decode_2(String s) {
        // Initialize a List to hold the decoded strings
        List<String> decodedStrings = new ArrayList<>();

        // Initialize a StringBuilder to hold the current string being built
        StringBuilder currentString = new StringBuilder();

        // Initialize an index 'i' to start of the string
        int i = 0;

        // Iterate while 'i' is less than the length of the encoded string
        while (i < s.length()) {
            // If we encounter the delimiter '/:'
            if (i + 1 < s.length() && s.charAt(i) == '/' && s.charAt(i + 1) == ':') {
                // Add the currentString to the list of decodedStrings
                decodedStrings.add(currentString.toString());

                // Clear currentString for the next string
                currentString = new StringBuilder();

                // Move the index 2 steps forward to skip the delimiter
                i += 2;
            }
            // If we encounter an escaped slash '//'
            else if (i + 1 < s.length() && s.charAt(i) == '/' && s.charAt(i + 1) == '/') {
                // Add a single slash to the currentString
                currentString.append('/');

                // Move the index 2 steps forward to skip the escaped slash
                i += 2;
            }
            // Otherwise, just add the character to currentString and move the index 1 step forward.
            else {
                currentString.append(s.charAt(i));
                i++;
            }
        }

        // Return the list of decoded strings
        return decodedStrings;
    }

    // V3
    // IDEA : Chunked Transfer Encoding
    // https://leetcode.com/problems/encode-and-decode-strings/editorial/
    public String encode_3(List<String> strs) {
        // Initialize a StringBuilder to hold the encoded string.
        StringBuilder encodedString = new StringBuilder();
        for (String s : strs) {
            // Append the length, the delimiter, and the string itself.
            encodedString.append(s.length()).append("/:").append(s);
        }
        return encodedString.toString();
    }

    public List<String> decode_3(String s) {
        // Initialize a list to hold the decoded strings.
        List<String> decodedStrings = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            // Find the delimiter.
            int delim = s.indexOf("/:", i);
            // Get the length, which is before the delimiter.
            int length = Integer.parseInt(s.substring(i, delim));
            // Get the string, which is of 'length' length after the delimiter.
            String str = s.substring(delim + 2, delim + 2 + length);
            // Add the string to the list.
            decodedStrings.add(str);
            // Move the index to the start of the next length.
            i = delim + 2 + length;
        }
        return decodedStrings;
    }

    // V4
    // https://leetcode.ca/2016-08-27-271-Encode-and-Decode-Strings/
    public class Codec {

        // Encodes a list of strings to a single string.
        public String encode(List<String> strs) {
            StringBuilder ans = new StringBuilder();
            for (String s : strs) {
                ans.append((char) s.length()).append(s);
            }
            return ans.toString();
        }

        // Decodes a single string to a list of strings.
        public List<String> decode(String s) {
            List<String> ans = new ArrayList<>();
            int i = 0, n = s.length();
            while (i < n) {
                int size = s.charAt(i++);
                ans.add(s.substring(i, i + size));
                i += size;
            }
            return ans;
        }
    }

}
