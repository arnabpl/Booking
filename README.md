Booking API Automation

 Project Overview

	This project is an API automation framework developed using Java, Rest Assured, Maven, TestNG, and 
	Extent Reports to validate the Restful-booker API.

	The primary objective is to ensure the reliability of booking operations, identify invalid data 
	handling issues, validate authentication, and detect regressions through automated API testing.

Base URL: https://restful-booker.herokuapp.com

 Test Strategy

	The testing approach follows a risk-based strategy, prioritizing booking data integrity and critical business workflows.

	What I Chose to Cover
		Health Check: Verify API availability using the /ping endpoint.
	
		Authentication: Validate successful token generation and ensure write operations reject missing or invalid authentication.
		
		Booking CRUD: Verify booking creation, retrieval, full updates using PUT, partial updates using PATCH, and cancellation.
		
		Negative Testing: Validate missing fields, empty payloads, incorrect data types, invalid booking IDs, and malformed requests.
		
		Boundary Testing: Specifically test negative and zero booking prices, malformed dates, and check-out dates earlier than check-in dates.
		
		Contract Testing: Validate response structures, mandatory fields, and expected data types.
		
		End-to-End Testing: Verify the complete booking lifecycle from creation through cancellation.

			For negative scenarios, assertions are based on expected API behaviour rather than the current implementation's response. 
			Unexpected acceptance of invalid data is treated as a potential defect and documented separately.

 What I Deliberately Left Out

	The E2E scenarios could be covered by multiple test cases. However, since my API tests already cover the individual API-level 
	scenarios comprehensively, I couldn't extend the E2E test coverage to the same extent.


 How to Run the Project
	
	Prerequisites
		Java JDK installed.
		Maven installed.
		IntelliJ IDEA or Eclipse with TestNG support.
		
 Execution Steps
		
		Clone the Git repository.
		Import the project into IntelliJ IDEA or Eclipse as a Maven project.
		Reload the Maven project to download the required dependencies.
		Navigate to testng.xml in the project root directory.
		Right-click on testng.xml.
		Select Run As → TestNG Suite (Eclipse) or Run 'testng.xml' (IntelliJ IDEA).
		Wait for the test execution to complete.
		Review the TestNG results and open the generated Extent HTML report in a browser.

