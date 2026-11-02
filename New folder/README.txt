COLLEGE FIRST-AID INVENTORY SYSTEM
==================================

REQUIREMENTS
- JDK 17 or newer (tested with JDK 21)
- MySQL Server
- MySQL database named: first_aid_db
- MySQL Connector/J is included in lib/

DATABASE CONFIGURATION (Windows PowerShell)
The database password is intentionally NOT stored in DBConnection.java.
Open PowerShell in this project folder and set your credentials for the current terminal:

  $env:FIRST_AID_DB_USER = "root"
  $env:FIRST_AID_DB_PASSWORD = "YOUR_MYSQL_PASSWORD"

Replace YOUR_MYSQL_PASSWORD locally. Do not share it or upload it.

COMPILE THE ENTIRE PROJECT
  Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
  New-Item -ItemType Directory bin | Out-Null
  javac -cp "lib/*" -d bin Main.java (Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName })

RUN THE APPLICATION
  java -cp "bin;lib/*" src.Main

RUN A TEST CLASS (after compiling all sources)
  java -cp "bin;lib/*" src.TestDB
  java -cp "bin;lib/*" src.TestMedicine
  java -cp "bin;lib/*" src.TestAddMedicine
  java -cp "bin;lib/*" src.TestAddStock
  java -cp "bin;lib/*" src.TestUpdateStock
  java -cp "bin;lib/*" src.TestMedicineExists
  java -cp "bin;lib/*" src.TestViewStock
  java -cp "bin;lib/*" src.TestExpiry

DATABASE TABLES EXPECTED
This application expects the MEDICINE, STOCK, STAFF and STOCK_HISTORY tables
with the columns referenced by the DAO classes. Keep your existing database
and tables; this project does not automatically recreate or delete data.

NOTES
- Main.java contains the complete console menu for the current project features.
- StockDAO.addStock and StockDAO.updateStockQuantity now return boolean values,
  matching the test classes.
- MedicineDAO.medicineExists is included.
- TestLowStock.java was empty in the uploaded project; the low-stock feature
  is available through Main menu option 6 and StockDAO.checkLowStock().
- StaffDAO currently compares password_hash directly with the entered password.
  This is a basic classroom implementation, not production-grade authentication.
  A real deployment should store salted password hashes and verify them securely.
