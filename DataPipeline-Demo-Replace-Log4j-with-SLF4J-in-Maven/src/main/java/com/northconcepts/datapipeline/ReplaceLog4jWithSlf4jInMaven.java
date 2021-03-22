package com.northconcepts.datapipeline;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

public class ReplaceLog4jWithSlf4jInMaven {
    private static final Logger logger = LoggerFactory.getLogger(ReplaceLog4jWithSlf4jInMaven.class);

    private static final String ACCESS_KEY = "YOUR ACCESS KEY";
    private static final String SECRET_KEY = "YOUR SECRET KEY";

    public static void main(String[] args) throws Throwable{
        logger.info("Example log from {}", ReplaceLog4jWithSlf4jInMaven.class.getSimpleName());

        URL resource = ReplaceLog4jWithSlf4jInMaven.class.getClassLoader().getResource("read_parquet_file.parquet");
        File file = new File(resource.toURI());

        ParquetDataReader reader = new ParquetDataReader(file);
        Job.run(reader, new StreamWriter(System.out));

        logger.info("Schema: {}", reader.getSchema());

        Configuration configuration = new Configuration();
        configuration.set("fs.s3a.access.key", ACCESS_KEY);
        configuration.set("fs.s3a.secret.key", SECRET_KEY);
        configuration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");

        Path inputPath = new Path("s3a://bucketName/input.parquet");
        DataReader parquetDataReader = new ParquetDataReader(HadoopInputFile.fromPath(inputPath, configuration));

        Path outputPath = new Path("s3a://bucketName/output.parquet");
        DataWriter parquetDataWriter = new ParquetDataWriter(HadoopOutputFile.fromPath(outputPath, configuration));

        Job.run(parquetDataReader, parquetDataWriter);
    }
}
