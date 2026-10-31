# EncryptVault

EncryptVault is a Java Swing desktop application for registering users, verifying identity by email OTP, and encrypting, reading, downloading, and deleting user files stored locally.

## Requirements

- JDK 17 or newer
- MySQL 8 or newer

The included JAR dependencies are in `lib/`.

## Set up the database

1. Create a local MySQL database named `encryptvault`.
2. Import `encryptvault.sql`. The published script creates the tables only; it contains no user records or encrypted files.
3. Configure the database connection for the terminal session before running:

```powershell
$env:DB_PASSWORD = "your_mysql_password"
$env:DB_USER = "root" # optional
$env:DB_URL = "jdbc:mysql://localhost:3306/encryptvault" # optional
```

For email OTP delivery, configure a Gmail app password:

```powershell
$env:EMAIL_PASS = "your_gmail_app_password"
```

## Build and run

```powershell
javac -cp ".;lib/*" -d build src/Main.java src/filemanager/*.java src/db/*.java src/security/*.java src/settings/*.java src/auth/*.java
java -cp "build;lib/*" Main
```

## Privacy note

Never commit database passwords, `.env` files, generated `.class` files, encrypted user files, or personal documents. The `.gitignore` file keeps these local-only items out of Git.
