<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@include file="article_header.jsp"%>
<div class="container">
	<div class="row">
		<div class="col-xs-12 col-sm-8 col-md-8">
		<div itemscope itemtype="http://schema.org/Article">
		<h1 itemprop="name">${article.name} 
		<br>
		<small> Автор: <a href="https://plus.google.com/+Urrecipe1/posts"><span
								itemprop="name">Умитай Турдыкулова</span></a></small></h1>
				
		<div itemprop="description">${article.about}</div>
		
		<%-- <c:set var="share_url" value="http://www.freedownload.su/article/${article.slug}" scope="request"/>
		<%@include file="/common/horizont_sosial_buttons.jsp"%> --%>
		
		<div class="bg-warning ads">
				<c:if test="${empty unvisible}">
				<c:choose>
				 	<c:when test="${empty article.ads_horizont1}">
				    	<%@include file="/common/ad_horizont.jsp"%>
				    </c:when>
				    <c:otherwise>
				        ${article.ads_horizont1}
				    </c:otherwise>
				</c:choose>
			    </c:if>
			    <br>
		</div>
		
		<div itemprop="articleBody"> ${article.description}</div>
		
		</div>
		
		<div class="bg-warning ads">
				<c:if test="${empty unvisible}">
				<c:choose>
				 	<c:when test="${empty article.ads_horizont2}">
				    	<%@include file="/common/ad_horizont.jsp"%>
				    </c:when>
				    <c:otherwise>
				        ${article.ads_horizont2}
				    </c:otherwise>
				    </c:choose>
			    </c:if>
		</div>
		
		</div>
		<!-- /.col-xs-12 col-sm-8 col-md-8 -->
		<div class="col-sm-4 col-md-4">
		
		<c:if test="${empty unvisible}">
				<c:choose>
				    <c:when test="${empty article.ads_side1}">
				    	<%@include file="/common/ad_side.jsp"%>
				    </c:when>
				    <c:otherwise>
				        ${article.ads_side1}
				    </c:otherwise>
				</c:choose>
			</c:if>
			
		 <%@include file="/item/item_side_list.jsp"%>
			
		<!-- /.col-sm-4 col-md-4 -->
		</div>
	</div>

<%@include file="/common/bottom.jsp"%>