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

## Ref:
- https://www.softwaretestinghelp.com/data-modeling-interview-questions-answers/