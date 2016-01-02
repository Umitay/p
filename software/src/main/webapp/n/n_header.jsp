<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"	href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet"	href="/static/styles.css">
<link rel="icon" href="/logo.png" type="image/x-icon" />
<link rel="shortcut icon" href="/logo.png" type="image/x-icon" />
<!-- Latest compiled and minified JavaScript -->
<script	src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script src="//tinymce.cachefly.net/4.2/tinymce.min.js"></script>
<script>tinymce.init({selector:'textarea',plugins: [
                                                    "advlist autolink autosave link image lists charmap print preview hr anchor pagebreak spellchecker",
                                                    "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
                                                    "table contextmenu template textcolor paste  textcolor"
                                                  ]});</script>

<title>Admin</title>
<style type="text/css">
body{ padding-top: 10%}
.navbar > div {
    padding-top: 0px;
}
</style>
</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container">
    <div class="navbar-header">
     <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Меню</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>
     <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
      <li><a href="/csv"><b>Upload Csv</b></a>  </li>
      <li><a href="/n/category/e/new"><b>+ Category</b></a>  </li>
      <li><a href="/n/software/e/new"><b>+ Software</b></a>  </li>
       <li><a href="/n/article/e/new"><b>+ Article</b></a>  </li>
         <c:if test="${!empty categories}">
		    <c:forEach items="${categories}" var="category">
		     <c:if test="${category.slug == 'hp'}">
		    	<li><a  style="display: inline;" href="/n">${category.name}</a><a style="display: inline;" href="/applications/e/${category.slug}">(edit)</a></li>
			   </c:if>
			   <c:if test="${category.slug == 'articles'}">
			   <li><a  style="display: inline;" href="/article/l">${category.name}</a><a style="display: inline;"href="/applications/e/${category.slug}">(edit)</a></li>
			
			   </c:if>
			   <c:if test="${category.slug != 'home' && category.slug != 'articles'}">
			   <li><a  style="display: inline;" href="/applications/l/${category.slug}">${category.name}</a><a style="display: inline;"href="/applications/e/${category.slug}">(edit)</a></li>
			   </c:if>
		    </c:forEach>
		   </c:if>
	       <li><a class="capitalize"  
		       href="/n/article/list">Интересно и Полезно</a>
		     </li>
      </ul>
     </div><!-- /.navbar-collapse -->
  </div><!-- /.container -->
</nav>
