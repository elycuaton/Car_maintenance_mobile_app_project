// Import necessary modules
const express = require('express');
const app = express();
const port = process.env.PORT || 5000;
const cors = require('cors');
const mongodb = require('mongodb');
var bodyParser = require('body-parser');

// Middleware setup
app.use(cors()); // Enable Cross-Origin Resource Sharing
app.use(express.json()); // Parse incoming JSON requests
var urlencodedParser = bodyParser.urlencoded({ extended: true }); // Parse incoming URL-encoded form data
// Server setup
app.listen(port, () => console.log(`Listening on port ${port}`));

// MongoDB connection details
const connectionString = "mongodb+srv://dbUser:dbPassword@cluster0.movfunp.mongodb.net/";
const client = new mongodb.MongoClient(connectionString);

// GET route to retrieve User Credentials from MongoDB
app.get('/Users', async (req, res) => {
  let connection;
  let result;
  try {
      // Connect to MongoDB
      connection = await client.connect();
      let db = await connection.db("CarMaintenanceApp");
      const userCredentials = await db.collection("UserCredentials");
      result = await userCredentials.find().toArray(); // Fetch userCredentials
  } catch (e) {
      console.log(e);
  } finally {
      await client.close(); // Close MongoDB connection
  }
  res.send(result); // Send fetched userCredentials as response
});


// POST route to retrieve User Credentials from MongoDB
app.post('/postUser', async (req, res) => {
    let connection;
    let result;
    var data = {
        name: req.body.name,
        username: req.body.email,
        password: req.body.password
    }

    // console.log(req)
    try {
        // Connect to MongoDB
        connection = await client.connect();
        let db = await connection.db("CarMaintenanceApp");
        const userCredentials = await db.collection("UserCredentials");
        console.log(data)
        result = await userCredentials.insertOne(data); // Fetch userCredentials
    } catch (e) {
        console.log(e);
    } finally {
        await client.close(); // Close MongoDB connection
    }
    res.send(result); // Send fetched userCredentials as response
  });

  // POST route to save service data to MongoDB
  app.post('/postService', async (req, res) => {
      let connection;
      let result;
      var data = {
          serviceType: req.body.serviceType,
          date: req.body.date,
          notes: req.body.notes
      };

      try {
          // Connect to MongoDB
          connection = await client.connect();
          let db = await connection.db("CarMaintenanceApp");
          const services = await db.collection("Services");
          result = await services.insertOne(data); // Insert service data
      } catch (e) {
          console.log(e);
      } finally {
          await client.close(); // Close MongoDB connection
      }
      res.send(result); // Send result of the operation
  });

  // POST route to add service type to ServiceList collection in MongoDB
  app.post('/addServiceToList', async (req, res) => {
      let connection;
      let result;
      const serviceType = req.body.serviceType;

      try {
          // Connect to MongoDB
          connection = await client.connect();
          const db = await connection.db("CarMaintenanceApp");
          const serviceList = await db.collection("ServiceList");
          result = await serviceList.insertOne({ serviceType });
      } catch (e) {
          console.log(e);
          res.status(500).send("Internal Server Error");
          return;
      } finally {
          await client.close(); // Close MongoDB connection
      }

      res.status(200).send("Service added to list successfully.");
  });

  // GET route to fetch service types from ServiceList collection in MongoDB
  app.get('/getServiceTypes', async (req, res) => {
      let connection;
      let result;
      try {
          // Connect to MongoDB
          connection = await client.connect();
          const db = await connection.db("CarMaintenanceApp");
          const serviceList = await db.collection("ServiceList");
          result = await serviceList.find().toArray(); // Fetch all documents from ServiceList collection
      } catch (e) {
          console.log(e);
          res.status(500).send("Internal Server Error");
          return;
      } finally {
          await client.close(); // Close MongoDB connection
      }

      res.status(200).json(result);
  });

  // GET route to retrieve services based on date from MongoDB
  app.get('/getServices', async (req, res) => {
      let connection;
      let result;
      const date = req.query.date; // Get the date from query parameters

      try {
          // Connect to MongoDB
          connection = await client.connect();
          const db = await connection.db("CarMaintenanceApp");
          const services = await db.collection("Services");
          result = await services.find({ date: date }).toArray(); // Fetch services based on date
      } catch (e) {
          console.log(e);
          res.status(500).send("Internal Server Error");
          return;
      } finally {
          await client.close(); // Close MongoDB connection
      }

      res.status(200).json(result);
  });


