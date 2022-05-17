<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%-- <form action="${pageContext.request.contextPath}/helloservlet" method="post"> --%>
<form action="http://localhost:8080/lib/helloservlet" method="post">
         <input type="submit" name="myButton" value="button" />
          <p>Message: ${data}</p>
</form>
</body>
</html>