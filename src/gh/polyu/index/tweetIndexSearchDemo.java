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
	        fail("query time��" + response.getQTime());  
	        fail("Elapsed Time��" + response.getElapsedTime());  
	        fail("status��" + response.getStatus()); 
	        
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
            //���ÿ�ͷ   
            query.setHighlightSimplePre("<span class='highlight'>");  
            query.setHighlightSimplePost("</span>"); //���ý�β   
            query.setStart(0);   
            query.setRows(10);//��������   
            //���ø�������Щ����   
            query.setParam("hl.fl", "content");   
            QueryResponse response= this.server.query(query);   
            SolrDocumentList list=response.getResults();   
               
            System.out.println("������ʾ��");   
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
            
            System.out.println("�ĵ�������" + docs.getNumFound());     
            System.out.println("��ѯʱ�䣺" + response.getQTime());   
            System.out.println("-------------------");   
               
//            for (SolrDocument doc : docs) {     
//                   
//                // ��ȡ��ѯ���ؽ��   
//                String id = doc.getFieldValue("id").toString();     
//                String name = doc.getFirstValue("name").toString();     
//                String text = doc.getFieldValue("twtText").toString();
//                
//                // ��ӡ��ѯ���   
//                System.out.println("��ţ�"+id);     
//                System.out.println("���⣺"+name);    
//                System.out.println("����: "+text);    
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
