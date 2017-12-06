package it.unisa.wms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.crmpa.activiti.ws.client.ActivitiWsClient;
import it.crmpa.activiti.ws.client.stub.ActivitiServiceStub.Category;
import it.unisa.wms.bean.Topic;
import it.unisa.wms.kb.KnowledgeBase;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private ActivitiWsClient wikipedia;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Search() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String testo =request.getParameter("testo");
		wikipedia = new ActivitiWsClient();
		Category[] topics = wikipedia.getCategoriesFromText(testo);

		PrintWriter writer= response.getWriter();
		if(topics != null){
			for (int i = 0; i < topics.length; i++) {
				writer.append("<a class='list-group-item'  onclick='showTopic(\""+topics[i].getKeyword()+"\");'>"+topics[i].getKeyword()+"</a>");
			}
		}else{
			writer.append("<a class='list-group-item' >Nessun risultato trovato... Prova con altre parole</a>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
