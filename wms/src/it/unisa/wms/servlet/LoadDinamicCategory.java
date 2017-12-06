package it.unisa.wms.servlet;

import it.unisa.wms.bean.Category;
import it.unisa.wms.bean.RSS;
import it.unisa.wms.kb.KnowledgeBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DispCategory
 */
@WebServlet("/LoadDinamicCategory")
public class LoadDinamicCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadDinamicCategory() {
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
		Map<Category, Integer> map = kb.getCategories();
		//List<RSS> lista=new ArrayList<RSS>();
		//lista=kb.getRSSByCategory("http://www.wms.net/ontology/"+converti(c));
		String risposta="<div id='list-rss' class='list-group'>"
				+ "<a href='#' class='list-group-item active'>Scelta categorie</a>";
		for(Category cat: map.keySet())
			risposta+= "<a href='#' onclick='loadRssModify(\""+cat.getURI()+"\",\""+cat.getDescription()+"\");' "
			+ " class='list-group-item'>"+cat.getDescription()+"<span id='' class='badge'></span></a>";
	
		risposta+="</div><button type='button' onclick='addCategory()' class='btn btn-success btn-circle btn-xl'><i class='glyphicon glyphicon-plus'></i></button></div>";
	    
	    response.getWriter().append(risposta);
	    
	    
	    
//	    <a href="#" class="list-group-item active">
//	    <h4>Scelta categorie</h4>
//	  </a>
//	    <a href="#" onclick="loadRssModify('cronaca');return false;"   class="list-group-item">cronaca<span id="spancronaca" class="badge"></span></a>
//		  <a href="#"  onclick="loadRssModify('sport');return false;" class="list-group-item">sport<span id="spansport" class="badge"></span></a>
//		  <a href="#" onclick="loadRssModify('cinema');return false;" class="list-group-item">cinema<span id="spancinema" class="badge"></span> </a>
//		  <a href="#" onclick="loadRssModify('economia');return false;" class="list-group-item">economia<span id="spaneconomia" class="badge"></span> </a>
//			<a href="#" onclick="loadRssModify('politica');return false;" class="list-group-item">politica<span id="spanpolitica" class="badge"></span> </a>
//			<a href="#" onclick="loadRssModify('cultura');return false;" class="list-group-item">cultura<span id="spancultura" class="badge"></span> </a>
//			<a href="#" onclick="loadRssModify('tecnologia');return false;" class="list-group-item">tecnologia<span id="spantecnologia" class="badge"></span> </a>
	
	   
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
