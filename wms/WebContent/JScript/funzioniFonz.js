var request=null;

function rss_from_categ(cat,element){
	var url="DispCategory?categ="+encodeURIComponent(cat);
	//alert("ci sono1");
	//load(url);
	request=new XMLHttpRequest();
	request.onreadystatechange=function(){
		if (request.readyState == 4 && request.status == 200) {
			var divRssCateg=request.responseText;
			//alert("cisono3");
			var a=document.getElementById("col1");
			if (a!=null) document.getElementById("mettiloqua").removeChild(a);
			document.getElementById("mettiloqua").innerHTML=divRssCateg;
			document.getElementById("titolo").firstChild.data=desc;
			document.getElementById("col2").style.visibility="visible";
			
			document.getElementById("idBtnInvia").style.visibility="visible";
			parentElement = element.parentElement;
			for (var int = 1; int < parentElement.children.length; int++) {
				var elem = parentElement.children[int];
				elem.setAttribute("class","list-group-item");
				
			}
			element.setAttribute("class","list-group-item selected");
		}
	}
	request.open("GET",url,"true");
	request.send();

}

function loadRssModify(cat,desc){
	URI_SEL=cat;
	var url="ModifyFeed?categ="+encodeURIComponent(cat);
	//alert("ci sono1");
	request=new XMLHttpRequest();
	request.onreadystatechange=function(){
		if (request.readyState == 4 && request.status == 200) {
			var divRssCateg=request.responseText;
			//alert("cisono3");
			var a=document.getElementById("col1");
			if (a!=null) document.getElementById("mettiloqua").removeChild(a);
			document.getElementById("mettiloqua").innerHTML=divRssCateg;
			document.getElementById("titolo").firstChild.data=desc;
		}
	}
	request.open("GET",url,"true");
	request.send();
}

var URI_SEL="";


function loadCategory(){
	var url="LoadDinamicCategory";
	request=new XMLHttpRequest();
	request.onreadystatechange=insertCategory;
	request.open("GET",url,"true");
	request.send();
}

function insertCategory(){
	if (request.readyState == 4 && request.status == 200) {
		document.getElementById("choosebox").innerHTML=request.responseText;
	}
}



function load(url){
	//alert("cisono2");
	request=new XMLHttpRequest();
	request.onreadystatechange=popout;
	request.open("GET",url,"true");
	request.send();
}

function popout(){
	if (request.readyState == 4 && request.status == 200) {
		var divRssCateg=request.responseText;
		//alert("cisono3");
		var a=document.getElementById("col1");
		if (a!=null) document.getElementById("mettiloqua").removeChild(a);
		document.getElementById("mettiloqua").innerHTML=divRssCateg;
	}
}

function popout(){
	if (request.readyState == 4 && request.status == 200) {
		var divRssCateg=request.responseText;
		//alert("cisono3");
		var a=document.getElementById("col1");
		if (a!=null) document.getElementById("mettiloqua").removeChild(a);
		document.getElementById("mettiloqua").innerHTML=divRssCateg;
	}
}

function removeRSS(id)
{
	 document.getElementById(id).remove();
	}

function addRss(){
	var a=document.createElement("a");
	a.setAttribute("class","list-group-item");
	a.innerHTML="<div id='insertnewrss' class='input-group'><input type='text' id='nuovoRss' class='form-control' placeholder='New rss...'><span class='input-group-btn'><button onclick='addToList()' class='btn btn-default' type='button'>Add</button></span></div>";
	document.getElementById("listarss").appendChild(a);
}

function addCategory(){
	var a=document.createElement("a");
	a.setAttribute("class","list-group-item");
	a.innerHTML="<div id='insertnewCategory' class='input-group'><input type='text' id='nuovoCategory' class='form-control' placeholder='New Category...'><span class='input-group-btn'><button onclick='addCategoryToList()' class='btn btn-default' type='button'>Add</button></span></div>";
	document.getElementById("list-rss").appendChild(a);
}

function addCategoryToList(){
	var nrss= document.getElementById("nuovoCategory").value;
	var removing=document.getElementById("insertnewCategory");
	document.getElementById("list-rss").lastChild.removeChild(removing);
	document.getElementById("list-rss").lastChild.innerHTML=nrss;
	insCategory(nrss);
}

function insCategory(cat){
	var url="InsNewCategory?categ="+encodeURIComponent(cat);
	go(url);
}


function addToList(){
	var nrss= document.getElementById("nuovoRss").value;
	var removing=document.getElementById("insertnewrss");
	document.getElementById("listarss").lastChild.removeChild(removing);
	document.getElementById("listarss").lastChild.innerHTML=nrss;
	insFeed(nrss);
	
}

function insFeed(nrss){
	var url="InsNewRssFeed?categ="+encodeURIComponent(URI_SEL)+"&newrss="+encodeURIComponent(nrss);
	go(url);
}

function getCateg(v){
	alert(v.substring(4,v.length))
	return v.substring(4,v.length);
	
}

function go(url){
	request=new XMLHttpRequest();
	request.onreadystatechange=insDone;
	request.open("GET",url,"true");
	request.send();
}

function insDone(){
	if (request.readyState == 4 && request.status == 200) {
		if (request.responseText=="ok") {
			}
	}
}

function carica_number_feed(){
	var url="CaricaNFeed";
	//alert("ci sono1");
	vai(url);
}

function vai(url){
	request=new XMLHttpRequest();
	request.onreadystatechange=getCategoriesandAmount;
	request.open("GET",url,"true");
	request.send();
}

function getCategoriesandAmount(){
	if (request.readyState == 4 && request.status == 200) {
		var r=request.responseXML.documentElement;
		categs=r.getElementsByTagName("categoria");
		desc=r.getElementsByTagName("descrizione");
		amounts=r.getElementsByTagName("count");
		s="<div class='list-group'><a href='#' class='list-group-item active'><h4>Categorie</h4></a>";
		for(i=0;i<categs.length;i++){
			s=s+"<a href='#' onclick='rss_from_categ(\""+categs[i].firstChild.data+"\",this);return false;' class='list-group-item'>"+desc[i].firstChild.data+"<span class='badge'>"+amounts[i].firstChild.data+"</span></a>";
		}
		s=s+"</div>";
		document.getElementById("choosebox").innerHTML=s;
		
	}
}

function getViralNews(){
	btnDate = document.getElementById("idBtnIntervallo").getAttribute("value");
	btnTutte = document.getElementById("idBtnTutte").getAttribute("value");
	if (btnDate == "show" && btnTutte=="show"){
		alert("Scegli intervallo");
		return;
	}
	
	if (btnDate == "show"){
		
	
	if (checkDatePicker()){
		var data_inizio=document.getElementById("datainizio").value;
		var data_fine=document.getElementById("datafine").value;
		var c=document.getElementById("titolo").getAttribute("name");
		
		var url="GetViral?categ="+encodeURIComponent(c)+"&di="+data_inizio+"&df="+data_fine;
		rispondi(url);
	}
	else{
		//fornisci un errore per date non congrue
		alert("Date errate");
	 }
	}
	else{//scelta tutte le date
		var date = new Date();
		var day = date.getDate();
		if(monthIndex < 10){
			day = "0"+(day);
		}
		else{
			day =(day);
		}
		var monthIndex = date.getMonth();
		if(monthIndex < 10){
			monthIndex = "0"+(monthIndex+1);
		}
		else{
			monthIndex =(monthIndex+1);
		}
		var year = date.getFullYear();
		
		

		var c=document.getElementById("titolo").getAttribute("name");
		var url="GetViral?categ="+encodeURIComponent(c)+"&di=1970-01-01&df="+year+"-"+monthIndex+1+"-"+08;
		rispondi(url);
	}
}

function checkDatePicker(){
	if (document.getElementById("datainizio").value<=document.getElementById("datafine").value) return true;
	return false;
}

function rispondi(url){
	request=new XMLHttpRequest();
	request.onreadystatechange=dammiViral;
	request.open("GET",url,"true");
	request.send();
}

function dammiViral(){
	if (request.readyState == 4 && request.status == 200) {
		//mostra la tabella contenente le viral news
		var r="";
		var src="";
		r=request.responseXML;
		doc=r.documentElement;
		categs=doc.getElementsByTagName("topic");
		desc=doc.getElementsByTagName("descrizione");
		amounts=doc.getElementsByTagName("count");
		s=" ";
		s="<div id='viralBox' class='col-xs-6 col-md-4'><div class='list-group' ><a href='#' class='list-group-item active'><h4>Viral Concept</h4></a><div style=' max-height: 300px; overflow-y:scroll;'>";
		for(i=0;i<categs.length;i++)
		{
			sentiment=amounts[i].firstChild.data;
			if(-1<=sentiment &&sentiment <-0.6) src="img/moltosad.png"
			else 
				if (-0.6<sentiment && sentiment<-0.2) src="img/sad.png"
			else 
				if (-0.2<sentiment && sentiment<0.2) src="img/indifferent.png"
			else 
				if (0.2<sentiment && sentiment<0.6)src="img/felice.png"
			else 
				if (0.6<sentiment && sentiment<=1) src="img/moltofelice.png"
			s=s+"<a href='#' id='"+categs[i].firstChild.data+"' data-toggle='modal' data-target='.bs-example-modal-lg' onclick='funzione_palle(this.id);return false;'   class='list-group-item'>"+desc[i].firstChild.data+"<span class='badge'><img src="+src+" height='20' width='20'></span></a>";
		}
		s=s+"</div></div></div>";
		document.getElementById("resultViral").innerHTML=s;
		
	}
}

function funzione_palle(uriConcept){
	//document.getElementById("provaID").style.display="block";
	document.getElementById("iframeNews").setAttribute("src","LodLive/app_it.html?"+uriConcept);
}

function visibilityBtnIntervallo (){
	var value = document.getElementById("idBtnTutte").getAttribute("value")
	if ((value == "show")){
		document.getElementById("idBtnTutte").style.visibility="hidden";
		document.getElementById("idBtnTutte").setAttribute("value", "hide");
	}
	else {
		document.getElementById("idBtnTutte").style.visibility="visible";
		document.getElementById("idBtnTutte").setAttribute("value", "show");
	}
}

function visibilityBtnTutte (){
	var value = document.getElementById("idBtnIntervallo").getAttribute("value")
	if ((value == "show")){
		document.getElementById("idBtnIntervallo").style.visibility="hidden";
		document.getElementById("idBtnIntervallo").setAttribute("value", "hide");
	}
	else {
		document.getElementById("idBtnIntervallo").style.visibility="visible";
		document.getElementById("idBtnIntervallo").setAttribute("value", "show");
	}
}


function getViralConcepts(){
	document.getElementById("idResultSearch").style.display="block";
	spanContainer = document.getElementById("idInnerHTML");
	while (spanContainer.hasChildNodes()) {   
		spanContainer.removeChild(spanContainer.firstChild);
	}
	var testo = document.getElementById("idInputSearch").value;
	
	var url="Search?testo="+testo;
	
	request=new XMLHttpRequest();
	request.onreadystatechange=function(){
		if (request.readyState == 4 && request.status == 200) {
			var response=request.responseText;
			document.getElementById("idInnerHTML").innerHTML = response;
		}
	}
	request.open("GET",url,"true");
	request.send();
}


function showTopic(topic){

	var url="ShowTopic?topic="+topic;
	
	request=new XMLHttpRequest();
	request.onreadystatechange=function(){
		if (request.readyState == 4 && request.status == 200) {
			var response=request.responseText;
			
			if (response == "NON_TROVATO"){
				alert("Il tuo topic non è nella nostra knowlege base")
			}
			else {
			document.getElementById("iframeNews").setAttribute("src","LodLive/app_it.html?"+response);
			
		        $("#myModal").modal();
		   
			}
		}
	}
	request.open("GET",url,"true");
	request.send();
}
