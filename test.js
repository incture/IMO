var urlPreview = require('./')
var text = 'Hey checkout this cool git doc - http://imgur.com/rRKKhZF. The best I have seen so far.'
var previewer = urlPreview.hasUrl(text)


previewer.then(function (p) {

  console.log(p)

    // console.log('Title:', p.title)
    // console.log('Preview:', p.preview)
    // console.log('URL:', p.url)

  }, function () {
    console.log('No URL detected')
  }
)
