
var tblAlerts;
$(function(){

    $('#menu_alter').addClass('active');

    $('#btnNewAlert').on('click', function(event){
        event.preventDefault();
        $('#modal_alert_category').modal();
    });

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

    tblAlerts = $('.datatable-alerts').DataTable({

        language: {
            'emptyTable': "There is no alerts found in system.",
            'info': 'Showing _START_ to _END_ of _TOTAL_ alerts',
            'infoEmpty': "No alerts found"
        },
        ajax: {
            dataSrc: '',
            type: 'POST',
            url: '/api/public/alerts',
            timeout: 60000
        },
        columnDefs: [
            {
                targets: [0],
                render: function(data, type, row){
                    return row.title;
                }
            },{
                targets: [1],
                render: function(data, type, row){
                    return row.description;
                }
            },{
                targets: [2],
                render: function(data, type, row){
                    text = '';
                    if (row.deliveryEmail){
                        text += 'E';
                    }
                    if(row.deliveryText){
                        text += ' T';
                    }

                    return text;
                }
            },{
                targets: [3],
                render: function(data, type, row){
                    return '3/day';
                }
            },{
                targets: [4],
                render: function(data, type, row){
                    return '';
                }
            }
        ]
    });

    // Enable Select2 select for the length option
    $('.dataTables_length select').select2({
        minimumResultsForSearch: Infinity,
        width: 'auto'
    });

    // Enable Select2 select for individual column searching
    $('.filter-select').select2();

    // Basic
    $('.select').select2();

    // Multiple
    $('.select-students').select2({
        containerCssClass: 'bg-indigo-400',
        dropdownCssClass: 'bg-indigo-400'
    });

    // Default initialization
    $(".styled, .multiselect-container input").uniform({
        radioClass: 'choice'
    });


    $('#btnAlertCategorySelection').on('click', function(event){
        event.preventDefault();
        var cat = $('#alertCategory').val();
        var catName = $('#alertCategory option:selected').text();

        // Based on selection popup different modal
        // Hide the catelogy select modal
        $('#modal_alert_category').modal('hide');

        switch(catName){
            case 'Grade':
                $('#modal_alert_new_grade').modal('show');
                break;
            case 'Schedule':

                break;
            case 'Other':

                break;

            default:
                console.log('Wrong option');
        }

    });

    /**
     * New grade alert button clicked event.
     */
    $('#btnNewAlertGrade').on('click', function(event){
        event.preventDefault();

        newGradeAlert();
    });

    $('#modal_alert_new_grade').on('hidden.bs.modal', function(){

        cleanNewGradeAlertFormError();
        $('#formNewAlertGrade')[0].reset();
        $('#formNewAlertGrade #form_new_alert_grade_delivery_email').change();
        $('#formNewAlertGrade #form_new_alert_grade_delivery_text').change();
    });

});

/**
 * Create new grade alert
 */
function newGradeAlert(){

    var data = new Object();
    data.title = $('#formNewAlertGrade #name_new_grade').val();
    data.condition = $('#formNewAlertGrade #condition_new_grade').val();
    data.threadhold = $('#formNewAlertGrade #threadhold_new_grade').val();
    data.students = $('#formNewAlertGrade #participants_new_grade').val();
    data.deliveryEmail = $('#formNewAlertGrade #form_new_alert_grade_delivery_email').prop('checked');
    data.deliveryText = $('#formNewAlertGrade #form_new_alert_grade_delivery_text').prop('checked');
    data.description = $('#formNewAlertGrade #description_new_grade').val();


    cleanNewGradeAlertFormError();

    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: '/api/parent/alert/new',
        data: JSON.stringify(data),
        dataType: 'json',
        timeout: 60000,
        success: function(data) {

            // Close dialog
            $('#modal_alert_new_grade').modal('toggle');
            tblAlerts.ajax.reload(null, false);

            // Reset form
            $('#formNewAlertGrade')[0].reset();
            $('#formNewAlertGrade #form_new_alert_grade_delivery_email').change();
            $('#formNewAlertGrade #form_new_alert_grade_delivery_text').change();
        },
        error: function(data){
            var errorList = data.responseJSON.errorList;

            _.each(errorList, function(err){
                if(_.isEqual(err.field, "title")){
                    // Title error
                    $('#formNewAlertGrade #name_new_grade').closest('div').addClass('has-error');
                    $('#formNewAlertGrade #name_new_grade').closest('div').append('<span class="help-block">' + err.defaultMessage + '</span>');
                }
            });
        }
    });
}

/**
 * Clean form error (New Grade Alert Form)
 */
function cleanNewGradeAlertFormError(){
    $('#formNewAlertGrade #name_new_grade').closest('div').removeClass('has-error');
    $('#formNewAlertGrade #name_new_grade').closest('div').find('span').filter('.help-block').remove();
}