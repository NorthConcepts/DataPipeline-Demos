package com.northconcepts.datapipeline;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

public class Example {
    private static final Logger logger = LoggerFactory.getLogger(Example.class);

    public static void main(String[] args) throws Throwable{
        logger.info("Example log from {}", Example.class.getSimpleName());

        URL resource = Example.class.getClassLoader().getResource("read_parquet_file.parquet");
        File file = new File(resource.toURI());

        ParquetDataReader reader = new ParquetDataReader(file);
        Job.run(reader, new StreamWriter(System.out));

        logger.info("Schema: {}", reader.getSchema());
    }
}
