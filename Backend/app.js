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

const w_qr_create_uploadFile = require("./routes/web/qr/create");
app.use('/api/web/qr/create/uploadFile', w_qr_create_uploadFile);

const w_qr_addPeopleToDoc = require("./routes/web/qr/addPeopleToDoc");
app.use('/api/web/qr/addPeopleToDoc', w_qr_addPeopleToDoc);

const w_qr_addCompanyToDoc = require("./routes/web/qr/addCompanyToDoc");
app.use('/api/web/qr/addCompanyToDoc', w_qr_addCompanyToDoc);


// TODO
// Will depricate
const w_c_create = require("./routes/web/company/create");
app.use('/api/web/company/create', w_c_create);

// Depricated
//const w_t_create = require("./routes/web/template/create");
//app.use('/api/web/template/create', w_t_create);

const w_qr_view = require("./routes/web/qr/view");
app.use('/api/web/qr/view', w_qr_view);

// TODO
// meaningful name and functionality
const TEST_DOWNLOAD = require("./routes/web/qr/downloadFileTest");
app.use('/api/TEST_DOWNLOAD', TEST_DOWNLOAD);

// Shared paths
const s_qr_addComment = require("./routes/shared/qr/addComment");
app.use('/api/qr/addComment', s_qr_addComment);

const s_u_signup = require("./routes/shared/user/signup");
app.use('/api/user/signup', s_u_signup);

const s_u_login = require("./routes/shared/user/login");
app.use('/api/user/login', s_u_login);

const s_qr_listDocuments = require("./routes/shared/qr/listDocuments");
app.use('/api/qr/listDocuments', s_qr_listDocuments);

module.exports = app;