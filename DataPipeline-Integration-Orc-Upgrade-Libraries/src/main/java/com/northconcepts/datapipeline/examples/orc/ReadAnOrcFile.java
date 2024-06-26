package com.northconcepts.datapipeline.examples.orc;

import java.io.File;

import com.northconcepts.datapipeline.core.StreamWriter;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.orc.OrcDataReader;

public class ReadAnOrcFile {

    public static void main(String[] args) {
        OrcDataReader reader = new OrcDataReader(new File("example/data/input/input_orc_file.orc"));
        Job.run(reader, new StreamWriter(System.out));

        System.out.println("============================================================");
        System.out.println("ORC Schema");
        System.out.println("============================================================");
        
        System.out.println(Util.formatJson(reader.getSchema().toJson()));
    }

}
