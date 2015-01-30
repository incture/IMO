CW URL Preview is a Node library for creating a preview object of URL. Pass a URL or a block of text containg a URL to the `load()` method of an instance of the library.

## Installation

```
$ npm install incture/cw-module-url-preview
```

## API

**Accepts**

`urlPreview.load()` accepts a URL, or a block of text, which may containg a URL.

**Returns**

A preview object with the following properties:

| Property       | Description |
|---------------|---------------
| baseUrl       | The base URL of the website.
| images        | Array of images found on the page. Limited to 5.
| previewText   | Preview text. Limited to 200 characters.
| url           | URL of the page.
| text          | Text that was passed to the `load()` method.
| title         | Title of the page.


## Usage

```
var urlPreview = require('cw-module-url-preview')

var text = 'Hey ladies, check these out - http://www.flipkart.com/women!'
var preview = urlPreview.load(text)
preview.then(function (p) {
    console.log(p)
  }, function (fail) {
    throw new Error(fail)
  }
)
```
