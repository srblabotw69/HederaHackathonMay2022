<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Home page</title>
  </head>
  <body>
 
<%--     <jsp:useBean id="hellocontract_id" class="HederaApp.HelloContractServlet"/>
 
	<h3>${hellocontract_id.webMain}</h3> --%>
  
  
<form action="http://localhost:8080/lib/HelloContractServlet" method="post">
         <input type="submit" name="myButton" value="button" />
          <p>Message: ${data}</p>
</form>
  </body>
</html>