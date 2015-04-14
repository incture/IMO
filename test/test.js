var urlPreview = require('../')
var expect = require('chai').expect
var express = require('express')

describe('Properties', function () {

  this.timeout(10000)

  it('should have title property', function (done) {
    var preview = urlPreview.load('')
    preview.then(function (p) {
        expect(p).to.have.property('title')
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should have previewText property', function (done) {
    var preview = urlPreview.load('')
    preview.then(function (p) {
        expect(p).to.have.property('previewText')
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should have images property', function (done) {
    var preview = urlPreview.load('')
    preview.then(function (p) {
        expect(p).to.have.property('images')
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should have images as an array', function (done) {
    var preview = urlPreview.load('')
    preview.then(function (p) {
        expect(p.images).to.be.an('array')
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should have text property', function (done) {
    var preview = urlPreview.load('')
    preview.then(function (p) {
        expect(p).to.have.property('text')
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should have url property', function (done) {
    var preview = urlPreview.load('')
    preview.then(function (p) {
        expect(p).to.have.property('url')
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should have baseUrl property', function (done) {
    var preview = urlPreview.load('')
    preview.then(function (p) {
        expect(p).to.have.property('baseUrl')
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

})


// NOTE: these tests are best written using local resources
// external sites may be gone
// using console.log() in the mean time
describe('URLs', function () {

  it('should recognize URL in text', function (done) {

    var text = 'Hey ladies, check these out - http://www.flipkart.com/women!'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should parse a common webpage', function (done) {

    var text = 'http://yourstory.in'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should ignore an invalid page', function (done) {

    var text = 'http://iaskdajsldkjadlk.in'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done(p)
      }, function (fail) {
        done()
      }
    )
  })

  it('should parse a URL without http prefix', function (done) {

    var text = 'www.wikipedia.com'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should parse a page requiring authentication', function (done) {

    var text = 'https://docs.google.com/a/incture.com/spreadsheets/d/1kfZZh59L99APd2GT3K_EGjk7tFzCt2vEyNUZP-EQi04/edit#gid=0'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })


  it('should follow temporary redirects', function (done) {

    var text = 'http://www.iana.org/domains/example/'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should follow permanent redirects', function (done) {

    var text = 'http://www.cherrywork.in'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should recognize a text which is a URL', function (done) {

    var text = 'http://www.flipkart.com/men'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should recognize Youtube Url and get the details from the video', function (done) {

    var text = 'https://www.youtube.com/watch?v=29XjUFmmdik'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        //console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
      }
    )
  })

  it('should ignore an invalid youtube url', function (done) {

    var text = 'https://www.youtube.com/watch'
    var preview = urlPreview.load(text)
    preview.then(function (p) {
        console.log(p)
        done(p)
      }, function (fail) {
        //console.log(fail)
        done()
      }
    )
  })

})


describe('Title', function () {

  // add more title specific tests

})

describe('Body', function () {

  // add more body specific tests

})

describe('Images', function () {

  // add more images specific tests

})
