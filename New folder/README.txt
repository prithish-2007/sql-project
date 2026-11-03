COLLEGE FIRST-AID INVENTORY SYSTEM — GUI VERSION
================================================

This version adds a Java Swing graphical interface with a modern clinical design:
- Deep teal navigation sidebar and soft off-white workspace
- Staff sign-in screen
- Dashboard summary cards (medicine count, available units, low stock,
  expiring batches and expired batches)
- Medicine stock table, add medicine, add stock and update stock screens
- Expiry tracking, low-stock alerts, medicine search, expired-stock report
  and stock history
- MySQL/JDBC remains the data store; the existing DAO classes are retained

REQUIREMENTS
- JDK 17 or newer
- MySQL Server
- Database named: first_aid_db
- MySQL Connector/J is included in lib/

DATABASE CREDENTIALS
DBConnection.java reads these environment variables:
  FIRST_AID_DB_USER (defaults to root)
  FIRST_AID_DB_PASSWORD (required)

Windows PowerShell example (use your own local password):
  $env:FIRST_AID_DB_USER = "root"
  $env:FIRST_AID_DB_PASSWORD = "YOUR_LOCAL_MYSQL_PASSWORD"
  code .

Close all VS Code windows first. Run `code .` from that same PowerShell window so the
new VS Code process inherits the variables. Then open src/Main.java and click Run,
or use Run and Debug > Run FirstAid Inventory GUI.
Do not put your real database password in source code or share it in screenshots.

COMPILE FROM POWERSHELL
  Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
  New-Item -ItemType Directory bin | Out-Null
  javac -cp "lib/*" -d bin (Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName })

RUN FROM POWERSHELL
  java -cp "bin;lib/*" src.Main

PROJECT STRUCTURE
- src/Main.java: starts the graphical application
- src/ui/: Swing login and dashboard screens
- src/dao/: database operations
- src/database/: JDBC connection
- src/model/: medicine, stock and staff models
- lib/: MySQL Connector/J

DATABASE TABLES
The project expects the existing MEDICINE, STOCK, STAFF and STOCK_HISTORY tables
with the columns referenced by the DAO and UI SQL queries. The application does
not recreate or delete database tables or records.

IMPORTANT NOTES
- The sign-in screen uses the existing StaffDAO.login method.
- The current StaffDAO compares the entered password directly to password_hash.
  This is only suitable for a classroom prototype; production authentication
  should use salted password hashing and secure password verification.
- The Update Stock screen asks for Staff ID because the existing stock-history
  operation requires it separately from the staff login.
