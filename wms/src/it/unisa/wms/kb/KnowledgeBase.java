package it.unisa.wms.kb;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.google.gson.Gson;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

import it.unisa.wms.bean.Category;
import it.unisa.wms.bean.News;
import it.unisa.wms.bean.RSS;
import it.unisa.wms.bean.Topic;
import virtuoso.jena.driver.*;

public class KnowledgeBase {

	private static final String EXPORT_FILE_NAME = "/Users/raffaeleschiavone/Desktop/export_wms.ttl";
	private static final Logger LOGGER = Logger.getLogger( KnowledgeBase.class.getName() );
	private static final Level LOGGER_LEVEL = Level.INFO;

	private static final String PASSWORD = "dba";
	private static final String USER = PASSWORD;

	private static final String URL_VIRTUOSO = "jdbc:virtuoso://192.168.53.130:1111";
	//private static final String URL_VIRTUOSO = "jdbc:virtuoso://192.168.0.107:1111";

	public static final String NS = "http://www.wms.net/ontology/";
	public static final String URI_RSS = NS+"RSS";
	public static final String NS_DBPEDIA = "http://it.dbpedia.org/resource/";

	private static final String PREFIX = "PREFIX wms: <http://www.wms.net/ontology/>\n"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";


	private VirtGraph DATASET;

	public static void main(String[] args) {
		System.out.println(formatDate("10-12-2015"));
		Gson gson = new Gson();
		KnowledgeBase kb = new KnowledgeBase();
		kb.exportToFile();
		//System.out.println(gson.toJson(kb.getCategories()));
		//		System.out.println(gson.toJson(kb.getRSS()));
	}


	public KnowledgeBase(){
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(LOGGER_LEVEL);
		LOGGER.addHandler(handler);
		DATASET = new VirtGraph (URL_VIRTUOSO, USER, PASSWORD);
	}

	public static String generateUri(String string) {
		return NS+string+"#"+UUID.randomUUID();
	}

	public String insertRSS(String uriCategory, String url){
		String uri = generateUri("RSS");
		String query = PREFIX+"INSERT INTO GRAPH <"+NS+"> { "
				+ "<"+uri+"> rdf:type wms:RSS ."
				+ "<"+uri+"> wms:hasCategory <"+uriCategory+">."
				+ "<"+uri+"> wms:URL \""+url+"\" ."
				+ "<"+uri+"> rdf:label \""+url+"\" "
				+ "}";
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, DATASET);
		vur.exec();
		return uri;
	}


	public String insertCategory(String description){
		String uri = generateUri("RSS");
		String query = PREFIX+"INSERT INTO GRAPH <"+NS+"> { "
				+ "<"+uri+"> a wms:Category ."
				+ "<"+uri+"> wms:description \""+description+"\" ."
				+ "<"+uri+"> rdf:label \""+description+"\" "
				+ "}";
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, DATASET);
		vur.exec();
		return uri;
	}
	public News getNewsByURL(String url){
		String query = PREFIX+"SELECT ?uri FROM <"+NS+"> WHERE {"
				+ "?uri a wms:News . "
				+ "?uri wms:URL \""+url+"\" . "
				+ " OPTIONAL {?uri wms:description ?desc}"
				+ "}";
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, DATASET);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution rs = results.nextSolution();
			News news = new News();
			news.setURL(rs.get("uri").toString());
			news.setURL(url);
			if(rs.get("desc")!=null) news.setDescription(rs.get("desc").toString());
			return news;
		}
		return null;
	}

	public String insertNews(News news) {
		if(getNewsByURL(news.getURL())!=null) return news.getURL();
		String uri = generateUri("News");
		String query = PREFIX+"INSERT INTO GRAPH <"+NS+"> { "
				+ "<"+uri+"> a wms:News ."
				+ "<"+uri+"> wms:title \""+news.getTitle()+"\" ."
				+ "<"+uri+"> wms:URL \""+news.getURL()+"\" ."
				+ "<"+uri+"> wms:description \""+news.getDescription()+"\" ."
				+ "<"+uri+"> wms:hasCategory <"+news.getCategory()+"> ."
				+ "<"+uri+"> wms:pubDate \""+news.getPubDate()+"\"^^xsd:dateTime ."
				+ "<"+uri+"> wms:sentimentScore \""+news.getSentimentScore()+"\"^^xsd:double ."
				+ "<"+uri+"> rdf:label \""+news.getTitle()+"\" ";
		for(String topic : news.getTopics()) {
			query += ". <"+uri+"> wms:hasTopic <"+topic+"> ";
			query += ". <"+topic+"> rdf:type wms:Topic";
			query += ". <"+topic+"> wms:description \""+topic.replace(NS, "")+"\" ";
			query += ". <"+topic+"> rdf:label \""+topic.replace(NS, "")+"\" ";
			query += ". <"+topic+"> owl:sameAs <"+topic.replace(NS,NS_DBPEDIA)+"> ";
		}
		query += "}";
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(query, DATASET);
		try{
			vur.exec();
		} catch (Exception e){
			System.out.println("ERRORE QUERY:ERRORE QUERY:ERRORE QUERY: \n"+query);
			e.printStackTrace();
		}
		return uri;
	}

	public List<RSS> getRSS(){
		String query = PREFIX+"SELECT ?uri ?url ?desc ?category FROM <"+NS+"> WHERE {"
				+ "?uri rdf:type wms:RSS . "
				+ "?uri wms:URL ?url . "
				+ "?uri wms:hasCategory ?category "
				+ "OPTIONAL {?uri wms:description ?desc}"
				+ "}";
		List<RSS> toReturn = new ArrayList<RSS>();
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, DATASET);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution rs = results.nextSolution();
			RSS rss = new RSS();
			rss.setURI(rs.get("uri").toString());
			rss.setURL(rs.get("url").toString());
			rss.setCategory(rs.get("category").toString());
			if(rs.get("desc")!=null) rss.setDescription(rs.get("desc").toString());
			toReturn.add(rss);
		}
		return toReturn;
	}

	public Map<Category, Integer> getCategories(){
		String query = PREFIX+"SELECT ?uri ?desc (COUNT(?rss) as ?count) FROM <"+NS+"> WHERE {"
				+ "?uri rdf:type wms:Category ."
				+ " OPTIONAL {"
				+ "?uri wms:description ?desc "
				+ "}"
				+ " OPTIONAL {"
				+ "?rss wms:hasCategory ?uri ."
				+ "?rss rdf:type wms:RSS "
				+ "}"
				+ "} GROUP BY ?uri ?desc";
		System.out.println(query);
		Map<Category, Integer> toReturn = new HashMap<Category, Integer>();
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, DATASET);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution rs = results.nextSolution();
			Category cat = new Category();
			cat.setURI(rs.get("uri").toString());
			if(rs.get("desc")!=null) 
				cat.setDescription(rs.get("desc").toString());
			else 
				cat.setDescription(cat.getURI());
			toReturn.put(cat, rs.get("count").asLiteral().getInt());
		}
		return toReturn;
	}

	public List<RSS> getRSSByCategory(String uriCategory){
		String query = PREFIX+"SELECT ?uri ?url ?pubDate ?desc FROM <"+NS+"> WHERE {"
				+ "?uri rdf:type wms:RSS ."
				+ "?uri wms:hasCategory <"+uriCategory+"> . "
				+ "?uri wms:URL ?url . "
				+ "OPTIONAL {?uri wms:description ?desc}"
				+ "}";
		List<RSS> toReturn = new ArrayList<RSS>();
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, DATASET);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution rs = results.nextSolution();
			RSS rss = new RSS();
			rss.setURI(rs.get("uri").toString());
			rss.setURL(rs.get("url").toString());
			rss.setCategory(uriCategory);
			if(rs.get("desc")!=null) 
				rss.setDescription(rs.get("desc").toString());
			else 
				rss.setDescription(rss.getURL());
			toReturn.add(rss);
		}
		return toReturn;
	}

	/**
	 * Restituisce una mappa di coppie k,v dove k è un topic e v è il numero di occorrenze di quel topic nel periodo startDate-endDate
	 * @param uriCategory
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Map<Topic, Integer> getBestTopics(String uriCategory, String startDate, String endDate){
		String start="\""+formatDate(startDate).toString()+"\"^^xsd:dateTime";
		String end="\""+formatDate(endDate).toString()+"\"^^xsd:dateTime";
		String query = PREFIX + "SELECT ?uri_topic ?description (COUNT(?uri_topic) as ?count) (AVG(?sentiment) as ?sentimentScore) FROM <"+NS+"> WHERE {"
				+ "?uri_news wms:hasTopic ?uri_topic . "
				+ "?uri_news wms:pubDate ?pubDate . "
				+ "OPTIONAL{?uri_news wms:sentimentScore ?sentiment} . "
				+ "?uri_news wms:hasCategory <"+uriCategory+"> . "
				+ " OPTIONAL { ?uri_topic wms:description ?description } ."
				+ "FILTER (?pubDate >= "+start+") . "
				+ "FILTER (?pubDate <= "+end+")"
				+ "} GROUP BY ?uri_topic ?description "
				+ "ORDER BY DESC(?count)";
		Map<Topic,Integer> toReturn = new LinkedHashMap<Topic, Integer>();
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, DATASET);
		ResultSet results = vqe.execSelect();
		while (results.hasNext()) {
			QuerySolution rs = results.nextSolution();
			int numberOfNews = rs.get("count").asLiteral().getInt();
			double totalSentimentScore = rs.get("sentimentScore").asLiteral().getDouble();
			Topic topic = new Topic();
			topic.setURI(rs.get("uri_topic").toString());
			topic.setSentimentScore(totalSentimentScore);
			if(rs.get("description") != null) 
				topic.setDescription(rs.get("description").toString());
			else 
				topic.setDescription(topic.getURI());
			toReturn.put(topic, numberOfNews);
		}
		return toReturn;
	}

	public static String formatDate(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String dt = formatter.format(date);
		return dt;
	}

	public static String formatDate(String date){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		try {
			d = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formatDate(d);
	}


	public String getTopics(String topic) {

		String t = topic;
		System.out.println(t); 

		String query = PREFIX + "SELECT ?s ?p ?o FROM <"+NS+"> WHERE {?s ?p <http://www.wms.net/ontology/"+topic+">}";
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, DATASET);
		ResultSet results = vqe.execSelect();
		
		if(results.hasNext()){
			return topic;
		}
		
		return "NON_TROVATO";
	}
	
	public void exportToFile(){
		exportToFile(EXPORT_FILE_NAME);
	}
	
	public void exportToFile(String filename){
		String query = PREFIX+"SELECT ?s ?p ?o FROM <"+NS+"> WHERE { ?s ?p ?o }";
		Query sparql = QueryFactory.create(query);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, DATASET);
		ResultSet results = vqe.execSelect();
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			while (results.hasNext()) {
				QuerySolution rs = results.nextSolution();
				String s = getParameterForExport(rs.get("s"));
				String p = getParameterForExport(rs.get("p"));
				String o = getParameterForExport(rs.get("o"));
				writer.write( s + " " + p + " " + o + " . \n");
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private String getParameterForExport(RDFNode rdfNode) {
		if((rdfNode.isURIResource()) || (rdfNode.isResource())) return "<"+rdfNode.toString()+">";
		if(rdfNode.isLiteral()) return "\""+rdfNode+"\"";
		return rdfNode.toString();
	}


}