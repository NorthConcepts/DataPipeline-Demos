<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.northconcepts.datapipeline.core.Record" %>
<%@ page import="com.northconcepts.datapipeline.core.RecordList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Events</title>
</head>
<body>


File Upload
<form action="" method="post" enctype="multipart/form-data">
    File: <input id="file1" type="file" name="file1" size="100" />
    <input type="submit" value="Upload"/>
</form>

<hr />
    
Export as <a href="/events2/events.csv">CSV</a> | <a href="/events2/events.excel">Excel</a>
<% RecordList recordList = (RecordList)request.getAttribute("recordList"); %>

<table cellpadding="4" cellspacing="0" border="1">
	<thead>
		<tr>
		<% for(String fieldName : recordList.get(0).getFieldNames()) { %>
			<td><%=fieldName%></td>
		<% } %>
		</tr>
	</thead>
	<tbody>
		<% for(int row=0; row<recordList.getRecordCount(); row++) { %>
		<tr>
			<% Record record = recordList.get(row);
			   for(int col=0; col<record.getFieldCount(); col++) {
			%>
				<td><%=record.getField(col).getValue()%></td>
			<% } %>
		</tr>
		<% } %>
	</tbody>
</table>

</body>
</html>