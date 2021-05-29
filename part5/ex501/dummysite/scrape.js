// scrape.js
const scrape = require('website-scraper');
const PuppeteerPlugin = require('website-scraper-puppeteer');
const path = require('path');

console.log("Scraping " + process.env.WEBSITE_URL)

scrape({
    // Provide the URL(s) of the website(s) that you want to clone
    // In this example, you can clone the Our Code World website
    urls: [process.env.WEBSITE_URL],
    // Specify the path where the content should be saved
    // In this case, in the current directory inside the ourcodeworld dir
    directory: path.resolve(__dirname, 'public'),
    // Load the Puppeteer plugin
    plugins: [ 
        new PuppeteerPlugin({
            launchOptions: { 
                // If you set  this to true, the headless browser will show up on screen
                headless: true,
                args: [
      // Required for Docker version of Puppeteer
      '--no-sandbox',
      '--disable-setuid-sandbox',
      // This will write shared memory files into /tmp instead of /dev/shm,
      // because Docker’s default for /dev/shm is 64MB
      '--disable-dev-shm-usage'
    ],
            }, /* optional */
            scrollToBottom: {
                timeout: 10000, 
                viewportN: 10 
            } /* optional */
        })
    ]
});

var http = require('http');

var finalhandler = require('finalhandler');
var serveStatic = require('serve-static');

var serve = serveStatic("./public");

var server = http.createServer(function(req, res) {
  var done = finalhandler(req, res);
  serve(req, res, done);
});

console.log("Starting server at port 8080")
server.listen(8080);
