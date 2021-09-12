/*

Merge

https://stackoverflow.com/questions/55223960/textbook-mergesort-implementation-in-sql-postgres

*/

CREATE OR REPLACE FUNCTION merge(A1 double precision[],A2 double precision[], i integer, j integer,acc double precision[]) 
RETURNS double precision[] AS $$
    SELECT 
        CASE WHEN (i > array_length(A1,1) and j > array_length(A2,1)) THEN acc
             WHEN i > array_length(A1,1) THEN merge(A1,A2,i,j+1,array_append(acc,A2[j]))
             WHEN j > array_length(A2,1) THEN merge(A1,A2,i+1,j,array_append(acc,A1[i]))
             WHEN A1[i] <  A2[j] THEN merge(A1,A2,i+1,j,array_append(acc, A1[i]))
             WHEN A1[i] >= A2[j] THEN merge(A1,A2,i,j+1,array_append(acc, A2[j]))                
        END;
$$ LANGUAGE SQL;