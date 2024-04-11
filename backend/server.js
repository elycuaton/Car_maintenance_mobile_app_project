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

// GET route to retrieve Course Data from MongoDB
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

