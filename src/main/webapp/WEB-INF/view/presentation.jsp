<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="row">
	<c:forEach items="${slides}" var="slide">
		<p>${slide.index}</p>
	</c:forEach>
</div>