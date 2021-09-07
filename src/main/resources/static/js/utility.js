function ajax_sentEmail(json_mail,prTitle,taskId,prmId,curtAssignee,div_msg){
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/email/send/htmlmail/"+ prTitle + "/" +taskId+ "/" +prmId + "/" +curtAssignee,
        data: JSON.stringify(json_mail),
        dataType: 'json',
        cache: false,
        async: true,
        complete: function () {
            show_mes(div_msg,"Sent out email",false);

        }
    })
}

function ajax_sentEmailWithoutTaskId(json_mail,prTitle,prmId,curtAssignee,div_msg){
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/email/send/htmlmail/withoutTaskId/"+ prTitle + "/" +prmId + "/" +curtAssignee,
        data: JSON.stringify(json_mail),
        dataType: 'json',
        cache: false,
        async: true,
        complete: function () {
            show_mes(div_msg,"Sent out email",false);

        }
    })
}

function show_mes(div_msg,message,err) {
    let $el= $("#"+ div_msg);
    $el.empty();

    if(err === false){
        $el.html("<div class='alert alert-success'>" +
            "<strong>"+message + "</strong>"+
            '</div>');
    }else{
        $el.html("<div class='alert alert-danger'>" +
            "<strong>"+message + "</strong>"+
            '</div>');
    }

};