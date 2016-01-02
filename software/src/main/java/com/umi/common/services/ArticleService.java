package com.umi.common.services;

import static com.umi.common.data.persist.OfyService.ofy;

import java.util.List;

import lombok.extern.java.Log;

import com.umi.common.data.Article;
import com.umi.common.data.SitemapIndex;
import com.umi.common.data.persist.DBService;
import com.umi.common.data.persist.EnvironmentConfig;
import com.umi.common.utils.StringUtil;
@Log
public class ArticleService extends DBService{
	public List<Article> loadArticles(Boolean active, Integer limit, Integer offset ) {
		if(active){
			return ofy().load().type(Article.class).filter( "active ==",active).limit(limit).offset(offset).order("-dateCreated").list();
		}else{
			return loadAll(Article.class);
		}
	}
	public List<Article> loadArticles(Boolean active) {
	return  loadArticles(active,10000000, 0 );
	}
	
	public Article loadArticle(String slug) {
		return load(Article.class,slug);
	}

	public Article saveArticle(Article newarticle) {
		Article  article =  null;
		
		try{
			
			article = load( Article.class , newarticle.getSlug() );
			
			if(article == null){
				article = new Article();
				article.setSlug( newarticle.getSlug() );
				article.setDateCreated( System.currentTimeMillis() );
				article.setDatePublished(System.currentTimeMillis() );
			}
			article.setDateModified(System.currentTimeMillis() );
			article.setPriority(newarticle.getPriority());
			article.setActive(newarticle.getActive());
		
			article.setThumbnailUrl(newarticle.getThumbnailUrl());
			article.setName(newarticle.getName());
			article.setAbout(newarticle.getAbout());
			article.setDescription(newarticle.getDescription());
			
			article.setAds_horizont1(newarticle.getAds_horizont1());
			article.setAds_horizont2(newarticle.getAds_horizont2());
			article.setAds_side1(newarticle.getAds_side1());
			article.setAds_side2(newarticle.getAds_side2());
			
			if(newarticle.getAlt()!=null){
				article.setAlt( newarticle.getAlt() );
			}else{
				article.setAlt(EnvironmentConfig.getInstance().getMeta_icon()+ newarticle.getName() );
			}
			
			if(newarticle.getMeta_title() != null){
				article.setMeta_title(newarticle.getMeta_title());
			}else{
				article.setMeta_title(newarticle.getName()+EnvironmentConfig.getInstance().getMeta_title());
			}
			
			if(newarticle.getMeta_keywords() != null){
				article.setMeta_keywords(newarticle.getMeta_keywords());
			}else{
				article.setMeta_keywords(EnvironmentConfig.getInstance().getMeta_keywords()+ newarticle.getName() );
			}
			
			if(newarticle.getMeta_description() != null){
				article.setMeta_description(newarticle.getMeta_description());
			}else if(newarticle.getAbout() != null){
				article.setMeta_description(newarticle.getAbout() + EnvironmentConfig.getInstance().getMeta_description());
			}
			
			article = save(article);
			
			SitemapIndex sitemap = load(SitemapIndex.class, "1");
			
			if(sitemap == null){
				sitemap = new SitemapIndex();
				sitemap.setId("1");
			}
			
			sitemap.setArticle_date_modified (System.currentTimeMillis());
			save(sitemap);
		}catch(Exception e ) {
			log.severe("newarticle.getarticle_name(): " + newarticle.getName());
			log.severe(StringUtil.exceptionFormat( e ));
		}
		
		return article;
		
	}
}
