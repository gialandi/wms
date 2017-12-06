package it.unisa.wms.bean;

public class Topic {
	
	private String URI;
	private String description;
	private Double sentimentScore;
	
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getSentimentScore() {
		return sentimentScore;
	}
	public void setSentimentScore(Double sentimentScore) {
		this.sentimentScore = sentimentScore;
	}

}
