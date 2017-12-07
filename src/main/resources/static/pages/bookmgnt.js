
var tblBooks;
$(function(){

    $('#menuLibraryMgnt').addClass('active');

    // Table setup
    // ------------------------------

    // Setting datatable defaults
    $.extend( $.fn.dataTable.defaults, {
        autoWidth: false,
        columnDefs: [{
            orderable: false,
            width: '100px',
            targets: [ 5 ]
        }],
        dom: '<"datatable-header"fl><"datatable-scroll"t><"datatable-footer"ip>',
        language: {
            search: '<span>Filter:</span> _INPUT_',
            searchPlaceholder: 'Type to filter...',
            lengthMenu: '<span>Show:</span> _MENU_',
            paginate: { 'first': 'First', 'last': 'Last', 'next': '&rarr;', 'previous': '&larr;' }
        },
        drawCallback: function () {
            $(this).find('tbody tr').slice(-3).find('.dropdown, .btn-group').addClass('dropup');
        },
        preDrawCallback: function() {
            $(this).find('tbody tr').slice(-3).find('.dropdown, .btn-group').removeClass('dropup');
        }
    });

    // Single row selection
    tblBooks = $('.datatable-books').DataTable({
        "ajax": {
            'url': '/api/book/all',
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
                    if(row.admin) {
                        return row.total + '&nbsp;&nbsp;<a href="#" data-bookid="' + row.id + '"><i class="icon-chevron-up addTotal"></i></a>' +
                            '<a href="#" data-bookid="' + row.id + '"><i class="icon-chevron-down minTotal"></i></a>';
                    }else{
                        return row.total;
                    }
                }
            },{
                targets:[3],
                render: function(data, type, row){
                    return row.available;
                }
            },{
                targets: [4],
                orderable: false,
                className: 'text-center',
                render: function(data, type, row){
                        return '<a data-bookid="' +
                            row.id + '"><i class="icon-books borrowBook"></i> </a>';
            }}
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

    // External table additions
    // ------------------------------

    // Enable Select2 select for the length option
    $('.dataTables_length select').select2({
        minimumResultsForSearch: Infinity,
        width: 'auto'
    });

    // Enable Select2 select for individual column searching
    $('.filter-select').select2();

    // Basic
    $('.select').select2();


    $('#btnNewBook').on('click', function(event){
        $('#modal_book_new').modal('show');
    });


    $("#modal_book_new").on("hidden.bs.modal", function () {
        // Clean all data and errors
        cleanNewBookFormError();
        $('#formNewBook')[0].reset();
    });

    // New student form submit
    $('#btnAddNewBook').on('click', function(event){
        event.preventDefault();
        addNewBook();
    });

});

/**
 * Setting up row actions here.
 * @param oSettings
 */
function settingRowActions(oSettings){

    $('.borrowBook').on('click', function(){

        // Show modal
        var aElement = $(this).closest('a');
        var bookId = aElement.attr('data-bookId');

        swal.queue([{
            title: 'Confirm your borrow',
            confirmButtonText: 'Yes, I Confirmed',
            text: 'Library system will handle your request and prompt the confirmation information ',
            showLoaderOnConfirm: true,
            preConfirm: ()=>{
                return new Promise((resolve) => {
                    return $.get("/book/borrow/"+ bookId).fail((data) => {
                        swal(
                            'Oops...',
                             data.responseJSON.errorList[0].defaultMessage,
                            'error'
                        );
                    }).done((data) => {
                        swal('Confirmed !!!');
                    }).always((data) => {tblBooks.ajax.reload(null,false);})
                })}
        }]);

    });

    $('.addTotal').on('click', function(){
        var data = new Object();
        var aElement = $(this).closest('a');
        var bookId = aElement.attr('data-bookId');

        data.bookid = bookId;
        data.increment = 1;

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/book/total/adjust',
            data: JSON.stringify(data),
            dataType: 'json',
            timeout: 60000,
            success: function(data){
                tblBooks.ajax.reload(null, false);
                swal({
                    type: 'success',
                    title: 'Book total adjusted',
                    showConfirmButton: false,
                    timer: 1000
                });
            },
            error: function(data){
                // Errors
                var errorList = data.responseJSON.errorList;
                _.each(errorList, function(err){

                    swal({
                        type: 'warning',
                        title: err.defaultMessage,
                        showConfirmButton: false,
                        timer: 1000
                    });
                });
            }
        });
    });

    $('.minTotal').on('click', function(){
        var data = new Object();
        var aElement = $(this).closest('a');
        var bookId = aElement.attr('data-bookId');

        data.bookid = bookId;
        data.increment = -1;

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/book/total/adjust',
            data: JSON.stringify(data),
            dataType: 'json',
            timeout: 60000,
            success: function(data){
                tblBooks.ajax.reload(null, false);
                swal({
                    type: 'success',
                    title: 'Book total adjusted',
                    showConfirmButton: false,
                    timer: 1000
                });
            },
            error: function(data){
                // Errors
                var errorList = data.responseJSON.errorList;
                _.each(errorList, function(err){

                    swal({
                        type: 'warning',
                        title: err.defaultMessage,
                        showConfirmButton: false,
                        timer: 1000
                    });
                });
            }
        });
    });

}

/**
 * AJAX submit new book
 */
function addNewBook(){
    var data = new Object();
    data.isbn = $('#bookISBN').val().split('-').join('');
    data.bookTotal = $('#bookTotal').val();

    // Clean error before request
    cleanNewBookFormError();
    // $('#formStudentErrorBar').fadeOut('slow');

    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/book/new',
        data: JSON.stringify(data),
        dataType: 'json',
        timeout: 60000,
        success: function(data){
            // Close modal
            $('#modal_book_new').modal('toggle');

            // Show message
            $('#bookMsgBarSuccessMsg').html('New book been added to library successfully !');
            $('#bookMsgBarSuccess').fadeIn('slow').delay(8000).fadeOut('slow');

            // reload ajax table
            tblBooks.ajax.reload(null, false);

            // Reset data
            // TODO move to event.
            $('#formNewBook')[0].reset();
        },
        error: function(data){
            // Errors
            var errorList = data.responseJSON.errorList;
            _.each(errorList, function(err){

                if(_.isEqual(err.field, "emptybook")){
                    $('#formBookNewErrorBarMsg').html(err.defaultMessage);
                    $('#formBookNewErrorBar').fadeIn('slow');
                }
                if(_.isEqual(err.field, "duplicated")){
                    $('#formBookNewErrorBarMsg').html(err.defaultMessage);
                    $('#formBookNewErrorBar').fadeIn('slow');
                }
            });
        }
    })
}

/**
 * Clear error message
 */
function cleanNewBookFormError(){

    $('#formBookNewErrorBar').fadeOut('slow');

}