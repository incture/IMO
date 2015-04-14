var request = require('request')
var cheerio = require('cheerio')
var Q = require('q')
var youtubeApi = "https://www.googleapis.com/youtube/v3/videos?key=AIzaSyB1OOSpTREs85WUMvIgJvLTZKye4BVsoFU&part=snippet&id=";
var re = /(((http|ftp|https):\/{2})+(([0-9a-z_-]+\.)+(aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|cz|de|dj|dk|dm|do|dz|ec|ee|eg|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mn|mn|mo|mp|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|nom|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ra|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sj|sk|sl|sm|sn|so|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw|arpa)(:[0-9]+)?((\/([~0-9a-zA-Z\#\+\%@\.\/_-]+))?(\?[0-9a-zA-Z\+\%@\/&\[\];=_-]+)?)?))\b/i
var URL = require('url');
//var re = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/

var imagesLimit = 5

module.exports = {

  load: function (text) {

    var q = Q.defer()
    var previewObject = {
      baseUrl: '',
      title: '',
      previewText: '',
      images: [],
      url: '',
      text: text
    }

    // check if text has a url
    var match = text.match(re)

    if (match) {
      var url = match[0]
      var youTubeUrl = URL.parse(url);
      if(youTubeUrl['host'] == 'www.youtube.com' || youTubeUrl['host'] == 'youtube.com'){
        //Handling a Youtube Url
        //var _videoId = youTubeUrl['query'] && youTubeUrl['query'].split('&') && youTubeUrl['query'].split('&').slice(2);
        var _videoId = youTubeUrl['query'] && youTubeUrl['query'].split('&')[0] && youTubeUrl['query'].split('&')[0].slice(2);
        if(_videoId){
          //console.log(_videoId)
          var _url = youtubeApi + _videoId;
          request(_url, function (error, response, body) {
            if (error) {
              q.reject(error);
            }
            else {
              var data = JSON.parse(body);
              if (response.statusCode == 200) {
                //console.log(data);
                var video = data.items[0].snippet;
                previewObject.baseUrl = url;
                previewObject.title = video.title;
                previewObject.text = video.description.slice(0, 200)
                previewObject.text = video.description;
                previewObject.images.push("http://img.youtube.com/vi/"+data.items[0].id+"/default.jpg")
                //previewObject.preView = ["http://img.youtube.com/vi/"+data.items[0].id+"/1.jpg","http://img.youtube.com/vi/"+data.items[0].id+"/2.jpg","http://img.youtube.com/vi/"+data.items[0].id+"/3.jpg"];
                q.resolve(previewObject);
              }
              else {
                q.reject(data.error);
              }
            }
          });
        }
        else{
          q.reject(new Error("Input Url is not pointing to a youtube video."));
        }
      }
      else{
        request(url, function (error, response, body) {

          if (!error && response.statusCode == 200) {

            var $ = cheerio.load(body)

            previewObject.baseUrl = url.split('/', 3).join('/')
            previewObject.title = $('title').text().trim() || ''
            previewObject.previewText = $('p').text().replace(/\s{2,}/g, '').substring(0, 200)

            var images = []
            var imageCount = 0
            $('img').each(function (i, image) {

              if (imageCount == imagesLimit) return false
              if ($(image).attr('src')) {

                var src = $(image).attr('src').trim()
                var ext = src.split('.').slice(-1)[0].toLowerCase()
                //console.log(src, ext)
                if (ext !== 'svg') {
                  images.push(src)
                  imageCount++
                }
              }

            })

            previewObject.images = images

            previewObject.url = url

            q.resolve(previewObject)

          }
          else {
            q.reject(error)
          }
        })
      }
    }
    else {
      q.resolve(previewObject)
    }
    return q.promise
  }
}
