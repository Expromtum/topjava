<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Marina Sokurenko
  Date: 09.06.2019
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<%
    List<MealTo> meals = (List<MealTo>) request.getAttribute("meals");
%>

<head>
    <link rel="stylesheet" href="css/table_style.css" type="text/css"/>
    <title>Meals</title>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<table>
    <caption><h2>ЕДА</h2></caption>
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калорийность</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr
                <c:choose>
                    <c:when test="${meal.excess}">
                        class="highlight_red"
                    </c:when>
                    <c:otherwise>
                        class="highlight_green"
                    </c:otherwise>
                </c:choose>
        >
            <fmt:parseDate value="${meal.dateTime}" pattern="y-M-dd'T'H:m" var="myParseDate"></fmt:parseDate>
            <td><fmt:formatDate value="${myParseDate}" pattern="yyyy.MM.dd HH:mm"></fmt:formatDate></td>

            <td>${meal.description}</td>
            <td><c:out value="${meal.calories}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
