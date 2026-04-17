# DevOps 面試常見問題

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

## 1. Docker 基礎

### 容器 vs 虛擬機

| 特性 | 容器（Container） | 虛擬機（VM） |
|------|-------------------|-------------|
| 隔離層級 | OS 進程層級（共享 Kernel） | 完整硬體虛擬化 |
| 啟動時間 | 秒級 | 分鐘級 |
| 資源消耗 | 輕量 | 較重 |
| 移植性 | 高（映像檔跨環境一致） | 較低 |
| 安全隔離 | 較弱 | 較強 |

### Dockerfile 重要概念

```dockerfile
# 使用輕量基底映像
FROM node:18-alpine

WORKDIR /app

# 先複製 package.json（利用 Layer Cache 加速 build）
COPY package*.json ./
RUN npm ci --only=production

# 再複製程式碼（變動頻繁，放最後）
COPY . .

# 非 root 用戶執行（安全性）
USER node

EXPOSE 3000
CMD ["node", "server.js"]
```

**最佳實踐：**
- 將變動頻率低的步驟（安裝依賴）放前面，充分利用 Layer Cache
- 使用 `.dockerignore` 排除不必要的檔案（`node_modules`、`.git`）
- 使用 Multi-stage build 縮減最終映像大小

**Multi-stage build 範例（Go）：**

```dockerfile
# Stage 1：編譯
FROM golang:1.21 AS builder
WORKDIR /app
COPY . .
RUN CGO_ENABLED=0 go build -o server .

# Stage 2：只保留執行檔
FROM scratch
COPY --from=builder /app/server /server
ENTRYPOINT ["/server"]
```

### 常用 Docker 指令

```bash
docker build -t my-app:v1 .          # 建立映像
docker run -d -p 8080:3000 my-app:v1  # 後台執行
docker logs -f my-container           # 追蹤日誌
docker exec -it my-container sh        # 進入容器
docker ps                              # 列出運行中容器
docker images                          # 列出本地映像
docker system prune -af               # 清理未使用資源
```

### Docker Compose（本地多容器開發）

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:3000"
    environment:
      - DB_HOST=db
      - REDIS_HOST=redis
    depends_on:
      - db
      - redis

  db:
    image: postgres:15-alpine
    environment:
      POSTGRES_PASSWORD: secret
    volumes:
      - db_data:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
    command: redis-server --requirepass secret

volumes:
  db_data:
```

---

## 2. Kubernetes 基礎

### 架構概覽

```
Cluster
├── Control Plane（主節點）
│   ├── API Server      ← 所有操作的入口
│   ├── etcd            ← 儲存叢集狀態（key-value）
│   ├── Scheduler       ← 決定 Pod 要跑在哪個 Node
│   └── Controller Manager ← 確保叢集狀態符合期望
└── Worker Node（工作節點）
    ├── kubelet         ← 管理此節點上的 Pod
    ├── kube-proxy      ← 處理網路規則
    └── Container Runtime (containerd)
```

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

### Deployment 範例

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
  namespace: production
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 1   # 更新時最多 1 個 Pod 不可用
      maxSurge: 1         # 更新時最多多建 1 個 Pod
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-app
        image: my-app:v2
        ports:
        - containerPort: 3000
        resources:
          requests:
            cpu: "250m"
            memory: "128Mi"
          limits:
            cpu: "500m"
            memory: "256Mi"
        readinessProbe:        # 就緒探針：Pod 準備好接流量了嗎？
          httpGet:
            path: /health
            port: 3000
          initialDelaySeconds: 5
          periodSeconds: 10
        livenessProbe:         # 存活探針：Pod 還活著嗎？不健康就重啟
          httpGet:
            path: /health
            port: 3000
          initialDelaySeconds: 15
          periodSeconds: 20
```

### Service 類型

| 類型 | 說明 | 使用場景 |
|------|------|---------|
| **ClusterIP** | 只在叢集內部可訪問（預設） | 服務間內部溝通 |
| **NodePort** | 在每個 Node 上開放指定端口 | 開發/測試環境 |
| **LoadBalancer** | 自動建立雲端 Load Balancer | 生產環境對外服務 |
| **ExternalName** | 將 Service 對應到外部 DNS 名稱 | 訪問外部服務 |

### 部署策略比較

| 策略 | 說明 | 優點 | 缺點 |
|------|------|------|------|
| **Rolling Update** | 逐步替換舊 Pod | 零停機，資源需求低 | 同時存在新舊版本 |
| **Blue-Green** | 維護兩套環境，瞬間切換流量 | 快速回滾，版本乾淨 | 資源消耗翻倍 |
| **Canary** | 先讓小部分流量走新版本 | 風險可控，可漸進驗證 | 需要流量分配機制 |
| **Recreate** | 先停掉所有舊 Pod，再啟動新 Pod | 版本乾淨 | 有停機時間 |

### HPA（水平自動擴縮）

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: my-app-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-app
  minReplicas: 2
  maxReplicas: 20
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 65
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 75
```

### 常用 kubectl 指令

```bash
# 查看資源
kubectl get pods -n production
kubectl get deployments,services -n production
kubectl get all -n production

# 除錯
kubectl logs -f my-pod -n production --previous  # 上一次崩潰的日誌
kubectl describe pod my-pod -n production         # 詳細資訊與 Events
kubectl exec -it my-pod -n production -- sh       # 進入 Pod

# 部署操作
kubectl set image deployment/my-app my-app=my-app:v3 -n production
kubectl rollout status deployment/my-app -n production
kubectl rollout undo deployment/my-app -n production            # 回滾到上一版
kubectl rollout undo deployment/my-app --to-revision=2          # 回滾到指定版本
kubectl rollout history deployment/my-app -n production

# 擴縮
kubectl scale deployment/my-app --replicas=5 -n production

# 強制刪除卡住的 Pod
kubectl delete pod my-pod -n production --grace-period=0 --force
```

---

## 3. K8s 資源估算（QPS → Requests/Limits）

### 核心公式

```
總 CPU    ≈ QPS × 每次請求的 CPU 消耗
總 Memory ≈ 基礎記憶體 + (並發數 × 每次請求的記憶體)
```

### 步驟一：估算並發數（Little's Law）

```
並發數 = QPS × 平均回應時間（秒）
```

範例：QPS = 500，平均延遲 = 200ms
```
並發數 = 500 × 0.2 = 100 個同時進行的請求
```

### 步驟二：量測每次請求消耗

用 k6 / Locust / JMeter 進行負載測試，再推算：

```
CPU per request    = 負載測試時總 CPU 使用量 / QPS
Memory per request ≈ (峰值記憶體 - 基礎記憶體) / 並發數
```

### 步驟三：計算總資源需求

```
總 CPU    = QPS × CPU per request  × 安全係數（1.5～2）
總 Memory = (基礎記憶體 + 並發數 × Memory per request) × 安全係數（1.3～1.5）
```

### 端對端範例

條件：QPS = 1000，延遲 = 100ms，CPU per req = 5m，基礎記憶體 = 200MB，Memory per req = 1MB

| 計算項目 | 公式 | 結果 |
|---------|------|------|
| 並發數 | 1000 × 0.1 | 100 |
| 總 CPU（含 buffer） | 1000 × 5m × 2 | ~10 cores |
| 總 Memory（含 buffer） | (200 + 100) × 1.4 | ~420 MB |

切分 10 個 Pod，每 Pod：

```yaml
resources:
  requests:
    cpu: "500m"
    memory: "64Mi"
  limits:
    cpu: "1000m"
    memory: "128Mi"
```

### Requests vs Limits 設定原則

| 資源 | Requests | Limits |
|------|----------|--------|
| CPU | 預期用量的 70～80% | requests 的 1.5～2x（超出只會限流，不會崩潰） |
| Memory | 接近實際用量 | 略高於 requests（超出直接 OOM Kill，要謹慎） |

### 不同應用類型的特性

| 類型 | CPU | Memory |
|------|-----|--------|
| CPU-bound（加密、轉碼） | 高 | 低 |
| IO-bound（API 呼叫、DB 查詢） | 低 | 中 |
| JVM 應用 | 中 | 高（需大 buffer） |
| Node.js | 低 | 中 |
| Python | 中（可能有 spike） | 中 |

---

## 4. CI/CD 流程設計

### 標準 Pipeline 階段

```
程式碼提交（PR）
  → Lint / Static Analysis（程式碼品質）
  → Unit Test（單元測試）
  → Build & Package（Docker Image）
  → Integration Test（整合測試）
  → Push to Registry（推送映像）
  → Deploy to Staging（部署測試環境）
  → Smoke Test / E2E Test（驗收測試）
  → 手動審核（Production Gate）
  → Deploy to Production（部署正式環境）
  → 監控觀察（告警、回滾機制）
```

### GitHub Actions 範例

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: '18'
          cache: 'npm'
      - run: npm ci
      - run: npm run lint
      - run: npm test -- --coverage

  build:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v4
      - name: Build and push Docker image
        env:
          IMAGE_TAG: ${{ github.sha }}
        run: |
          docker build -t my-app:$IMAGE_TAG .
          docker push my-app:$IMAGE_TAG

  deploy-staging:
    needs: build
    runs-on: ubuntu-latest
    environment: staging
    steps:
      - name: Deploy to Staging
        run: |
          kubectl set image deployment/my-app \
            my-app=my-app:${{ github.sha }} -n staging
          kubectl rollout status deployment/my-app -n staging

  deploy-prod:
    needs: deploy-staging
    runs-on: ubuntu-latest
    environment: production      # 需要手動審核
    steps:
      - name: Deploy to Production
        run: |
          kubectl set image deployment/my-app \
            my-app=my-app:${{ github.sha }} -n production
```

### 部署策略選擇

| 場景 | 建議策略 |
|------|---------|
| 一般 Web 服務 | Rolling Update |
| 需即時切換 / 快速回滾 | Blue-Green |
| 高風險功能上線 | Canary（5% → 20% → 100%） |
| 資料庫 Schema 變更 | 先做 backward-compatible migration，再更新應用 |

---

## 5. 監控與可觀測性

### 可觀測性三大支柱

| 支柱 | 說明 | 常用工具 |
|------|------|---------|
| **Metrics（指標）** | 量化數值，如 QPS、CPU 使用率、錯誤率 | Prometheus + Grafana |
| **Logs（日誌）** | 事件的文字紀錄，用於排查問題 | ELK Stack (Elasticsearch + Logstash + Kibana)、Loki |
| **Traces（分散式追蹤）** | 單一請求跨多個服務的完整鏈路 | Jaeger、Zipkin、OpenTelemetry |

### 黃金信號（Google SRE Four Golden Signals）

| 指標 | 說明 | 範例 |
|------|------|------|
| **Latency（延遲）** | 請求的回應時間 | p50 < 100ms，p99 < 500ms |
| **Traffic（流量）** | 每秒請求數 | RPS、QPS |
| **Errors（錯誤率）** | 失敗請求的比例 | 5xx 錯誤率 < 0.1% |
| **Saturation（飽和度）** | 系統資源使用程度 | CPU < 70%，Queue depth < N |

### Prometheus 告警規則範例

```yaml
groups:
- name: app-alerts
  rules:
  - alert: HighErrorRate
    expr: |
      rate(http_requests_total{status=~"5.."}[5m])
      / rate(http_requests_total[5m]) > 0.05
    for: 2m
    labels:
      severity: critical
    annotations:
      summary: "HTTP 5xx 錯誤率超過 5%"
      runbook: "https://wiki.internal/runbooks/high-error-rate"

  - alert: HighP99Latency
    expr: |
      histogram_quantile(0.99,
        rate(http_request_duration_seconds_bucket[5m])) > 2
    for: 5m
    labels:
      severity: warning
    annotations:
      summary: "P99 延遲超過 2 秒"

  - alert: PodCrashLooping
    expr: rate(kube_pod_container_status_restarts_total[15m]) > 0
    for: 5m
    labels:
      severity: critical
    annotations:
      summary: "Pod {{ $labels.pod }} 持續重啟"
```

### 告警設計原則

- 只針對**影響用戶體驗**的指標告警，避免告警疲勞（Alert Fatigue）
- 每個告警都應有對應的 **Runbook**（操作手冊）
- 設定合理的 `for` 持續時間，避免短暫 spike 觸發告警
- 按 severity 分級：`critical`（立即處理）→ `warning`（工作時間處理）→ `info`

---

## 6. SRE 可靠性工程

### SLI / SLO / SLA 的區別

| 縮寫 | 全名 | 說明 | 範例 |
|------|------|------|------|
| **SLI** | Service Level Indicator | 衡量服務品質的具體指標 | 成功請求比例、P99 延遲 |
| **SLO** | Service Level Objective | 對 SLI 設定的內部目標 | 99.9% 的請求在 500ms 內完成 |
| **SLA** | Service Level Agreement | 對外承諾的服務協議，通常比 SLO 低 | 99.5% 可用性（對客戶承諾） |

> 原則：`SLA ≤ SLO`（內部目標要比對外承諾更嚴格）

### 錯誤預算（Error Budget）

```
錯誤預算 = 100% - SLO

SLO = 99.9% 可用性
→ 每月允許停機時間 = 30天 × 24h × 60m × 0.1% ≈ 43 分鐘
```

**錯誤預算的用法：**
- 預算充足 → 可以加快新功能部署、做 infra 變更
- 預算耗盡 → 凍結新功能部署，專注於可靠性修復
- 讓開發和 SRE 共同對可靠性負責

### 常見可用性目標

| SLO | 每月允許停機 | 適用場景 |
|-----|------------|---------|
| 99% | ~7.3 小時 | 內部工具 |
| 99.9% | ~43 分鐘 | 一般 Web 服務 |
| 99.95% | ~21 分鐘 | 商業服務 |
| 99.99% | ~4.3 分鐘 | 關鍵業務系統 |

### 事故應對流程（Incident Response）

```
偵測（Detection）
  → 告警觸發 or 用戶回報
  
分類（Triage）
  → 評估影響範圍和嚴重程度
  → 指定事故指揮官（Incident Commander）

緩解（Mitigation）— 先止血，再找根因
  → 回滾部署？擴容？切換流量？

根本原因分析（Root Cause Analysis）
  → 5 Whys 分析

事後檢討（Post-mortem）
  → 無責備文化（Blameless）
  → 記錄時間線、影響、根因、改善行動
```

### Toil（重複性工作）管理

SRE 原則：`Toil < 50%` 的工作時間
- **Toil** = 手動、重複、無自動化的運維工作
- 超過上限應優先投入自動化
- 範例：手動重啟服務 → 改為 liveness probe 自動重啟

---

## 7. 網路基礎

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

### DNS 解析流程

```
瀏覽器輸入 www.example.com
  → 查本地 Cache（/etc/hosts、瀏覽器）
  → 查本地 DNS Resolver（通常是路由器或 ISP）
  → 查 Root Name Server（.）
  → 查 TLD Name Server（.com）
  → 查 Authoritative Name Server（example.com）
  → 取得 IP 位址，建立連線
```

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

### Kubernetes 網路模型

```
外部流量
  → LoadBalancer Service / Ingress Controller
  → Service（ClusterIP，kube-proxy 做 DNAT）
  → Pod（每個 Pod 有獨立 IP）
```

**Ingress 路由範例：**

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  tls:
  - hosts:
    - api.example.com
    secretName: tls-secret
  rules:
  - host: api.example.com
    http:
      paths:
      - path: /users
        pathType: Prefix
        backend:
          service:
            name: user-service
            port:
              number: 80
      - path: /orders
        pathType: Prefix
        backend:
          service:
            name: order-service
            port:
              number: 80
```

### Service Mesh（服務網格）

用於微服務架構，在不修改應用程式碼的情況下提供：

| 功能 | 說明 |
|------|------|
| **流量管理** | 灰度發布、重試、超時、熔斷 |
| **可觀測性** | 自動生成服務間的 Metrics 和 Traces |
| **安全性** | mTLS 自動加密服務間通訊 |

常見工具：**Istio**、**Linkerd**、**Consul Connect**

---

## 8. Linux 常用指令與概念

### 進程與系統管理

```bash
# 查看進程
ps aux | grep my-app          # 列出所有進程
top / htop                    # 動態查看系統資源使用
kill -9 <PID>                 # 強制終止進程
lsof -i :8080                 # 查看佔用 8080 端口的進程

# 系統資源
free -h                       # 記憶體使用
df -h                         # 磁碟使用
du -sh /var/log/*             # 目錄大小
vmstat 1                      # 系統虛擬記憶體統計

# CPU 和負載
uptime                        # 系統負載（load average）
nproc                         # CPU 核心數
```

### 網路診斷

```bash
# 連線測試
ping -c 4 google.com
curl -v https://api.example.com/health    # 測試 HTTP 請求
curl -o /dev/null -s -w "%{http_code}\n" https://example.com

# 網路資訊
netstat -tlnp                 # 查看監聽中的端口
ss -tlnp                      # 更快的 netstat 替代
ip addr                       # 查看網路介面和 IP
traceroute google.com         # 追蹤路由路徑
nslookup / dig example.com    # DNS 查詢

# 防火牆
iptables -L                   # 列出防火牆規則
```

### 日誌與文件

```bash
# 查看日誌
tail -f /var/log/nginx/access.log      # 追蹤日誌
journalctl -u nginx -f                 # systemd 服務日誌
journalctl --since "2024-01-01 00:00"  # 指定時間範圍

# 搜尋
grep -r "ERROR" /var/log/ --include="*.log"
grep -c "500" access.log               # 計算出現次數
awk '{print $1}' access.log | sort | uniq -c | sort -rn  # 統計最多的 IP

# 文件操作
find / -name "*.log" -mtime +30 -delete    # 刪除 30 天前的日誌
tar -czf backup.tar.gz /etc/nginx/         # 壓縮備份
```

### 常見 Linux 概念

**Load Average（系統負載）：**
```
uptime 輸出：load average: 1.23, 0.95, 0.88
              最近1分鐘  最近5分鐘  最近15分鐘

規則：值 ≈ CPU 核心數 表示滿載；> 核心數 表示過載
```

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

## 9. K8s 持久化儲存與 StatefulSet

### 儲存資源

| 資源 | 說明 |
|------|------|
| **PersistentVolume (PV)** | 叢集層級的儲存資源（管理員建立）|
| **PersistentVolumeClaim (PVC)** | Pod 對儲存的請求（開發者建立）|
| **StorageClass** | 定義儲存的類型（SSD、HDD、雲端儲存），支援動態佈建 |

### PVC 範例

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
spec:
  accessModes:
    - ReadWriteOnce          # 只能被一個 Node 掛載
  storageClassName: standard
  resources:
    requests:
      storage: 10Gi
```

**Access Mode 類型：**
- `ReadWriteOnce (RWO)`：單一 Node 讀寫（最常見，適合 DB）
- `ReadOnlyMany (ROX)`：多個 Node 唯讀
- `ReadWriteMany (RWX)`：多個 Node 讀寫（需 NFS 或雲端共享儲存）

### StatefulSet（有狀態應用）

StatefulSet vs Deployment 的關鍵差異：

| 特性 | Deployment | StatefulSet |
|------|-----------|-------------|
| Pod 名稱 | 隨機（my-app-abc123） | 有序且穩定（my-db-0, my-db-1）|
| 儲存 | 共享或無持久化 | 每個 Pod 有獨立的 PVC |
| 啟動/停止順序 | 無序 | 有序（0, 1, 2...）|
| 適用場景 | 無狀態應用（API Server） | 有狀態應用（MySQL、Kafka、Redis Cluster）|

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mysql
spec:
  serviceName: mysql
  replicas: 3
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:8.0
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: password
        volumeMounts:
        - name: data
          mountPath: /var/lib/mysql
  volumeClaimTemplates:         # 為每個 Pod 自動建立 PVC
  - metadata:
      name: data
    spec:
      accessModes: ["ReadWriteOnce"]
      resources:
        requests:
          storage: 20Gi
```

---

## 10. GitOps 工作流程

### 什麼是 GitOps？

**核心原則：Git 是唯一真實來源（Single Source of Truth）**

- 所有基礎設施和應用配置都用 Git 管理
- 任何變更都透過 PR + Code Review
- 自動化工具持續確保叢集狀態與 Git 一致
- 回滾 = `git revert`，操作直覺且有審計記錄

### GitOps vs 傳統 CI/CD

```
傳統 CI/CD（Push 模式）：
CI Pipeline → kubectl apply → Cluster
（CI 有 K8s 憑證，安全風險較高）

GitOps（Pull 模式）：
開發者 → Git Commit → Git Repo
                          ↑ 持續 diff
                       ArgoCD/Flux（在叢集內）→ 自動同步
```

### ArgoCD 基本概念

ArgoCD 是最常見的 GitOps 工具，在叢集內持續監控 Git 與叢集狀態的差異：

```yaml
# ArgoCD Application 定義
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: my-app
  namespace: argocd
spec:
  project: default
  source:
    repoURL: https://github.com/my-org/my-app-config
    targetRevision: main
    path: k8s/production           # Git repo 中 YAML 檔案的路徑
  destination:
    server: https://kubernetes.default.svc
    namespace: production
  syncPolicy:
    automated:
      prune: true                  # 自動刪除 Git 中已移除的資源
      selfHeal: true               # 有人手動修改叢集時自動還原
    syncOptions:
    - CreateNamespace=true
```

### GitOps 實踐流程

```
1. 開發者修改應用程式碼
   → 觸發 CI Pipeline（測試、建立 Docker Image、推送到 Registry）
   → CI 自動更新 Config Repo 中的 image tag

2. Config Repo 收到新 commit
   → ArgoCD 偵測到差異（Sync Status: OutOfSync）
   → ArgoCD 自動或手動同步到 K8s Cluster

3. 需要回滾？
   → git revert 到上一個 commit
   → ArgoCD 自動同步，叢集回到舊版本
```

---

## 11. 安全性（Security）

### K8s RBAC（角色存取控制）

**最小權限原則（Principle of Least Privilege）**

```yaml
# 定義 Role（限定在特定 Namespace）
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: pod-reader
  namespace: production
rules:
- apiGroups: [""]
  resources: ["pods", "pods/log"]
  verbs: ["get", "list", "watch"]   # 只能讀，不能修改

---
# 將 Role 綁定給 ServiceAccount
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: read-pods-binding
  namespace: production
subjects:
- kind: ServiceAccount
  name: my-app-sa
  namespace: production
roleRef:
  kind: Role
  apiGroup: rbac.authorization.k8s.io
  name: pod-reader
```

### Secret 安全管理

K8s Secret 預設只是 Base64 編碼，**並非加密**。生產環境建議：

| 方案 | 說明 |
|------|------|
| **Vault（HashiCorp）** | 集中式 Secret 管理，支援動態 Secret、自動輪替 |
| **AWS Secrets Manager** | 雲端原生方案，支援跨服務共享 |
| **Sealed Secrets** | 將 Secret 加密後存入 Git（適合 GitOps）|
| **External Secrets Operator** | 將外部 Secret 同步進 K8s |

### Network Policy（網路隔離）

```yaml
# 只允許來自 frontend namespace 的流量進入 backend
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-frontend-only
  namespace: backend
spec:
  podSelector:
    matchLabels:
      app: backend-api
  policyTypes:
  - Ingress
  ingress:
  - from:
    - namespaceSelector:
        matchLabels:
          name: frontend
    ports:
    - protocol: TCP
      port: 8080
```

### 容器安全最佳實踐

```yaml
spec:
  securityContext:
    runAsNonRoot: true           # 禁止以 root 執行
    runAsUser: 1000
    fsGroup: 2000
  containers:
  - name: my-app
    securityContext:
      allowPrivilegeEscalation: false  # 禁止提權
      readOnlyRootFilesystem: true     # 根目錄唯讀
      capabilities:
        drop:
        - ALL                          # 移除所有 Linux capabilities
```

---

## 12. 基礎設施即代碼（IaC）

### 為什麼使用 IaC？

- **版本控制**：基礎設施變更可追蹤、可審查（PR + Code Review）
- **可重複性**：同樣的代碼在不同環境產生一致的基礎設施
- **自動化**：減少人工操作，降低出錯機率
- **災難恢復**：可快速重建整個環境

### Terraform 核心概念

```hcl
# 宣告 Provider
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  # 將狀態存在遠端（多人協作必須）
  backend "s3" {
    bucket = "my-tf-state"
    key    = "production/terraform.tfstate"
    region = "us-east-1"
  }
}

# 建立 VPC
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  tags = {
    Name        = "main-vpc"
    Environment = "production"
  }
}

# 輸出資源資訊
output "vpc_id" {
  value = aws_vpc.main.id
}
```

```bash
terraform init      # 初始化，下載 providers
terraform plan      # 預覽變更（不實際執行）
terraform apply     # 執行變更
terraform destroy   # 銷毀所有資源（謹慎使用）
terraform import    # 將既有資源納入 Terraform 管理
terraform state list  # 查看目前管理的資源
```

### Helm（Kubernetes 套件管理）

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

# 安裝，並覆寫預設值
helm install my-redis bitnami/redis \
  --namespace production \
  --create-namespace \
  --set auth.password=secret \
  --set replica.replicaCount=2

helm upgrade my-redis bitnami/redis --set image.tag=7.2  # 升級
helm rollback my-redis 1                                   # 回滾到版本 1
helm list -n production                                    # 查看安裝的 release
helm uninstall my-redis -n production                     # 卸載
```

---

## 13. 常見面試題

### Q1：Docker 容器和虛擬機的主要差別？

- 容器共享 Host OS 的 Kernel，VM 有自己完整的 OS
- 容器啟動快（秒級）、資源消耗少；VM 啟動慢（分鐘）、資源消耗大
- VM 隔離性更強（適合多租戶）；容器更輕量（適合微服務）

---

### Q2：K8s 中 Deployment、Pod、ReplicaSet 的關係？

```
Deployment（管理更新策略與版本）
  └── ReplicaSet（確保指定數量的 Pod 在運行）
       └── Pod（實際執行的容器，最小部署單位）
```

通常只操作 Deployment，不直接建立 ReplicaSet 或 Pod。

---

### Q3：readinessProbe 和 livenessProbe 的差別？

| 探針 | 用途 | 失敗後的行為 |
|------|------|------------|
| **readinessProbe** | 判斷 Pod 是否準備好接收流量 | 從 Service 的端點清單移除（不接流量），不重啟 |
| **livenessProbe** | 判斷 Pod 是否還存活 | 重啟容器 |
| **startupProbe** | 給啟動慢的應用額外的初始化時間 | 在通過前，livenessProbe 不生效 |

---

### Q4：如何實現零停機部署？

1. 使用 **Rolling Update** 或 **Blue-Green** 策略
2. 設定 `readinessProbe`，確保新 Pod 就緒後才接收流量
3. 設定 `terminationGracePeriodSeconds`，讓舊 Pod 優雅關閉（處理完進行中的請求）
4. 使用 **PodDisruptionBudget** 確保最少可用 Pod 數

```yaml
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: my-app-pdb
spec:
  minAvailable: 2    # 或 maxUnavailable: 1
  selector:
    matchLabels:
      app: my-app
```

---

### Q5：SLO 和 SLA 的差別，以及錯誤預算的用途？

- **SLO** 是內部目標（如 99.9% 可用性）；**SLA** 是對客戶的對外承諾，通常 SLA < SLO
- **錯誤預算** = 100% - SLO，代表允許發生的錯誤量
- 預算充足 → 可積極部署新功能；預算耗盡 → 凍結新功能，優先修復可靠性

---

### Q6：何時使用 StatefulSet 而非 Deployment？

需要以下特性時使用 StatefulSet：
- Pod 需要**穩定的、唯一的網路識別符**（my-db-0, my-db-1）
- Pod 需要**持久化儲存**，且每個 Pod 的資料不能混用
- Pod 需要**有序的部署和擴縮**（如 MySQL 主從複製，主節點要先啟動）

典型使用場景：MySQL、PostgreSQL、MongoDB、Kafka、ZooKeeper、Redis Cluster

---

### Q7：如何選擇 ConfigMap 和 Secret？如何安全管理 Secret？

- **ConfigMap**：非敏感設定（feature flags、環境名稱、日誌等級）
- **Secret**：敏感資料（密碼、API Key、TLS 憑證）

K8s Secret 預設只是 Base64 編碼，非加密。生產環境應搭配 Vault 或 External Secrets Operator，避免將 Secret 明文存入 Git。

---

### Q8：CI/CD Pipeline 中有哪些重要的安全實踐？

1. **最小權限**：CI 只給必要的 K8s 權限（RBAC）
2. **掃描映像漏洞**：在 Pipeline 中加入 Trivy 或 Snyk 掃描
3. **Secret 不入 Git**：使用 Vault 或雲端 Secret 管理服務
4. **簽署映像**：使用 cosign 對 Docker Image 做數位簽署
5. **審查 PR**：所有 infra 變更都需要 Code Review，不允許直接 push 到 main
