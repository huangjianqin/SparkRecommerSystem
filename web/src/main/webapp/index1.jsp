<%--
  Created by IntelliJ IDEA.
  User: 健勤
  Date: 2017/4/19
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <link rel="stylesheet" href="css/bootstrap.min.css"/>
    <link rel="stylesheet" href="css/buttons.css"/>
    <link rel="stylesheet" href="css/responsive-nav.css"/>
    <link rel="stylesheet" href="css/common.css"/>
    <link rel="stylesheet" href="css/flat-ui.min.css"/>
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/style.css">
    <!--文件上传（下面两个文件）-->
    <link rel="stylesheet" href="css/fileinput.css"/>
    <link rel="stylesheet" href="css/default.css"/>
    <link rel="stylesheet" href="css/button-style.css"/>

    <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/stickUp.min.js"></script>
    <script type="text/javascript" src="js/responsive-nav.min.js"></script>
    <script type="text/javascript" src="js/flat-ui.min.js"></script>
    <script type="text/javascript" src="js/zepto.min.js"></script>
    <script type="text/javascript" src="js/iscroll-zoom.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
    <!--文件上传-->
    <script type="text/javascript" src="js/fileinput.js"></script>
    <script type="text/javascript" src="js/jquery-form.js"></script>

    <%--bootstrap时间控件--%>
    <link rel="stylesheet" href="css/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css"/>
    <script type="text/javascript" src="js/bootstrap-datetimepicker/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="js/bootstrap-datetimepicker/bootstrap-datetimepicker.zh-CN.js"></script>

    <%--bootstrap fileinput--%>
    <link rel="stylesheet" href="css/bootstrap-fileinput/fileinput.min.css" media="all" type="text/css"/>
    <script type="text/javascript" src="js/bootstrap-fileinput/fileinput.min.js"></script>
    <script type="text/javascript" src="js/bootstrap-fileinput/zh.js"></script>

    <%-- 星形评分--%>
    <link rel="stylesheet" href="css/star-rating/star-rating.min.css" media="all" type="text/css" />
    <script type="text/javascript" src="js/star-rating/star-rating.min.js" ></script>

    <%--自定义CSS--%>

    <%--自定义js--%>
    <script type="text/javascript" src="js/impl/staticVar.js"></script>
    <script type="text/javascript" src="js/impl/userop.js"></script>
    <script type="text/javascript" src="js/impl/movieop.js"></script>
    <script type="text/javascript" src="js/impl/ratingop.js"></script>
    <script type="text/javascript" src="js/impl/predictop.js"></script>
    <script type="text/javascript" src="js/impl/control.js"></script>

    <meta charset="UTF-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <style type="text/css">
        <%--评论者头像--%>
        #area_comment img {
            width: 30px;
        }

        /*评论按钮*/
        #area_comment button {
            border: none;
        }

        #center {
            background-color: #d9d9d9;
            padding: 2%;
        }

        /*评论区域*/
        #area_self_comment {
            padding: 2%;
        }

        #area_self_comment textarea {
            width: 100%;
        }

        span {
            color: #808080;
            margin-left: 2%;
        }

        a:hover {
            text-decoration: none;
        }

        <%--右上角的东西--%>
        .right {
            text-align: right;
        }

        /*收藏评论点赞按钮*/
        #area_btn {
            background-color: white;
        }

        /*收藏评论点赞按钮 button标签*/
        #area_btn button {
            outline: none;
            border: none;
            width: 100%;
            background-color: #d9d9d9;;
        }

        <%--收藏评论点赞按钮 img标签--%>
        #area_btn img {
            width: 30px;
        }

        /*头像*/
        #header img {
            width: 50px;
            height: 50px;
            padding: 5px;
        }

        /*分享图片*/
        #area_pic img {
            width: 200px;
            height: 200px;
        }

        #user_list li {
            display: flex;
            margin-top: 20px;
        }

        #keyword_select {
            position: absolute;
            margin-left: -200px;
            margin-top: 35px;
            width: 200px;
            z-index: 2;
        }

        #searchBox {
            margin-left: 20%;
        }

        #bnt_share {
            background: #3498db;
        }

        .star-rating{
            display: inline-block;
        }

        /*以下四个用于修改滚动条*/
        ::-webkit-scrollbar {
            width: 8px;
            height: 16px;
        }

        ::-webkit-scrollbar-track-piece {
            background: rgba(158, 188, 188, 0.72);
            -webkit-border-radius: 6px;
        }

        ::-webkit-scrollbar-thumb:vertical {
            background-color: rgba(0, 77, 170, 0.6);
            -webkit-border-radius: 6px;
        }

        ::-webkit-scrollbar-thumb:horizontal {
            background-color: rgba(158, 188, 188, 0.72);
            -webkit-border-radius: 6px;
        }

    </style>

</head>

<body style="background-color: #444;">
<div class="container">
    <div class="row" id="yyg-nav">
        <nav class="navbar navbar-default" role="navigation" style="max-height: inherit">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <%-- logo--%>
                <div class="navbar-header">
                    <a class="navbar-brand" href=''>Brand</a>
                </div>
                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-bottom: inherit">
                    <%-- 搜索框--%>
                    <div class="navbar-form navbar-left" role="search" id="searchBox">
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="Search" id="searchKey"
                                   onclick="showKeywordSelect()">
                        </div>
                        <select id="keyword_select" class="selectpicker keyword" multiple data-live-search="true"
                                onclick="selectKeyword()">
                        </select>
                        <button class="btn btn-primary" onclick='search()'>搜索</button>
                    </div>

                    <%-- 用户头像--%>
                    <ul id="userOp" class="nav navbar-nav navbar-right" onclick='showLogin()' style="width: inherit">
                        <li class="dropdown">
                            <img id="user_headPicture" style="width:53px;height:53px" alt="..."
                                 src="../img/default_headpicture.jpg">
                        </li>
                        <li style="width: inherit; margin-right: 0px;">
                            <p id="username">
                                hjq
                            </p>
                        </li>
                    </ul>
                </div>
                <!-- /.navbar-collapse -->
            </div>
            <!-- /.container-fluid -->
        </nav>

    </div>

    <div class="row">
        <div class="col-xs-1">
            <div id="left">
                <!--left-->
                <div id="nav">
                    <ul>
                        <li>
                            <a href="#" style="float: left;" onclick="">未观看</a>
                        </li>
                        <li>
                            <a href="#" onclick="">已观看</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="col-xs-8">
            <!--展示内容-->
            <div class="main" id="main_div">
                <div id="comment_content">
                </div>
                <hr/>
                <div id="main_content">
                    <div style="display: initial;">
                        <div style="width: 300px;display: inline-block">
                            <p style="margin: 0px">Tin Drum</p>
                        </div>
                        <div style="display: inline;">
                            <input value="4" type="number" class="rating" min=0 max=5 step=0.5 data-size="sm" disabled="disabled">
                            <div style="display: inline-block; width: 700px">
                                <p style="margin: 0px">类型:Drama,War</p>
                            </div>
                            <button class="btn btn-primary" onclick='' style="padding: 8px;">提交</button>
                        </div>
                    </div>
                    <hr style='FILTER: progid:DXImageTransform.Microsoft.Glow(color=#987cb9,strength=10)' width='80%' color=#987cb9 SIZE=1/>

                </div>
            </div>
        </div>

        <div class="col-xs-3">
            <p>猜你喜欢</p>
            <div id="myTabContent" class="tab-content">
                <div class="tab-pane fade in active" id="like_now">
                    <div style="color: yellow">
                        Shawshank Redemption
                    </div>
                    <div style="color: yellow">
                        What's Eating Gilbert Grape
                    </div>
                    <div style="color: yellow">
                        Star Wars: Episode IV - A New Hope
                    </div>
                    <div style="color: yellow">
                        Full Metal Jacket
                    </div>
                    <div style="color: yellow">
                        Forrest Gump
                    </div>
                    <div style="color: yellow">
                        Casino
                    </div>
                    <div style="color: yellow">
                        Piano
                    </div>
                    <div style="color: yellow">
                        Reservoir Dogs
                    </div>
                    <div style="color: yellow">
                        Crying Game
                    </div>
                    <div style="color: yellow">
                        Shadowlands
                    </div>
                    <div style="color: yellow">
                        Shining
                    </div>
                    <div style="color: yellow">
                        Fargo
                    </div>
                    <div style="color: yellow">
                        Mission: Impossible
                    </div>
                    <div style="color: yellow">
                        Fugitive
                    </div>
                    <div style="color: yellow">
                        Star Wars: Episode VI - Return of the Jedi
                    </div>
                    <div style="color: yellow">
                        Four Weddings and a Funeral
                    </div>
                    <div style="color: yellow">
                        American President
                    </div>
                    <div style="color: yellow">
                        Godfather: Part II
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%--登录框--%>
<div id="loginModal" class="modal fade" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h1 class="text-center text-primary" style="font-size: 36px;">登录</h1>
            </div>
            <div class="modal-body" style="padding: 0px;">
                <div class="form col-md-12 center-block">
                    <span id='login_warningInfo'></span>

                    <div class="form-group">
                        <input type="text" id="login_tel" class="form-control input-lg" placeholder="手机号码">
                    </div>
                    <div class="form-group">
                        <input type="password" id="login_password" class="form-control input-lg" placeholder="密码">
                    </div>
                    <div class="form-group">
                        <span><a href="#" class="pull-right" onclick="login2Registry()">注册</a></span>
                        <button class="btn btn-primary btn-lg btn-block" onclick="login()">立刻登录</button>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

<%--注册框--%>
<div id="registryModal" class="modal fade" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss='modal' aria-hidden="true">x</button>
                <h1 class="text-center text-primary" style="font-size: 36px;">注册</h1>
            </div>
            <div class="modal-body" style='padding: 0px;'>
                <form id="registry_form" class="form col-md-12 center-block" action="/user/info/register.do"
                      enctype="multipart/form-data" method="post">
                    <span id='registry_warningInfo'></span>

                    <div class="form-group">
                        <input type="text" id="registry_tel" class="form-control input-lg" name="account"
                               placeholder="手机号码">
                    </div>
                    <div class="form-group">
                        <input type="text" id="registry_username" class="form-control input-lg" name="username"
                               placeholder="用户名">
                    </div>
                    <div class="form-group">
                        性别:
                        <label class="checkbox-inline">
                            <input type="radio" name="sex" id="registry_sex_boy" value="1" checked>男
                        </label>
                        <label class="checkbox-inline">
                            <input type="radio" name="sex" id="registry_sex_gril" value="0">女
                        </label>
                    </div>
                    <div class="input-group">
                        <input type="text" class="form-control form_datetime" id="registry_birth" name="birth" value=""
                               placeholder="选择出生日期">
                        <span class="input-group-addon" id="basic-addon2"><span class="glyphicon glyphicon-time"
                                                                                aria-hidden="true"></span></span>
                    </div>
                    <div class="form-group">
                        选择头像:<input type="file" id="registry_headPicture" name="headPicture" style="display: inline;">
                    </div>
                    <div class="form-group">
                        <input type="password" id="registry_password" class="form-control input-lg" name="MD5Password"
                               placeholder="密码">
                    </div>
                    <div class="form-group">
                        <input type="password" id="registry_confirmPassword" class="form-control input-lg"
                               placeholder="确认密码">
                    </div>
                    <div class="form-group">
                        <span><a href="#" onclick='registry2Login()'>返回登录</a></span>
                        <button type="button" class="btn btn-primary btn-lg btn-block" onclick="registerUser()">立即注册
                        </button>
                    </div>
                </form>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>

<%--登录后信息框--%>
<div id="infoOpModal" class="modal fade" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="min-height: 0px;padding: 0px;">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        style="margin-bottom: 10px;">x
                </button>
            </div>
            <div class="modal-body" style="padding: 0px;">
                <div class="form col-md-12 center-block">
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" onclick='infoOp2modifyPassword()'>修改密码</button>
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" onclick='showModifyInfo()'>修改信息</button>
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" onclick='logout()'>账号退出</button>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

<%--修改密码框--%>
<div id="modifyPasswordModal" class="modal fade" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="min-height: 0px;padding: 0px;">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        style="margin-bottom: 10px;">x
                </button>
            </div>
            <div class="modal-body" style="padding: 0px;">
                <div class="form col-md-12 center-block">
                    <div class="form-group">
                        <input type="password" id="modifyPassword_oldMD5Pass" class="form-control input-lg"
                               placeholder="原密码">
                    </div>
                    <div class="form-group">
                        <input type="password" id="modifyPassword_newMD5Pass" class="form-control input-lg"
                               placeholder="新密码">
                    </div>
                    <div class="form-group">
                        <input type="password" id="modifyPassword_comfirmNewMD5Pass" class="form-control input-lg"
                               placeholder="再次输入新密码">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" onclick='modifyPassword()'>确认修改</button>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

<%--回复评论框--%>
<div id="replyCommentModal" class="modal fade" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="min-height: 0px;padding: 0px;">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        style="margin-bottom: 10px;">x
                </button>
            </div>
            <div class="modal-body" style="padding: 0px;">
                <div class="form col-md-12 center-block">
                    <div class="form-group">
                        <input type="text" id="replyComment_text" class="form-control input-lg" placeholder="回复内容">
                    </div>
                    <div class="form-group">
                        <button id="replyCommentModal_reply" class="btn btn-primary btn-lg btn-block">回复</button>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

<%--回复回复框--%>
<div id="replyReplyModal" class="modal fade" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="min-height: 0px;padding: 0px;">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        style="margin-bottom: 10px;">x
                </button>
            </div>
            <div class="modal-body" style="padding: 0px;">
                <div class="form col-md-12 center-block">
                    <div class="form-group">
                        <input type="text" id="replyReply_text" class="form-control input-lg" placeholder="回复内容">
                    </div>
                    <div class="form-group">
                        <button id="replyReplyModal_reply" class="btn btn-primary btn-lg btn-block" onclick=''>回复
                        </button>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

<%--发布分享的模态框--%>
<div id="publishShareModal" class="modal fade" aria-hidden="true">
    <div class="modal-dialog" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header" style="min-height: 0px;padding: 0px;">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        style="margin-bottom: 10px;margin-top:-30px">x
                </button>
                <h1 class="text-center text-primary" style="font-size: 36px;">发布</h1>
            </div>
            <div class="modal-body" style="padding: 0px;">
                <div class="form col-md-12 center-block">
                    <div class="form-group">
                        <form id='form_addr' method='post' action='/share/publishShare.do' style='margin-top: 3em'
                              enctype='multipart/form-data'>
                            <div class='form-group'>
                                <textarea id='share_text' name='share_text'
                                          style='width: 98%;height: 128px;margin-left: 1%;margin-top:-40px;'
                                          placeholder="请输入分享内容"></textarea>

                                <div style='text-align: right; font-size: 1px'>最多300个字</div>
                                <div style='display: flex;' id='preShowPicture'>
                                    <input name="pictures" id='pictures' type='file' multiple="" data-upload-url="#"
                                           data-preview-file-icon=''>
                                </div>
                                <div>
                                    <input type='checkbox' name='visible' id='isVisible' checked/>

                                    <p style='display: inline-block;'>对所有人可见</p>
                                </div>
                                <button type="button" class='btn btn-primary' id="publishShare"
                                        onclick="publishShareToServer()">上传
                                </button>
                                <button class='btn btn-default' type='reset'>重置</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

<%--修改信息的模态框--%>
<div id="modifyInfoModal" class="modal fade" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss='modal' aria-hidden="true">x</button>
                <h1 class="text-center text-primary" style="font-size: 36px;">修改信息</h1>
            </div>
            <div class="modal-body" style='padding: 0px;'>
                <form id="modifyInfo_form" class="form col-md-12 center-block" action="/user/info/modify/all.do"
                      enctype="multipart/form-data" method="post">
                    <span id='modifyInfo_warningInfo'></span>

                    <div class="form-group">
                        <input type="text" id="modifyInfo_tel" class="form-control input-lg" name="tel"
                               placeholder="手机号码">
                    </div>
                    <div class="form-group">
                        <input type="text" id="modifyInfo_username" class="form-control input-lg" name="username"
                               placeholder="用户名">
                    </div>
                    <div class="form-group">
                        性别:
                        <label class="checkbox-inline">
                            <input type="radio" name="sex" id="modifyInfo_sex_boy" value="1" checked>男
                        </label>
                        <label class="checkbox-inline">
                            <input type="radio" name="sex" id="modifyInfo_sex_gril" value="0">女
                        </label>
                    </div>
                    <div class="input-group">
                        <input type="text" class="form-control form_datetime" id="modifyInfo_birth" name="birth"
                               value="" placeholder="选择出生日期">
                        <span class="input-group-addon" id="basic-addon3"><span class="glyphicon glyphicon-time"
                                                                                aria-hidden="true"></span></span>
                    </div>
                    <div class="form-group">
                        选择头像:<input type="file" id="modifyInfo_headPicture" name="headPicture" style="display: inline;">
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-primary btn-lg btn-block"
                                onclick="modifyAllInfoSubmitAction()">保存
                        </button>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>

</body>
<script type="text/javascript">
    $("#file-4").fileinput({
        uploadExtraData: {
            kvId: '10'
        }
    });
    $(document).ready(function () {
        $("#test-upload").fileinput({
            'showPreview': false,
            'allowedFileExtensions': ['jpg', 'png', 'gif'],
            'elErrorContainer': '#errorBlock'
        });
        //页面访问时马上加载最近的分享
//        accessShareData();

        //加载首页今日点赞榜
//        todayNiceRank();
    });
    //initiating jQuery
    jQuery(function ($) {
        $(document).ready(function () {
            //enabling stickUp on the '.navbar-wrapper' class
            $('#yyg-nav').stickUp();
        });
        //隐藏下拉框
        $("#keyword_select").hide();
        //点击其他位置隐藏下拉框
        $(document).click(function (e) {
            //获取点击元素id
            var clickName = $(e.target).attr('id');

            if (clickName != 'searchKey') {
//                hideKeywordSelect();
            }

        });
        //给有输入的弹框添加hide事件
        $("#loginModal").on('hidden.bs.modal', function () {
            clearLogin();
        });
        $("#registryModal").on('hidden.bs.modal', function () {
            clearRegistry();
        });
        $("#modifyPasswordModal").on('hidden.bs.modal', function () {
            clearModifyPassword();
        });
        $("#modifyInfoModal").on('hidden.bs.modal', function () {
            clearModifyInfo();
        });

        //加载时间控件的一些属性
        $('.form_datetime').datetimepicker({
            minView: "month", //选择日期后，不会再跳转去选择时分秒
            language: 'zh-CN',
            format: 'yyyy-mm-dd',
            todayBtn: 1,
            autoclose: 1,
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        //给注册表单绑定事件,等待submit事件
        $("#registry_form").ajaxForm(function (data) {
            if (data != null && data.rn_flag == 1) {
                alert(data.rn_info + "!,现自动为您登陆账号~~~");
                hideRegistry();
                location.href = '';
            }
            else {
                alert("咨询运维");
            }
        }).submit(function () {
            //这是不用手动提交,也就是说ajaxForm封装ajax交互???
            return false;
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        //给注册表单绑定事件,等待submit事件
        $("#form_addr").ajaxForm(function (data) {
            if (data != null && data.rn_flag == 1) {
                alert(data.rn_info);
                hidePublishShare();
                clearPublishShare();
                location.href = '';
            }
            else {
                alert("咨询运维");
            }
        }).submit(function () {
            //这是不用手动提交,也就是说ajaxForm封装ajax交互???
            return false;
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        //给修改信息表单绑定事件,等待submit事件
        $("#modifyInfo_form").ajaxForm(function (data) {
            if (data != null && data.rn_flag == 1) {
                alert("修改信息成功!");
                hideModifyInfo();
                showInfoOp();
            }
            else {
                alert("咨询运维");
            }
        }).submit(function () {
            //这是不用手动提交,也就是说ajaxForm封装ajax交互???
            return false;
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        //监听滚动条事件,实现实时更新
        $("#main_div").scroll(function () {
            var $this = $(this);
            var viewH = $(this).height();//可见高度
            var contentH = $(this).get(0).scrollHeight;//内容高度
            var scrollTop = $(this).scrollTop();//滚动高度,离顶端

            if (contentH - viewH - scrollTop <= 100) { //到达底部100px时,加载新内容
//				if(scrollTop/(contentH -viewH)>=0.95){ //到达底部100px时,加载新内容
                // 这里加载数据..
                if ($("#bnt_share").is(":visible")) {
//                    accessShareDataAndAppend();
                }
            }
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        $("#pictures").fileinput({
            language: 'zh',
            allowedFileExtensions: ['jpg', 'png', 'jpeg'],
            maxFileCount: 6,
            enctype: 'multipart/form-data',
            browseClass: "btn btn-primary", //按钮样式
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
            showUpload: false,
            dropZoneEnabled: false,//是否显示拖拽区域
            maxFileSize: 20480,//单位为kb，如果为0表示不限制文件大小
            initialCaption: '请选择分享图片',
            overwriteInitial: false,
            showPreview: true,
            showCancel: true,
        });
    });
</script>

</html>
