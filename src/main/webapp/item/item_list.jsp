<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${!empty items}">
	<section itemscope="" itemtype="http://schema.org/ItemList">
		<meta content=true itemprop="mainContentOfPage">
		<meta content="Descending" itemprop="itemListOrder">
		<c:forEach items="${items}" var="item">
			<div itemprop="itemListElement" class="item_list_element">
				<div class="image-bolck col-xs-3 col-sm-3 col-md-3 col-lg-3">
					<a href="/software/${item.slug}"> 
					    <c:if test="${!empty item.thumbnailUrl}">
							<img alt="${item.alt}" width="64" height="64" itemprop="image"
								class="img-responsive" src="${item.thumbnailUrl}">
						</c:if>
					</a>
				</div>
				<div class="col-xs-9 col-sm-9 col-md-9 col-lg-9">
					<h4 itemprop="name">
						<a href="/software/${item.slug}" title="${item.link_title}"
							itemprop="url">${item.name}</a>
					</h4>
					<a href="/software/${item.slug}" style="color: #485f71;" title="${item.link_title}"> 
							<div itemprop="description">${item.meta_description}</div>
					</a>
				</div>
				<hr>
			</div>
		</c:forEach>
	</section>
</c:if>