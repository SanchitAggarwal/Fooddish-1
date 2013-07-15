// The angular module for the page
var fooddish = angular.module('fooddish', []);
var topItemListURL= "http://10.10.1.193:8080/Food/service/topItems";
var hotelInfoURL="http://10.10.1.193:8080/Food/service/hotel";
var  hotelList = [];
var itemList=[];
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

// The main angular controller for the page
var fooddishController = function ($scope, $http) {
    // Page related objects
    $scope.error = true;
    //$scope.topItemListURL= "http://10.10.1.193:8080/Food/service/topItems";
   // $scope.hotelInfoURL="http://10.10.1.193:8080/Food/service/hotel";
    $scope.hotelInfoURL=hotelInfoURL;
    $scope.topItemListURL= topItemListURL;
    // Variables related to the top food item list
    $scope.foodItemList = [];
    $http({ // Load the initial data
        url: $scope.topItemListURL,
        method: 'GET'
    }).success(function (foodItem) {
            console.log(foodItem)
            $scope.foodItemList = foodItem["result"];
        }).
        error(function(data, status) {
            //alert(data)
            $scope.data = data || "Request failed";
            $scope.status = status;
        });

    // Variables related to the top hotel list
    $scope.hotelList = [];

    $http({ // Load the initial data
        url: $scope.hotelInfoURL,
        method: 'GET'
    }).success(function (hotelData) {
            console.log(hotelData)
            $scope.hotelList = hotelData["result"];
        }).
        error(function(data, status) {
            //alert(data)
            $scope.data = data || "Request failed";
            $scope.status = status;
        });
    hotelList  = $scope.hotelList;
    itemList=$scope.foodItemList;

    //To get the hotelList by Item Id
    $scope.getHotelByItem=function (itemId) {
        $scope.hotelInfoURL=hotelInfoURL+"?"+"itemID="+itemId;
        $http({ // Load the initial data
            url: $scope.hotelInfoURL,
            method: 'GET'
        }).success(function (hotelData) {
                console.log(hotelData)
                $scope.hotelList = hotelData["result"];
            }).
            error(function(data, status) {
                //alert(data)
                $scope.data = data || "Request failed";
                $scope.status = status;
            });
        hotelList  = $scope.hotelList;
        itemList=$scope.foodItemList;
    }

};


//Main function on load
$(document).ready(function(){
    try
    {
        //get position from the device
        navigator.geolocation.getCurrentPosition(myPositionSuccess, myPositionError, {frequency:5000, maximumAge: 30000, timeout:10000, enableHighAccuracy: true});

        //watch for change in position
        navigator.geolocation.watchPosition(myWatchSuccess,myPositionError,{frequency:5000})
    }

    catch(error2)
    {
        //alert(error2);
    }


    $('#topFoodCarousel').carousel();
    for(hotel in hotelList) {
        var hotelobj=hotelList[hotel];
        var element= "#"+hotelobj["hotelId"]+"_"+hotelobj["ratings"];
        var stars="";
        for(i=0;i<hotelobj["ratings"];i++){
            stars=stars+'<i class="icon-star"></i>';
        }
        $(element).append(stars);
    }
});

/*function read()
{
    try
    {
        document.addEventListener("deviceready", onDeviceReady, false);
    }

    catch(error1)
    {
        //alert(error1);
    }
} */

// Cordova is ready

function onDeviceReady()
{
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
        //alert(error3);
    }
}

function myPositionError(error)
{
    //alert('code: '    + error.code    + '\n' +    'message: ' + error.message + '\n');
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
       // alert(error4);
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


function setHotelMarker(){
    for(hotel in hotelList){
        var a=hotelList[hotel]["location"] ["lat"];
        var b=hotelList[hotel]["location"] ["lng"];
        var hotelLatLng = new google.maps.LatLng(a,b);
        var locmarker = new google.maps.Marker({
            position: hotelLatLng,
            map: map,
            //title: 'My Position'
        });
        locmarker.setAnimation(google.maps.Animation.BOUNCE);
        var contentString = hotelList[hotel]["hotelName"];
        var infowindow = new google.maps.InfoWindow({
            content: contentString,
            maxWidth: 200
        });
        google.maps.event.addListener(locmarkermarker, 'click', function() {
            infowindow.open(map,locmarkermarker);
        });
    }
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






