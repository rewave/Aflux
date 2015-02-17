Aflux
------
![Aflux](http://i.imgur.com/1Yc5IAr.png)

Data collection app based on a parse backend used for collecting sensor data for [Rewave Motion Controller](http://rewaveapp.com) . 


----------


More than being a fully functional data collection system, Aflux is a prototype of a design pattern that we believe, will ease android development process. The idea is to implement PURE java mechanisms.

The system is divided into four parts : 

 1. Core
 2. Repository
 3. Ui
	 4. Fragments 
	 5. Adapters
	 6. Intent filters
	 7. Broadcast Receivers
 4. Activity

**Core** consists of Object Oriented representation of your app's domain( getters and setters), in this case, it is :

 1. The person from whom data is being collected
 2. The gesture she performs
 3. The sensor values read during performance of that gesture

**Repository** includes mechanisms to interact with Core objects. Repo. uses an interface which is to be implemented by the fragment. 

**Fragment** uses an interface that needs to be implemented by initiator Activity.  *Adapters*, *Intent Filters* and *Broadcast Receivers* use an interface which again needs to be implemented by fragments. 

This pattern is a work in progress and mature in no sense. Design Advice Welcomed :).

