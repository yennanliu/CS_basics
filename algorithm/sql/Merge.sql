/*

MERGE -- the merge step of merge sort, as a recursive SQL function (PostgreSQL)

Scope: combines two SORTED arrays into one sorted array. Mergesort.sql
       calls it; load THIS file first, because mergesort() refers to
       merge() by name.

How it works
    SQL has no loops, so the "two pointers" of the usual merge become
    function ARGUMENTS, and each step is one recursive call:

        A1, A2   the two sorted input arrays
        i, j     the next unused position in A1 and in A2 (1-based:
                 PostgreSQL arrays start at index 1)
        acc      the merged result built so far (the "accumulator")

        merge('{2,5}', '{1,4}', 1, 1, '{}')
          -> 1 is smaller: merge('{2,5}', '{1,4}', 1, 2, '{1}')
          -> 2 is smaller: merge('{2,5}', '{1,4}', 2, 2, '{1,2}')
          -> 4 is smaller: merge('{2,5}', '{1,4}', 2, 3, '{1,2,4}')
          -> A2 is used up: merge('{2,5}', '{1,4}', 3, 3, '{1,2,4,5}')
          -> both used up: return '{1,2,4,5}'

The five cases of the CASE below, in order
    1) both arrays used up            -> the accumulator is the answer
    2) A1 used up                     -> take the next value of A2
    3) A2 used up                     -> take the next value of A1
    4) A1's value <= A2's value       -> take from A1
    5) otherwise                      -> take from A2

    Case 4 uses <= (not <) so that on a TIE the value from A1, the left
    array, goes first. That is what makes the sort STABLE.

Time  : O(N + M) recursive calls. array_append copies the accumulator
        each time, so the real cost is O((N + M)^2) -- this is a
        demonstration of the algorithm, not a way to sort in SQL.
        (Real SQL sorts with ORDER BY.)
Space : O(N + M) recursion depth; PostgreSQL's max_stack_depth limits
        how long the arrays can be.

Not handled: NULL elements (a comparison with NULL is NULL, so no case
matches) and empty input arrays (array_length of an empty array is
NULL, not 0). mergesort() never passes either.

Reference:
    https://stackoverflow.com/questions/55223960/textbook-mergesort-implementation-in-sql-postgres

*/

CREATE OR REPLACE FUNCTION merge(
    A1  double precision[],
    A2  double precision[],
    i   integer,
    j   integer,
    acc double precision[]
)
RETURNS double precision[] AS $$
    SELECT
        CASE
            -- 1) both arrays are used up: done
            WHEN i > array_length(A1, 1) AND j > array_length(A2, 1)
                THEN acc
            -- 2) A1 is used up: the rest comes from A2
            WHEN i > array_length(A1, 1)
                THEN merge(A1, A2, i, j + 1, array_append(acc, A2[j]))
            -- 3) A2 is used up: the rest comes from A1
            WHEN j > array_length(A2, 1)
                THEN merge(A1, A2, i + 1, j, array_append(acc, A1[i]))
            -- 4) the left value is smaller, or they tie: take the LEFT one (stable)
            WHEN A1[i] <= A2[j]
                THEN merge(A1, A2, i + 1, j, array_append(acc, A1[i]))
            -- 5) the right value is strictly smaller
            WHEN A1[i] > A2[j]
                THEN merge(A1, A2, i, j + 1, array_append(acc, A2[j]))
        END;
$$ LANGUAGE SQL;

-- Usage:
--   SELECT merge(ARRAY[2, 5]::double precision[],
--                ARRAY[1, 4]::double precision[],
--                1, 1, ARRAY[]::double precision[]);
--   -> {1,2,4,5}
