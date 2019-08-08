# Note for Database

## 1) DB type

- RDBMS (SQL) 
- No SQL
- Others

## 2) DB properties 

- RDBMS 
	- ACID : atomicity, consistency, isolation, and durability
		- Atomicity	
			- Guarantee that either all of the transaction succeeds or none of 
			it does. You don’t get part of it succeeding and part of it not. If 
			one part of the transaction fails, the whole transaction fails. With 
			atomicity, it’s either “all or nothing”.

		- Consistency
			- This ensures that you guarantee that all data will be consistent. 
			All data will be valid according to all defined rules, including any 
			constraints, cascades, and triggers that have been applied on the 
			database.
		
		- Isolation
			- Guarantees that all transactions will occur in isolation. No 
			transaction will be affected by any other transaction. So a 
			transaction cannot read data from any other transaction that has not 
			yet completed.

		- Durability
			- Once transaction is committed, it will remain in the system – even 
			if there’s a system crash immediately following the transaction. Any 
			changes from the transaction must be stored permanently. If the 
			system tells the user that the transaction has succeeded, the 
			transaction must have, in fact, succeeded.

## 3) DB design 

- Concept 
- Process 
- Example 

## 4) DB tuning 

## 5) DB managment 

## 6) Case study 