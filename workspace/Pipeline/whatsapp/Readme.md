# Whatsapp

## Part 0) Biz Requirement
- Case 1:
	- Define product success metrics (whatsapp)
	- Case Study:
		- If MAU is donw in someday, what may be the reasons ?
		- How to define new users, churned users, returned users ?
			- how to define a table, for above calculation ? (pct is needed as well)
	- SQL:
		- how to get thses value ? `UID, first_active_date, last_active_date, previous_active_date`
		- how to update this table per daily data partition (distribution) ?
	- Python (ETL)
		- implement above SQL questions via python
- Case 2:
	- Case Study:
		- Define product success metrics (consider a photo-upload app)
		- how to evaluate if product is successful ?
	- Python (ETL)
		- write a stream etl process the data for above metric calculation
