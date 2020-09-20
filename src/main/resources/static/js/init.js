

$(document).ready(function () {
    $('#dt').DataTable();
    $('.dataTables_length').addClass('bs-select');
});

if (navigator.language == 'pl') {
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
        format: 'd mmmm yyyy',
        formatSubmit: 'yyyy/mm/dd'
    });
} else {
    $('.datepicker').datepicker({
        min: 0,
        max: false,
        disable: [6, 7]
    });
}

$(document).ready(function () {
    $('.stepper').mdbStepper();
})

$(document).ready(function() {
    $('.mdb-select').materialSelect();
});
