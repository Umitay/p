package com.umi.common.action;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import lombok.extern.java.Log;

import com.google.appengine.repackaged.com.google.api.client.util.Lists;
import com.umi.common.data.Article;
import com.umi.common.data.Category;
import com.umi.common.data.Item;
import com.umi.common.data.persist.EnvironmentConfig;
import com.umi.common.services.ArticleService;
import com.umi.common.services.CategoryService;
import com.umi.common.services.ItemService;
import com.umi.common.utils.CustomException;
import com.umi.common.utils.StringUtil;

@Path("/category")
@Log
@PermitAll
public class CategoryServlet extends BaseServlet {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response index()throws IOException, ServletException{
		log.info("1");
		response.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
		request.getRequestDispatcher("/404.jsp").forward(request, response);
		return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
	}
	
	
	@Path("/{slug}")
	@GET
	public Response view(@PathParam("slug") String slug ) throws IOException, URISyntaxException, ServletException {
		log.info("Start view");

		if(request.getServerName().contains("appspot.com")){
			request.setAttribute("unvisible", true);
		}
		request.setAttribute("site_name", EnvironmentConfig.getInstance().getSite_name() );
		request.setAttribute("domain_url", "http://"+EnvironmentConfig.getInstance().getPublicDomain()+"/" );
		request.setAttribute("domain", EnvironmentConfig.getInstance().getPublicDomain());
	
		Category category =  categoryService.loadCategory(slug); 
		
		if( category == null ){
			response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
			request.getRequestDispatcher("/404.jsp").forward(request, response);
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		}
		
		List<Item>  items = itemService.loadItemsByCategory(slug,40,0,true);
		Collections.shuffle(items);

		String meta_description=category.getMeta_description();
		if(meta_description == null || meta_description.length() <=0){
			meta_description = category.getName()+ EnvironmentConfig.getInstance().getMeta_description();
		}
		
		String meta_title = category.getMeta_title();
		if(meta_title == null || meta_title.length() <= 0 ){
			meta_title = category.getName();
		}
		String meta_keywords = category.getMeta_keywords();
		if(meta_keywords == null || meta_keywords.length() <= 0 ){
			meta_keywords = category.getName();
		}
		
		try {
			request.setAttribute("articles", articles);
			request.setAttribute("category", category);
			request.setAttribute("categories", categories);
			request.setAttribute("items", items);
			
			request.setAttribute("meta_title",  meta_title +" | "+EnvironmentConfig.getInstance().getSite_name());
			request.setAttribute("meta_keywords", meta_keywords );
			request.setAttribute("meta_description", meta_description);
			
			request.setAttribute("share_url", "http://"+EnvironmentConfig.getInstance().getPublicDomain()+"/category/"+category.getSlug() );
			
			request.getRequestDispatcher("/category/category.jsp").forward(request, response);
			
		} catch (ServletException | IOException e) {
			log.severe(e.getMessage());
			response.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
			request.getRequestDispatcher("/404.jsp").forward(request, response);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		log.info("End view");
		return Response.ok().build();
	}
}
