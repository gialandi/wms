package it.unisa.wms.kb;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.unisa.wms.bean.Topic;
import it.unisa.wms.utilities.*;
public class WikipediaMinerClient {
	
	public static final String WIKIPEDIAMINER_URL ="http://193.205.191.123:8110/wikipediaminer/services/wikify?";
	public static final double MIN_PROBABILITY = 0.01;
	public List<Topic> getTopics(String text){
		text = DateParser.removeDate(text);
		List<Topic> toReturn = new ArrayList<Topic>();
		try {
			String encodedText = URLEncoder.encode(text, "UTF-8");
			String url = WIKIPEDIAMINER_URL;
			url += "responseFormat=json";
			url += "&minProbability="+MIN_PROBABILITY;
			url += "&source="+encodedText;
			JSONObject json = readJsonFromUrl(url);
			System.out.println(WIKIPEDIAMINER_URL+encodedText);
			if(!json.has("detectedTopics")) return toReturn;
			System.out.println(json);
			JSONArray topics = json.getJSONArray("detectedTopics");
			for(int i=0; i < topics.length(); i++){
				JSONObject topic = topics.getJSONObject(i);
				Topic t = new Topic();
				System.out.println(topic.getInt("id"));
				System.out.println(topic.getString("title"));
				System.out.println(topic.getDouble("weight"));
				toReturn.add(t);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
	
	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		BufferedReader rd = new BufferedReader(new InputStreamReader(yc.getInputStream(),Charset.forName("UTF-8")));
		try {
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			rd.close();
		}
	}

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		WikipediaMinerClient client = new WikipediaMinerClient();
		List<Topic> x = client.getTopics("L’alta pressione se ne va e arriva il freddo su tutta la Penisola con pioggia e neve per alleviare l’effetto smog.");
		
	}
}
