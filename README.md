Amazon Wrist Watches Filter Automation

This project automates the Amazon India website using Selenium WebDriver and Java.

What This Project Does
Opens Amazon India
Searches for "Wrist Watches"
Applies the following filters:
Analogue
Leather Band
Titan Brand
25% Off or More
Price Range ₹4000 - ₹8000
Captures the first product details:
Price
MRP
Discount Percentage
Prints the details in the console
Technologies Used
Java
Selenium WebDriver
Maven
WebDriverManager
Eclipse IDE
Prerequisites

Make sure the following are installed:

Java JDK 17+
Maven
Google Chrome
Eclipse IDE
How to Run
1. Clone the Project
git clone <repository-url>
2. Import into Eclipse
Open Eclipse
Click File → Import
Select Maven → Existing Maven Projects
Choose the project folder
Click Finish
3. Update Maven Dependencies
Right-click the project
Select Maven → Update Project
Click OK
4. Run the Test
Open AmazonTest.java
Right-click the file
Select Run As → Java Application
Expected Output
Price filter (4000-8000) applied.
Filter 'Analogue' applied.
Filter 'Leather' applied.
Filter 'Titan' applied.
Filter '25% Off or more' applied.

    Product Details 
Price    : ₹5,995
MRP      : ₹9,995
Discount : 40% Off


Notes
ChromeDriver setup is handled automatically by WebDriverManager.
No need to download ChromeDriver manually.
Explicit waits are used for better stability.
If any filter is not found, Amazon may have changed its page layout.
