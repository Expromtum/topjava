<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Marina Sokurenko
  Date: 09.06.2019
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<%
    List<MealTo> list = (List<MealTo>) request.getAttribute("list");
%>

<head>
    <link rel="stylesheet" href="css/control_style.css" type="text/css"/>
    <title>Meals</title>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<a class="buttons" href="meals?action=create">Добавить</a>
<table class="reestr-table">
    <caption><h2>ЕДА</h2></caption>
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калорийность</th>
        <th></th>
    </tr>
    <c:forEach items="${list}" var="meal">
        <jsp:useBean id="meal1" class="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="<c:out value="${meal.excess ? 'highlight_red' : 'highlight_green'}"/>">
            <fmt:parseDate value="${meal.dateTime}" pattern="y-M-dd'T'H:m" var="myParseDate"></fmt:parseDate>
            <td class="column-align-center"><fmt:formatDate value="${myParseDate}"
                                                            pattern="yyyy.MM.dd HH:mm"></fmt:formatDate></td>

            <td>${meal.description}</td>
            <td class="column-align-right"><c:out value="${meal.calories}"/></td>

            <td class="column-align-center">
                <a class="buttons" href="meals?action=update&id=<c:out value="${meal.id}"/>">Изменить</a>
                <a class="buttons" href="meals?action=delete&id=<c:out value="${meal.id}"/>">Удалить</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
