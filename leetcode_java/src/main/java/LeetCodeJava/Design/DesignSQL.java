package LeetCodeJava.Design;

// https://leetcode.com/problems/design-sql/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  2408. Design SQL
 *  Medium
 *  (premium / locked problem)
 *
 *  You are given two string arrays, names and columns, both of size n. The ith table is
 *  represented by the name names[i] and contains columns[i] number of columns.
 *
 *  You need to implement a class that supports the following operations:
 *   - Insert a row in a specific table with an id assigned using an auto-increment
 *     method, where the id of the first inserted row is 1, and the id of each new row
 *     inserted into the same table is one greater than the id of the last inserted row,
 *     EVEN IF the last row was removed.
 *   - Remove a row from a specific table. Removing a row does not affect the id of the
 *     next inserted row.
 *   - Select a specific cell from any table and return its value.
 *
 *  Implement the SQL class:
 *
 *   - SQL(String[] names, int[] columns) Creates the n tables.
 *   - void insertRow(String name, String[] row) Adds a row to the table name. It is
 *     guaranteed that the table will exist, and the array row has the required number
 *     of elements.
 *   - void deleteRow(String name, int rowId) Removes the row rowId from the table name.
 *     It is guaranteed that the table and row will exist.
 *   - String selectCell(String name, int rowId, int columnId) Returns the value of the
 *     cell of the row rowId and the column columnId of the table name.
 *
 *  Example 1:
 *    Input
 *      ["SQL","insertRow","selectCell","insertRow","deleteRow","selectCell"]
 *      [[["one","two","three"],[2,3,1]],["two",["first","second","third"]],["two",1,3],
 *       ["two",["fourth","fifth","sixth"]],["two",1],["two",2,2]]
 *    Output
 *      [null,null,"third",null,null,"fifth"]
 *    Explanation
 *      the first insert into "two" gets id 1, the second gets id 2.
 *      deleting row 1 does NOT roll the counter back -- row 2 keeps the id 2, so
 *      selectCell("two", 2, 2) returns "fifth".
 *
 *  Constraints:
 *    n == names.length == columns.length
 *    1 <= n <= 10^4
 *    1 <= names[i].length, columns[i] <= 20
 *    names[i] consists of lowercase English letters.
 *    1 <= row.length <= 20
 *    1 <= row[i].length <= 100
 *    row[i] consists of uppercase and lowercase English letters.
 *    At most 250 calls will be made to insertRow and deleteRow.
 *    At most 10^4 calls will be made to selectCell.
 */
public class DesignSQL {

    // V0
    // IDEA: PER TABLE, A MAP rowId -> ROW PLUS A NEVER-DECREASING ID COUNTER
    //
    //   the one non-obvious rule is that ids are NOT RECYCLED: deleting a row must not
    //   roll the counter back. so each table keeps its OWN `nextId` that only ever
    //   increases, independent of what the map currently holds.
    //
    //   the column counts are not needed for lookups (rows are guaranteed well-formed),
    //   so `columns` is only used to know which tables exist.
    //
    //   rows and columns are both 1-INDEXED in the API, hence the -1 when indexing
    //   into the stored row.
    /**
     * time = O(1) per operation
     * space = O(total rows stored)
     */
    private final Map<String, Map<Integer, String[]>> rows = new HashMap<>();
    private final Map<String, Integer> nextId = new HashMap<>();

    public DesignSQL(List<String> names, List<Integer> columns) {
        for (String name : names) {
            rows.put(name, new HashMap<Integer, String[]>());
            nextId.put(name, 1);
        }
    }

    public void insertRow(String name, List<String> row) {
        int rid = nextId.get(name);
        nextId.put(name, rid + 1);
        rows.get(name).put(rid, row.toArray(new String[0]));
    }

    public void deleteRow(String name, int rowId) {
        rows.get(name).remove(rowId);
    }

    public String selectCell(String name, int rowId, int columnId) {
        return rows.get(name).get(rowId)[columnId - 1];
    }
}
