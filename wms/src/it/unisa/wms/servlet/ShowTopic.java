package it.unisa.wms.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.wms.bean.Topic;
import it.unisa.wms.kb.KnowledgeBase;

/**
 * Servlet implementation class ShowTopic
 */
@WebServlet("/ShowTopic")
public class ShowTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowTopic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String topic = request.getParameter("topic");
		KnowledgeBase kb = new KnowledgeBase();
		
		String  result = kb.getTopics(topic);
		
		if(result.equals("NON_TROVATO")){
			response.getWriter().append("NON_TROVATO");
		}
		else{
			response.getWriter().append("http://www.wms.net/ontology/"+topic);
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
