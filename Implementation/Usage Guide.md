# Prerequisites

Make sure the following tools are installed on your computer before getting started:

- Docker Desktop
- Java 25
- Node.js
- Gradle

# Step 1 -- Start the Database

From the project root directory, run :

```
docker compose up -d
```

The project uses PostgreSql running inside Docker.

# Step 2 -- Start the Backend

If you use IntelliJ IDEA IDE, you can run `OlineOrderApplication`.

# Step 3 -- Start the Frontend

Open a new terminal, navigate to the `onlineorder_webpage` directory, and run :

```
npm install   # only needed the first time
npm run dev
```

Then you can get the URL after running these command. 

Open the URL that you get in your browser.



These are restaurant information and images under `test_data` folder. You can use them to test this project's functions. 



