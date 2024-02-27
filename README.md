Quiz-App-Spring-Boot-Backend-Application

**Description**

Application that combines fun and knowledge. On a daily basis user can compete with friends in own league or check global knowledge and rank in global league.
Questions for categories are fetched frequently from openai api. 
Every user can build up knowledge answering on questions from previously selected favourited categories.


 **Features:**
 - scheduled tasks for fetching questions for different categories from OPENAI api with custom prompt
 - scheduled task for selecting daily questions for each league
 - calculating statistics and ranks for every user
 - CRUD
 - three layer arhitecture(controller, service, repository)
 - interacting with Postgresql database
 - ORM
 - DTOs and mapping functions (data transfer objects) for communication with frontend
 - endpoints
