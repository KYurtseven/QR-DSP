const express = require('express');
const mongoose = require("mongoose");
const app = express();
const morgan = require("morgan");
const bodyParser = require("body-parser");

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

// Mobile paths


// Web paths
const w_qr_createQR = require("./routes/web/qr/createQR");
app.use('/web/createQR', w_qr_createQR);

// Shared paths
const s_qr_view = require("./routes/shared/qr/view");
app.use('/qr/view', s_qr_view);

const s_qr_addcomment = require("./routes/shared/qr/addcomment");
app.use('/qr/addcomment', s_qr_addcomment);

const s_u_create = require("./routes/shared/user/createuser");
app.use('/user/signup', s_u_create);

const s_u_login = require("./routes/shared/user/login");
app.use('/user/login', s_u_login);


module.exports = app;