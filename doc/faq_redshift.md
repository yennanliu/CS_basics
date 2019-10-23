# AWS REDSHIFT FAQ

- Database Developer guide for AWS Redshift 

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c-who-should-use-this-guide.html

- Difference between Redshift and PostgreSQL
	- Amazon Redshift is specifically designed for online analytic processing `(OLAP)`and business intelligence (BI) applications, which require complex queries against large datasets. Because it addresses very different requirements, the specialized data storage schema and query execution engine that Amazon Redshift uses are completely different from the PostgreSQL implementation. For example, where online transaction processing (OLTP) applications typically store data in rows, ***`Amazon Redshift` stores data in columns, using specialized data compression encodings for optimum memory usage and disk I/O***. ***Some `PostgreSQL` features that are suited to smaller-scale OLTP processing, such as secondary indexes and efficient single-row data manipulation operations, have been omitted to improve performance***.

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c_redshift-and-postgres-sql.html

- Table design guide 
	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/c_designing-tables-best-practices.html
	- https://github.com/awsdocs/amazon-redshift-developer-guide

- Select sort keys 
	- When you create a table, you can specify one or more columns as the sort key. Amazon Redshift stores your data on disk in sorted order according to the sort key. How your data is sorted has an important effect on disk I/O, columnar compression, and query performance.

	- In this step, you choose sort keys for the SSB tables based on these best practices:

		- If recent data is queried most frequently, specify the timestamp column as the leading column for the sort key.

		- If you do frequent range filtering or equality filtering on one column, specify that column as the sort key.

		- If you frequently join a (dimension) table, specify the join column as the sort key.

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/tutorial-tuning-tables-sort-keys.html

- Select Distribution Styles
	- When you load data into a table, Amazon Redshift distributes the rows of the table to each of the node slices according to the table's distribution style. The number of slices per node depends on the node size of the cluster. For example, the dc1.large cluster that you are using in this tutorial has four nodes with two slices each, so the cluster has a total of eight slices. The nodes all participate in parallel query execution, working on data that is distributed across the slices.

	- When you execute a query, the query optimizer redistributes the rows to the compute nodes as needed to perform any joins and aggregations. Redistribution might involve either sending specific rows to nodes for joining or broadcasting an entire table to all of the nodes.

	- You should assign distribution styles to achieve these goals.

		- Collocate the rows from joining tables

		- When the rows for joining columns are on the same slices, less data needs to be moved during query execution.

		- Distribute data evenly among the slices in a cluster. If data is distributed evenly, workload can be allocated evenly to all the slices.

	- Distribution Styles
		- KEY distribution
		- ALL distribution
		- EVEN distribution

	- https://docs.aws.amazon.com/en_us/redshift/latest/dg/tutorial-tuning-tables-distribution.html


