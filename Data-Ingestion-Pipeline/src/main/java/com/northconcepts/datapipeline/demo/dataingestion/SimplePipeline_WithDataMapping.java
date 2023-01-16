package com.northconcepts.datapipeline.demo.dataingestion;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.datamapping.DataMapping;
import com.northconcepts.datapipeline.foundations.datamapping.DataMappingReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;

public class SimplePipeline_WithDataMapping {

    public static void main(String[] args) {
        
        DataMapping dataMapping = new DataMapping()
                .addFieldMapping("date", "parseDate(source.date, 'MM/dd/yyyy')")
                .addFieldMapping("symbol", "source.symbol")
                .addFieldMapping("shares", "toBigDecimal(source.shares)")
                .addFieldMapping("share_price", "toBigDecimal(source.share_price)")
                .addFieldMapping("cost", "target.share_price * target.shares")
                .addFieldMapping("acb", "toBigDecimal(source.acb_per_share)")
                ;
        
        System.out.println(dataMapping.toXml());

        /*    
        <data-mapping>
          <field-mappings>
            <field-mapping fieldName="date" sourceExpression="parseDate(source.date, 'MM/dd/yyyy')"/>
            <field-mapping fieldName="symbol" sourceExpression="source.symbol"/>
            <field-mapping fieldName="shares" sourceExpression="toBigDecimal(source.shares)"/>
            <field-mapping fieldName="share_price" sourceExpression="toBigDecimal(source.share_price)"/>
            <field-mapping fieldName="cost" sourceExpression="target.share_price * target.shares"/>
            <field-mapping fieldName="acb" sourceExpression="toBigDecimal(source.acb_per_share)"/>
          </field-mappings>
        </data-mapping>
        */    
                
        // Ingest data
        DataReader reader = new CSVReader(new File("data/input/portfolio.csv")).setFieldNamesInFirstRow(true).setAllowMultiLineText(true);
        reader = new DataMappingReader(reader, dataMapping);
        DataWriter writer = new ParquetDataWriter(new File("data/output/portfolio-incoming.parquet"));
        Job.run(reader, writer);
        
        // Show ingested data
        reader = new ParquetDataReader(new File("data/output/portfolio-incoming.parquet"));
        writer = StreamWriter.newSystemOutWriter();
        Job.run(reader, writer);
        
    }


}
