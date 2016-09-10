# ws-sth

Authors
-------

[Davide Tiriticco](https://www.linkedin.com/in/davide-tiriticco-2278719a)
 
[Alessandro Granato](https://www.linkedin.com/in/alessandro-granato-40b03081)

Course
------
[Pervasive Systems 2015/16 Course](http://ichatz.me/index.php/Site/PervasiveSystems2016)

Smart Treasure Hunt
===================

INTRODUCTION
------------

Smart Treasure Hunt is a pervasive game designed and developed for the Pervasive Systems course at Università degli Studi di Roma "La Sapienza".
This project allowed us to address common issues about the limits of current techniques for what concern indoor localization and deal 
with technologies that typically belongs to the "Pervasive World" (e.g, proximity algorithms, wireless sensor networks, web services, Bluetooth and so on).

The goal of the game is to let players find an hidden device treasure by using their own smartphones/tablets. The application provides suggestions
based on devices sensors (i.e., suggestions about the environmental conditions percept by the treasure device) and GPS/Bluetooth technologies 
(i.e, suggestions about the distance among players and treasure).

REPOSITORY LINKS
------------
[[Android App Repository](https://github.com/davtir/pervasive-sth)]

[[Web Server Repository](https://github.com/davtir/ws-sth)]

TECHNOLOGIES
-------------

The main technologies used for this project are:

* Android, Android Studio 2.1
* NetBeans 8.1
* Tomcat 8.0
* Global Positioning System (GPS): Used for outdoor distance measurement
* Assisted-GPS (A-GPS): Used for outdoor/indoor distance measurement 
* Bluetooth: Used for nearby distance measurement
* Wi-Fi: For network connection and accuracy improvement in distance measurement
* Sensors: Environmental conditions perception
	* Photoresistor
	* Thermometer
	* Microphone
	* Accelerometer
* RESTful Web Server: Used for storing players and treasure metadata


ARCHITECTURE
------------

![alt tag](https://raw.githubusercontent.com/davtir/pervasive-sth/master/arch.jpg)

The system architecture is composed by:
* Web Server: Keeps informations about the treasure (Bluetooth ID, Name, coordinates, media data, sensors data).
* Treasure Device: After the registration on the Web Server, periodically updates its own data on the web server in order to make available this informations 
			 to the players.
			 Moreover, continuously sends advertising packets via Bluetooth in order to be sensed by nearby hunter devices.
* Hunter Device:	After the registration on the Web Server, periodically retrieves treasure data from the web server in order to know the environmental 
			conditions around the treasure device and its coordinates. 
			Continuously performs Bluetooth discovery task in order to find the treasure device and computes the distance based on RSSI.  

![alt tag](https://raw.githubusercontent.com/davtir/pervasive-sth/master/flow.jpg)


ACTIVITIES
------------
* WelcomeActivity: Displays authors and course informations
* MainActivity:	Let the players choose the role of the device (Treasure, Hunter)
* TreasureActivity: Embed the treasure task described above
* HunterActivity: Embed the hunter task described above
* TreasureCaught: Displays the winner photo to all the players

TASKS
------------
* HunterTask: periodically generates random suggestions based on sensors and media data retrieved from the treasure through the webserver
* HunterDistanceTask: periodically executes the bluetooth discovery process and retrieves gps treasure's coordinates from webserver. Then, it computes an approximation of the distance between the hunter and treasure devices.
* TreasureTask: continuously updates treasure status and sensors data on the webserver.
* TreasureMediaTask: continuously updates treasure media data on the webserver.
 

![alt tag](https://raw.githubusercontent.com/davtir/pervasive-sth/master/sensorflow.jpg)

Each device, independently from their role, reads its own sensors.
If the device is the treasure, it posts this data on the web server.
Otherwise, if the device is the hunter, it retrieves treasure data from the web server and compares them to its 
own in order to recognize significant changes in the environmental conditions.

SCREENSHOTS
-------------------------
<img src="https://github.com/davtir/pervasive-sth/blob/master/Screenshot/Screenshot_2016-09-10-16-19-46.jpg" width="250">
<img src="https://github.com/davtir/pervasive-sth/blob/master/Screenshot/Screenshot_2016-09-10-16-32-31.jpg" width="250">
<img src="https://github.com/davtir/pervasive-sth/blob/master/Screenshot/Screenshot_2016-09-10-16-52-56.jpg" width="250">
<img src="https://github.com/davtir/pervasive-sth/blob/master/Screenshot/Screenshot_2016-09-10-16-36-55.jpg" width="250">
<img src="https://github.com/davtir/pervasive-sth/blob/master/Screenshot/Screenshot_2016-09-10-16-50-27.jpg" width="250">
<img src="https://github.com/davtir/pervasive-sth/blob/master/Screenshot/Screenshot_2016-09-10-16-20-28.jpg" width="250">
<img src="https://github.com/davtir/pervasive-sth/blob/master/Screenshot/Screenshot_2016-09-10-16-51-30.jpg" width="250">

INSTALLATION INSTRUCTIONS
-------------------------

* Download and install java (1.8.0+).
* Download and install Android Studio (latest version).
* Download NetBeans with Tomcat web server and install them (latest version).
* Import the project SmartTreasureHunt on Android Studio.
* Import the project WSPervasiveSTH on NetBeans.
* Build and deploy the WSPervasiveSTH web server.
* Install the SmartTreasureHunt app on your android device.
* Run the SmartTreasureHunt app from your android device.
* Enjoy the hunt!



   
