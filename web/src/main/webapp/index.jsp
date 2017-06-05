<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/buttons.css" />
    <link rel="stylesheet" href="css/responsive-nav.css" />
    <link rel="stylesheet" href="css/common.css" />
    <link rel="stylesheet" href="css/flat-ui.min.css" />
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/style.css">

    <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/stickUp.min.js"></script>
    <script type="text/javascript" src="js/responsive-nav.min.js"></script>
    <script type="text/javascript" src="js/flat-ui.min.js"></script>
    <script type="text/javascript" src="js/zepto.min.js"></script>
    <script type="text/javascript" src="js/iscroll-zoom.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <script type="text/javascript" src="js/impl/staticVar.js"></script>
    <script type="text/javascript" src="js/impl/userop.js"></script>
    <script type="text/javascript" src="js/impl/movieop.js"></script>
    <script type="text/javascript" src="js/impl/ratingop.js"></script>
    <script type="text/javascript" src="js/impl/predictop.js"></script>
    <script type="text/javascript" src="js/impl/control.js"></script>

    <style type="text/css">
        #srs-tabs{
            background-color: #00bdef;
            width: auto;
        }

        td{
            text-align: center;
        }

    </style>

</head>

<body>
    <div id="srs">
        <div id="srs-tabs">
            <%-- 顶部tabs--%>
            <ul class="nav nav-tabs" style="margin-left: 111px">
                <li class="active">
                    <a href="#srs" data-toggle="tab" onclick="selectSRS()">
                        推荐系统
                    </a>
                </li>
                <li>
                    <a href="#environment" data-toggle="tab" onclick="selectEnvironment()">
                        环境
                    </a>
                </li>
            </ul>
        </div>

        <div id="domain">
            <div id="srs-main" >
                <div id="srs-main-add">
                    格式>>>用户ID,电影ID,评分
                    <button class="btn btn-primary" onclick="insertRatingInfo()" style="margin-left: 10px">添加评分</button>
                    <br/>
                    <textarea id="srs-main-rating" style="height: 150px; width: 600px; margin-top: 5px" placeholder="格式为用户ID::电影ID::评分, 每条记录一行"></textarea><br/>
                </div>

                <hr style="background: lightgreen; height: 2px;"/>

                <div id="srs-main-search">
                    <input id="srs-main-searchKey" type="text" placeholder="请输入用户ID">
                    <button class="btn btn-primary" onclick="selectPredict()" style="margin-left: 10px">搜索</button>
                </div>

                <hr style="background: lightgreen; height: 2px; margin-top: 5px"/>

                <div id="srs-main-searchResult">

                </div>


            </div>

            <div id="environment-main" hidden>
                <div id="environment-left">
                    <div class="col-xs-1">
                        <div id="nav">
                            <ul>
                                <li>
                                    <a href="#" onclick="selectUserEnvironment()">用户</a>
                                </li>
                                <li>
                                    <a href="#" onclick="selectMovieEnvironment()">电影</a>
                                </li>
                                <li>
                                    <a href="#" onclick="selectRatingEnvironment()">评分集</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div id="environment-right" class="col-xs-11">
                    <div id="environment-user">
                        <div id="environment-user-add">
                            用户ID:<input id="add-userId" placeholder="请输入用户ID">
                            <button class="btn btn-primary" onclick="insertUserInfo()" style="margin-left: 10px">添加</button>
                            <br/>
                        </div>

                        <hr style="background: lightgreen; height: 2px;"/>

                        <div id="environment-user-search" >
                            <input id="environment-user-searchKey" type="text" placeholder="请输入用户ID">
                            <button class="btn btn-primary" onclick="findUserById()" style="margin-left: 10px">搜索</button>
                            <input id="environment-user-searchPage" type="text" placeholder="跳转至">
                            <button class="btn btn-primary" onclick="selectAllUser()" style="margin-left: 10px">跳转</button>
                        </div>

                        <hr style="background: lightgreen; height: 2px; margin-top: 5px"/>

                        <div id="environment-user-searchResult">

                        </div>


                    </div>

                    <div id="environment-movie"hidden>
                        <div id="environment-movie-add">
                            用户ID:<input id="add-movieId" placeholder="请输入电影ID">
                            电影名称:<input id="add-movieName" placeholder="请输入电影名称" style="margin-left: 15px;">
                            电影类型:<input id="add-movieGenres" placeholder="请输入电影类型" style="margin-left: 15px;">
                            <button class="btn btn-primary" onclick="insertMovieInfo()" style="margin-left: 10px">添加</button>
                            <br/>
                        </div>

                        <hr style="background: lightgreen; height: 2px;"/>

                        <div id="environment-movie-search">
                            <input id="environment-movie-searchKey" type="text" placeholder="请输入电影ID">
                            <button class="btn btn-primary" onclick="findMovieByIdOrName()" style="margin-left: 10px">搜索</button>
                            <input id="environment-movie-searchPage" type="text" placeholder="跳转至">
                            <button class="btn btn-primary" onclick="selectAllMovie()" style="margin-left: 10px">跳转</button>
                        </div>

                        <hr style="background: lightgreen; height: 2px; margin-top: 5px"/>

                        <div id="environment-movie-searchResult">

                        </div>
                    </div>

                    <div id="environment-rating" hidden>
                        <div id="environment-rating-search">
                            <input id="environment-rating-searchUserKey" type="text" placeholder="请输入用户ID">
                            <input id="environment-rating-searchMovieKey" type="text" placeholder="请输入电影ID">
                            <button class="btn btn-primary" onclick="findRatingByUserMovie()" style="margin-left: 10px">搜索</button>
                            <input id="environment-rating-searchPage" type="text" placeholder="跳转至">
                            <button class="btn btn-primary" onclick="selectAllRating()67" style="margin-left: 10px">跳转</button>
                        </div>

                        <hr style="background: lightgreen; height: 2px; margin-top: 5px"/>

                        <div id="environment-rating-searchResult">

                        </div>
                    </div>

                </div>
            </div>
        </div>


    </div>
</body>
</html>
