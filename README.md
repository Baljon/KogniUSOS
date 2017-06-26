KogniUSOS
==============

An application designed to simplify the process of signing up to the Specialization Module 2 (Moduł Specjalizacyjny 2)
for Cognitive Science students at University of Warsaw (Institute of Philosophy).

Developed as a course project for the Basics of Application Programming course by @pwilkin.

### Stack
This is a Java web application using:
 * Vaadin web framework;
 * MySQL database at Amazon Web Services;
 * Hibernate ORM;
 * Maven management;
 * Google gson, Apache deltaspike, iTextPDF, ...
 
 ### Functionality
 As for now, the application provides basic functionality for course proposing and registration.
 
 The application communicates with the USOS API to get course information.
 
 From a student's perspective, the core functionality now includes:
  * signing up (University of Warsaw student ID qnd e-mail address required);
  * logging and courses preview;
  * proposing courses for MS2 (course ID from USOS required);
  * course registration (for CogSci internal purposes only, not USOS-integrated).
  
  From an admin's perspective, the core functionality now includes:
   * logging, courses and students preview;
   * removing courses and students;
   * accepting/rejecting proposed courses;
   * to be added soon: generating CogSci student lists for associated faculties for registration purposes.
  
  Other possibilities can be freely explored, though definitely not every feature is fully functional at the moment.
  
 ### Perspectives
 The app is aimed at becoming a real-life module facilitating the MS2 registration process.
 Major fixes are possibly required, though at the present stage basic procedures are by and large functional.
 
 Free access to the application through website will be added soon.
 For now, the app lives @ Amazon and can be remotely accessed using the developers' connection data (private).
 
  
  

