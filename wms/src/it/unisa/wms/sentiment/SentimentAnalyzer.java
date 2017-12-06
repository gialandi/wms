package it.unisa.wms.sentiment;

import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.alchemyapi.api.AlchemyAPI;

public class SentimentAnalyzer {

	private static final String API_KEY = "c3baef1006c370b944f29ace0c0c2fd5d1849538";
	private static final AlchemyAPI alchemy = AlchemyAPI.GetInstanceFromString(API_KEY);

	public static String getSentimentScore(String text){
		try {
			Document doc = alchemy.TextGetTextSentiment(text);
			return getSentimentScoreFromDocument(doc);
		} catch (XPathExpressionException | IOException | SAXException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	private static String getSentimentScoreFromDocument(Document doc){
		String s = getStringFromDocument(doc);
		final Pattern pattern = Pattern.compile("<score>(.+?)</score>");
		final Matcher matcher = pattern.matcher(s);
		matcher.find();
		try{
			return (matcher.group(1));
		} catch (Exception e) {
			return "0";
		}
	}

	// utility method
	private static String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);

			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}


}
