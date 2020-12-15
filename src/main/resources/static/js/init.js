$(document).ready(function () {
    $('#dt').DataTable();
    $('.dataTables_length').addClass('bs-select');
});

if (navigator.language === 'pl') {
    $('.datepicker').datepicker({
        min: 0,
        max: false,
        disable: [6,7],
        monthsFull: [ 'styczeń', 'luty', 'marzec', 'kwiecień', 'maj', 'czerwiec', 'lipiec', 'sierpień', 'wrzesień', 'październik', 'listopad', 'grudzień' ],
        monthsShort: [ 'sty', 'lut', 'mar', 'kwi', 'maj', 'cze', 'lip', 'sie', 'wrz', 'paź', 'lis', 'gru' ],
        weekdaysFull: [ 'niedziela', 'poniedziałek', 'wtorek', 'środa', 'czwartek', 'piątek', 'sobota' ],
        weekdaysShort: [ 'niedz.', 'pn.', 'wt.', 'śr.', 'cz.', 'pt.', 'sob.' ],
        today: 'Dzisiaj',
        clear: 'Usuń',
        close: 'Zamknij',
        firstDay: 1,
        format: 'yyyy-mm-dd'
    });
} else {
    $('.datepicker').datepicker({
        firstDay: 1,
        min: 0,
        max: false,
        disable: [6, 7],
        format: 'yyyy-mm-dd'
    });
}

$(document).ready(function () {
    $('.stepper').mdbStepper();
})

$(document).ready(function() {
    $('.mdb-select').materialSelect();
});

$(document).ready(function () {
    $('#dtMaterialDesignExample').DataTable();
    $('#dtMaterialDesignExample_wrapper').find('label').each(function () {
        $(this).parent().append($(this).children());
    });
    $('#dtMaterialDesignExample_wrapper .dataTables_filter').find('input').each(function () {
        const $this = $(this);
        $this.attr("placeholder", "Search");
        $this.removeClass('form-control-sm');
    });
    $('#dtMaterialDesignExample_wrapper .dataTables_length').addClass('d-flex flex-row');
    $('#dtMaterialDesignExample_wrapper .dataTables_filter').addClass('md-form');
    $('#dtMaterialDesignExample_wrapper select').removeClass(
        'custom-select custom-select-sm form-control form-control-sm');
    $('#dtMaterialDesignExample_wrapper select').addClass('mdb-select');
    $('#dtMaterialDesignExample_wrapper .mdb-select').materialSelect();
    $('#dtMaterialDesignExample_wrapper .dataTables_filter').find('label').remove();
});
