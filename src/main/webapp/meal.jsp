<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%--
  Created by IntelliJ IDEA.
  User: Marina Sokurenko
  Date: 09.06.2019
  Time: 15:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<jsp:useBean id="meal" class="ru.javawebinar.topjava.model.Meal" scope="request"/>

<head>
    <link rel="stylesheet" href="css/control_style.css" type="text/css"/>
    <title>Meal</title>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<form action="meals" method="post">
    <table class="onerec-table">
        <tr>
            <td><label>Дата:</label></td>
            <td><input type="datetime-local" name="dateTime" value="${meal.dateTime}" required></td>
        </tr>
        <tr>
            <td><label>Описание:</label></td>
            <td><input type="text" name="description" value="${meal.description}" autocomplete="off" required></td>
        </tr>
        <tr>
            <td><label>Калорийность:</label></td>
            <td><input type="number" name="calories" value="${meal.calories}" autocomplete="off" required></td>
        </tr>
        <tr>
            <td><input type="submit" value="Сохранить"></td>
            <td><button onclick="window.history.back()">Отменить</button></td>
        </tr>
        <input type="hidden" name="id" value="${meal.id}">
    </table>
</form>
</body>
</html>
