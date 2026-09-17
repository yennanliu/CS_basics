<!-- dc67f6779e2c -->
# 資料工程 FAQ

<!-- 5b54ed64e8e0 -->
#### 0. 用 Python 準備資料工程師面試
- https://realpython.com/data-engineer-interview-questions-python/?fbclid=IwAR3gV5Ot3LQDu_cx1ROSrxYh7TJY1W7hNkEGlsWcpbEhbvWVv7U2C5BjgO8

<!-- efb12f1f2951 -->
#### 1. `file format` 和 `storage system` 差在哪？

- `File format`（檔案格式）講的是檔案裡記錄的資訊該怎麼存、怎麼讀。例如圖片就有 gif、jpeg、png 等不同格式，它們都在存圖片，但檔案內部的資訊組織方式各不相同。

- `Filesystem`（檔案系統）講的是檔案、目錄與連結的結構與排列，以及這些東西怎麼落在磁碟上。檔案系統是「檔案在媒體上如何存放」的一種表示方式。甚至有些檔案本身就是一個磁碟映像檔，而那個映像檔裡面可以再有自己的檔案系統，用合適的工具就能掛載讀取。


- https://www.quora.com/What-is-the-difference-between-filesystem-and-file-format

- https://www.quora.com/What-is-the-difference-between-a-file-system-and-a-file-format

<!-- 0ea6ca0d6361 -->
#### 2. `Cluster` 和 `Node` 差在哪？

- 在 Hadoop 這類分散式系統裡，Node（節點）是單一台機器，負責存放與處理資料；Cluster（叢集）則是多個節點的集合，節點之間互相通訊，一起完成一組工作。

- 把多個節點配置起來共同完成一組工作，就叫做 Cluster。一個 Hadoop 叢集包含一個 Master 節點與多個 Slave 節點：Master 節點就是 Namenode，存放實際資料的中介資料（metadata）；Slave 節點就是 Datanode，存放實際資料。

- 簡單說，Cluster 就是一組 Node

- https://www.edureka.co/community/43355/difference-between-cluster-and-nodes

- https://www.quora.com/What-is-the-difference-between-cluster-and-node
