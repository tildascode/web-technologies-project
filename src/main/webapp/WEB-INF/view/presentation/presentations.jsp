<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="col-xl-8 col-md-8">
    <div class="section_title mb-33">
        <h3>${param.heading}</h3>
    </div>
    <%@include file="paging.jsp" %>
    <div class="row">
        <c:forEach items="${presentations}" var="presentation">
            <div class="single_blog">
                <div class="blog_thumb">
                    <a href="#">
                        <img src="/img/most_recent/2.jpg" alt="">
                    </a>
                </div>
                <div class="blog_meta">
                    <p><a href="slides?presentationID=${presentation.id}">${presentation.name}</a></p>
                    <h3><a href="slides?presentationID=${presentation.id}">${presentation.name}</a></h3>
                </div>
            </div>
        </c:forEach>
    </div>
    <jsp:include page="paging.jsp"/>
</div>