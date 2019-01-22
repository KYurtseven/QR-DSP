const express = require('express');
const mongoose = require("mongoose");
const app = express();
const morgan = require("morgan");
const bodyParser = require("body-parser");

const userRoute = require("./routes/userroute");
const qrdocRoute = require("./routes/qrdocroute");
const userqrRoute = require("./routes/userqrroute");
const mobileRoute = require("./routes/qrdocmobile");

//const denemesubRoute = require("./routes/denemesub");
//const denemeRoutes = require("./routes/deneme");

mongoose.connect(
    "mongodb://korayyurtseven:123123abc@ds127704.mlab.com:27704/qrdsp"
)

app.use(morgan("dev"));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use((req, res, next) => {
  res.header("Access-Control-Allow-Origin", "*");
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept, Authorization"
  );
  if (req.method === 'OPTIONS') {
      res.header('Access-Control-Allow-Methods', 'PUT, POST, PATCH, DELETE, GET');
      return res.status(200).json({});
  }
  next();
});

app.use('/user', userRoute);
app.use('/qrdoc', qrdocRoute);
app.use('/userqr', userqrRoute);
app.use('/mobile',mobileRoute);

//app.use('/deneme', denemeRoutes);
//app.use('/denemesub', denemesubRoute);
/*
app.use((req, res, next) => {
    res.status(200).json({
        message: 'It works!'
    });
});

*/
module.exports = app;