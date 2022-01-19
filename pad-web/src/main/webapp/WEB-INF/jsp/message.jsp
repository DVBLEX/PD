<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">

<link rel="stylesheet" href="lib/bootstrap-3.3.7/css/bootstrap.min.css" />
<link rel="stylesheet" href="app/css/pad.css" />

<link rel="apple-touch-icon" sizes="57x57" href="app/img/favicon/apple-icon-57x57.png">
<link rel="apple-touch-icon" sizes="60x60" href="app/img/favicon/apple-icon-60x60.png">
<link rel="apple-touch-icon" sizes="72x72" href="app/img/favicon/apple-icon-72x72.png">
<link rel="apple-touch-icon" sizes="76x76" href="app/img/favicon/apple-icon-76x76.png">
<link rel="apple-touch-icon" sizes="114x114" href="app/img/favicon/apple-icon-114x114.png">
<link rel="apple-touch-icon" sizes="120x120" href="app/img/favicon/apple-icon-120x120.png">
<link rel="apple-touch-icon" sizes="144x144" href="app/img/favicon/apple-icon-144x144.png">
<link rel="apple-touch-icon" sizes="152x152" href="app/img/favicon/apple-icon-152x152.png">
<link rel="apple-touch-icon" sizes="180x180" href="app/img/favicon/apple-icon-180x180.png">
<link rel="icon" type="image/png" sizes="192x192" href="app/img/favicon/android-icon-192x192.png">
<link rel="icon" type="image/png" sizes="32x32" href="app/img/favicon/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="96x96" href="app/img/favicon/favicon-96x96.png">
<link rel="icon" type="image/png" sizes="16x16" href="app/img/favicon/favicon-16x16.png">
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage" content="app/img/favicon/ms-icon-144x144.png">
<meta name="theme-color" content="#ffffff">

<title>AGS - Port Access Dakar</title>

</head>

<body>

    <nav class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="/pad/login.htm?tp"> <img src="app/img/ags_logo.png" alt="" style="height: 77%; float: left; margin-top: 2px;" /> &nbsp; Port Access Dakar <c:if
                        test="${isTestEnvironment == true}">
                        <span>(Sandbox)</span>
                    </c:if>
                    <div style="position:absolute;font-size: x-small;color: gray;padding-left: 58px;">
                        <%@ include file="buildNumber.jsp"%>
                    </div>
                </a>
            </div>
        </div>
    </nav>

    <div class="container-fluid form-horizontal">
        <div class="row margin-top-10px">
            <div class="col-sm-12">
                <h4 class="text-center">${message}</h4>
            </div>
        </div>
    </div>

    <script type="text/javascript" src="lib/jquery-2.2.4/js/jquery.min-2.2.4.js"></script>
    <script type="text/javascript" src="js/popper.min-1.14.6.js"></script>
    <script type="text/javascript" src="lib/bootstrap-3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
