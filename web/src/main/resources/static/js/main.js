$(document).ready(function () {
    $('.questions-progress').animate({
        scrollLeft: $('.active').position().left - ($('.questions-progress').get(0).clientWidth / 2)
    }, 1);

    $('.answer-button').click(function () {
        var id = this.id;
        if($('#answer').is(':disabled')){
            alertify.error('Нельзя изменять свой ответ');
        }
        else {
            $('#answer').val(id);
            $('#answer-form').submit();
        }
    });
});