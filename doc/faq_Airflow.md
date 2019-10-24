# Airflow FAQ

1. How can you scale Airflow? 
	- General 
		- https://eng.lyft.com/running-apache-airflow-at-lyft-6e53bb8fccff
		- https://medium.com/@tomaszdudek/yet-another-scalable-apache-airflow-with-docker-example-setup-84775af5c451
		- https://www.astronomer.io/guides/airflow-scaling-workers/
	- Scale Airflow with gcloud 
		- https://medium.com/traveloka-engineering/enabling-autoscaling-in-google-cloud-composer-ac84d3ddd60

2. Scale airflow via `airflow.cfg `
	- https://www.astronomer.io/guides/airflow-scaling-workers/

	- parallelism : is the max number of task instances that can run concurrently on airflow. This means that across all running DAGs, no more than 32 tasks will run at one time.

	- dag_concurrency : is the number of task instances allowed to run concurrently within a specific dag. In other words, you could have 2 DAGs running 16 tasks each in parallel, but a single DAG with 50 tasks would also only run 16 tasks - not 32

	- max_threads : max_threads = 2 setting can be used to increase the number of threads running on the scheduler. This can prevent the scheduler from getting behind, but may also require more resources. If you increase this, you may need to increase CPU and/or memory on your scheduler. This should be set to n-1 where n is the number of CPUs of your scheduler.

	- pools : are a way of limiting the number of concurrent instances of a specific type of task. This is great if you have a lot of workers in parallel, but you don’t want to overwhelm a source or destination. For example, with the default settings above, and a DAG with 50 tasks to pull data from a REST API, when the DAG starts, you would get 16 workers hitting the API at once and you may get some throttling errors back from your API. You can create a pool and give it a limit of 5. Then assign all of the tasks to that pool. Even though you have plenty of free workers, only 5 will run at one time.
	***Airflow pool is used to limit the execution parallelism. Users could increase the priority_weight for the task if it is a critical one.***

	- scheduler_heartbeat_sec: User should consider increasing scheduler_heartbeat_sec config to a higher value (e.g. 60 secs) which controls how frequently the Airflow scheduler gets the heartbeat and updates the job’s entry in the Airflow metastore.

	- Q "Why are more tasks not running even after I add workers?"

		- worker_concurrency is related, but it determines how many tasks a single worker can process. So, if you have 4 workers running at a worker concurrency of 16, you could process up to 64 tasks at once. Configured with the defaults above, however, only 32 would actually run in parallel. (and only 16 if all tasks are in the same DAG)

		- If you increase worker_concurrency, make sure your worker has enough resources to handle the load. You may need to increase CPU and/or memory on your workers. Note: This setting only impacts the CeleryExecutor

3. Improve Airflow reliability
	- https://eng.lyft.com/running-apache-airflow-at-lyft-6e53bb8fccff

	- Source Control For Pools: We maintain the Airflow pool configuration in source control and review each team’s pool request with their estimations on the max task slots. The updated pool configuration is applied in runtime for Airflow.

	- Integration Test For DAG: We have integration tests running in Continuous Integration phase, which do checks to ensure Airflow best practices including a sanity check on all the DAG definitions; a start_date parameter check to guarantee all DAGs have a fixed start_date; a pool check to ensure there is no unused pool and a check to ensure pool specified in any DAG actually exists, etc.

	- Secure UI access: We disable write access on a couple of important UI ModelViews (e.g, PoolModelView, VariableView, DagRunModelView) on Airflow. This is to avoid users accidentally modifying Pool, Variable, and DagRun tables in the Airflow metastore from the UI.
