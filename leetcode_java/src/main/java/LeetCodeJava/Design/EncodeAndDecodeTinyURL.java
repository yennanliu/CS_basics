package LeetCodeJava.Design;

// https://leetcode.com/problems/encode-and-decode-tinyurl/description/

import java.util.HashMap;
import java.util.Map;

/**
 *535. Encode and Decode TinyURL
 * Solved
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * Note: This is a companion problem to the System Design problem: Design TinyURL.
 * TinyURL is a URL shortening service where you enter a URL such as https://leetcode.com/problems/design-tinyurl and it returns a short URL such as http://tinyurl.com/4e9iAk. Design a class to encode a URL and decode a tiny URL.
 *
 * There is no restriction on how your encode/decode algorithm should work. You just need to ensure that a URL can be encoded to a tiny URL and the tiny URL can be decoded to the original URL.
 *
 * Implement the Solution class:
 *
 * Solution() Initializes the object of the system.
 * String encode(String longUrl) Returns a tiny URL for the given longUrl.
 * String decode(String shortUrl) Returns the original long URL for the given shortUrl. It is guaranteed that the given shortUrl was encoded by the same object.
 *
 *
 * Example 1:
 *
 * Input: url = "https://leetcode.com/problems/design-tinyurl"
 * Output: "https://leetcode.com/problems/design-tinyurl"
 *
 * Explanation:
 * Solution obj = new Solution();
 * string tiny = obj.encode(url); // returns the encoded tiny url.
 * string ans = obj.decode(tiny); // returns the original url after decoding it.
 *
 *
 * Constraints:
 *
 * 1 <= url.length <= 104
 * url is guranteed to be a valid URL.
 *
 */
public class EncodeAndDecodeTinyURL {

    // Your Codec object will be instantiated and called as such:
    // Codec codec = new Codec();
    // codec.decode(codec.encode(url));

    // V0
//    public class Codec {
//
//        // Encodes a URL to a shortened URL.
//        public String encode(String longUrl) {
//
//        }
//
//        // Decodes a shortened URL to its original URL.
//        public String decode(String shortUrl) {
//
//        }
//    }

    // V1-1
    // https://leetcode.ca/2017-05-18-535-Encode-and-Decode-TinyURL/
    class Codec_1_1{
        private static final String BASE_HOST = "http://tinyurl.com/";
        private static final String SEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // maintain 2 mapping relationships, could be in memory or database or disk file
        private Map<String, String> keyToUrl = new HashMap<>();
        private Map<String, String> urlToKey = new HashMap<>();

        // Encodes a URL to a shortened URL.
        public String encode(String longUrl) {
            if (urlToKey.containsKey(longUrl)) { // could also be collision and hash to different tinyUrl
                return BASE_HOST + urlToKey.get(longUrl);
            }

            String key = null;
            do {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 6; i++) {
                    int r = (int)(Math.random() * SEED.length());
                    sb.append(SEED.charAt(r));
                }
                key = sb.toString();
            } while (keyToUrl.containsKey(key));

            keyToUrl.put(key, longUrl);
            urlToKey.put(longUrl, key);
            return BASE_HOST + key;
        }

        // Decodes a shortened URL to its original URL.
        public String decode(String shortUrl) {
            return keyToUrl.get(shortUrl.replace(BASE_HOST, ""));
        }
    }


    // V1-2
    // https://leetcode.ca/2017-05-18-535-Encode-and-Decode-TinyURL/
    public class Codec_1_2 {
        private Map<String, String> m = new HashMap<>();
        private int idx = 0;
        private String domain = "https://tinyurl.com/";

        // Encodes a URL to a shortened URL.
        public String encode(String longUrl) {
            String v = String.valueOf(++idx);
            m.put(v, longUrl);
            return domain + v;
        }

        // Decodes a shortened URL to its original URL.
        public String decode(String shortUrl) {
            int i = shortUrl.lastIndexOf('/') + 1;
            return m.get(shortUrl.substring(i));
        }
    }

    // V2
    // https://leetcode.com/problems/encode-and-decode-tinyurl/solutions/1110551/js-python-java-c-easy-map-solution-w-exp-wieg/
    public class Codec_2 {
        Map<String, String> codeDB = new HashMap<>(), urlDB = new HashMap<>();
        static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        private String getCode() {
            char[] code = new char[6];
            for (int i = 0; i < 6; i++)
                code[i] = chars.charAt((int) (Math.random() * 62));
            return "http://tinyurl.com/" + String.valueOf(code);
        }

        public String encode(String longUrl) {
            if (urlDB.containsKey(longUrl))
                return urlDB.get(longUrl);
            String code = getCode();
            while (codeDB.containsKey(code))
                code = getCode();
            codeDB.put(code, longUrl);
            urlDB.put(longUrl, code);
            return code;
        }

        public String decode(String shortUrl) {
            return codeDB.get(shortUrl);
        }
    }

    // V3
    // https://leetcode.com/problems/encode-and-decode-tinyurl/solutions/7084995/solution-using-java-by-aadithya_s_j-cbvj/
    public class Codec_3 {

        Map<String, String> Db = new HashMap<String, String>();
        static final String codes = "0123456789ZXCVBNMLKJHGFDSAQWERTYUIOPqwertyuioplkjhgfdsazxcvbnm";

        private String makeCode() {
            char[] code = new char[6];
            for (int i = 0; i < 6; i++)
                code[i] = codes.charAt((int) (Math.random() * 62));
            return "http://tinyurl.com/" + String.valueOf(code);
        }

        // Encodes a URL to a shortened URL.
        public String encode(String longUrl) {
            for (Map.Entry<String, String> map : Db.entrySet()) {
                if (map.getValue().equals(longUrl))
                    return map.getKey();
            }
            String code = makeCode();
            while (Db.containsKey(code))
                code = makeCode();
            Db.put(code, longUrl);
            return code;
        }

        // Decodes a shortened URL to its original URL.
        public String decode(String shortUrl) {
            return Db.get(shortUrl);
        }
    }


}
