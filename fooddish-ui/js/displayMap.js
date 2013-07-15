var latitude;
var longitude;
var accuracy;
var latlng;
var speed;
var timestamp;
var loc;
var map;
var mapOptions;
var myLatLng;
var marker;
var eastLat;
var eastLong;
var westLat;
var westLong;

function read()
{
        try
        {
                document.addEventListener("deviceready", onDeviceReady, false);                         
        }
        
        catch(error1)
        {
                alert(error1);
        }       
}
                
// Cordova is ready
                
function onDeviceReady() 
{
        try
        {               
                        //get position from the device  
                        navigator.geolocation.getCurrentPosition(myPositionSuccess, myPositionError, {frequency:5000, maximumAge: 30000, timeout:10000, enableHighAccuracy: true});
                        
                        //watch for change in position
                        navigator.geolocation.watchPosition(myWatchSuccess,myPositionError,{frequency:5000})
        }
        
        catch(error2)
        {
                alert(error2);
        }       
        
}
        
        
// on getting the geolocation

function myPositionSuccess(position) 
{
        try
        {
                //get latitude,longitude,accuracy etc values
                latitude = position.coords.latitude;
                longitude = position.coords.longitude;
                accuracy = position.coords.accuracy;
                speed = position.coords.speed;
                timestamp = position.timestamp;
                displayMap();
               // displayLocation();
                
        }
        
        catch(error3) 
        {
                alert(error3);
        }
}

function myPositionError(error) 
{
        alert('code: '    + error.code    + '\n' +    'message: ' + error.message + '\n');
}

//each time the position changes
function myWatchSuccess(position) 
{
        try
        {
                //get latitude,longitude,accuracy etc values
                latitude = position.coords.latitude;
                longitude = position.coords.longitude;
                accuracy = position.coords.accuracy;
                speed = position.coords.speed;
                timestamp = position.timestamp;
                
                displayMap();
                //displayLocation();
                //sendLocation();
                
        }
                
        catch(error4) 
        {
                alert(error4);
        }
        
}
function displayMap() {
        mapOptions = {
          	center: new google.maps.LatLng(latitude,longitude),
          	zoom: 8,
          	mapTypeId: google.maps.MapTypeId.ROADMAP
        	};
        	map = new google.maps.Map(document.getElementById("map"),
            mapOptions);
            displayMarker();
            getBound();
        }
        
function displayMarker(){
            myLatLng = new google.maps.LatLng(latitude,longitude);
   		  	marker = new google.maps.Marker({
      		position: myLatLng,
      		map: map,
      		//title: 'My Position'
  		});
  			marker.setAnimation(google.maps.Animation.BOUNCE);
  			var contentString = 'This is my Position';
  			var infowindow = new google.maps.InfoWindow({
     		content: contentString,
    	 	maxWidth: 200
  		});
  			google.maps.event.addListener(marker, 'click', function() {
    		infowindow.open(map,marker);
  		});
  	}
  	
function getBound(){
  			bounds = map.getBounds();
  			eastLat = bounds.getNorthEast().lat();
  			eastLong = bounds.getNorthEast().lng();
  			westLat = bounds.getSouthWest().lat();
  			westLong = bounds.getSouthWest().lng();
  		}

/*function displayLocation()
{
        var element = document.getElementById('geolocation');
        element.innerHTML = 'Latitude: '           + latitude              + '<br />' +
                        'Longitude: '          + longitude             + '<br />' +
                      //'Altitude: '           + altitude              + '<br />' +
                        'Accuracy: '           + accuracy              + '<br />' +
                      //'Heading: '            + heading               + '<br />' +
                        'Speed: '              + speed                 + '<br />' +
                        'eLat: '          + eastLat                   + '<br />'+
                        'eLong: '          + eastLong                    + '<br />'+
                        'wLat: '          + westLat                    + '<br />'+
                        'wLong: '          + westLong                    + '<br />'+
                        'Timestamp: '          + timestamp                    + '<br />';
} 
*/      
  


     


