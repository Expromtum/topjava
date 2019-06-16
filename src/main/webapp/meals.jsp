<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <link rel="stylesheet" href="css/control_style.css" type="text/css"/>

<%--    <script type="text/javascript" src="js/filter.js"></script>--%>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <label for="userId">Пользователь</label>
    <select name="userId" id="userId" onChange="window.location.href='meals?action=SelectUser&userId='+this.value">
        <option value="1">Пользователь 1</option>
        <option  value="2">Пользователь 2</option>
    </select>
    <%
        int userId = (Integer) request.getAttribute("userId");
    %>
    <script type="text/javascript">
        var fieldUser = document.getElementById("userId");
        fieldUser.value = "${userId}";
    </script>
    <hr/>
    <h2>Meals</h2>
    <form id="filter" method="GET" action="meals">
        <input type="hidden">
        <table cellpadding="8" cellspacing="0">
            <tr>
                <th><label for="startDate">От даты</label></th>
                <th><label for="endDate">До даты</label></th>
                <th><label for="startTime">От времени</label></th>
                <th><label for="endTime">До времени</label></th>
            </tr>
            <tr>
                <td><input type="date" id="startDate" name="startDate" value="${param.startDate}"></td>
                <td><input type="date" id="endDate" name="endDate" value="${param.endDate}"></td>
                <td><input type="time" id="startTime" name="startTime" value="${param.startTime}"></td>
                <td><input type="time" id="endTime" name="endTime" value="${param.endTime}"></td>
            </tr>
            <br>
            <tr>
                <td></td>
                <td></td>
                <td><button type="reset" class="buttons" onclick="window.location.href='meals'">Отменить</button></td>
                <td><button type="submit" class="buttons">Фильтровать</button></td>
            </tr>
        </table>
    </form>
    <br>
    <a class="buttons" href="meals?action=create">Add Meal</a>
    <br>
    <table class="reestr-table">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td class="column-align-center">
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td class="column-align-right">${meal.calories}</td>
                <td class="column-align-center"><a class="buttons" href="meals?action=update&id=${meal.id}">Update</a>
                </td>
                <td class="column-align-center"><a class="buttons" href="meals?action=delete&id=${meal.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>