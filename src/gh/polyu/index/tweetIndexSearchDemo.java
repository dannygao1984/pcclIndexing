package gh.polyu.index;


import java.awt.List;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

import gh.polyu.core._Tweet;
import gh.polyu.dbTweet.twitterRetriver;

import org.apache.solr.client.solrj.impl.HttpSolrServer; 
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;

public class tweetIndexSearchDemo {

	private static final String DEFAULT_URL = "http://localhost:8983/solr/"; 
	private SolrServer server = null;   
    
	public tweetIndexSearchDemo()
    {
		server = new HttpSolrServer(DEFAULT_URL);   	
    }	
    
    public final void fail(Object o) {  
        System.out.println(o);  
    }  
	
	public void DoIndexing(ArrayList<_Tweet> listTweets)
	{
				
		ArrayList<SolrInputDocument> listDoc = new ArrayList<SolrInputDocument>();
		for(_Tweet twt : listTweets)
		{
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", twt.getTweetId());
			doc.addField("name", twt.getTweetFromUserId());
			doc.addField("manu", twt.getTweetText());
			doc.addField("url", twt.getTweetId());
			System.err.println("name:" + twt.getTweetFromUserId());
			listDoc.add(doc);			
		}
		
		try {
			
			UpdateResponse response = server.add(listDoc);
			fail(server.commit());
			fail(response);  
	        fail("query time：" + response.getQTime());  
	        fail("Elapsed Time：" + response.getElapsedTime());  
	        fail("status：" + response.getStatus()); 
	        
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void search(String key) {
		// TODO Auto-generated method stub
		
		SolrQuery query = new SolrQuery(key); 
		
		query.setQuery(key);
		try {      
            
            query.setHighlight(true);  
            //设置开头   
            query.setHighlightSimplePre("<span class='highlight'>");  
            query.setHighlightSimplePost("</span>"); //设置结尾   
            query.setStart(0);   
            query.setRows(10);//设置行数   
            //设置高亮的哪些区域   
            query.setParam("hl.fl", "content");   
            QueryResponse response= this.server.query(query);   
            SolrDocumentList list=response.getResults();   
               
            System.out.println("高亮显示：");   
            for(SolrDocument sd:list)
            {   
                String id=(String) sd.getFieldValue("id"); 
                String name = (String) sd.getFieldValue("fromUser");
                String text = (String) sd.getFieldValue("twtText");
                String date = (String) sd.getFieldValue("postDate");
                if(response.getHighlighting().get(id)!=null){   
                    System.out.println(id + " @" + name + " " + date + " " + text);
                }
            }   
               
            System.out.println("-------------------");   
               
            SolrDocumentList docs = response.getResults();     
            
            System.out.println("文档个数：" + docs.getNumFound());     
            System.out.println("查询时间：" + response.getQTime());   
            System.out.println("-------------------");   
               
//            for (SolrDocument doc : docs) {     
//                   
//                // 获取查询返回结果   
//                String id = doc.getFieldValue("id").toString();     
//                String name = doc.getFirstValue("name").toString();     
//                String text = doc.getFieldValue("twtText").toString();
//                
//                // 打印查询结果   
//                System.out.println("编号："+id);     
//                System.out.println("标题："+name);    
//                System.out.println("内容: "+text);    
//           
//                System.out.println("-------------------");   
//                   
//            }     
//               
  
        } catch (SolrServerException e) {     
             e.printStackTrace();     
        }  
	}
	
	public void search_deprecated(String query) {  
        SolrParams params = new SolrQuery(query);  
          
        try {
        	
            QueryResponse response = server.query(params);  
              
            SolrDocumentList list = response.getResults();  
            for (int i = 0; i < list.size(); i++) {  
                fail(list.get(i));  
            }  
        } catch (SolrServerException e) {  
            e.printStackTrace();  
        }  
    } 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		twitterRetriver retriver = new twitterRetriver();
//		ArrayList<_Tweet> listTweets = retriver.DBRetriveTweet("Jon Diebler", 30);

		tweetIndexSearchDemo index = new tweetIndexSearchDemo();
//		index.DoIndexing(listTweets);
		index.search("Howard");
	}

}
