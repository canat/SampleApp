<html>
<body>
<link rel="stylesheet" href="//releases.flowplayer.org/7.0.4/skin/skin.css">
<script src="http://releases.flowplayer.org/7.0.4/flowplayer.min.js"></script>
<script src="http://releases.flowplayer.org/hlsjs/flowplayer.hlsjs.light.min.js"></script>

<style>
#hlsjslive {
  background-color: #2f2f4f;
}
</style>
<div id="hlsjslive" class="fp-slim"></div>
<script type="text/javascript">
  flowplayer("#hlsjslive", {
    autoplay: true,
    ratio: 9/16,
    // stream only available via https:
    // force loading of Flash HLS via https
    swfHls: "https://releases.flowplayer.org/7.0.4/flowplayerhls.swf",
    clip: {
      live: true,
      sources: [
        { type: "application/x-mpegurl",
          src: "http://localhost:5080/PSCP/streams/6D3uCpLCQQ7XyPma1500015749198_adaptive.m3u8" }
      ]
    }
  });

</script>
<!-- http://d2fwxbu2yw8im8.cloudfront.net/vod/streams/beytepe_adaptive.m3u8 -->
</body>
</html>
