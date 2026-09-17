<!-- d325616051a0 -->
# Airflow FAQ

1. Airflow 要怎麼擴展（scale）？
	- 一般做法
		- https://eng.lyft.com/running-apache-airflow-at-lyft-6e53bb8fccff
		- https://medium.com/@tomaszdudek/yet-another-scalable-apache-airflow-with-docker-example-setup-84775af5c451
		- https://www.astronomer.io/guides/airflow-scaling-workers/
	- 在 gcloud 上擴展 Airflow
		- https://medium.com/traveloka-engineering/enabling-autoscaling-in-google-cloud-composer-ac84d3ddd60

2. 用 `airflow.cfg ` 調整規模
	- https://www.astronomer.io/guides/airflow-scaling-workers/

	- parallelism：整個 Airflow 同時可以跑的 task instance 上限。也就是說，把所有正在跑的 DAG 加起來，同一時間最多只會有 32 個 task 在跑。

	- dag_concurrency：單一個 DAG 裡面同時可以跑的 task instance 數量。換句話說，你可以有 2 個 DAG 各自並行跑 16 個 task；但一個有 50 個 task 的 DAG，同時也只會跑 16 個 —— 不是 32 個。

	- max_threads：把 max_threads = 2 調高，可以增加 scheduler 上執行的執行緒數量。這能避免 scheduler 落後，但也要吃更多資源。調高的話，可能要一併加大 scheduler 的 CPU 或記憶體。建議設成 n-1，其中 n 是 scheduler 的 CPU 數。

	- pools：用來限制某一類 task 的同時執行數量。當你有很多 worker 並行、但又不想把上游或下游打爆時非常好用。舉例來說，用上面的預設值，一個 DAG 有 50 個 task 要去打某個 REST API，DAG 一啟動就會有 16 個 worker 同時打那支 API，你很可能收到一堆 throttling 錯誤。這時可以建一個 pool、把上限設成 5，再把這些 task 都指定到該 pool；就算 worker 還很空，同一時間也只會跑 5 個。
	***Airflow pool 是用來限制執行的並行度。若某個 task 特別關鍵，可以把它的 priority_weight 調高。***

	- scheduler_heartbeat_sec：這個設定控制 Airflow scheduler 多久取一次 heartbeat、並更新 metastore 裡該 job 的紀錄；可以考慮調高一點（例如 60 秒）。

	- Q「為什麼我加了 worker，跑的 task 還是沒變多？」

		- worker_concurrency 也有關，但它決定的是**單一個 worker** 能處理幾個 task。所以 4 個 worker、worker concurrency 設 16，理論上可以同時處理 64 個 task。可是在上面那組預設值下，實際上只會有 32 個真的並行跑（如果所有 task 都在同一個 DAG 裡，甚至只有 16 個）。

		- 如果要調高 worker_concurrency，先確認 worker 撐得住這個負載，可能需要加大 worker 的 CPU 或記憶體。注意：這個設定只對 CeleryExecutor 有效。

3. 提升 Airflow 的可靠性
	- https://eng.lyft.com/running-apache-airflow-at-lyft-6e53bb8fccff

	- Pool 納入版控：把 Airflow 的 pool 設定放進版本控制，每個團隊申請 pool 時附上他們估的最大 task slot 數，經過 review 後再套用；更新後的 pool 設定在 runtime 生效。

	- DAG 的整合測試：在 CI 階段跑整合測試，檢查是否符合 Airflow 的最佳實務 —— 對所有 DAG 定義做 sanity check、檢查每個 DAG 都有固定的 start_date、檢查沒有沒人用的 pool，以及 DAG 裡指定的 pool 是否真的存在等等。

	- 鎖住 UI 的寫入權限：我們關掉 Airflow 幾個重要 UI ModelView（例如 PoolModelView、VariableView、DagRunModelView）的寫入權限，避免使用者從 UI 不小心改動 metastore 裡的 Pool、Variable 與 DagRun 資料表。
