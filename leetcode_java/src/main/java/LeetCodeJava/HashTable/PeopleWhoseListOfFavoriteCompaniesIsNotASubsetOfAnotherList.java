package LeetCodeJava.HashTable;

// https://leetcode.com/problems/people-whose-list-of-favorite-companies-is-not-a-subset-of-another-list/description/

import java.util.*;

/**
 * 1452. People Whose List of Favorite Companies Is Not a Subset of Another List
 * Medium
 *
 * Given the array favoriteCompanies where favoriteCompanies[i] is the list of
 * favorites companies for the ith person (indexed from 0).
 *
 * Return the indices of people whose list of favorite companies is not a subset of
 * any other list of favorites companies. You must return the indices in increasing
 * order.
 *
 *
 * Example 1:
 *
 * Input: favoriteCompanies = [["leetcode","google","facebook"],["google","microsoft"],["google","facebook"],["google"],["amazon"]]
 * Output: [0,1,4]
 * Explanation:
 * Person with index=2 has favoriteCompanies[2]=["google","facebook"] which is a
 * subset of favoriteCompanies[0]=["leetcode","google","facebook"] corresponding to
 * the person with index 0.
 * Person with index=3 has favoriteCompanies[3]=["google"] which is a subset of
 * favoriteCompanies[0]=["leetcode","google","facebook"] and
 * favoriteCompanies[1]=["google","microsoft"].
 * Other lists of favorite companies are not a subset of another list, therefore,
 * the answer is [0,1,4].
 *
 * Example 2:
 *
 * Input: favoriteCompanies = [["leetcode","google","facebook"],["leetcode","amazon"],["facebook","google"]]
 * Output: [0,1]
 *
 * Example 3:
 *
 * Input: favoriteCompanies = [["leetcode"],["google"],["facebook"],["amazon"]]
 * Output: [0,1,2,3]
 *
 *
 * Constraints:
 *
 * 1 <= favoriteCompanies.length <= 100
 * 1 <= favoriteCompanies[i].length <= 500
 * 1 <= favoriteCompanies[i][j].length <= 20
 * All strings in favoriteCompanies[i] are distinct.
 * All lists of favorite companies are distinct, that is, If we sort alphabetically
 * each list then favoriteCompanies[i] != favoriteCompanies[j].
 * All strings consist of lowercase English letters only.
 *
 */
public class PeopleWhoseListOfFavoriteCompaniesIsNotASubsetOfAnotherList {

    // V0
    // IDEA: HASH TABLE (intern each company name to an int id) + PAIRWISE SUBSET CHECK
    /**
     * time = O(N^2 * M)
     * space = O(N * M)
     */
    public List<Integer> peopleIndexes(List<List<String>> favoriteCompanies) {
        int people = favoriteCompanies.size();

        // comparing small ints is cheaper than comparing 20-char strings
        Map<String, Integer> companyId = new HashMap<>();
        List<Set<Integer>> favorites = new ArrayList<>();
        for (List<String> companies : favoriteCompanies) {
            Set<Integer> ids = new HashSet<>();
            for (String company : companies) {
                if (!companyId.containsKey(company)) {
                    companyId.put(company, companyId.size());
                }
                ids.add(companyId.get(company));
            }
            favorites.add(ids);
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < people; i++) {
            boolean isSubset = false;
            for (int j = 0; j < people; j++) {
                // a smaller-or-equal set is the only kind that can be contained in another
                if (i != j
                        && favorites.get(i).size() <= favorites.get(j).size()
                        && favorites.get(j).containsAll(favorites.get(i))) {
                    isSubset = true;
                    break;
                }
            }
            if (!isSubset) {
                result.add(i);
            }
        }
        return result;
    }
}
