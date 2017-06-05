/**
 * Created by 健勤 on 2017/3/29.
 */
function selectSRS(){
    $("#srs-main").show();
    $("#environment-main").hide();
}

function selectEnvironment(){
    $("#environment-main").show();
    $("#srs-main").hide();

    $("#srs-main-rating").val("");
    $("#srs-main-searchKey").val("");

    selectUserEnvironment();
}

function selectUserEnvironment(){
    $("#environment-user").show();
    $("#environment-movie").hide();
    $("#environment-rating").hide();

    $("#environment-user-searchKey").val("");
    $("#environment-user-searchPage").val("");

    selectAllUser();
}

function selectMovieEnvironment(){
    $("#environment-movie").show();
    $("#environment-user").hide();
    $("#environment-rating").hide();

    $("#environment-movie-searchKey").val("");
    $("#environment-movie-searchPage").val("");

    selectAllMovie();
}

function selectRatingEnvironment(){
    $("#environment-rating").show();
    $("#environment-user").hide();
    $("#environment-movie").hide();

    $("#environment-rating-searchUserKey").val("");
    $("#environment-rating-searchMovieKey").val("");
    $("#environment-rating-searchPage").val("");

    selectAllRating();
}
