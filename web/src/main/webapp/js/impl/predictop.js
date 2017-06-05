/**
 * Created by 健勤 on 2017/3/29.
 */
function selectPredict(){
    $.ajax({
        url:"/select/findPredictById.do",
        type:"post",
        async:true,
        data:{"id": $("#srs-main-searchKey").val()},
        dataType:"json",
        success:function(data){
            if(data != null && data.flag != -1){
                var result = data.result;
                if(result != null){
                    showPredictInfo(result);
                }
                else{
                    $("#srs-main-searchResult").html('未找到数据');
                }
            }else{
                alert("请求出错");
            }
        }
    });
}

function showPredictInfo(result){
    var htmlStr =
        '<table border="2" width="700px">' +
            '<tr>' +
                '<td width="500px" colspan="21">推荐结果</td>' +
            '</tr>' +
            '<tr>' +
                '<td >电影ID</td>' +
                '<td >预测评分</td>' +
            '</tr>';

    for(var i = 0; i < result.predicts.length; i++){
        var tmpHtmlStr = '<tr>';

        tmpHtmlStr += '<td>' + result.predicts[i].movieId + '</td>';
        tmpHtmlStr += '<td>' + result.predicts[i].rating + '</td>';

        tmpHtmlStr += '</tr>';

        htmlStr += tmpHtmlStr;
    }

    htmlStr +=
        '</table>';

    $("#srs-main-searchResult").html(htmlStr);
}