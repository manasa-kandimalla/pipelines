import org.apache.spark.sql.SparkSession

val spark: SparkSession = SparkSession.builder().config("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS").config("spark.hadoop.google.cloud.auth.service.account.enable", "true").config("spark.jars", "gs://modak-nabu-bucket/mt4002_testing/gs_test/spark-bigquery-with-dependencies_2.12-0.30.0.jar").config("spark.hadoop.google.cloud.auth.service.account.json.keyfile","gs://modak-nabu-bucket/mt4002_testing/gs_test/modak-nabu-29d9cc20c4fc.json").enableHiveSupport().getOrCreate()
spark.sparkContext.hadoopConfiguration.set("fs.gs.impl","com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
spark.sparkContext.hadoopConfiguration.set("fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")

val jdbcDF = spark.read.format("jdbc").option("driver", "org.postgresql.Driver").option("url", "jdbc:postgresql://w3.training5.modak.com:5432/training").option("dbtable", "tr5.mt4002_cats").option("user", "mt4002").option("password", "mt4002@m02y22").load()

jdbcDF.write.format("csv").mode("overwrite").save("gs://modak-nabu-bucket/mt4002_testing/ingestion/mk_training_public_cats_test.parquet")
