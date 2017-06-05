/**
 * Created by 健勤 on 2017/3/29.
 */
function insertRatingInfo(){
    $.ajax({
        url:"/insert/ratingInfo.do",
        type:"post",
        async:true,
        data:{"ratings": $("#srs-main-rating").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                alert('插入成功');
            }else{
                alert("请求出错");
            }
        }
    });
}

function selectAllRating(){
    $.ajax({
        url:"/select/allRating.do",
        type:"post",
        async:true,
        data:{"page": $("#environment-rating-searchPage").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                var result = data.result;
                if(result.length > 0){
                    showRatingInfo(result);
                }
                else{
                    $("#environment-rating-searchResult").html('未找到数据');
                }
            }else{
                alert("请求出错");
            }
        }
    });
}

function findRatingByUserMovie(){
    $.ajax({
        url:"/select/findRatingByUserMovie.do",
        type:"post",
        async:true,
        data:{"userId": $("#environment-rating-searchUserKey").val(), "movieId": $("#environment-rating-searchMovieKey").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                var result = data.result;
                showRatingInfo(result);
            }else{
                alert("请求出错");
            }
        }
    });
}

function showRatingInfo(result){
    var htmlStr =
        '<table border="2" width="700px">' +
            '<tr>' +
            '<td>用户ID</td>' +
            '<td>电影ID</td>' +
            '<td width="150px">评分</td>' +
            '<td>操作</td>' +
            '</tr>';

    for(var i = 0; i < result.length; i++){
        htmlStr +=
            '<tr>' +
            '<td>' + result[i].userId + '</td>' +
            '<td>' + result[i].movieId + '</td>' +
            '<td>' + result[i].rating + '</td>' +
            '<td><button class="btn btn-primary">删除</button></td>' +
            '</tr>';
    }

    htmlStr +=
        '</table>';

    $("#environment-rating-searchResult").html(htmlStr);
}