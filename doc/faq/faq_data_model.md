# FAQ : Data Modeling


### 1) What are the different design schemas in Data Modelling? Explain with the example?
- 2 kinds of common design
    - Star Schema
        - The simplest of the schemas is star schema where we have a fact table in the center that references multiple dimension tables around it. All the dimension tables are connected to the fact table. The primary key in all dimension tables acts as a foreign key in the fact table.
        - The star schema is quite simple, flexible and it is in `denormalized` form.

    - Snowflake Schema
        - In a snowflake schema, the level of `normalization` increases. The fact table here remains the same as in the star schema. However, the dimension tables are normalized.  Due to several layers of dimension tables, it looks like a snowflake, and thus it is named as snowflake schema.

### 2)  Which schema is better – star or snowflake?
- depends on project and scenarios
- Star Schema
    - `denormalized` -> redundant data -> a bit hard to maintain
    - query is more simple (less join), run faster (compared to snowflake)
    - purpose : do more of a metrics analysis. e.g. : “what is the claim amount paid to a particular subscriber?”
    - more memory consumption
- Snowflake Schema
    - `normalized` -> no redundant data -> easy to maintain
    - query is a bit complext (join), run slower (compared to star)
    - do more of dimension analysis. e.g. :  “how many subscribers are tied to a particular plan which is currently active?” 
    - less memory consumption

### 3) What is a fact, dimension table ?
- Fact
    - Facts represent quantitative data.
    - the net amount due is a fact. A fact table contains numerical data and foreign keys from related dimensional tables
    - Generally, fact table is in normalized form
- Dimension
    - Dimensions represent qualitative data. For Example, plan, product, class are all dimensions.
    - A dimension table contains descriptive or textual attributes
    - Generally, dimension table is in de-normalized form

### 4)  What are the different types of dimensions you have come across? Explain each of them in detail with an example?
- 5 types of dimensions
    - Conformed dimensions
    - Junk dimensions
    - Role-playing dimensions
    - `Slowing changing dimension (SCD)`
    - Degenerated dimensions

### 5) Explain `Slowing changing dimension (SCD)` ?
- SCD : Slowly Changing Dimensions
- Type
    - Type 0 :
        - do nothing, (will always remain the same irrespective of the time.)
    - Type 1 : 
        - The new record replaces the original record. No trace of the old record exists.
        - These are the dimensions where the previous value of the attribute is replaced by the current value. No history is maintained in the Type-1 dimension
    - Type 2 : 
        - A new record is added into the customer dimension table. Therefore, the customer is treated essentially as two people.
        - These are the dimensions where unlimited history is preserved
    - Type 3 : 
        - The original record is modified to reflect the change.
        - These are the type of dimensions where limited history is preserved. And we use an additional column to maintain the history. 
    - Type 4
        - In this type of dimension, the historical data is preserved in a separate table. The main dimension table holds only the current data
- ref
    - https://www.1keydata.com/datawarehousing/slowly-changing-dimensions.html
    - https://www.gushiciku.cn/pl/ggNK/zh-tw
    - https://help.aliyun.com/document_detail/295435.html#:~:text=SCD%E7%AE%80%E4%BB%8B,%E5%85%B3%E9%94%AEETL%E4%BB%BB%E5%8A%A1%E4%B9%8B%E4%B8%80%E3%80%82
    - https://www.cnblogs.com/biwork/p/3363749.html
    - https://www.codenong.com/cs107062349/

### 6) Give your idea regarding factless fact? And why do we use it?
- ideas
    -  Factless fact table is a fact table that contains no fact measure in it. It has only the dimension keys in it.
    - At times, certain situations may arise in the business where you need to have a factless fact table.
    - example :  suppose you are maintaining an employee attendance record system, you can have a factless fact table having three keys.
    - You can see that below table does not contain any measure. Now, if you want to answer the below question, you can do easily using the above single factless fact table rather than having two separate fact tables:
        - “How many employees of a particular department were present on a particular day?”
```
# sql schema
EMPLOYEE_ID
DEPARTMENT_ID
TIME_ID
```

### 7) Distinguish between OLTP and OLAP?
- OLTP 
    - `Online Transaction Processing System`
    - OLTP maintains the transactional data of the business & is highly `normalized` generally.
    - star schema
- OLAP
    - `Online Analytical Processing System`
    - OLAP is for `analysis and reporting` purposes & it is in `denormalized`form.
    - snowflake schema
- extra
    - MPP (massively parallel processing)
        - An MPP database is a database that is optimized to be processed in parallel for many operations to be performed by many processing units at a time.
- ref
    - https://searchdatamanagement.techtarget.com/definition/MPP-database-massively-parallel-processing-database#:~:text=An%20MPP%20database%20is%20a,different%20parts%20of%20the%20program
    - https://medium.com/@jockeyng/hadoop%E8%88%87mpp%E6%98%AF%E4%BB%80%E9%BA%BC%E9%97%9C%E4%BF%82-%E6%9C%89%E4%BB%80%E9%BA%BC%E5%8D%80%E5%88%A5%E5%92%8C%E8%81%AF%E7%B9%AB-afb4397e12a1


### 8) What is a `Surrogate key`? How is it different from a primary key?
- `Surrogate Key` is a `unique identifier or a system-generated sequence number key that can act as a primary key`. It can be `a column or a combination of columns`. Unlike a primary key, it is `not` picked up from the `existing` application data fields.

### 9) Is this true that all databases should be in 3NF?
- It is not mandatory for a database to be in 3NF. However, if your purpose is the easy maintenance of data, less redundancy, and efficient access then you should go with a de-normalized database.


### 10) Explain 1NF, 2ND, 3NF, 4NF, 5NF?
- 1NF: 
    - The domain of each attribute contains only atomic values, and the value of each attribute contains only a single value from that domain.
- 2NF: 
    - No non-prime attribute in the table is functionally dependent on a proper subset of any candidate key.
- 3NF: 
    - Every non-prime attribute is non-transitively dependent on every candidate key in the table. The attributes that do not contribute to the description of the primary key are removed from the table. In other words, no transitive dependency is allowed.
- 4NF: 
    - Every non-trivial multivalued dependency in the table is a dependency on a superkey.
- 5NF: 
    - Every non-trivial join dependency in the table is implied by the superkeys of the table.

### 11) List out a few common mistakes encountered during Data Modelling?
- Building massive data models: 
    - Large data models are like to have more design faults. Try to restrict your data model to not more than 200 tables.
- Lack of purpose: 
    - If you do not know that what is your business solution is intended for, you might come up with an incorrect data model. So having clarity on the business purpose is very important to come up with the right data model.
- Inappropriate use of surrogate keys: 
    - `Surrogate key should not be used unnecessarily. Use surrogate key only when the natural key cannot serve the purpose of a primary key`.
- Unnecessary de-normalization: 
    - `Don’t denormalize until and unless you have a solid & clear business reason to do so because de-normalization creates redundant data which is difficult to maintain`.

### 12)  If a unique constraint is applied to a column then will it throw an error if you try to insert two nulls into it?
- No, it will `not` throw any error in this case because `a null value is NOT equal to another null value`. So, more than one null will be inserted in the column without any error.

### 13) What is cardinality?
- Thinking mathematically, it is the number of elements in a set. Thinking in the database world, cardinality has to do with the counts in a relationship, `one-to-one, one-to-many, or many-to-many`.

### Ref:
- DB model general
    - https://www.softwaretestinghelp.com/data-modeling-interview-questions-answers/
    - https://resources.biginterview.com/industry-specific/sql-interview-questions/#1-What-is-cardinality
- keys in DB
    - https://begriffs.com/posts/2018-01-01-sql-keys-in-depth.html
- DB model tool
    - https://www.softwaretestinghelp.com/data-modeling-tools/