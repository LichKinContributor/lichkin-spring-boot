var onBodyloaded = function () {
  var w = document.documentElement.clientWidth;
  var h = document.documentElement.clientHeight;
  var topHeight = document.getElementById('topDiv').offsetHeight;
  var bottomHeight = document.getElementById('bottomDiv').offsetHeight;
  var size = h - topHeight - bottomHeight;
  if (w > h) {
    document.getElementById('logo').style.width = document.getElementById('logo').style.height = size + 'px';
  } else {
    document.getElementById('centerDiv').style.height = size + 'px';
    document.getElementById('logo').style.width = document.getElementById('logo').style.height = w + 'px';
    document.getElementById('logo').style.marginTop = (size - w) / 2 + 'px';
  }
};