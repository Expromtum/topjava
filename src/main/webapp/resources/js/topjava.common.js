var context, form;

function makeEditable(ctx) {
    context = ctx;
    form = $('#detailsForm');
    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});
}

function add() {
    $("#modalTitle").html(i18n["addTitle"]);

    form.find(":input").val("");

    // Init DefaultValue if exists
    form.find(":input").filter("[defaultValue!=''][defaultValue]").each(function(i,elem) {
        $(elem).val($(elem).attr("defaultValue"));
    });

    initDateTimePicker();

    $("#editRow").modal();
}

function updateRow(id) {
    $("#modalTitle").html(i18n["editTitle"]);

    $.get(context.ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            form.find("input[name='" + key + "']").val(value);
        });

        initDateTimePicker();

        $('#editRow').modal();
    });
}

function deleteRow(id) {
    if (confirm(i18n['common.confirm'])) {
        $.ajax({
            url: context.ajaxUrl + id,
            type: "DELETE"
        }).done(function () {
            context.updateTable();
            successNoty("common.deleted");
        });
    }
}

function initDateTimePicker() {
    form.find(":input")
        .filter("[type='data-datetime']")
        .each(function(i,elem) {
            $(elem).datetimepicker({
                format: 'yy-m-d h:m'
            });

            if ($(elem).val()) {
                let dt = $(elem).val().slice(0, 16).replace('T', ' ');
                $(elem).val(dt);
            } else {
                $(elem).val("");
            }
    });
}

function updateTableByData(data) {
    context.datatableApi.clear().rows.add(data).draw();
}

function getSerializedFormData() {
    var formData = form.serializeArray();
    var serializedData = {};

    $.each(formData,
        function(i, pair) {
            if ( pair.name === "dateTime" ) {
                //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
                let dt = moment(pair.value, "YYYY-MM-DD HH.mm");
                if (dt.isValid()) {
                    serializedData[pair.name] = dt.toISOString().substring(0, 19)
                } else {
                    serializedData[pair.name] = pair.value;
                }
            } else {
                serializedData[pair.name] = pair.value;
            }
        });
    return serializedData;
}

function save() {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: getSerializedFormData()
    }).done(function () {
        $("#editRow").modal("hide");
        context.updateTable();
        successNoty("common.saved");
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(key) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + i18n[key],
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + i18n["common.errorStatus"] + ": " + jqXHR.status + (jqXHR.responseJSON ? "<br>" + jqXHR.responseJSON : ""),
        type: "error",
        layout: "bottomRight"
    }).show();
}

function renderEditBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='updateRow(" + row.id + ");'><span class='fa fa-pencil'></span></a>";
    }
}

function renderDeleteBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='deleteRow(" + row.id + ");'><span class='fa fa-remove'></span></a>";
    }
}