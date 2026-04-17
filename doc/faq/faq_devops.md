# DevOps 面試常見問題

## 目錄

1. [Docker 基礎](#1-docker-基礎)
2. [Kubernetes 基礎](#2-kubernetes-基礎)
3. [K8s 資源估算（QPS → Requests/Limits）](#3-k8s-資源估算qps--requestslimits)
4. [CI/CD 流程設計](#4-cicd-流程設計)
5. [監控與可觀測性](#5-監控與可觀測性)
6. [基礎設施即代碼（IaC）](#6-基礎設施即代碼iac)
7. [常見面試題](#7-常見面試題)

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

# 設定工作目錄
WORKDIR /app

# 先複製 package.json（利用 Layer Cache 加速 build）
COPY package*.json ./
RUN npm ci --only=production

# 再複製程式碼
COPY . .

# 非 root 用戶執行（安全性）
USER node

EXPOSE 3000
CMD ["node", "server.js"]
```

**Layer Cache 最佳實踐：**
- 將變動頻率低的步驟（安裝依賴）放前面
- 將變動頻率高的步驟（複製程式碼）放後面
- 使用 `.dockerignore` 排除不必要的檔案

### 常用指令

```bash
# 建立映像
docker build -t my-app:v1 .

# 執行容器
docker run -d -p 8080:3000 --name my-container my-app:v1

# 查看日誌
docker logs -f my-container

# 進入容器
docker exec -it my-container sh

# 列出運行中的容器
docker ps

# 清理未使用的資源
docker system prune -af
```

### Docker Compose

用於在本地啟動多容器應用：

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:3000"
    environment:
      - DB_HOST=db
    depends_on:
      - db

  db:
    image: postgres:15
    environment:
      POSTGRES_PASSWORD: secret
    volumes:
      - db_data:/var/lib/postgresql/data

volumes:
  db_data:
```

---

## 2. Kubernetes 基礎

### 核心概念

```
Cluster
 └── Node（工作節點）
      └── Pod（最小部署單位）
           └── Container（應用容器）
```

| 資源 | 說明 |
|------|------|
| **Pod** | 一個或多個容器的集合，共享網路和儲存 |
| **Deployment** | 管理 Pod 的副本數量與滾動更新 |
| **Service** | 為 Pod 提供穩定的網路端點（LoadBalancer / ClusterIP） |
| **ConfigMap** | 存放非敏感的設定資料 |
| **Secret** | 存放敏感資料（密碼、金鑰），Base64 編碼 |
| **Ingress** | 管理外部 HTTP/HTTPS 流量進入 Cluster |
| **Namespace** | 邏輯隔離不同環境（dev / staging / prod） |

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
      maxUnavailable: 1
      maxSurge: 1
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
        readinessProbe:
          httpGet:
            path: /health
            port: 3000
          initialDelaySeconds: 5
          periodSeconds: 10
        livenessProbe:
          httpGet:
            path: /health
            port: 3000
          initialDelaySeconds: 15
          periodSeconds: 20
```

### 部署策略比較

| 策略 | 說明 | 優點 | 缺點 |
|------|------|------|------|
| **Rolling Update** | 逐步替換舊 Pod | 零停機，節省資源 | 同時存在新舊版本 |
| **Blue-Green** | 同時維護兩套環境，瞬間切換流量 | 快速回滾，版本乾淨 | 資源消耗翻倍 |
| **Canary** | 先讓一小部分流量走新版本 | 風險可控，可逐步驗證 | 需要流量分配機制 |

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
        averageUtilization: 65   # 保持 CPU 在 65% 觸發擴縮
```

### 常用 kubectl 指令

```bash
# 查看所有 Pod
kubectl get pods -n production

# 查看 Pod 日誌
kubectl logs -f my-pod -n production

# 描述資源（除錯用）
kubectl describe pod my-pod -n production

# 進入 Pod
kubectl exec -it my-pod -n production -- sh

# 滾動更新
kubectl set image deployment/my-app my-app=my-app:v3 -n production

# 快速回滾
kubectl rollout undo deployment/my-app -n production

# 查看 rollout 歷史
kubectl rollout history deployment/my-app -n production
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

範例：QPS=500，平均延遲=200ms
```
並發數 = 500 × 0.2 = 100 個同時進行的請求
```

### 步驟二：量測每次請求消耗（最重要）

用 k6、Locust、JMeter 進行負載測試：

```
CPU per request = 負載測試時總 CPU 使用量 / QPS
Memory per request ≈ (峰值記憶體 - 基礎記憶體) / 並發數
```

### 步驟三：計算總資源需求

```
總 CPU = QPS × CPU per request × 安全係數(1.5~2)
總 Memory = 基礎記憶體 + (並發數 × Memory per request) × 安全係數(1.3~1.5)
```

### 端對端範例

**條件：**
- QPS = 1000，平均延遲 = 100ms
- CPU per request = 5 millicores，基礎記憶體 = 200MB，Memory per request = 1MB

| 計算項目 | 公式 | 結果 |
|---------|------|------|
| 並發數 | 1000 × 0.1 | 100 |
| 總 CPU（含 buffer） | 1000 × 5m × 2 | ~10 cores |
| 總 Memory（含 buffer） | (200 + 100×1) × 1.4 | ~420MB |

**切分 10 個 Pod，每 Pod 設定：**

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

- **CPU requests**：設為預期用量的 70~80%
- **CPU limits**：設為 requests 的 1.5~2x（CPU 可限流，不會 OOM）
- **Memory requests**：接近實際用量
- **Memory limits**：略高於 requests（超出會直接 OOM Kill，要謹慎）

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
程式碼提交
  → Lint / Static Analysis
  → Unit Test
  → Build & Package（Docker Image）
  → Integration Test
  → Push to Registry
  → Deploy to Staging
  → Smoke Test / E2E Test
  → Deploy to Production（手動審核 或 自動）
  → 監控觀察
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
      - uses: actions/checkout@v3
      - name: Set up Node
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      - run: npm ci
      - run: npm test

  build-and-push:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v3
      - name: Build and push Docker image
        run: |
          docker build -t my-app:${{ github.sha }} .
          docker push my-app:${{ github.sha }}

  deploy:
    needs: build-and-push
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to K8s
        run: |
          kubectl set image deployment/my-app \
            my-app=my-app:${{ github.sha }}
```

### 部署策略選擇

| 場景 | 建議策略 |
|------|---------|
| 一般 Web 服務 | Rolling Update |
| 需要即時切換/快速回滾 | Blue-Green |
| 高風險功能上線 | Canary（5% → 20% → 100%） |
| 資料庫 Schema 變更 | Backward-compatible migration 先行 |

---

## 5. 監控與可觀測性

### 可觀測性三大支柱

| 支柱 | 說明 | 工具 |
|------|------|------|
| **Metrics（指標）** | 量化的數值，如 QPS、CPU 使用率、錯誤率 | Prometheus + Grafana |
| **Logs（日誌）** | 事件的文字紀錄 | ELK Stack、Loki |
| **Traces（追蹤）** | 單一請求跨多個服務的完整鏈路 | Jaeger、Zipkin、OpenTelemetry |

### 關鍵監控指標（黃金信號）

Google SRE 定義的 **Four Golden Signals**：

| 指標 | 說明 |
|------|------|
| **Latency（延遲）** | 請求的回應時間（p50, p95, p99） |
| **Traffic（流量）** | 每秒請求數（QPS/RPS） |
| **Errors（錯誤率）** | 失敗請求的比例（4xx, 5xx） |
| **Saturation（飽和度）** | 系統資源的使用程度（CPU, Memory, Queue depth） |

### 告警設計原則

- 只針對**影響用戶體驗**的指標告警（避免告警疲勞）
- 設定合理的告警閾值和持續時間（避免短暫 spike 觸發告警）
- 告警應可操作（actionable），每個告警都應有對應的 runbook

### Prometheus 監控範例

```yaml
# 告警規則範例
groups:
- name: app-alerts
  rules:
  - alert: HighErrorRate
    expr: rate(http_requests_total{status=~"5.."}[5m]) / rate(http_requests_total[5m]) > 0.05
    for: 2m
    labels:
      severity: critical
    annotations:
      summary: "錯誤率超過 5%"

  - alert: HighLatency
    expr: histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m])) > 2
    for: 5m
    annotations:
      summary: "P99 延遲超過 2 秒"
```

---

## 6. 基礎設施即代碼（IaC）

### 為什麼使用 IaC？

- **版本控制**：基礎設施變更可追蹤、可審查
- **可重複性**：同樣的代碼在不同環境產生一致的基礎設施
- **自動化**：減少人工操作，降低出錯機率
- **災難恢復**：可快速重建整個環境

### Terraform 基本概念

```hcl
# 定義 AWS EC2 instance
resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t3.medium"

  tags = {
    Name        = "web-server"
    Environment = "production"
  }
}

# 輸出資源資訊
output "instance_ip" {
  value = aws_instance.web.public_ip
}
```

```bash
terraform init      # 初始化，下載 providers
terraform plan      # 預覽變更（不實際執行）
terraform apply     # 執行變更
terraform destroy   # 銷毀資源
```

### Helm（Kubernetes 套件管理）

Helm 是 Kubernetes 的套件管理工具，類似 apt/yum/brew：

```bash
# 新增 chart repository
helm repo add bitnami https://charts.bitnami.com/bitnami

# 安裝 chart（如 Redis）
helm install my-redis bitnami/redis \
  --set auth.password=secret \
  --namespace production

# 升級
helm upgrade my-redis bitnami/redis --set image.tag=7.0

# 回滾
helm rollback my-redis 1

# 查看已安裝的 release
helm list -n production
```

---

## 7. 常見面試題

### Q1：Docker 容器和虛擬機的主要差別？

**重點回答：**
- 容器共享 Host OS 的 Kernel，VM 有自己完整的 OS
- 容器啟動快（秒級）、資源消耗少；VM 啟動慢（分鐘）、資源消耗大
- VM 隔離性更強（適合多租戶）；容器更輕量（適合微服務）

---

### Q2：K8s 中 Deployment、Pod、ReplicaSet 的關係？

```
Deployment（管理更新策略）
  └── ReplicaSet（確保 Pod 數量）
       └── Pod（實際執行的容器）
```

- **Deployment** 描述期望狀態，自動建立 ReplicaSet
- **ReplicaSet** 確保指定數量的 Pod 在運行
- **Pod** 是最小部署單位，通常不直接建立 Pod

---

### Q3：如何實現零停機部署？

1. 使用 **Rolling Update** 或 **Blue-Green** 策略
2. 設定 `readinessProbe`，確保新 Pod 就緒後才接收流量
3. 設定 `preStop hook` 和 `terminationGracePeriodSeconds`，確保舊 Pod 優雅關閉
4. 使用 **PodDisruptionBudget（PDB）** 確保最少可用 Pod 數

```yaml
# 確保至少 2 個 Pod 同時可用
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: my-app-pdb
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: my-app
```

---

### Q4：CI/CD pipeline 中發現測試失敗，如何處理？

1. Pipeline **立即失敗**，不繼續部署
2. 自動通知相關開發者（Slack、Email）
3. 開發者在本地重現並修復
4. Push fix → 重新觸發 Pipeline
5. 若是 flaky test，先標記為 quarantine，避免阻礙主線

---

### Q5：如何選擇 ConfigMap 和 Secret？

| 情況 | 使用 |
|------|------|
| 非敏感設定（feature flags、環境名稱、日誌等級） | ConfigMap |
| 敏感資料（密碼、API Key、TLS 憑證） | Secret |

> 注意：K8s Secret 預設只是 Base64 編碼，**不是加密**。生產環境應搭配 Vault 或 AWS Secrets Manager 等工具。

---

### Q6：監控和告警的最佳實踐？

1. 監控**黃金信號**：延遲、流量、錯誤率、飽和度
2. 告警只針對影響用戶的問題，避免告警疲勞
3. 每個告警都要有 **Runbook**（如何處理的操作手冊）
4. 定期演練 **On-call Runbook** 和故障演練（Chaos Engineering）
5. 使用分散式追蹤（Distributed Tracing）協助定位跨服務問題
