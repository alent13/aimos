$(document).ready(function () {
    $("#contacts").on('change keyup paste', function () {
        var login = $("#contacts").val();
        var formData = new FormData();
        formData.append('login', login);
        if(login.length > 0) {
            $.ajax({
                type: "POST",
                url: "searchContact",
                data: ({login: login}),
                success: function (contacts) {
                    var search_contact = $("#search_contact");
                    search_contact.empty();

                    for(var i = 0; i < contacts.length; i++){
                        var contact = contacts[i];
                        var login = contact.login;
                        var name = contact.name;
                        var surname = contact.surname;

                        code = '<div style="padding: 5px" class="media">' +
                            '<div class="media-body">'+
                            '<h4 class="media-heading">'+
                            name + ' ' + surname +
                            '</h4>'+
                            '<p>' + '@' + login + '</p>'+
                            '</div>'+
                            '<div class="media-right">'+
                            '<a href="#"><span class="glyphicon glyphicon-envelope"></span></a>'+
                            '</div>'+
                            '<div class="media-right">'+
                            '<a hint="Добавить в контакты" href="/addToFriendList/' + login + '"><span class="glyphicon glyphicon-plus"></span></a>'+
                            '</div>'+
                            '</div>';

                        search_contact.append(code);

                        $("#contact_list").hide();
                        search_contact.show();
                    }
                },
                error: function () {
                    alert("error");
                }
            })
        }
        else {
            $("#contact_list").show();
            $("#search_contact").hide();
        }
    })
    $("#contact_search_clear").click(function () {
        $("#contacts").val("");
        $("#contact_list").show();
        $("#search_contact").hide();
    })
})