package it.unisa.wms.servlet;

import it.unisa.wms.bean.Category;
import it.unisa.wms.bean.RSS;
import it.unisa.wms.kb.KnowledgeBase;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CaricaNFeed
 */
@WebServlet("/CaricaNFeed")
public class CaricaNFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CaricaNFeed() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		KnowledgeBase kb=new KnowledgeBase();
		Map<Category,Integer> map=kb.getCategories();
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter writer=response.getWriter();
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.append("<lista>");
		Set<Category> c=map.keySet();
		for(Category i:c){
			writer.append("<item>").append("<categoria>").append(i.getURI()).append("</categoria>").append("<descrizione>").append(i.getDescription()).append("</descrizione>").append("<count>").append(map.get(i).toString()).append("</count>").append("</item>");
		}

		writer.append("</lista>");
		
		/*
		 * il file xml creato sarà del tipo
		 * <lista>
		 * 	<item>
		 * 		<categoria>spanCATEGORIA</cateogria>
		 * 		<count>NUMERO</count>
		 * 	</item>
		 * ...
		 * </lista>		
		 */
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
