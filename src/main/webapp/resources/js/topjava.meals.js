$(function () {
    makeEditable({
            ajaxUrl: "ajax/profile/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "order": [[0, "desc"]],
                "scrollY": 200,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    }, {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
            })
        }
    );
});

function updateTable() {
    var formFilter = $('#formFilter');

    $.ajax({
        type: "POST",
        url: context.ajaxUrl + "filter/",
        data: formFilter.serialize(),
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

// $('#formFilter').on( "submit", function( event ) {
//     event.preventDefault();
//
//     updateTable();
// });

$('#formFilter').submit(function( event ) {
    event.preventDefault();

    updateTable();
});

