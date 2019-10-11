<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body onload="ll()">
<h2>Hello World!</h2>
<form action="/asd.do" enctype="multipart/form-data" method="post">
    <input type="file" name="text">
    <input type="text">
    <input type="submit">
</form>
<form action="/copyf.do"  method="post">
    <input type="file" name="copy">
    <input type="submit" value="复制二进制">
</form>
<form action="/copy.do"  method="post">
    <input type="file" name="copy">
    <input type="submit" value="复制">
</form>
<form action="/move.do" enctype="multipart/form-data" method="post">
    <input type="file" name="copy">
    <input type="submit" value="移动">
</form>
<form action="/copyj.do" enctype="multipart/form-data" method="post">
    <input type="file" name="copy">
    <input type="submit" value="文件加密复制">
</form>
<form action="/copyji.do" enctype="multipart/form-data" method="post">
    <input type="file" name="copy">
    <input type="submit" value="文件解密复制">
</form>
<form action="/md5.do" method="post">
    <input type="text" name="md5">
    <input type="submit" value="加密">
</form>
<form action="/download.do" method="post">
    <input type="text" name="path">
    <input type="radio" name="online" id="down" value="false"><label for="down">仅下载</label>
    <input type="radio" name="online" id="downyu" value="true"><label for="downyu">在线预览</label>
    <input type="submit" value="下载">
</form>
<form action="/down.do" method="post">
    <input type="text" name="path">
    <input type="submit" value="URL一次写完下载">
</form>
<form action="/download1.do" method="post">
    <input type="text" name="path">
    <input type="submit" value="URL边读边写下载">
</form>
<form action="/download2.do" method="post">
    <label for="number">几个线程</label>><input type="number" name="number" id="number" value="3">
    <label for="path">地址</label><input type="text" name="path" id="path">
    <input type="submit" value="断点下载">
</form>
<a href="/ll1.do">asdfghjkl</a>
<input>
<div>
        ${md5}
</div>
<a href="aaa.xml">asjdkfsaj</a>
<div id="divv"></div>
</body>
</html>
<script>
    window.onload=function() {
        // alert("ASD")
        var aaa=document.getElementsByTagName("a");
        debugger;
        var divv=document.getElementById("divv");
        for (var i=0;i<aaa.length;i++){
            var a=aaa[i];
            a.onclick = function () {
                var method="GET";
                var url=this.href;
                var xhr=new XMLHttpRequest();
                xhr.open(method,url);
                xhr.send(null)
                // xhr.onload=function(){
                //     var xml=xhr.responseXML;
                //     var name=xml.getElementsByTagName("name")[0].firstChild.nodeValue;
                //     var value=xml.getElementsByTagName("value")[0].childNodes[0].nodeValue;
                //     console.log(name)
                //     divv.innerHTML=name+value;
                // }
                xhr.onreadystatechange=function () {
                    console.log("123123123"+xhr.status+"_____________"+xhr.readyState)
                    if(xhr.status==200&&xhr.readyState==4){
                        var xml=xhr.responseXML;
                        var name=xml.getElementsByTagName("name")[0].firstChild.nodeValue;
                        var value=xml.getElementsByTagName("value")[0].childNodes[0].nodeValue;
                        // console.log(name)
                        divv.innerHTML=name+"123123123"+value;
                    }
                }
                // alert("sdaf");
                return false;
            }
        }
    }
</script>
