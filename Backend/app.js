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
// simplified view for mobile
// we will use this mostly
const m_qr_view = require("./routes/mobile/qr/view");
app.use('/qr/view', m_qr_view);

// Web paths
const w_qr_createQR = require("./routes/web/qr/createQR");
app.use('/web/createQR', w_qr_createQR);

const w_c_createCompany = require("./routes/web/company/createCompany");
app.use('/web/createCompany', w_c_createCompany);

const w_t_createTemplate = require("./routes/web/template/createTemplate");
app.use('/web/createTemplate', w_t_createTemplate);

// TODO
// meaningful name and functionality
const TEST_DOWNLOAD = require("./routes/web/qr/downloadFileTest");
app.use('/TEST_DOWNLOAD', TEST_DOWNLOAD);

// Shared paths
const s_qr_addComment = require("./routes/shared/qr/addComment");
app.use('/qr/addComment', s_qr_addComment);

const s_u_create = require("./routes/shared/user/createuser");
app.use('/user/signup', s_u_create);

const s_u_login = require("./routes/shared/user/login");
app.use('/user/login', s_u_login);


module.exports = app;