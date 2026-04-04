
### Error msg

#### msg 1
```
diagnostics=[Vertex vertex_1620108515464_0001_1_00 [Map 1] killed/failed due to:ROOT_INPUT_INIT_FAILURE, Vertex Input: hive_test initializer failed, vertex=vertex_1620108515464_0001_1_00 [Map 1], java.io.IOException: com.amazon.ws.emr.hadoop.fs.shaded.com.amazonaws.services.s3.model.AmazonS3Exception: Access Denied (Service: Amazon S3; Status Code: 403; Error Code: AccessDenied; Request ID: QASDYA5GDKR4HG26; S3 Extended Request ID: l+VCC4UduNhAymZ4g4Nor3EwK/a7mimma+NqYoLAgUBolRyFnXTl8AkpobwhZdccB6sndN5a56g=), S3 Extended Request ID: l+VCC4UduNhAymZ4g4Nor3EwK/a7mimma+NqYoLAgUBolRyFnXTl8AkpobwhZdccB6sndN5a56g=
	at com.amazon.ws.emr.hadoop.fs.s3n.Jets3tNativeFileSystemStore.list(
```

```
/Users/yennanliu/my_personel_save/courses/AWS_big_data_course/ELS_2021-master/hive_log/user/hadoop/hive.log

-> S3 AccessDenied
```

#### msg 2

```
2021-05-04T06:14:08,617 ERROR [pool-11-thread-2([])]: util.UserData (UserData.java:getUserData(70)) - Error encountered while try to get user data
java.io.IOException: File '/var/aws/emr/userData.json' cannot be read
	at com.amazon.ws.emr.hadoop.fs.shaded.org.apache.commons.io.FileUtils.openInputStream(FileUtils.java:296) ~[emrfs-hadoop-assembly-2.40.0.jar:?]
```

```
/Users/yennanliu/my_personel_save/courses/AWS_big_data_course/ELS_2021-master/hive_log/user/hive/hive.log

-> exception : java.io.IOException: File '/var/aws/emr/userData.json' cannot be read
```