node -v
npm -v

npm install -g ember-cli
npm install -g bower

npm install -g phantomjs

ember new my-new-app
cd my-new-app
ember server


in ember-cli-build.js

app.import('bower_components/pure/pure-min.css');
app.import('bower_components/font-awesome/css/font-awesome.min.css');
app.import('bower_components/font-awesome/fonts/fontawesome-webfont.woff2', {
  destDir: 'fonts'
});
app.import('bower_components/font-awesome/fonts/fontawesome-webfont.woff', {
  destDir: 'fonts'
});
app.import('bower_components/font-awesome/fonts/fontawesome-webfont.ttf', {
  destDir: 'fonts'
});
