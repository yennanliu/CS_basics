/*

MERGE SORT -- as a recursive SQL function (PostgreSQL)

Scope: the top-down recursion. The merge step lives in Merge.sql, which
       must be loaded FIRST. See algorithm/python/merge_sort.py for the
       algorithm explained with a picture and a trace.

Divide and conquer
    1) split the array in half
    2) sort each half (by calling mergesort again)
    3) merge the two sorted halves

    mergesort('{5,2,4,1}')
      = merge( mergesort('{5,2}'), mergesort('{4,1}') )
      = merge( '{2,5}',            '{1,4}' )
      = '{1,2,4,5}'

Where the split falls
    PostgreSQL arrays are 1-based and slices include both ends, so for
    an array of length n:

        mid          = floor((1 + n) / 2)
        left half    = A[1 : mid]
        right half   = A[mid + 1 : n]

        n = 4 -> mid = 2 -> A[1:2] and A[3:4]
        n = 5 -> mid = 3 -> A[1:3] and A[4:5]      (the left half gets the extra one)

    Both halves are non-empty whenever n >= 2, which is what guarantees
    the recursion gets shorter every time.

The base case
    An array of length 0 or 1 is already sorted and is returned as is
    (the ELSE branch). array_length() of an empty array is NULL, and
    `1 < NULL` is not true, so the empty array lands there too.

Time  : O(N log N) merge steps (see the note in Merge.sql about the
        cost of array_append). In real SQL, sort with ORDER BY.
Space : O(N), plus O(log N) recursion depth

Reference:
    https://stackoverflow.com/questions/55223960/textbook-mergesort-implementation-in-sql-postgres

*/

CREATE OR REPLACE FUNCTION mergesort(A double precision[])
RETURNS double precision[] AS $$
    SELECT
        CASE
            -- two or more elements: sort each half, then merge them
            WHEN 1 < array_length(A, 1)
                THEN merge(
                    -- the left half, sorted:  A[1 : mid]
                    mergesort(A[1 : floor((1 + array_length(A, 1)) / 2)::integer]),
                    -- the right half, sorted: A[mid + 1 : n]
                    mergesort(A[floor((1 + array_length(A, 1)) / 2)::integer + 1 : array_length(A, 1)]),
                    1,                              -- i: start of the left half
                    1,                              -- j: start of the right half
                    ARRAY[]::double precision[]     -- acc: nothing merged yet
                )
            -- zero or one element: already sorted
            ELSE A
        END;
$$ LANGUAGE SQL;

-- Usage (after loading Merge.sql, then this file):
--   SELECT mergesort(ARRAY[5, 2, 4, 1]::double precision[]);
--   -> {1,2,4,5}
