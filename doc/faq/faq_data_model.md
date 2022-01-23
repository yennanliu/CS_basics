# FAQ : Data Modeling


## 1) What are the different design schemas in Data Modelling? Explain with the example?
- 2 kinds of common design
    - Star Schema
        - The simplest of the schemas is star schema where we have a fact table in the center that references multiple dimension tables around it. All the dimension tables are connected to the fact table. The primary key in all dimension tables acts as a foreign key in the fact table.
        - The star schema is quite simple, flexible and it is in `denormalized` form.

    - Snowflake Schema
        - In a snowflake schema, the level of `normalization` increases. The fact table here remains the same as in the star schema. However, the dimension tables are normalized.  Due to several layers of dimension tables, it looks like a snowflake, and thus it is named as snowflake schema.

## 2)  Which schema is better – star or snowflake?
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

## Ref:
- https://www.softwaretestinghelp.com/data-modeling-interview-questions-answers/