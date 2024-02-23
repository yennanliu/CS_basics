# DE FAQ

#### 0. Data Engineer Interview Questions With Python
- https://realpython.com/data-engineer-interview-questions-python/?fbclid=IwAR3gV5Ot3LQDu_cx1ROSrxYh7TJY1W7hNkEGlsWcpbEhbvWVv7U2C5BjgO8

#### 1. Difference between `file format` and `storage system` ? 

- `Fileformat` is different ways describing how the information recorded in files should be stored and read. There is for example different image file formats gif,jpeg and png and they each store images but the information inside the file differs between the different file formats.

- `Filesystem` is the structure and layout of files, directories and links and how this is stored on the disk. File system is a way to represent how the files are stored on a medium. There can even be files, those format is a disk image. And that disk image can have its own file system that is accessible using suitable tools.


- https://www.quora.com/What-is-the-difference-between-filesystem-and-file-format

- https://www.quora.com/What-is-the-difference-between-a-file-system-and-a-file-format

#### 2.  Differene between `Cluster` and `Node`?

- In Hadoop distributed system, Node is a single system which is responsible to store and process data. Whereas Cluster is a collection of multiple nodes which communicates with each other to perform set of operation.

- Multiple nodes are configured to perform a set of operations we call it Cluster. A Hadoop cluster includes a single Master node and multiple Slave nodes. Master node that is Namenode which contains the metadata of the actual data and Slave nodes that is the Datanode whicih contains actual data.

- In short, a Cluster is a set of Nodes

- https://www.edureka.co/community/43355/difference-between-cluster-and-nodes

- https://www.quora.com/What-is-the-difference-between-cluster-and-node
