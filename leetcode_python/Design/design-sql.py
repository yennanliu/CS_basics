"""

2408. Design SQL
Medium
(premium / locked problem)

You are given two string arrays, names and columns, both of size n. The ith table is represented by the name names[i] and contains columns[i] number of columns.

You need to implement a class that supports the following operations:

Insert a row in a specific table with an id assigned using an auto-increment method, where the id of the first inserted row is 1, and the id of each new row inserted into the same table is one greater than the id of the last inserted row, even if the last row was removed.
Remove a row from a specific table. Removing a row does not affect the id of the next inserted row.
Select a specific cell from any table and return its value.

Implement the SQL class:

SQL(String[] names, int[] columns) Creates the n tables.
void insertRow(String name, String[] row) Adds a row to the table name. It is guaranteed that the table will exist, and the array row has the required number of elements.
void deleteRow(String name, int rowId) Removes the row rowId from the table name. It is guaranteed that the table and row will exist.
String selectCell(String name, int rowId, int columnId) Returns the value of the cell of the row rowId and the column columnId of the table name.


Example 1:

Input
["SQL", "insertRow", "selectCell", "insertRow", "deleteRow", "selectCell"]
[[["one", "two", "three"], [2, 3, 1]], ["two", ["first", "second", "third"]], ["two", 1, 3], ["two", ["fourth", "fifth", "sixth"]], ["two", 1], ["two", 2, 2]]
Output
[null, null, "third", null, null, "fifth"]

Explanation
SQL sql = new SQL(["one", "two", "three"], [2, 3, 1]); // creates three tables.
sql.insertRow("two", ["first", "second", "third"]);    // adds a row to the table "two". Its id is 1.
sql.selectCell("two", 1, 3);                           // return "third", finds the value of the third
                                                       // column in the row with id 1 of the table "two".
sql.insertRow("two", ["fourth", "fifth", "sixth"]);    // adds another row to the table "two". Its id is 2.
sql.deleteRow("two", 1);                               // deletes the first row of the table "two".
                                                       // Note that the second row will still have the id 2.
sql.selectCell("two", 2, 2);                           // return "fifth", finds the value of the second
                                                       // column in the row with id 2 of the table "two".


Constraints:

n == names.length == columns.length
1 <= n <= 10^4
1 <= names[i].length, columns[i] <= 20
names[i] consists of lowercase English letters.
1 <= row.length <= 20
1 <= row[i].length <= 100
row[i] consists of uppercase and lowercase English letters.
At most 250 calls will be made to insertRow and deleteRow.
At most 10^4 calls will be made to selectCell.

"""

# V0
# IDEA : PER TABLE, A DICT rowId -> ROW PLUS A NEVER-DECREASING ID COUNTER
#
#   the one non-obvious rule is that ids are NOT recycled : deleting a row
#   must not roll the counter back. so each table keeps its own `next_id`
#   that only ever increases, independent of the dict's contents.
#
#   rows and columns are both 1-INDEXED in the API, hence the -1 when
#   indexing into the stored list.
#
# time = O(1) per operation, space = O(total rows stored)
class SQL(object):

    def __init__(self, names, columns):
        # column counts are not needed for lookups, the rows are well-formed
        self.rows = {name: {} for name in names}
        self.next_id = {name: 1 for name in names}

    def insertRow(self, name, row):
        rid = self.next_id[name]
        self.next_id[name] += 1
        self.rows[name][rid] = row

    def deleteRow(self, name, rowId):
        self.rows[name].pop(rowId, None)

    def selectCell(self, name, rowId, columnId):
        return self.rows[name][rowId][columnId - 1]


# Your SQL object will be instantiated and called as such:
# obj = SQL(names, columns)
# obj.insertRow(name,row)
# obj.deleteRow(name,rowId)
# param_3 = obj.selectCell(name,rowId,columnId)
