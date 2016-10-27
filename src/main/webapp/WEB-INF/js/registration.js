var v_complexity = 0;

$(function () {
    $('#password').complexify({}, function (valid, complexity) {
        var progressBar = $('#strength-progress');

        progressBar.toggleClass('progress-bar-success', valid);
        progressBar.toggleClass('progress-bar-danger', !valid);
        progressBar.css({'width': complexity + '%'});

        progressBar.text(Math.round(complexity) + '%');

        v_complexity = complexity;
        if(complexity > 30) {
            $("#password").removeClass("has-error");
        }
    });
});