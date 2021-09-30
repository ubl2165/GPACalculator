/*
 * Name:  Ji Li
 * Assignment:  Assignment 3
 * Description: javascript file for load customized navigation
 * menu in all pages.
 *
 */

/*The path for the loaded page*/
path1 = window.location.pathname;

/* This function is used to hide the navigation item according to 
the page's path
*/
function hideMenuItem(path) {
	
	/*document.getElementById("demoPage").innerHTML = "Page path is: " + path;*/
	
	switch (path) {
		case "/":
		case "/index.htm":
		case "/index.html":
			document.getElementById("Main").setAttribute("class", "hidden");
			
			break;
		case "/evaluation":
			document.getElementById("Evaluation Calculator").setAttribute("class", 
			"hidden");
			
			break;
		case "/gpa":
			document.getElementById("GPA Calculator").setAttribute("class", 
			"hidden");
		
			break;
		case "/course":
			document.getElementById("Add Course").setAttribute("class", 
			"hidden");
		
			break;
	}

}

/*call hide MenuItem method to hide the menu item accordingly*/
hideMenuItem(path1);

