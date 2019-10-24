# AWS REDSHIFT FAQ

- 1) Database Developer guide for AWS Redshift 

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c-who-should-use-this-guide.html

- 2) Difference between Redshift and PostgreSQL
	- Amazon Redshift is specifically designed for online analytic processing `(OLAP)`and business intelligence (BI) applications, which require complex queries against large datasets. Because it addresses very different requirements, the specialized data storage schema and query execution engine that Amazon Redshift uses are completely different from the PostgreSQL implementation. For example, where online transaction processing (OLTP) applications typically store data in rows

	- ***`Amazon Redshift` stores data in columns, using specialized data compression encodings for optimum memory usage and disk I/O***. 

	- ***Some `PostgreSQL` features that are suited to smaller-scale OLTP processing, such as secondary indexes and efficient single-row data manipulation operations, have been omitted to improve performance***.

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c_redshift-and-postgres-sql.html

- 3) Table design guide 
	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c_designing-tables-best-practices.html
	- https://github.com/awsdocs/amazon-redshift-developer-guide

- 4) Select sort keys 
	- When you create a table, you can specify one or more columns as the sort key. Amazon Redshift stores your data on disk in sorted order according to the sort key. How your data is sorted has an important effect on disk I/O, columnar compression, and query performance.

	- In this step, you choose sort keys for the SSB tables based on these best practices:

		- If recent data is queried most frequently, specify the timestamp column as the leading column for the sort key.

		- If you do frequent range filtering or equality filtering on one column, specify that column as the sort key.

		- If you frequently join a (dimension) table, specify the join column as the sort key.

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/tutorial-tuning-tables-sort-keys.html

- 5) Select Distribution Styles
	- When you load data into a table, Amazon Redshift distributes the rows of the table to each of the node slices according to the table's distribution style. The number of slices per node depends on the node size of the cluster. For example, the dc1.large cluster that you are using in this tutorial has four nodes with two slices each, so the cluster has a total of eight slices. The nodes all participate in parallel query execution, working on data that is distributed across the slices.

	- When you execute a query, the query optimizer redistributes the rows to the compute nodes as needed to perform any joins and aggregations. Redistribution might involve either sending specific rows to nodes for joining or broadcasting an entire table to all of the nodes.

	- You should assign distribution styles to achieve these goals.

		- Collocate the rows from joining tables

		- When the rows for joining columns are on the same slices, less data needs to be moved during query execution.

		- Distribute data evenly among the slices in a cluster. If data is distributed evenly, workload can be allocated evenly to all the slices.

	- Distribution Styles
		- KEY distribution
			- The rows are distributed according to the values in one column. The leader node will attempt to place matching values on the same node slice. If you distribute a pair of tables on the joining keys, the leader node collocates the rows on the slices according to the values in the joining columns so that matching values from the common columns are physically stored together.

		- ALL distribution
			- A copy of the entire table is distributed to every node. Where EVEN distribution or KEY distribution place only a portion of a table's rows on each node, ALL distribution ensures that every row is collocated for every join that the table participates in.

		- EVEN distribution
			- The rows are distributed across the slices in a round-robin fashion, regardless of the values in any particular column. EVEN distribution is appropriate when a table does not participate in joins or when there is not a clear choice between KEY distribution and ALL distribution. EVEN distribution is the default distribution style.

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/tutorial-tuning-tables-distribution.html

- 5) Review Compression Encodings
	- Compression is a column-level operation that reduces the size of data when it is stored. Compression conserves storage space and reduces the size of data that is read from storage, which reduces the amount of disk I/O and therefore improves query performance.

	- By default, Amazon Redshift stores data in its raw, uncompressed format. When you create tables in an Amazon Redshift database, you can define a compression type, or encoding, for the columns. For more information, see Compression Encodings.

	- You can apply compression encodings to columns in tables manually when you create the tables, or you can use the COPY command to analyze the load data and apply compression encodings automatically.

	- Type of compresison encodings
		- Raw
		- Bytedict
		- LZO
		- Runlength
		- Text255
		- Text32K
	- Example 	
		- https://docs.aws.amazon.com/en_us/redshift/latest/dg/Examples__compression_encodings_in_CREATE_TABLE_statements.html
