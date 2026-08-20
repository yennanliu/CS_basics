package LeetCodeJava.Design;

// https://leetcode.com/problems/encrypt-and-decrypt-strings/

import java.util.HashMap;
import java.util.Map;

/**
 *  2227. Encrypt and Decrypt Strings
 *  Hard
 *
 *  You are given a character array keys containing unique characters and a string
 *  array values containing strings of length 2. You are also given another string
 *  array dictionary that contains all permitted original strings after decryption.
 *  You should implement a data structure that can encrypt or decrypt a 0-indexed
 *  string.
 *
 *  A string is encrypted with the following process:
 *    1. For each character c in the string, we find the index i satisfying
 *       keys[i] == c in keys.
 *    2. Replace c with values[i] in the string.
 *  Note that in case a character of the string is not present in keys, the
 *  encryption process cannot be carried out, and an empty string "" is returned.
 *
 *  A string is decrypted with the following process:
 *    1. For each substring s of length 2 occurring at an even index in the string,
 *       we find an i such that values[i] == s. If there are multiple valid i, we
 *       choose any one of them. This means a string could have multiple possible
 *       strings it can decrypt to.
 *    2. Replace s with keys[i] in the string.
 *
 *  Implement the Encrypter class:
 *    Encrypter(char[] keys, String[] values, String[] dictionary) Initializes the
 *      Encrypter class with keys, values, and dictionary.
 *    String encrypt(String word1) Encrypts word1 with the encryption process
 *      described above and returns the encrypted string.
 *    int decrypt(String word2) Returns the number of possible strings word2 could
 *      decrypt to that also appear in dictionary.
 *
 *  Example 1:
 *    Input
 *      ["Encrypter", "encrypt", "decrypt"]
 *      [[['a','b','c','d'], ["ei","zf","ei","am"],
 *        ["abcd","acbd","adbc","badc","dacb","cadb","cbda","abad"]],
 *       ["abcd"], ["eizfeiam"]]
 *    Output
 *      [null, "eizfeiam", 2]
 *    Explanation
 *      encrypt("abcd") -> "eizfeiam"
 *      decrypt("eizfeiam") -> 2   ("abad" and "abcd" are in dictionary)
 *
 *  Constraints:
 *    1 <= keys.length == values.length <= 26
 *    values[i].length == 2
 *    1 <= dictionary.length <= 100
 *    1 <= dictionary[i].length <= 100
 *    All keys[i] and dictionary[i] are unique.
 *    1 <= word1.length <= 2000
 *    2 <= word2.length <= 200
 *    All word1[i] appear in keys.
 *    word2.length is even.
 *    keys, values[i], dictionary[i], word1 and word2 only contain lowercase letters.
 *    At most 200 calls will be made to encrypt and decrypt in total.
 */
public class EncryptAndDecryptStrings {

    // V0
    // IDEA: HASH MAP (char -> value) FOR encrypt +
    //       PRE-ENCRYPT THE WHOLE DICTIONARY FOR decrypt
    //
    //       decrypt(word2) asks "how many dictionary words encrypt to word2 ?"
    //       -> encryption is deterministic, so encrypt every dictionary word ONCE
    //          up front and keep a counter of the produced ciphertexts.
    //          then decrypt is a single map lookup, and we never have to
    //          enumerate the (exponentially many) possible decryptions.
    /**
     * time = O(SUM(|dictionary[i]|)) for the constructor,
     *        O(|word1|) per encrypt, O(|word2|) per decrypt
     * space = O(SUM(|dictionary[i]|))
     */
    private final Map<Character, String> enc;
    private final Map<String, Integer> cipherCnt;

    public EncryptAndDecryptStrings(char[] keys, String[] values, String[] dictionary) {
        this.enc = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            // keys are unique, so there is no overwrite ambiguity here
            this.enc.put(keys[i], values[i]);
        }
        this.cipherCnt = new HashMap<>();
        for (String w : dictionary) {
            String c = encrypt(w);
            if (c.isEmpty()) {
                continue; // not encryptable -> can never be an answer
            }
            Integer old = this.cipherCnt.get(c);
            this.cipherCnt.put(c, old == null ? 1 : old + 1);
        }
    }

    public String encrypt(String word1) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word1.length(); i++) {
            String v = enc.get(word1.charAt(i));
            if (v == null) {
                return "";
            }
            sb.append(v);
        }
        return sb.toString();
    }

    public int decrypt(String word2) {
        Integer cnt = cipherCnt.get(word2);
        return cnt == null ? 0 : cnt;
    }
}
