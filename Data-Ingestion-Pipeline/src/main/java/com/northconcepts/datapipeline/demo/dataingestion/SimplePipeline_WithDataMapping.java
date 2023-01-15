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
        
        // Ingest data
        DataReader reader = new CSVReader(new File("data/input/portfolio.csv")).setFieldNamesInFirstRow(true).setAllowMultiLineText(true);
        reader = new DataMappingReader(reader, dataMapping);
        DataWriter writer = new ParquetDataWriter(new File("data/output/portfolio-incoming-2023-01-16.parquet"));
        Job.run(reader, writer);
        
        // Show ingested data
        reader = new ParquetDataReader(new File("data/output/portfolio-incoming-2023-01-16.parquet"));
        writer = StreamWriter.newSystemOutWriter();
        Job.run(reader, writer);
        
    }

/*    
    0:[date]:STRING=[11/4/2016]:String
    1:[type]:STRING=[SELL_LOT]:String
    2:[symbol]:STRING=[AAPL]:String
    3:[shares]:STRING=[60]:String
    4:[share_price]:STRING=[109.1827]:String
    5:[costs]:STRING=[0]:String
    6:[fees]:STRING=[5.15]:String
    7:[total_amount]:STRING=[6545.81]:String
    8:[div_amount]:STRING=[0]:String
    9:[shares_affected]:STRING=[0]:String
    10:[currency]:STRING=[USD]:String
    11:[rate]:STRING=[1]:String
    12:[cash_affected]:STRING=[true]:String
    13:[name]:STRING=[APPLE INC COM]:String
    14:[comment]:STRING=[APPLE INC]:String
    15:[brokerage_id]:STRING=[null]
    16:[taxes]:STRING=[0]:String
    17:[credits]:STRING=[0]:String
    18:[rate_currency]:STRING=[USD]:String
    19:[acb_per_share]:STRING=[97.39466465]:String
    20:[uuid]:STRING=[b5ee1d39-6837-45a9-9dea-8f4484226464]:String
    21:[linked_uuid]:STRING=[5cdbc138-c762-47b1-b7be-5618282e007a]:String
    22:[use_rate_ccy]:STRING=[false]:String
    23:[provider]:STRING=[YAHOO]:String
*/    

}
