package it.unisa.wms.bean;

import java.util.ArrayList;
import java.util.List;

import it.crmpa.activiti.ws.client.stub.ActivitiServiceStub.Category;
import it.unisa.wms.kb.KnowledgeBase;

public class News {
	
	private String URI;
	private String title;
	private String URL;
	private String description;
	private String pubDate;
	private String category;
	private List<String> topics;
	private double sentimentScore;
	
	public News(){
		topics = new ArrayList<String>();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		title = title.replaceAll("\"", "'");
		title = title.replaceAll("\n", " ");
		this.title = title;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		description = description.replaceAll("\"", "'");
		description = description.replaceAll("\n", " ");
		this.description = description;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public List<String> getTopics() {
		return topics;
	}
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public void setTopics(Category[] topics) {
		this.topics = new ArrayList<String>();
		if(topics == null) return;
		for(Category topic : topics) {
			if(topic.getKeyword() != null) this.topics.add(KnowledgeBase.NS + topic.getKeyword());
		}
	}

	public double getSentimentScore() {
		return sentimentScore;
	}

	public void setSentimentScore(double sentimentScore) {
		this.sentimentScore = sentimentScore;
	}
	
	public void setSentimentScore(String sentimentScore) {
		this.sentimentScore = Double.parseDouble(sentimentScore);
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}
	

}
