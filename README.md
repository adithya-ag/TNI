# Telecom Network Inventory Management (TNI)

## Table of Contents

- [Introduction](#introduction)
- [Project Description](#project-description)
- [Features](#features)
- [Languages and Technologies](#languages-and-technologies)
- [Architecture and Design](#architecture-and-design)
- [Installation and Usage](#installation-and-usage)
- [License](#license)
- [Acknowledgments and Credits](#acknowledgments-and-credits)
- [Conclusion](#conclusion)

## Introduction

Welcome to the Telecom Network Inventory Management (TNI) project! This repository hosts a powerful Spring Boot web application designed for comprehensive inventory management. The application offers a wide range of features, from basic CRUD operations to advanced functionalities, including CSV conversion and seamless data import and export. Whether you need to manually input data or perform mass loading from Excel spreadsheets, this application simplifies the process and ensures data validation.To accommodate different users, we provide distinct permissions, authorities, and access levels within the application.

The application also has implementation of AI and Machine Learning-based duplicate data detection system, integrated with a Flask API for Python code execution. This innovative feature promises to further streamline data management within the Telecom Network Inventory Management system.

## Languages and Technologies

Our tech stack comprises:

- [Java](#)
- [Maven](#)
- [Python](#)
- [Flask](#)
- [HTML](#)
- [CSS](#)
- [Spring Boot](#)
- [MVC Architecture](#)
- [Thymeleaf](#)

## Project Description

The Telecom Network Inventory Management (TNI) project is a comprehensive solution for managing inventory data in the telecommunications industry. This application is designed to streamline the process of tracking and managing network assets, ensuring data accuracy and accessibility. Some key aspects of the project include:

- **Inventory Management:** Effectively manage all telecom network assets, including routers, switches, cables, and more. The application allows for easy data entry and retrieval, ensuring that inventory data is always up-to-date.

- **Duplicate Data Detection:** The project includes an AI and Machine Learning-based system for detecting duplicate data entries. This feature enhances data quality and reduces errors.

- **Access Control:** TNI provides different user roles and permissions to ensure that only authorized personnel can perform certain actions within the application.

## Features

- **CRUD Operations:** Perform Create, Read, Update, and Delete operations on inventory data with ease.

- **CSV Conversion:** Convert inventory data to CSV format for easy sharing and analysis.

- **Excel Data Import/Export:** Seamlessly import and export data to and from Excel spreadsheets, simplifying bulk data management.

- **Data Validation:** The application performs automatic data validation to ensure the accuracy and integrity of the data being input.

- **AI and Machine Learning Integration:** The upcoming AI and Machine Learning system will enhance data quality by identifying and handling duplicate entries.

## Architecture and Design

TNI is built on a robust architecture that ensures scalability and maintainability. The core components of the architecture include:

- **Spring Boot:** The application is developed using the Spring Boot framework, which simplifies the development process and provides a solid foundation for building web applications.

- **MVC Architecture:** TNI follows the Model-View-Controller (MVC) design pattern, which separates the application into three interconnected components to improve modularity and maintainability.

- **Flask API:** The AI and Machine Learning-based duplicate data detection system is powered by a Flask API, allowing seamless integration with Python-based algorithms for data analysis.

- **Thymeleaf:** Thymeleaf is used for templating in the application, making it easier to create dynamic web pages and display data.


## Installation and Usage

The Installation:
The requred jar file is now avalable for downlode in the [release](https://github.com/adithya-ag/TNI/releases) section.
once you downlode it, you need to run the jar file using the command "java -jar your-app.jar" so in our case "java -jar intern-telecom-inventory-1.0.0.jar".
Note you have to be in the same folder/directory of that where the jar file is present to run this cmmand in the command prompt. For this eithre use the CD command to go to the requred directoory or after you are in the correct location in your file explorer where the jar file is present, in the path section enter "cmd" and press enter. Then run the above command to start/run the application.
To terminate the app... go to the command prompt where the app is running and press "alt+c" this will terminate the application.

Usage:
you need to go to you're browser and enter the url [http://localhost:8081/tni](http://localhost:8081/tni) to start the application.
for the database and user creation you need to make some changes...soon to be updated.

## License

This project is licensed under the [MIT License](LICENSE.md).

## Acknowledgments and Credits

I would like to express my sincere gratitude to [Sincera Consultancy](https://www.sincera.in/), the company where the I completed my internship. The guidance, support, and real-world experience gained during this internship have been invaluable to me in the development of the Telecom Network Inventory Management (TNI) project.

Special thanks to the mentors who provided invaluable insights and guidance throughout the project:

- [Dhruv G](https://www.linkedin.com/in/dhruv-gupta-5300171b): Dhruv sir has played a pivotal role in the project, sharing extensive knowledge and expertise in both front-end and backend developement. To starting the application from scratch and building the web application on java springBoot MVC as a maven project .

- [Srivatsa G](https://www.linkedin.com/in/gorursrivatsa/): Srivastsa sir's mentorship and guidance were instrumental in shaping the architecture and design of the TNI project.

- [Kamal](https://www.linkedin.com/in/kamal-nath-tiwari-61143a67/): Kamal sir's contributions to the project, particularly in the areas of AI and Machine Learning integration, were highly commendable. Also was guid for Flask API integration significantly enhanced the application's functionality.

- [Prudvi](https://www.linkedin.com/in/prudvi-g-a4a871269/): Prudvi's expertise in back end development for reading, validating and loading the data into the DB after importing it form excel file.

- [Anand GP](https://www.linkedin.com/in/anand-gp-58963b26/): Anand sir's dedication and attention to detail in inventory management have greatly improved the project's data accuracy and efficiency.
 

## Conclusion

The Telecom Network Inventory Management (TNI) project represents a significant step forward in the efficient management of telecommunications network assets. The applications is commitment to enhancing data accuracy, accessibility, and efficiency has driven this project from its inception.

As I move forward, I envision to developing better skills by working on TNI continuing to evolve, with new features, improved performance, and increased flexibility. Excited to make a positive impact on the telecommunications industry by providing a reliable and user-friendly solution for inventory management.
