var expect = require('chai').expect
var express = require('express')
var urlPreview = require('../')

describe('URL detection in text', function () {


  it('should detect URL in text', function (done) {

    var app = express()
    app.use(express.static(__dirname + '/fixtures'))
    var server = app.listen(function () {

      //var testUrl = 'go get http://www.flipkart.com/women?otracker=nmenu_women jaj'
      var testUrl = 'http://localhost:' + server.address().port + '/image.html'

      var text = 'There is something cool here ' + testUrl + '. What do you say?'
      console.log(text)
      var preview = urlPreview.load(text)

      preview.then(function (p) {

        console.log(p)

          // console.log('Title:', p.title)
          // console.log('Preview:', p.preview)
          // console.log('URL:', p.url)

          done()

        }, function () {
          console.log('No URL detected')
        }
      )


    })



  })




})
