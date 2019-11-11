$(document).ready(function () {
    $('.questions-progress').animate({
        scrollLeft: $('.active').position().left - ($('.questions-progress').get(0).clientWidth / 2)
    }, 1);

    $('.answer-button').click(function () {
        var id = this.id;
        $('#answer').val(id);
        $('#answer-form').submit();
    });
});