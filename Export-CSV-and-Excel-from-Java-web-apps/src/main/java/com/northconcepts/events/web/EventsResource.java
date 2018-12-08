package com.northconcepts.events.web;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.excel.ExcelDocument;
import com.northconcepts.datapipeline.excel.ExcelWriter;
import com.northconcepts.datapipeline.jdbc.JdbcReader;
import com.northconcepts.datapipeline.job.JobTemplate;
import com.northconcepts.datapipeline.memory.MemoryWriter;
import com.northconcepts.datapipeline.transform.BasicFieldTransformer;
import com.northconcepts.datapipeline.transform.CopyField;
import com.northconcepts.datapipeline.transform.SetCalculatedField;
import com.northconcepts.datapipeline.transform.TransformingReader;

@Path("/")
public class EventsResource {
    
    private static final DB db = new DB();

    static {
        // Create EVENT table
        db.executeUpdate("CREATE TABLE EVENT (" + 
        		"    EVENT_ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) not null, " + 
        		"    EVENT_TYPE VARCHAR(20)," + 
        		"    TITLE VARCHAR(200)," + 
        		"    SUMMARY LONGVARCHAR," + 
        		"    LOCATION VARCHAR(8000)," + 
                "    START_TIME DATETIME," + 
        		"    DURATION_MINUTES NUMERIC(6,0)" + 
        		")");
        
        // Populate EVENT table with data
        for (int i = 0; i < 10; i++) {
            db.executeUpdate("INSERT INTO EVENT(EVENT_TYPE, TITLE, SUMMARY, LOCATION, START_TIME, DURATION_MINUTES) " + 
            		"  VALUES('Business', 'Title "+i+"', 'Summary "+i+"', 'Location "+i+"', '2012-12-03 8:45:00', 135)");
        }
    }
    
    private DataReader getEvents() {
        DataReader reader = new JdbcReader(db.getConnection(), "SELECT * FROM EVENT");
        
        reader = new TransformingReader(reader)
            .add(new CopyField("DURATION_MINUTES", "DURATION", true))
            .add(new BasicFieldTransformer("DURATION").numberToMinutes())
            ;
        
        reader = new TransformingReader(reader)
            .add(new SetCalculatedField("END_TIME", "START_TIME + DURATION"))
            ;
    
        reader = new TransformingReader(reader)
            .add(new CopyField("START_TIME", "START", true))
            .add(new BasicFieldTransformer("START").dateTimeToString("EE MMM d, yyyy 'at' h:mm a"))
            ;
        
        reader = new TransformingReader(reader)
            .add(new CopyField("END_TIME", "END", true))
            .add(new BasicFieldTransformer("END").dateTimeToString("h:mm a"))
            ;
    
        return reader;
    }

    @GET
    @Path("/")
    public Response getHome() throws Throwable {
        // Redirect "/" to "/events"
        return Response.seeOther(new URI("/events")).build();
    }

    @GET
    @Path("/events")
    public void getEventsAsHtml(
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws Throwable {
        
        DataReader reader = getEvents();
        MemoryWriter writer = new  MemoryWriter();
        JobTemplate.DEFAULT.transfer(reader, writer);
       
        RecordList recordList = writer.getRecordList();
        request.setAttribute("recordList", recordList);
        
        // Workaround for https://issues.jboss.org/browse/RESTEASY-903
        request = ResteasyProviderFactory.getContextData(HttpServletRequest.class);  
        response = ResteasyProviderFactory.getContextData(HttpServletResponse.class);
        
        request.getRequestDispatcher("/WEB-INF/jsp/events.jsp").forward(request, response);
    }

    @GET
    @Path("/events.excel")
    public Response getEventsAsExcel(@Context HttpServletResponse response) throws Throwable {
        DataReader reader = getEvents();

        ExcelDocument excelDocument = new ExcelDocument();
        DataWriter writer = new ExcelWriter(excelDocument).setFieldNamesInFirstRow(true);
        
        JobTemplate.DEFAULT.transfer(reader, writer);
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"events.xlsx\"");
        OutputStream outputStream = response.getOutputStream();
        excelDocument.save(outputStream);

        return Response.ok().build();
    }

    @GET
    @Path("/events.csv")
    public Response getEventsAsCsv(@Context HttpServletResponse response) throws Throwable {
        DataReader reader = getEvents();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"events.csv\"");
        PrintWriter printWriter = response.getWriter();
        DataWriter writer = new CSVWriter(printWriter).setFieldNamesInFirstRow(true);
        
        JobTemplate.DEFAULT.transfer(reader, writer);
        
        return Response.ok().build();
    }
    
}
