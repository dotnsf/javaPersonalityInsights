<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.LinkedHashMap" %>
<jsp:useBean id="resultText" scope="request" class="java.lang.String"/>
<jsp:useBean id="resultBig5" scope="request" class="java.lang.String"/>
<jsp:useBean id="resultNeeds" scope="request" class="java.lang.String"/>
<jsp:useBean id="resultValues" scope="request" class="java.lang.String"/>
<jsp:useBean id="message" scope="request" class="java.lang.String"/>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Personal Insights</title>
	<style type="text/css">
	<!--
		thead > tr > td {
			background-color: #BBBBBB;
			font-family: "Times New Roman";
			font-size: 12pt;
			font-weight: bold;
		}

		.parent > td {
			background-color: #BBBBBB;
			font-family: "Times New Roman";
			font-size: 12pt;
			font-weight: bold;
		}

		.child > td {
			background-color: #BBBBBB;
			font-family: "Times New Roman";
			font-size: 12pt;
			font-weight: bold;
		}

		.grandson > td {
			background-color: #FFFFFF;
			font-family: "Times New Roman";
			font-size: 10pt;
			font-weight: normal;
		}

		.greatgrandson > td {
			font-family: "Times New Roman";
			font-size: 10pt;
			font-weight: normal;
		}
	-->
	</style>
</head>
<body>
	<h2>Personal Insights（サンプル）</h2>
	<form action="./Insights" method="post">
		<textarea name="data" cols="120" rows="8"></textarea>
		<input type="submit" value="解析">
	</form>
	
	<hr/>
	<div>
		<%= message %>
	</div>
<% if( resultBig5 != null && resultBig5.length() > 0 ){ %>
	<hr/>
	<div>
		<h2>Big5</h2>
		<%= resultBig5 %>
	</div>
<% } %>
<% if( resultNeeds != null && resultNeeds.length() > 0 ){ %>
	<hr/>
	<div>
		<h2>Needs</h2>
		<%= resultNeeds %>
	</div>
<% } %>
<% if( resultValues != null && resultValues.length() > 0 ){ %>
	<hr/>
	<div>
		<h2>Values</h2>
		<%= resultValues %>
	</div>
<% } %>
	<hr/>
	<!-- 
	<%= resultText %>
	 -->
</body>
</html>
