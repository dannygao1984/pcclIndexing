package gh.polyu.index;

import gh.polyu.apps.tweetRetriver;
import gh.polyu.apps.tweetTrending;
import gh.polyu.conf.dbConf;
import gh.polyu.core._Tweet;
import gh.polyu.edu.utility.propertyHandle;
import gh.polyu.utility.StringOper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

public class IndexTweetInSolr {

	private SolrServer server = null; 
	private Properties prop = null;
	
	public IndexTweetInSolr(String config)
	{
		propertyHandle dbProper = new propertyHandle();
		prop = dbProper.getProperties("properties/solrServer.properties");
		
		server = new HttpSolrServer(prop.getProperty("DEFAULT_URL"));
	}
	
	public void ServerOptimize()
	{
		try {
			server.optimize();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int DoIndexing(ArrayList<_Tweet> listTweets)
	{
		
		ArrayList<SolrInputDocument> listDoc = new ArrayList<SolrInputDocument>();
		for(_Tweet twt : listTweets)
		{
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", twt.getTweetId());
			doc.addField("name", twt.getTweetFromUserName());
		    doc.addField("fromUser", twt.getTweetFromUserId());	
		    doc.addField("toUser", twt.getTweetToUserId());	
			doc.addField("text", twt.getTweetText());
			doc.addField("twtText", twt.getTweetText());
			//System.out.println(twt.getTweetText());
			doc.addField("twtUrl", twt.getTweetId());
			doc.addField("postDate", twt.getTweetDate().toString());
			listDoc.add(doc);			
		}
		
		try {
			
			UpdateResponse response = server.add(listDoc);
			server.commit();			 
	        return listDoc.size();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public final void fail(Object o) {  
        System.out.println(o);  
    }  
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		
		IndexTweetInSolr solrIndex = new IndexTweetInSolr("properties/solrServer.properties");
		String[] listTable = dbConf.TABLELIST;
		tweetRetriver retTwt = new tweetRetriver();
		tweetTrending retTrd = new tweetTrending();
		
		int iTopicCnt = 0;
		for(String table : listTable)
		{
			System.out.println("Processing the table: " + table);
			ArrayList<String> listTrend = retTrd.getTrending(table);
						
			for(String trd : listTrend)
			{
				if(!StringOper.IsAllEnglishLetterORNumberSign(trd))
				{
					continue;
				}
				
				//System.out.print("Indexing " + iTopicCnt + " " + trd);
				
				ArrayList<_Tweet> listTweet = retTwt.getTweet(table, trd);
				int iIndexedTwt = solrIndex.DoIndexing(listTweet);
				
				System.out.println(iTopicCnt + ": Totally " + iIndexedTwt + " tweets are " +
						"indexed for trending " + trd);
				iTopicCnt ++;
				if(iTopicCnt%100 == 0)
				{
					solrIndex.ServerOptimize();
				}
			}	
			
		}
		System.out.println("Totally index: " + iTopicCnt + " trends!");
		
	}

}
