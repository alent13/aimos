$(document).ready(function(){

    $("#showpsw").mousedown(function(){
        var element = $('#password');
        element.replaceWith(element.clone().attr('type',(element.attr('type') == 'password') ? 'text' : 'password'));
    }).mouseup(function () {
        var element = $('#password');
        element.replaceWith(element.clone().attr('type',(element.attr('type') == 'password') ? 'text' : 'password'));
    });

});