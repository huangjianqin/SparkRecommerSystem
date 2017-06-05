/**
 * Created by 健勤 on 2017/3/28.
 */
function showLogin(){
    $("#loginModal").modal('show');
}
function hideLogin(){
    $("#loginModal").modal('hide');
}
function clearLogin(){
    $("#login_tel").val("");
    $("#login_password").val("");

}

function insertUserInfo(){
    $.ajax({
        url:"/insert/userInfo.do",
        type:"post",
        async:true,
        data:{"id": $("#add-userId").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                alert('插入成功');
                $("#add-userId").val("");
            }else{
                alert("请求出错");
            }
        }
    });
}

function selectAllUser(){
    $.ajax({
        url:"/select/allUser.do",
        type:"post",
        async:true,
        data:{"page": $("#environment-user-searchPage").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                var result = data.result;
                if(result.length > 0){
                    showUserInfo(result);
                }
                else{
                    $("#environment-user-searchResult").html('未找到数据');
                }
            }else{
                alert("请求出错");
            }
        }
    });
}

function findUserById(){
    $.ajax({
        url:"/select/findUserById.do",
        type:"post",
        async:true,
        data:{"userId": $("#environment-user-searchKey").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                var result = data.result;
                if(result.length > 0){
                    showUserInfo(result);
                }
                else{
                    $("#environment-user-searchResult").html('未找到数据');
                }
            }else{
                alert("请求出错");
            }
        }
    });
}

function showUserInfo(result){
    var htmlStr = '<table border="2" width="200px">' +
        '<tr>' +
        '<td>用户ID</td>' +
        '<td>操作</td>' +
        '</tr>';

    for(var i = 0; i < result.length; i++){
        htmlStr +=  '<tr>' +
            '<td>' + result[i].id + '</td>' +
            '<td><button class="btn btn-primary" style="padding: 5px">删除</button></td>' +
            '</tr>';
    }

    htmlStr += '</table>';
    $("#environment-user-searchResult").html(htmlStr);
}