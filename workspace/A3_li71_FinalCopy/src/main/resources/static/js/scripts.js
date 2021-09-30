/**
 * Wendi Jollymore
 * Ji Li Modified 2021 Aug.	
 */
document.addEventListener("DOMContentLoaded", init);
 
function init() {
 
    // get all the elements with a class name of clink
    links = document.getElementsByClassName("tooltip");

    // add a click event handler to each link
    for (let item of links) {
        item.addEventListener("mouseover", function() {
 			
		//add a 'T' to make id unique.
            div = document.getElementById('T' + item.id);  // display div
			
			/*For debug*/	
/*			console.log(item);
 			console.log(item.id);
		    console.log(item.id.slice(0,9));*/
		
            // invoke the handler that gets a course by Code
            fetch('http://localhost:8080/api/course/get/' + item.id.slice(0,9))
                .then(data => data.json()) // convert to json
               
                // function to execute on the json data
                .then(function(data) {
                    var output = data.title + "<br>"
                        + "Credits: " + data.credits;
 
                    // finally, display the container output in the div
                    div.innerHTML = output;
                });
 
        });
    }
 
}