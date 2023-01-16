package com.northconcepts.datapipeline.demo.dataingestion;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.foundations.schema.EntityDef;
import com.northconcepts.datapipeline.foundations.schema.NumericFieldDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaDef;
import com.northconcepts.datapipeline.foundations.schema.SchemaTransformer;
import com.northconcepts.datapipeline.foundations.schema.TemporalFieldDef;
import com.northconcepts.datapipeline.foundations.schema.TextFieldDef;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.parquet.ParquetDataReader;
import com.northconcepts.datapipeline.parquet.ParquetDataWriter;
import com.northconcepts.datapipeline.transform.TransformingReader;

public class SimplePipeline_WithSchema {

    public static void main(String[] args) {
        
        SchemaDef schema = new SchemaDef("ingestion")
                .addEntity(new EntityDef("portfolio")
                        .addField(new TemporalFieldDef("date", FieldType.DATETIME).setPattern("MM/dd/yyyy").setRequired(true))
                        .addField(new TextFieldDef("symbol", FieldType.STRING))
                        .addField(new NumericFieldDef("shares", FieldType.BIG_DECIMAL).setMinimum(0))
                        .addField(new NumericFieldDef("share_price", FieldType.BIG_DECIMAL))
                        .addField(new NumericFieldDef("acb_per_share", FieldType.BIG_DECIMAL))
                        );
        
        System.out.println(schema.toXml());

        /*    
        <schema name="ingestion">
          <entities>
            <entity addMissingOptionalFields="false" allowExtraFieldsInMapping="true" allowExtraFieldsInValidation="true" name="portfolio">
              <fields>
                <field array="false" lenientPattern="true" name="date" pattern="MM/dd/yyyy" required="true" strictArrays="true" type="DATETIME"/>
                <field allowBlank="true" array="false" name="symbol" required="false" strictArrays="true" type="STRING"/>
                <field array="false" minimum="0" name="shares" required="false" strictArrays="true" type="BIG_DECIMAL"/>
                <field array="false" name="share_price" required="false" strictArrays="true" type="BIG_DECIMAL"/>
                <field array="false" name="acb_per_share" required="false" strictArrays="true" type="BIG_DECIMAL"/>
              </fields>
            </entity>
          </entities>
        </schema>
        */    

        
        // Ingest data
        DataReader reader = new CSVReader(new File("data/input/portfolio.csv")).setFieldNamesInFirstRow(true).setAllowMultiLineText(true);
        reader = new TransformingReader(reader)
                .add(new SchemaTransformer(schema.getEntity("portfolio")));
        DataWriter writer = new ParquetDataWriter(new File("data/output/portfolio-incoming.parquet"));
        Job.run(reader, writer);
        
        // Show ingested data
        reader = new ParquetDataReader(new File("data/output/portfolio-incoming.parquet"));
        writer = StreamWriter.newSystemOutWriter();
        Job.run(reader, writer);
        
    }


}
