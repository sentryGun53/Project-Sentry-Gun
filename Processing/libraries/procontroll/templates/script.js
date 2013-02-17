var example;

function findObject(n) {
  	var p,i,x;
  	var d=document;
  	if(!(x=d[n])&&d.all){//InternetExplorer
  		x=d.all[n]; 
	}
  	for(i=0;!x&&d.layers&&i<d.layers.length;i++){
		x=findObject(n,d.layers[i].document);//Netscape 4.0
	}
  	if(!x && d.getElementById){//Netscape 6.0
   		x=d.getElementById(n);
   	}
  	return x;
}

var exampleBox = findObject("example");
var exampleButton = findObject("exampleButton");

function setButtonText(buttonText){
	var textLength = exampleButton.firstChild.nodeValue.length;
	exampleButton.firstChild.deleteData(0,textLength);
	exampleButton.firstChild.insertData(0,buttonText);
}

var exampleClosed;

function closeExample(){
	example = exampleBox.removeChild(exampleBox.firstChild);
	exampleBox.appendChild(document.createTextNode("."));
	setButtonText("open example");
	exampleClosed = true;
}

closeExample();

function openExample(){
	exampleBox.removeChild(exampleBox.firstChild);
	exampleBox.appendChild(example);
	setButtonText("close example");
	exampleClosed = false;
}

function handleExample(){
	if(exampleClosed) openExample();
	else closeExample();
}

