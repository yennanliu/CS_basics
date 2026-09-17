<!-- e767112b1f8e -->
# DevOps 面試常見問題

<!-- 7d78876d9498 -->
## 目錄

1. [Docker 基礎](#1-docker-基礎)
2. [Kubernetes 基礎](#2-kubernetes-基礎)
3. [K8s 資源估算（QPS → Requests/Limits）](#3-k8s-資源估算qps--requestslimits)
4. [CI/CD 流程設計](#4-cicd-流程設計)
5. [監控與可觀測性](#5-監控與可觀測性)
6. [SRE 可靠性工程](#6-sre-可靠性工程)
7. [網路基礎](#7-網路基礎)
8. [Linux 常用指令與概念](#8-linux-常用指令與概念)
9. [K8s 持久化儲存與 StatefulSet](#9-k8s-持久化儲存與-statefulset)
10. [GitOps 工作流程](#10-gitops-工作流程)
11. [安全性（Security）](#11-安全性security)
12. [基礎設施即代碼（IaC）](#12-基礎設施即代碼iac)
13. [常見面試題](#13-常見面試題)

---

<!-- c5eadbc0a745 -->
## 1. Docker 基礎

<!-- 2bca6474a94b -->
### 容器 vs 虛擬機

| 特性 | 容器（Container） | 虛擬機（VM） |
|------|-------------------|-------------|
| 隔離層級 | OS 進程層級（共享 Kernel） | 完整硬體虛擬化 |
| 啟動時間 | 秒級 | 分鐘級 |
| 資源消耗 | 輕量 | 較重 |
| 移植性 | 高（映像檔跨環境一致） | 較低 |
| 安全隔離 | 較弱 | 較強 |

<!-- ed000eeae871 -->
### Dockerfile 重要概念

<!--CODE-->

**最佳實踐：**
- 將變動頻率低的步驟（安裝依賴）放前面，充分利用 Layer Cache
- 使用 `.dockerignore` 排除不必要的檔案（`node_modules`、`.git`）
- 使用 Multi-stage build 縮減最終映像大小

**Multi-stage build 範例（Go）：**

<!--CODE-->

<!-- 575d9cdc541b -->
### 常用 Docker 指令

<!--CODE-->

<!-- feb0e0ced664 -->
### Docker Compose（本地多容器開發）

<!--CODE-->

---

<!-- cb1016508ddf -->
## 2. Kubernetes 基礎

<!-- 742504069377 -->
### 架構概覽

<!--CODE-->

<!-- 4de1e6facda3 -->
### 核心資源

| 資源 | 說明 |
|------|------|
| **Pod** | 一個或多個容器的集合，共享網路和儲存，是最小部署單位 |
| **Deployment** | 管理無狀態應用的副本數量與滾動更新 |
| **StatefulSet** | 管理有狀態應用（DB、Kafka），保證 Pod 有穩定的名稱和儲存 |
| **DaemonSet** | 確保每個 Node 都運行一個 Pod（如日誌收集、監控 Agent） |
| **Service** | 為 Pod 提供穩定的網路端點 |
| **Ingress** | 管理外部 HTTP/HTTPS 流量，支援路由和 TLS |
| **ConfigMap** | 存放非敏感設定資料 |
| **Secret** | 存放敏感資料（密碼、金鑰） |
| **Namespace** | 邏輯隔離不同環境（dev / staging / prod） |
| **PersistentVolume** | 持久化儲存資源 |

<!-- bc9da74b4f6a -->
### Deployment 範例

<!--CODE-->

<!-- 0ef169aaede9 -->
### Service 類型

| 類型 | 說明 | 使用場景 |
|------|------|---------|
| **ClusterIP** | 只在叢集內部可訪問（預設） | 服務間內部溝通 |
| **NodePort** | 在每個 Node 上開放指定端口 | 開發/測試環境 |
| **LoadBalancer** | 自動建立雲端 Load Balancer | 生產環境對外服務 |
| **ExternalName** | 將 Service 對應到外部 DNS 名稱 | 訪問外部服務 |

<!-- fab109d42051 -->
### 部署策略比較

| 策略 | 說明 | 優點 | 缺點 |
|------|------|------|------|
| **Rolling Update** | 逐步替換舊 Pod | 零停機，資源需求低 | 同時存在新舊版本 |
| **Blue-Green** | 維護兩套環境，瞬間切換流量 | 快速回滾，版本乾淨 | 資源消耗翻倍 |
| **Canary** | 先讓小部分流量走新版本 | 風險可控，可漸進驗證 | 需要流量分配機制 |
| **Recreate** | 先停掉所有舊 Pod，再啟動新 Pod | 版本乾淨 | 有停機時間 |

<!-- 8d939f311e08 -->
### HPA（水平自動擴縮）

<!--CODE-->

<!-- 7a0bd30dcab1 -->
### 常用 kubectl 指令

<!--CODE-->

---

<!-- 81b843cc9f2b -->
## 3. K8s 資源估算（QPS → Requests/Limits）

<!-- d6e80f0579ab -->
### 核心公式

<!--CODE-->

<!-- 5812a8ef2b38 -->
### 步驟一：估算並發數（Little's Law）

<!--CODE-->

範例：QPS = 500，平均延遲 = 200ms
<!--CODE-->

<!-- 8247812a19a7 -->
### 步驟二：量測每次請求消耗

用 k6 / Locust / JMeter 進行負載測試，再推算：

<!--CODE-->

<!-- a96ea8ed2c83 -->
### 步驟三：計算總資源需求

<!--CODE-->

<!-- 37778fe17294 -->
### 端對端範例

條件：QPS = 1000，延遲 = 100ms，CPU per req = 5m，基礎記憶體 = 200MB，Memory per req = 1MB

| 計算項目 | 公式 | 結果 |
|---------|------|------|
| 並發數 | 1000 × 0.1 | 100 |
| 總 CPU（含 buffer） | 1000 × 5m × 2 | ~10 cores |
| 總 Memory（含 buffer） | (200 + 100) × 1.4 | ~420 MB |

切分 10 個 Pod，每 Pod：

<!--CODE-->

<!-- 0d26d2fe57ab -->
### Requests vs Limits 設定原則

| 資源 | Requests | Limits |
|------|----------|--------|
| CPU | 預期用量的 70～80% | requests 的 1.5～2x（超出只會限流，不會崩潰） |
| Memory | 接近實際用量 | 略高於 requests（超出直接 OOM Kill，要謹慎） |

<!-- 7b7180ab15e3 -->
### 不同應用類型的特性

| 類型 | CPU | Memory |
|------|-----|--------|
| CPU-bound（加密、轉碼） | 高 | 低 |
| IO-bound（API 呼叫、DB 查詢） | 低 | 中 |
| JVM 應用 | 中 | 高（需大 buffer） |
| Node.js | 低 | 中 |
| Python | 中（可能有 spike） | 中 |

---

<!-- 626c2678cc66 -->
## 4. CI/CD 流程設計

<!-- e1a2d1a7c7f5 -->
### 標準 Pipeline 階段

<!--CODE-->

<!-- 8e5f820a4ed4 -->
### GitHub Actions 範例

<!--CODE-->

<!-- c2cf6c91f6d9 -->
### 部署策略選擇

| 場景 | 建議策略 |
|------|---------|
| 一般 Web 服務 | Rolling Update |
| 需即時切換 / 快速回滾 | Blue-Green |
| 高風險功能上線 | Canary（5% → 20% → 100%） |
| 資料庫 Schema 變更 | 先做 backward-compatible migration，再更新應用 |

---

<!-- 14866761deb9 -->
## 5. 監控與可觀測性

<!-- cb554ac5edf5 -->
### 可觀測性三大支柱

| 支柱 | 說明 | 常用工具 |
|------|------|---------|
| **Metrics（指標）** | 量化數值，如 QPS、CPU 使用率、錯誤率 | Prometheus + Grafana |
| **Logs（日誌）** | 事件的文字紀錄，用於排查問題 | ELK Stack (Elasticsearch + Logstash + Kibana)、Loki |
| **Traces（分散式追蹤）** | 單一請求跨多個服務的完整鏈路 | Jaeger、Zipkin、OpenTelemetry |

<!-- a6764db94070 -->
### 黃金信號（Google SRE Four Golden Signals）

| 指標 | 說明 | 範例 |
|------|------|------|
| **Latency（延遲）** | 請求的回應時間 | p50 < 100ms，p99 < 500ms |
| **Traffic（流量）** | 每秒請求數 | RPS、QPS |
| **Errors（錯誤率）** | 失敗請求的比例 | 5xx 錯誤率 < 0.1% |
| **Saturation（飽和度）** | 系統資源使用程度 | CPU < 70%，Queue depth < N |

<!-- f5264fc1060b -->
### Prometheus 告警規則範例

<!--CODE-->

<!-- b4c4d5b04774 -->
### 告警設計原則

- 只針對**影響用戶體驗**的指標告警，避免告警疲勞（Alert Fatigue）
- 每個告警都應有對應的 **Runbook**（操作手冊）
- 設定合理的 `for` 持續時間，避免短暫 spike 觸發告警
- 按 severity 分級：`critical`（立即處理）→ `warning`（工作時間處理）→ `info`

---

<!-- 81a01e0039be -->
## 6. SRE 可靠性工程

<!-- 43798492dd49 -->
### SLI / SLO / SLA 的區別

| 縮寫 | 全名 | 說明 | 範例 |
|------|------|------|------|
| **SLI** | Service Level Indicator | 衡量服務品質的具體指標 | 成功請求比例、P99 延遲 |
| **SLO** | Service Level Objective | 對 SLI 設定的內部目標 | 99.9% 的請求在 500ms 內完成 |
| **SLA** | Service Level Agreement | 對外承諾的服務協議，通常比 SLO 低 | 99.5% 可用性（對客戶承諾） |

> 原則：`SLA ≤ SLO`（內部目標要比對外承諾更嚴格）

<!-- 52f915f3d105 -->
### 錯誤預算（Error Budget）

<!--CODE-->

**錯誤預算的用法：**
- 預算充足 → 可以加快新功能部署、做 infra 變更
- 預算耗盡 → 凍結新功能部署，專注於可靠性修復
- 讓開發和 SRE 共同對可靠性負責

<!-- 80f114110c05 -->
### 常見可用性目標

| SLO | 每月允許停機 | 適用場景 |
|-----|------------|---------|
| 99% | ~7.3 小時 | 內部工具 |
| 99.9% | ~43 分鐘 | 一般 Web 服務 |
| 99.95% | ~21 分鐘 | 商業服務 |
| 99.99% | ~4.3 分鐘 | 關鍵業務系統 |

<!-- 3540b416eefe -->
### 事故應對流程（Incident Response）

<!--CODE-->

<!-- 5d1209b38848 -->
### Toil（重複性工作）管理

SRE 原則：`Toil < 50%` 的工作時間
- **Toil** = 手動、重複、無自動化的運維工作
- 超過上限應優先投入自動化
- 範例：手動重啟服務 → 改為 liveness probe 自動重啟

---

<!-- c38b145ceead -->
## 7. 網路基礎

<!-- 22909dd8628f -->
### OSI 七層 vs TCP/IP 四層

| OSI 七層 | TCP/IP 四層 | 協定範例 |
|---------|------------|---------|
| 應用層（7） | 應用層 | HTTP、HTTPS、DNS、gRPC |
| 表示層（6） | 應用層 | TLS/SSL |
| 會話層（5） | 應用層 | — |
| 傳輸層（4） | 傳輸層 | TCP、UDP |
| 網路層（3） | 網路層 | IP、ICMP |
| 資料鏈結層（2） | 網路存取層 | Ethernet |
| 實體層（1） | 網路存取層 | 網路線、Wi-Fi |

<!-- e86d1f787c9b -->
### DNS 解析流程

<!--CODE-->

<!-- 736e4ea1c418 -->
### 負載平衡（Load Balancer）

| 層級 | 說明 | 範例 |
|------|------|------|
| **L4（傳輸層）** | 依據 IP + Port 轉發，速度快 | AWS NLB、HAProxy TCP mode |
| **L7（應用層）** | 依據 HTTP Header、URL、Cookie 做路由 | AWS ALB、Nginx、Ingress Controller |

**常見負載平衡演算法：**
- **Round Robin**：輪流分配
- **Least Connections**：分配給目前連線數最少的伺服器
- **IP Hash**：同一 IP 固定到同一伺服器（Session 親和性）
- **Weighted Round Robin**：依伺服器權重分配

<!-- 7c7ccbb47de7 -->
### Kubernetes 網路模型

<!--CODE-->

**Ingress 路由範例：**

<!--CODE-->

<!-- 2b76d495ab94 -->
### Service Mesh（服務網格）

用於微服務架構，在不修改應用程式碼的情況下提供：

| 功能 | 說明 |
|------|------|
| **流量管理** | 灰度發布、重試、超時、熔斷 |
| **可觀測性** | 自動生成服務間的 Metrics 和 Traces |
| **安全性** | mTLS 自動加密服務間通訊 |

常見工具：**Istio**、**Linkerd**、**Consul Connect**

---

<!-- 63d2d3f0f311 -->
## 8. Linux 常用指令與概念

<!-- c9dabc13138e -->
### 進程與系統管理

<!--CODE-->

<!-- 10d8fac9caf7 -->
### 網路診斷

<!--CODE-->

<!-- 100d58f00c81 -->
### 日誌與文件

<!--CODE-->

<!-- 728a87abbd06 -->
### 常見 Linux 概念

**Load Average（系統負載）：**
<!--CODE-->

**進程狀態（Process States）：**

| 狀態 | 說明 |
|------|------|
| R (Running) | 正在執行或等待執行 |
| S (Sleeping) | 等待事件（IO、Timer） |
| D (Uninterruptible Sleep) | 等待 IO，無法被中斷（通常是 IO 問題）|
| Z (Zombie) | 已結束但父進程未回收 |
| T (Stopped) | 被暫停（Ctrl+Z） |

**OOM Killer（記憶體不足）：**
- 當系統記憶體不足時，Linux Kernel 會選擇一個進程強制終止
- 可透過 `dmesg | grep -i "oom"` 確認是否發生
- 預防方式：設定合理的 K8s Memory Limits，避免記憶體洩漏

---

<!-- e2243e0051b3 -->
## 9. K8s 持久化儲存與 StatefulSet

<!-- 8801c86cd3b4 -->
### 儲存資源

| 資源 | 說明 |
|------|------|
| **PersistentVolume (PV)** | 叢集層級的儲存資源（管理員建立）|
| **PersistentVolumeClaim (PVC)** | Pod 對儲存的請求（開發者建立）|
| **StorageClass** | 定義儲存的類型（SSD、HDD、雲端儲存），支援動態佈建 |

<!-- e06b89b66a8f -->
### PVC 範例

<!--CODE-->

**Access Mode 類型：**
- `ReadWriteOnce (RWO)`：單一 Node 讀寫（最常見，適合 DB）
- `ReadOnlyMany (ROX)`：多個 Node 唯讀
- `ReadWriteMany (RWX)`：多個 Node 讀寫（需 NFS 或雲端共享儲存）

<!-- 61d7a988de62 -->
### StatefulSet（有狀態應用）

StatefulSet vs Deployment 的關鍵差異：

| 特性 | Deployment | StatefulSet |
|------|-----------|-------------|
| Pod 名稱 | 隨機（my-app-abc123） | 有序且穩定（my-db-0, my-db-1）|
| 儲存 | 共享或無持久化 | 每個 Pod 有獨立的 PVC |
| 啟動/停止順序 | 無序 | 有序（0, 1, 2...）|
| 適用場景 | 無狀態應用（API Server） | 有狀態應用（MySQL、Kafka、Redis Cluster）|

<!--CODE-->

---

<!-- cfbb29eb0413 -->
## 10. GitOps 工作流程

<!-- 7e16a2bb5f42 -->
### 什麼是 GitOps？

**核心原則：Git 是唯一真實來源（Single Source of Truth）**

- 所有基礎設施和應用配置都用 Git 管理
- 任何變更都透過 PR + Code Review
- 自動化工具持續確保叢集狀態與 Git 一致
- 回滾 = `git revert`，操作直覺且有審計記錄

<!-- fdd57d03cb80 -->
### GitOps vs 傳統 CI/CD

<!--CODE-->

<!-- d6475537c8c4 -->
### ArgoCD 基本概念

ArgoCD 是最常見的 GitOps 工具，在叢集內持續監控 Git 與叢集狀態的差異：

<!--CODE-->

<!-- 24cc8e85215e -->
### GitOps 實踐流程

<!--CODE-->

---

<!-- 6bc520345805 -->
## 11. 安全性（Security）

<!-- a9d7d320287a -->
### K8s RBAC（角色存取控制）

**最小權限原則（Principle of Least Privilege）**

<!--CODE-->

<!-- 1760479bb4ef -->
### Secret 安全管理

K8s Secret 預設只是 Base64 編碼，**並非加密**。生產環境建議：

| 方案 | 說明 |
|------|------|
| **Vault（HashiCorp）** | 集中式 Secret 管理，支援動態 Secret、自動輪替 |
| **AWS Secrets Manager** | 雲端原生方案，支援跨服務共享 |
| **Sealed Secrets** | 將 Secret 加密後存入 Git（適合 GitOps）|
| **External Secrets Operator** | 將外部 Secret 同步進 K8s |

<!-- 5cbf4436dc58 -->
### Network Policy（網路隔離）

<!--CODE-->

<!-- 3203d20e0418 -->
### 容器安全最佳實踐

<!--CODE-->

---

<!-- a3da1115a5da -->
## 12. 基礎設施即代碼（IaC）

<!-- 64d925c15860 -->
### 為什麼使用 IaC？

- **版本控制**：基礎設施變更可追蹤、可審查（PR + Code Review）
- **可重複性**：同樣的代碼在不同環境產生一致的基礎設施
- **自動化**：減少人工操作，降低出錯機率
- **災難恢復**：可快速重建整個環境

<!-- 07bbecb2e73e -->
### Terraform 核心概念

<!--CODE-->

<!--CODE-->

<!-- 0abbb6351bda -->
### Helm（Kubernetes 套件管理）

<!--CODE-->

---

<!-- 39fb15cc43c2 -->
## 13. 常見面試題

<!-- f5697b62ac7d -->
### Q1：Docker 容器和虛擬機的主要差別？

- 容器共享 Host OS 的 Kernel，VM 有自己完整的 OS
- 容器啟動快（秒級）、資源消耗少；VM 啟動慢（分鐘）、資源消耗大
- VM 隔離性更強（適合多租戶）；容器更輕量（適合微服務）

---

<!-- f094304ac195 -->
### Q2：K8s 中 Deployment、Pod、ReplicaSet 的關係？

<!--CODE-->

通常只操作 Deployment，不直接建立 ReplicaSet 或 Pod。

---

<!-- 697635e4437b -->
### Q3：readinessProbe 和 livenessProbe 的差別？

| 探針 | 用途 | 失敗後的行為 |
|------|------|------------|
| **readinessProbe** | 判斷 Pod 是否準備好接收流量 | 從 Service 的端點清單移除（不接流量），不重啟 |
| **livenessProbe** | 判斷 Pod 是否還存活 | 重啟容器 |
| **startupProbe** | 給啟動慢的應用額外的初始化時間 | 在通過前，livenessProbe 不生效 |

---

<!-- 78d99160479d -->
### Q4：如何實現零停機部署？

1. 使用 **Rolling Update** 或 **Blue-Green** 策略
2. 設定 `readinessProbe`，確保新 Pod 就緒後才接收流量
3. 設定 `terminationGracePeriodSeconds`，讓舊 Pod 優雅關閉（處理完進行中的請求）
4. 使用 **PodDisruptionBudget** 確保最少可用 Pod 數

<!--CODE-->

---

<!-- ab0a942f49bb -->
### Q5：SLO 和 SLA 的差別，以及錯誤預算的用途？

- **SLO** 是內部目標（如 99.9% 可用性）；**SLA** 是對客戶的對外承諾，通常 SLA < SLO
- **錯誤預算** = 100% - SLO，代表允許發生的錯誤量
- 預算充足 → 可積極部署新功能；預算耗盡 → 凍結新功能，優先修復可靠性

---

<!-- daab21a8c305 -->
### Q6：何時使用 StatefulSet 而非 Deployment？

需要以下特性時使用 StatefulSet：
- Pod 需要**穩定的、唯一的網路識別符**（my-db-0, my-db-1）
- Pod 需要**持久化儲存**，且每個 Pod 的資料不能混用
- Pod 需要**有序的部署和擴縮**（如 MySQL 主從複製，主節點要先啟動）

典型使用場景：MySQL、PostgreSQL、MongoDB、Kafka、ZooKeeper、Redis Cluster

---

<!-- a6394542c797 -->
### Q7：如何選擇 ConfigMap 和 Secret？如何安全管理 Secret？

- **ConfigMap**：非敏感設定（feature flags、環境名稱、日誌等級）
- **Secret**：敏感資料（密碼、API Key、TLS 憑證）

K8s Secret 預設只是 Base64 編碼，非加密。生產環境應搭配 Vault 或 External Secrets Operator，避免將 Secret 明文存入 Git。

---

<!-- 0175426e6a9c -->
### Q8：CI/CD Pipeline 中有哪些重要的安全實踐？

1. **最小權限**：CI 只給必要的 K8s 權限（RBAC）
2. **掃描映像漏洞**：在 Pipeline 中加入 Trivy 或 Snyk 掃描
3. **Secret 不入 Git**：使用 Vault 或雲端 Secret 管理服務
4. **簽署映像**：使用 cosign 對 Docker Image 做數位簽署
5. **審查 PR**：所有 infra 變更都需要 Code Review，不允許直接 push 到 main
