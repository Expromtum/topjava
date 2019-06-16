function clearFilter() {

    alert("clear_filter");
    document.getElementById("startDate").value = "";
    document.getElementById("endDate").value = "";
    document.getElementById("startTime").value = "";
    document.getElementById("endTime").value = "";

    filter();
}
var formLevelDiv = document.getElementById(href);
// $.get(mealAjaxUrl, updateTableByData);

function filter() {
    alert("filter");
    $.ajax({
        type: "GET",
        url: "meals?action=all",
        data: $("#filter").serialize()
    });
    //.done(updateTableByData);
}