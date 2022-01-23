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
- Dimension
    - Dimensions represent qualitative data. For Example, plan, product, class are all dimensions.
    - A dimension table contains descriptive or textual attributes

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
- OLAP
    - `Online Analytical Processing System`
    - OLAP is for `analysis and reporting` purposes & it is in `denormalized`form.
- extra
    - MPP (massively parallel processing)
        - An MPP database is a database that is optimized to be processed in parallel for many operations to be performed by many processing units at a time.
- ref
    - https://searchdatamanagement.techtarget.com/definition/MPP-database-massively-parallel-processing-database#:~:text=An%20MPP%20database%20is%20a,different%20parts%20of%20the%20program
    - https://medium.com/@jockeyng/hadoop%E8%88%87mpp%E6%98%AF%E4%BB%80%E9%BA%BC%E9%97%9C%E4%BF%82-%E6%9C%89%E4%BB%80%E9%BA%BC%E5%8D%80%E5%88%A5%E5%92%8C%E8%81%AF%E7%B9%AB-afb4397e12a1

## Ref:
- https://www.softwaretestinghelp.com/data-modeling-interview-questions-answers/