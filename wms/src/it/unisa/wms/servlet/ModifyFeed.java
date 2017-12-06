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
@WebServlet("/ModifyFeed")
public class ModifyFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyFeed() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("questa è una prova");
		String c=request.getParameter("categ");
		KnowledgeBase kb=new KnowledgeBase();
		//List<RSS> lista=new ArrayList<RSS>();
		//lista=kb.getRSSByCategory("http://www.wms.net/ontology/"+converti(c));
		String risposta="<div id='col1' class='col-xs-6 col-md-4' style='visibility: ;'><div id='listarss' class='list-group' style='overflow:auto;'><a href='#' class='list-group-item active'><h4 id='titolo'>Rss "+c.split("#")[1]+"</h4></a>";
	    for(RSS feed:kb.getRSSByCategory(c))
	   // for(int i=0;i<lista.size();i++){
	   // 	risposta+="<a class='list-group-item'>"+lista.get(i)+"</a>";
	    	risposta+="<a class='list-group-item'>"+feed.getURL()+"</a>";
	 
	  //  }
	    risposta+="</div><button type='button' onclick='addRss()' class='btn btn-success btn-circle btn-xl'><i class='glyphicon glyphicon-plus'></i></button></div>";
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
