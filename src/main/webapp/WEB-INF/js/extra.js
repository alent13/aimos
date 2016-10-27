$(document).ready(function(){
    $("#login_modal_btn").click(function(){
        $("#login_modal").modal();
    });

    $("#menu-toggle").click(function (e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });

    $("#reg-modal-btn").click(function(){
        $("#registration_modal").modal();
    });

    $("#showpsw").mousedown(function(){
        var element = $('#password');
        element.replaceWith(element.clone().attr('type',(element.attr('type') == 'password') ? 'text' : 'password'));
    }).mouseup(function () {
        var element = $('#password');
        element.replaceWith(element.clone().attr('type',(element.attr('type') == 'password') ? 'text' : 'password'));
    });
});