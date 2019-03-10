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
app.use('/api/qr/view', m_qr_view);

// Web paths
const w_qr_create = require("./routes/web/qr/create");
app.use('/api/web/qr/create', w_qr_create);

const w_c_create = require("./routes/web/company/create");
app.use('/api/web/company/create', w_c_create);

const w_t_create = require("./routes/web/template/create");
app.use('/api/web/template/create', w_t_create);

const w_qr_view = require("./routes/web/qr/view");
app.use('/api/web/qr/view', w_qr_view);

// TODO
// meaningful name and functionality
const TEST_DOWNLOAD = require("./routes/web/qr/downloadFileTest");
app.use('/api/TEST_DOWNLOAD', TEST_DOWNLOAD);

// Shared paths
const s_qr_addComment = require("./routes/shared/qr/addComment");
app.use('/api/qr/addComment', s_qr_addComment);

const s_u_create = require("./routes/shared/user/createuser");
app.use('/api/user/signup', s_u_create);

const s_u_login = require("./routes/shared/user/login");
app.use('/api/user/login', s_u_login);

const s_qr_listDocuments = require("./routes/shared/qr/listDocuments");
app.use('/api/qr/listDocuments', s_qr_listDocuments);

module.exports = app;