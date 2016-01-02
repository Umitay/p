package com.umi.common.services;

import static com.umi.common.data.persist.OfyService.ofy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.java.Log;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.umi.common.data.Category;
import com.umi.common.data.Item;
import com.umi.common.data.SitemapIndex;
import com.umi.common.data.X_CategoryItem;
import com.umi.common.data.persist.DBService;
import com.umi.common.data.persist.EnvironmentConfig;
import com.umi.common.utils.StringUtil;

@Log
public class ItemService extends DBService{
	
	
	public Item loadItem( String slug   ){

		Item  item = null;
		
		try{
				item = load( Item.class , slug );
		
		}catch(Exception e ) {
			log.severe(StringUtil.exceptionFormat( e ));
		}
		
		return item;
	}
	public List<Item> loadItems(Integer limit, Integer offset){

		List<Item>  items = Lists.newArrayList() ;
		
		try{
		 items = ofy().load().type(Item.class).filter("active", true).order("-dateCreated").limit(limit).offset(offset).list();
		}catch(Exception e ) {
			log.severe(StringUtil.exceptionFormat( e ));
		}
		
		return items;
	}
	public List<Item> loadItemsByCategory(String cat_slug, Integer limit, Integer offset, Boolean active ){
		log.info("Start loadItemsByCategory: "+cat_slug + active);
		List<Item>  items = Lists.newArrayList() ;
		List<String> ids =  Lists.newArrayList() ;
		List<X_CategoryItem> x_list = null;
		try{
			if(active){
				x_list =ofy().load().type(X_CategoryItem.class).filter("cat_slug",cat_slug).filter("active", active).order("-dateCreated").limit(limit).offset(offset).list();
			}else {
				x_list =ofy().load().type(X_CategoryItem.class).filter("cat_slug",cat_slug).order("-dateCreated").limit(limit).offset(offset).list();
			}
		
			for(X_CategoryItem x:x_list){
				ids.add( x.getItem_slug() );
			}
			
			Map<String, Item> items_map = ofy().load().type(Item.class).ids(ids);
		
			for(Entry<String, Item> i:items_map.entrySet()){
				items.add(i.getValue());
			}
		}catch(Exception e ) {
			log.severe(StringUtil.exceptionFormat( e ));
		}
		
		log.info("End loadItemsByCategory");
		return items;
	}


	public void loadToDatastroge1(List<String[]> content ) {
		log.info("Start uploadCsvtoDataStore");
		
		String[] line= null;
		List<Item> itemList =  Lists.newArrayList();
		String[] header = content.get(0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		for (int rowIndex= 1; rowIndex < content.size(); rowIndex++) {
			
			line = content.get(rowIndex);
			
			String slug = StringUtil.generateSlug(line[5].toLowerCase());
			
			Item item = loadItem( slug );
			
			if(item == null){
				item = new Item();
			}
			
			item.setSlug(slug);
			item.setName( line[5]);
			
			List<String> categoriesList =Lists.newArrayList();
			for(int colIndex = 0; colIndex < line.length; colIndex++){
				
				
				if(line[colIndex] != null && header[colIndex] !=null){
					
					switch (header[colIndex]) {
						case "alt": item.setAlt(line[colIndex]); break;
						case "thumbnailUrl": item.setThumbnailUrl(line[colIndex]); break;
						//case "thumbnailUrl2": item.setThumbnailUrl2(line[colIndex]); break;
						case "about": item.setAbout(line[colIndex]); break;
						case "description": 
							String  description = line[colIndex];
							if(description.length() >0){
								description = description.replaceAll("(\\r\\n|\\n)", "<br>");
								description = description.replaceAll("<br><br>", "<br>");
								
								item.setDescription(description);
							}
							 break;
						case "categories": 
							String[] array = line[colIndex].split(",");
							for(String a:array){
								categoriesList.add(a.toLowerCase().trim());
							}
						
							item.setCategories(categoriesList); 
							break;
							
						//case "totalTime": item.setTotalTime(line[colIndex]); break;
						//case "recipeYield": item.setRecipeYield(line[colIndex]); break;
						case "ingredients":
							String  ingredients = line[colIndex];
							log.info(ingredients);
							if(ingredients.length() >0){
								ingredients = ingredients.replaceAll("(\\r\\n|\\n)",  "<br>");
								ingredients = ingredients.replaceAll("<br><br>", "<br>");
							//	item.setIngredients(ingredients);
							}
							 break;
						//case "nutrition": item.setNutrition(line[colIndex]); break;
						case "priority": item.setPriority(Integer.valueOf(line[colIndex])); break;
						case "dateCreated":
							
								try {
									Date parsedDate = dateFormat.parse ( line[colIndex] );
									item.setDateCreated( parsedDate.getTime() );
								} catch (ParseException e) {
									log.severe("parsedDate:  "+ line[colIndex]+" "+ e.getMessage() );
									item.setDateCreated(System.currentTimeMillis());
								}
							
							 break;
						case "fb_share": item.setFb_share(line[colIndex]); break;
						case "vk_share": item.setVk_share(line[colIndex]); break;
						case "lj_share": item.setLj_share(line[colIndex]); break;
						case "twitter_share": item.setTwitter_share(line[colIndex]); break;
						default:log.info("unregistered column "+header[colIndex]); break;
					}
					
				}// end of if
				
			}// end of loop by colindex
			
			if(!item.getName().isEmpty()){
				
				if(item.getDateCreated() == null ){
					item.setDateCreated(System.currentTimeMillis());
				}
				item.setDatePublished( System.currentTimeMillis() );
				item.setDateModified( System.currentTimeMillis() );
				item.setActive(true);
				item = save(item);
				saveItemCategory(item, categoriesList,item.getActive());
			}
		}// end of cycle
		
		log.info("end uploadCsv toDataStore");
	}

	public void loadToDatastroge(List<String[]> content ) throws ParseException {
		log.info("Start uploadCsvtoDataStore");
		
		List<Item> itemList =  Lists.newArrayList();
		String[] header = content.get(0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		for (int rowIndex= 1; rowIndex < content.size(); rowIndex++) {
			String[] line= null;
			line = content.get(rowIndex);
			List<String> categoryList =Lists.newArrayList();
			Item item = null;
			String name = "";
			String slug = "";
			String alt = "";
			String thumbnailUrl = "";
			String about = "";
			String brief = "";
			String description = "";
			String schema_category = "";
			String priority = "";
			Long parsedDate = null;
			String fb_share = "";
			String vk_share= "";
			String lj_share= "";
			String twitter_share= "";
			String meta_description = "";
			
			for(int colIndex = 0; colIndex < line.length; colIndex++){
				
				if(line[colIndex] != null && header[colIndex] !=null){
					
					switch (header[colIndex]) {
						case "name": 
							slug = StringUtil.generateSlug(line[colIndex].toLowerCase());
							name = line[colIndex];
						break;
						case "alt": alt=line[colIndex]; break;
						case "thumbnailUrl": thumbnailUrl =line[colIndex];   break;
						case "about": about =line[colIndex] ;  break;
						case "meta_description": meta_description = line[colIndex]; 
						break;
						case "description": 
							description = line[colIndex];
							if(description.length() >0){
								description = description.replaceAll("(\\r\\n|\\n)", "<br>");
								description = description.replaceAll("<br><br>", "<br>");
								
							}
							 break;
						case "category": 
							String[] array = line[colIndex].split(",");
							for(String a:array){
								categoryList.add(a.toLowerCase().trim());
							}
							break;
						case "schema_category":
							schema_category = line[colIndex];
							break;
						//case "priority": priority = line[colIndex]; break;
						case "dateCreated":
							
									if(line[colIndex].isEmpty()){
										parsedDate =  System.currentTimeMillis();
									}else{
										 parsedDate = dateFormat.parse ( line[colIndex] ).getTime();
									}
							
							 break;
						case "fb_share": fb_share = line[colIndex];  break;
						case "vk_share": vk_share = line[colIndex];  break;
						case "lj_share": lj_share = line[colIndex]; break;
						case "twitter_share": twitter_share = line[colIndex]; break;
						default:log.info("unregistered column "+header[colIndex]); break;
					}
					
				}// end of if
				
			}// end of loop by colindex
			

			item = loadItem( slug );
			if(item == null){
				item = new Item();
			}
			item.setName(name); 
			item.setSlug(slug);
			item.setAlt(alt);
			item.setThumbnailUrl(thumbnailUrl);
			item.setAbout(about );
			item.setMeta_description(meta_description);
			item.setDescription(description);
			item.setCategories(categoryList); 
			item.setSchema_category(schema_category);
			item.setPriority(0);//Integer.valueOf(priority)); 
			item.setDateCreated( parsedDate );
			item.setFb_share(fb_share );
			item.setVk_share(vk_share);
			item.setLj_share(lj_share); 
			item.setTwitter_share(twitter_share);
			
			if(!item.getName().isEmpty()){
				
				if(item.getDateCreated() == null ){
					item.setDateCreated(System.currentTimeMillis());
				}
				item.setDatePublished( System.currentTimeMillis() );
				item.setDateModified( System.currentTimeMillis() );
				item.setActive(true);
				item = save(item);
				saveItemCategory(item, categoryList,item.getActive());
			}
		}// end of cycle
		
		log.info("end uploadCsv toDataStore");
	}
	public Item saveItem(String slug, String name, String alt, String thumbnailUrl, String thumbnailUrl2,
			String about, String description, String categories,
			String totalTime, String recipeYield, String ingredients,
			String nutrition,  Boolean active,
			Long datePublished, Long dateCreated, Long dateModified,
			String fb_share , String vk_share , String lj_share ,
			String twitter_share, Boolean is_admin,
			String link_title, String meta_title,String meta_keywords, String meta_description, 
			String ads_horizont1, String ads_horizont2, String ads_side1, String ads_side2) {
		
			List<String> categoriesList =null;
			Item  item =  null;
			
			if(!categories.isEmpty()){
				String[] array = categories.split(",");
				categoriesList = Arrays.asList(array);
			}
			
			try{
			
				item = load( Item.class ,slug);
				
				if(item == null){
					item = new Item();
					item.setDateCreated( System.currentTimeMillis() );
				}
				
				item.setSlug( slug );
				item.setAbout(about);
				item.setDescription(description);
				item.setName( name );
				item.setDatePublished( System.currentTimeMillis() );
				item.setDateModified( System.currentTimeMillis() );
				item.setCategories(categoriesList); 
				item.setThumbnailUrl( thumbnailUrl );      
				item.setActive(active);
				
				item.setAds_horizont1(ads_horizont1);
				item.setAds_horizont2(ads_horizont2);
				item.setAds_side1(ads_side1);
				item.setAds_side2(ads_side2);
				
				item.setFb_share( fb_share );
				item.setVk_share( vk_share ); 
				item.setLj_share( lj_share ); 
				item.setTwitter_share( twitter_share ); 
				
				if(link_title!=null){
					item.setLink_title(link_title);
				}else{
					item.setLink_title(EnvironmentConfig.getInstance().getMeta_link_title()+ item.getName() );
				}
				
				if(alt!=null){
					item.setAlt( alt );
				}else{
					item.setAlt(EnvironmentConfig.getInstance().getMeta_icon() + item.getName() );
				}
				
				if(meta_title != null){
					item.setMeta_title(meta_title);
				}else{
					item.setMeta_title(item.getName()+EnvironmentConfig.getInstance().getMeta_title());
				}
				
				if(meta_keywords != null){
					item.setMeta_keywords(meta_keywords);
				}else{
					item.setMeta_keywords(EnvironmentConfig.getInstance().getMeta_keywords()+ item.getName() );
				}
				
				if(meta_description != null){
					item.setMeta_description(meta_description);
				}else if(fb_share != null){
					item.setMeta_description(fb_share + EnvironmentConfig.getInstance().getMeta_description());
				}
				
				item =  save(item);
				saveItemCategory(item, categoriesList,active);
				
				SitemapIndex sitemap = load(SitemapIndex.class, "1");
				sitemap.setRecipe_date_modified (System.currentTimeMillis());
				save(sitemap);
				
			}catch(Exception e ) {
				log.severe("newitem.getitem_name(): " + name);
				log.severe(StringUtil.exceptionFormat( e ));
			}
		
		return item;
	}
	public void saveItemCategory(Item item, List<String> slug_list){
		log.info("Start  saveItemCategory");
		saveItemCategory( item, slug_list, false);
		log.info("end  saveItemCategory");
	}
	private void saveItemCategory(Item item, List<String> slug_list, Boolean active) {
		if(item == null || slug_list.isEmpty() ) return;
		
		CategoryService categoryService = new CategoryService(); 
		
		try {
			for(String cat_slug:slug_list){
				cat_slug = cat_slug.trim();
				log.info("X_CategoryItem:: cat_slug "+cat_slug);
			    Category category =  categoryService.loadCategory(cat_slug);
			    log.info(" category.getSlug() " + category.getSlug() );
			   
			    X_CategoryItem x = null;
			    x = ofy().load().type(X_CategoryItem.class).filter("cat_slug",cat_slug).filter("item_slug", item.getSlug() ).first().now();
				
			    if( x == null){
					x = new X_CategoryItem();
				}
				
			    if(category!=null){
			    	x.setActive(active);
			    	x.setCat_slug( cat_slug );
			    	x.setItem_slug( item.getSlug() );
			    	x.setDateCreated(System.currentTimeMillis());
			    	save(x);
			    	log.info(" x.getCat_slug(): "+x.getCat_slug() );
			    }
				
			}	
		} catch (Exception e) {
			log.severe("item.getSlug(): " + item.getSlug());
			log.severe(StringUtil.exceptionFormat( e ));
		}
	
	}
	
	}
