package it.unisa.wms.servlet;

import it.unisa.wms.bean.Category;
import it.unisa.wms.bean.Topic;
import it.unisa.wms.kb.KnowledgeBase;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetViral
 */
@WebServlet("/GetViral")
public class GetViral extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetViral() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/xml;charset=UTF-8");
		String c=request.getParameter("categ");
		String di=request.getParameter("di");
		String df=request.getParameter("df");
		//gestisci date per interrogare KB
		//query interroga KB per recuperare dati
		KnowledgeBase kb=new KnowledgeBase();
		Map<Topic,Integer> viral = kb.getBestTopics(c, di, df);
		PrintWriter writer=response.getWriter();
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.append("<lista>");
		for(Topic t:viral.keySet())
		{
			writer.append("<item>").append("<topic><![CDATA[").append(t.getURI()).append("]]></topic>").append("<descrizione><![CDATA[").append(t.getDescription()).append("]]></descrizione>").append("<count>").append(Double.toString(t.getSentimentScore())).append("</count>").append("</item>");
//			System.out.println(t.getURI());
//			System.out.println(t.getDescription());
//			System.out.println(t.getSentimentScore());
			
		}

		writer.append("</lista>");
	
		
		
	}		
		
		
		
		
		
		
		
		
		
//		PrintWriter writer= response.getWriter();
//		writer.append("<div id='viralBox' class='col-xs-6 col-md-4'><div class='list-group' ><a href='#' class='list-group-item active'><h4>Viral Concept</h4></a><div style=' max-height: 300px; overflow-y:scroll;'>");
//		for ( Topic key : viral.keySet() ) {
//			writer.append("<a href='#' id='"+key.getURI()+"' data-toggle='modal' data-target='.bs-example-modal-lg' onclick='funzione_palle(this.id);return false;'   class='list-group-item'>"+key.getDescription()+"</a>");
//		}
//		writer.append("</div></div></div>"); 
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	public String converti(String s){
		String a=s.substring(0, 1);
		a=a.toUpperCase();
		String res=a+s.substring(1);
		return res;
		
	}

}
