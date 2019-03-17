const app = require('./app');


var https = require('https');
var fs = require('fs');
var options = {
    key: fs.readFileSync('./localhost.key'),
    cert: fs.readFileSync('./localhost.cert')
};

var port = process.env.PORT || 443;
var server = https.createServer( options, app );

server.listen( port, function () {
    console.log( 'Express server listening on port ' + server.address().port );
} );

/*
const http = require('http');

const port = process.env.PORT || 3000;

const server = http.createServer(app);

server.listen(port);

*/