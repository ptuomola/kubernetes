FROM buildkite/puppeteer
WORKDIR /app
RUN npm install website-scraper website-scraper-puppeteer finalhandler serve-static
COPY scrape.js .
EXPOSE 8080
CMD [ "node", "scrape.js" ]
