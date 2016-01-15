<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${!empty items}">
		<section itemscope="" itemtype="http://schema.org/ItemList">
			<meta content=true itemprop="mainContentOfPage">
			<meta content="Descending" itemprop="itemListOrder">
			
				<c:forEach items="${items}" var="item" varStatus="loop" begin="0" end="2">
					<div itemprop="itemListElement" class="item_list_element thumbnail">
							<c:if test="${!empty item.thumbnailUrl}">
							 <a href="/software/${item.slug}"> <img alt="${item.alt}"
								width="300" height="225" itemprop="image"
								src="${item.thumbnailUrl}"> 
							 </a>
							 </c:if>
							<div class="caption">
								<h3 itemprop="name">
									<a href="/software/${item.slug}" title="${item.link_title}"
										itemprop="url">${item.name}</a>
								</h3>
								<p itemprop="description">${item.about}</p>
							</div>
						</div>
				</c:forEach>
		</section>
	</c:if>