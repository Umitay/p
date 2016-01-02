<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@include file="item_header.jsp"%>

<div class="container">
	<div class="row">
		<div class="thumbnail col-xs-12 col-sm-8 col-md-8" style="padding: 20px;">
			<div id="item" itemtype="http://schema.org/SoftwareApplication" itemscope="">
				<link itemprop="SoftwareApplicationCategory" href="${item.schema_category}" type="text/html" />
				<meta itemprop="url" content="/software/${item.slug}">
				<meta itemprop="dateModified" content="${item_dateModified}">
				<div class="page-header">
					<img itemprop="image" src="${item.thumbnailUrl}"  alt="${item.name} icon" title="${item.name} - Logo"/>
				 	<h1 itemprop="name">${item.name} <span>${item.version}</span> <small>Бесплатно!</small></h1>
				<div>
					Опубликовано:
					<time datetime="${item_datePublished}" itemprop="datePublished">${item_datePublished}</time>
				</div>
				
				<div itemprop="description">${item.about}</div>
		
				</div>
				
				<%-- <c:set var="share_url"
					value="http://www.freedownload.su/software/${item.slug}"
					scope="request" /> --%>
				<%-- @include file="/common/horizont_sosial_buttons.jsp" --%>
				
				<div class="bg-warning ads">
					<c:if test="${empty unvisible }">
						<c:choose>
							<c:when test="${empty item.ads_horizont1}">
								<%@include file="/common/ad_horizont.jsp"%>
							</c:when>
							<c:otherwise>
						        ${item.ads_horizont1}
						    </c:otherwise>
						</c:choose>
					</c:if>
					<br>
				 </div>
				 
				<h2>Обзор</h2>
				
				<article itemprop="reviews" itemscope itemtype="http://schema.org/Review">
				<div itemprop="author" itemscope=""	itemtype="http://schema.org/Person">
					Автор: <a  itemprop="url" href="https://plus.google.com/+alex/posts"  target="_blank" >
					<span itemprop="name">Alex</span>
					</a>
				</div>
				<div itemprop="review">${item.description}</div>
				</article>
			</div>
			<!-- /itemscope  -->
		</div>
		<!-- /.col-xs-12 col-sm-8 col-md-8 -->
		<div class="col-sm-4 col-md-4">
		
				
			<div class="bg-warning ads">
					<c:if test="${empty unvisible }">
						<c:choose>
							<c:when test="${empty item.ads_horizont2}">
								<%@include file="/common/ad_horizont.jsp"%>
							</c:when>
							<c:otherwise>
						        ${item.ads_horizont2}
						    </c:otherwise>
						</c:choose>
					</c:if>
				</div>
				
				<%@include file="/article/article_side_list.jsp"%>
		</div>
		<!-- /.col-sm-4 col-md-4 -->
	</div>
	<!-- /.row -->
</div>
<!-- /.container -->
<%@include file="/common/bottom.jsp"%>