package it.unisa.wms.servlet;

import it.unisa.wms.bean.RSS;
import it.unisa.wms.kb.KnowledgeBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DispCategory
 */
@WebServlet("/DispCategory")
public class DispCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DispCategory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		String c=request.getParameter("categ");
		String temp=c.split("#")[1];
		KnowledgeBase kb=new KnowledgeBase();
		List<RSS> lista=new ArrayList<RSS>();
		lista=kb.getRSSByCategory(c);
		System.out.println(c);
		String risposta="<div id='col1' class='col-xs-6 col-md-4' style='visibility: ;'><div id='listarss' class='list-group' style='overflow:auto;'><a href='#' class='list-group-item active'><h4 id='titolo' name='"+c+"'>Rss "+c+"</h4></a>";
	    
	    for(int i=0;i<lista.size();i++){
	    	risposta+="<a class='list-group-item'>"+lista.get(i).getURI()+"</a>";
	 
	    }
	    risposta+="</div></div>";
	    response.getWriter().append(risposta);
	   
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public String converti(String s){
		String a=s.substring(0, 1);
		a=a.toUpperCase();
		String res=a+s.substring(1);
		return res;
		
	}
	
}
