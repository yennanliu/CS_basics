/*

Mergesort

https://stackoverflow.com/questions/55223960/textbook-mergesort-implementation-in-sql-postgres

*/

CREATE OR REPLACE FUNCTION mergesort(A double precision[]) 
RETURNS double precision[] AS $$
    SELECT 
        CASE WHEN 1 < array_length(A,1) 
              THEN merge(mergesort(A[1:floor((1+array_length(A,1))/2)::integer]),
                         mergesort(A[floor((1+array_length(A,1))/2)::integer+1:array_length(A,1)]),
                         1,
                         1,
                         ARRAY[]::double precision[])
             ELSE A
        END;
$$ LANGUAGE SQL;