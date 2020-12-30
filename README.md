# AskAway

A Stackoverflow clone with a gameifying experience

## About this project
The goal of this project was to mimic Stackoverflow and add a point system to drive future user interaction. We didn't want to admonish or punish users for adding repeat questions to ensure that the user experience was positive, so we don't award points for comments. Any answer or question you post awards you points, so even if you aren't sure how to answer a question, you can still ask your own. The idea behind this is that a user will continue to interact even if they don't currently have the confidence to answer questions and will be rewarded for their curiosity.

## How does it work (TL/DR)?
The application is comprised of a REST API built in spring web mvc and run on Apache Tomcat. The application pulls data from a database hosted on AWS RDS in Postgres using Hibernate and Spring ORM.

The client uses Angular to present the visuals of the application. Angular will use the HTTP Client to send a request to our API and the API will serve it the data as a JSON. The json is then parsed and cast to Angular objects, and then will display on each component.

## Contributors
* Javier Perez
* Cassandra Colvin
* Roger Bucci

## Technologies
Backend:
- Maven
- Spring MVC and ORM
- Hibernate and JPA
- JUnit

Frontend:
- [Angular](https://angular.io/docs)
- Javascript
- HTML/CSS
- [Bootstrap 5](https://getbootstrap.com/docs/5.0/getting-started/introduction/)
- JQuery(as required by Bootstrap)

## Notes on the Architecure
### Frontend
contained within the **p2-client** directory with the Angular project files contained within **p2-client/project2**

**Ensure you are opening project folders within the p2-client/project2 folder or you will encounter an error when trying to build or serve**

Components should be created within the **components** folder

Please use bootstrap classes for styling. see linked documentation under Technologies
If new classes must be created, comment what they are for.

**You may need to run npm install if this is your first pull. Otherwise Angular will not know where to look for some of the inputs**


### Backend
contained within the **p2-server** directory

All project files are contained within the com.ex package
- persistence: any persistence objects/daos go here
- web: any controllers.
- models: any models/POJOS
- search-engine: Any files related to the search engine.
	- Note: You will need to populate the paths to the different text files in the .java file 
- utilities: any utility classes
- config: configuration for any beans
