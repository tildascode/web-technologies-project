<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head> 
    <link href="/css/presentation.css" rel="stylesheet" type="text/css">
    <title>Home</title>
</head>
<body>
<%@include file='header.jsp' %>

    <h1>All Presentations</h1>
<c:choose>
    <c:when test="${not empty presentations}">
        <c:forEach items="${presentations}" var="presentation">
            <div>
                    ${presentation.name}
            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        You still don't have any presentations!
    </c:otherwise>
</c:choose>

</body>
</html>