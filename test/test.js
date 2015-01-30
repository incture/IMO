var expect = require('chai').expect
var express = require('express')
var urlPreview = require('../')


describe('Properties', function () {

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
        console.log(p)
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
        console.log(p)
        done()
      }, function (fail) {
        throw new Error(fail)
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
