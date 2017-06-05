/**
 * Created by 健勤 on 2017/3/29.
 */
function insertMovieInfo(){
    $.ajax({
        url:"/insert/movieInfo.do",
        type:"post",
        async:true,
        data:{"id": $("#add-movieId").val(), "name": $("#add-movieName").val(), "genres": $("#add-movieGenres").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                alert('插入成功');
                $("#add-movieId").val("");
                $("#add-movieName").val("");
                $("#add-movieGenres").val("");
            }else{
                alert("请求出错");
            }
        }
    });
}

function selectAllMovie(){
    $.ajax({
        url:"/select/allMovie.do",
        type:"post",
        async:true,
        data:{"page": $("#environment-movie-searchPage").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                var result = data.result;
                if(result.length > 0){
                    showMovieInfo(result);
                }
                else{
                    $("#environment-movie-searchResult").html('未找到数据');
                }
            }else{
                alert("请求出错");
            }
        }
    });
}

function findMovieByIdOrName(){
    $.ajax({
        url:"/select/findMovieByIdOrName.do",
        type:"post",
        async:true,
        data:{"keyword": $("#environment-movie-searchKey").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                var result = data.result;
                if(result.length > 0){
                    showMovieInfo(result);
                }
                else{
                    $("#environment-movie-searchResult").html('未找到数据');
                }
            }else{
                alert("请求出错");
            }
        }
    });
}

function showMovieInfo(result){
    var htmlStr =
        '<table border="2" width="1000px">' +
            '<tr>' +
            '<td>电影ID</td>' +
            '<td>电影名</td>' +
            '<td width="400px">电影类型</td>' +
            '<td>操作</td>' +
            '</tr>';

    for(var i = 0; i < result.length; i++){
        htmlStr +=
            '<tr>' +
            '<td>' + result[i].id + '</td>' +
            '<td>' + result[i].name + '</td>' +
            '<td>' + result[i].genres + '</td>' +
            '<td><button class="btn btn-primary">删除</button></td>' +
            '</tr>';
    }

    htmlStr +=
        '</table>';
    $("#environment-movie-searchResult").html(htmlStr);
}