var tblHistory;
$(function() {

    $('#menuHistory').addClass('active');

    // Table setup
    // ------------------------------

    // Setting datatable defaults
    $.extend($.fn.dataTable.defaults, {
        autoWidth: false,
        columnDefs: [{
            orderable: false,
            width: '100px',
            targets: [5]
        }],
        dom: '<"datatable-header"fl><"datatable-scroll"t><"datatable-footer"ip>',
        language: {
            search: '<span>Filter:</span> _INPUT_',
            searchPlaceholder: 'Type to filter...',
            lengthMenu: '<span>Show:</span> _MENU_',
            paginate: {'first': 'First', 'last': 'Last', 'next': '&rarr;', 'previous': '&larr;'}
        },
        drawCallback: function () {
            $(this).find('tbody tr').slice(-3).find('.dropdown, .btn-group').addClass('dropup');
        },
        preDrawCallback: function () {
            $(this).find('tbody tr').slice(-3).find('.dropdown, .btn-group').removeClass('dropup');
        }
    });

    // Single row selection
    tblHistory = $('.datatable-history').DataTable({
        "ajax": {
            'url': '/api/book/borrow/history',
            'type': 'POST',
            'dataSrc': ''
        },
        columnDefs: [
            {
                targets: [0],
                render: function(data, type, row){
                    return row.name;
                }
            },{
                targets: [1],
                render: function(data, type, row){
                    return row.isbn;
                }
            },{
                targets: [2],
                render: function(data, type, row){
                    return new Date(row.borrowDate).toLocaleDateString();
                }
            },{
                targets: [3],
                orderable: false,
                className: 'text-center',
                render: function(data, type, row){
                    if(!row.returned) {
                        return '<a data-historyid="' +
                            row.historyId + '"><i class="icon-database-arrow returnBook"></i> </a>';
                    }else{
                        return '';
                    }
                }
            }
        ],

        fnDrawCallback: function(oSettings){
            settingRowActions(oSettings);
        }
    });
    $('.datatable-students tbody').on('click', 'tr', function() {
        if ($(this).hasClass('success')) {
            $(this).removeClass('success');
        }
        else {
            tblStudents.$('tr.success').removeClass('success');
            $(this).addClass('success');
        }
    });

});

function settingRowActions(oSettings) {

    $('.returnBook').on('click', function(){

        // Show modal
        var aElement = $(this).closest('a');
        var historyId = aElement.attr('data-historyid');

        swal.queue([{
            title: 'Return book?',
            confirmButtonText: 'Yes, Return book',
            text: 'Are you sure you going to return the book ',
            showLoaderOnConfirm: true,
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            preConfirm: ()=>{
            return new Promise((resolve) => {
                return $.get("/book/return/"+ historyId).then((data) => {
                    swal('Returned !!!');
                    tblHistory.ajax.reload(null, false);
                })
            })}
        }]);

    });
}