### Ref
- https://docs.aws.amazon.com/zh_tw/emr/latest/ManagementGuide/emr-plan-debugging.html
- https://docs.amazonaws.cn/en_us/emr/latest/ReleaseGuide/tez-configure.html


```
#  https://docs.amazonaws.cn/en_us/emr/latest/ReleaseGuide/tez-configure.html

The example create-cluster command shown below creates a cluster with Tez, Hive, and Pig installed. The command references a file stored in Amazon S3, myConfig.json, which specifies properties for the tez-site classification that sets tez.am.log.level to DEBUG, and sets the execution engine to Tez for Hive and Pig using the hive-site and pig-properties configuration classifications.

-> need to customize emr config (with json)
when launch 
--configurations https://s3.amazonaws.com/mybucket/myfolder/myConfig.json

```

```
[
    {
      "Classification": "tez-site",
      "Properties": {
        "tez.am.log.level": "DEBUG"
      }
    },
    {
      "Classification": "hive-site",
      "Properties": {
        "hive.execution.engine": "tez"
      }
    },
    {
      "Classification": "pig-properties",
      "Properties": {
        "exectype": "tez"
      }
    }
  ]

```