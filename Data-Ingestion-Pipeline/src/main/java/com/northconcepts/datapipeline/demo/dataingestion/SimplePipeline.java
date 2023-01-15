package com.northconcepts.datapipeline.demo.dataingestion;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class SimplePipeline {

    public static void main(String[] args) {
        
        // Ingest data
        DataReader reader = new CSVReader(new File("data/input/portfolio.csv")).setFieldNamesInFirstRow(true).setAllowMultiLineText(true);
        DataWriter writer = new ParquetDataWriter(new File("data/output/portfolio-incoming-2023-01-16.parquet"));
        Job.run(reader, writer);
        
        // Show ingested data
        reader = new ParquetDataReader(new File("data/output/portfolio-incoming-2023-01-16.parquet"));
        writer = StreamWriter.newSystemOutWriter();
        Job.run(reader, writer);
        
    }

}
