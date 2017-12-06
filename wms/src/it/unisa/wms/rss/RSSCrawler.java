package it.unisa.wms.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import it.crmpa.activiti.ws.client.ActivitiWsClient;
import it.crmpa.activiti.ws.client.stub.ActivitiServiceStub.Category;
import it.unisa.wms.bean.News;
import it.unisa.wms.bean.RSS;
import it.unisa.wms.kb.KnowledgeBase;
import it.unisa.wms.sentiment.SentimentAnalyzer;

public class RSSCrawler {

	private ActivitiWsClient wikipedia;
	
	public RSSCrawler() {
		try {
			wikipedia = new ActivitiWsClient();
		} catch (AxisFault e) {
			e.printStackTrace();
		}
	}

	/*Aggiorna una base di conoscenza dall'ultimo update
	 * 
	 */
	public void updateNewsInKb(KnowledgeBase kb){
		List<RSS> list = kb.getRSS();
		for(RSS rss : list){
			try {
				List<News> listNews = getNewsFromRSS(rss);
				for(News news : listNews) {
					Category[] topics = wikipedia.getCategoriesFromText(news.getTitle());
	                news.setTopics(topics);
	                news.setSentimentScore(SentimentAnalyzer.getSentimentScore(news.getTitle()));
	                System.out.println(news.getTitle() + ": "+news.getSentimentScore());
					kb.insertNews(news);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}

	private List<News> getNewsFromRSS(RSS rss) throws IOException {
		System.out.println("getNewsFrom "+rss.getURL());
		//La nostra sorgente Feed
        URL url  = new URL(rss.getURL());
        XmlReader reader = null;
        List<News> toReturn = new ArrayList<News>();

        try {
            //Istanziamo uno stream reader dall'url della nostra sorgente feed
            reader = new XmlReader(url);
            //Diamo in pasto il nostro reader al parser RSS
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (Iterator<SyndEntry> i = feed.getEntries().iterator(); i.hasNext();) {
                SyndEntry entry = i.next();
                News news = new News();
                news.setCategory(rss.getCategory());
                news.setDescription(entry.getDescription().getValue());
                news.setPubDate(formatDate(entry.getPublishedDate()));
                news.setTitle(entry.getTitle());
                news.setURL(entry.getLink());
                
                toReturn.add(news);
                
              
//                System.out.println("titolo:" + entry.getTitle());
//                System.out.println("link:" + entry.getLink());
//                System.out.println("descrizione:" + entry.getDescription().getValue()+"n");
//                System.out.println("pubdate:" + entry.getPublishedDate().toString());
//                System.out.println("\n\n\n\n");
            }
         } catch (IllegalArgumentException | FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            //Chiudiamo lo stream precedentemente aperto.
            if (reader != null) reader.close();
         }
        return toReturn;
	}

	public static void main(String[] args) throws IOException, IllegalArgumentException, FeedException {
		KnowledgeBase kb = new KnowledgeBase();
		RSSCrawler rss = new RSSCrawler();
		rss.updateNewsInKb(kb);
	//	System.out.println(formatDate("Tue, 05 Jan 2016 15:02:39 UTC"));
		System.out.println("FATTO");
		
	}
	
	public static String formatDate(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String dt = formatter.format(date);
		return dt;
	}

}
