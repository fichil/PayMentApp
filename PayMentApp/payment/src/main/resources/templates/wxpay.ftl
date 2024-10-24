<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <script>
        function onBridgeReady() {
            WeixinJSBridge.invoke('getBrandWCPayRequest', {
                    "appId": "${appId}",     //公众号ID，由商户传入
                    "timeStamp": "${timeStamp}",     //时间戳，自1970年以来的秒数
                    "nonceStr": "${nonceStr}",      //随机串
                    "package": "${packageStr}",     //注意：在 FreeMarker 中，不能使用 `package` 作为变量名
                    "signType": "RSA",     //微信签名方式：
                    "paySign": "${paySign}"  //微信加密签名
                },
                function(res) {
                    if (res.err_msg == "get_brand_wcpay_request:ok") {
                        // 使用以上方式判断前端返回,微信团队郑重提示：
                        //res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
                        window.location.href = "http://hutu.natapp1.cc/#/WXSuccessPage"; // 跳转到成功页面
                    }
                });
        }
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                console.log("执行")
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        } else {
            onBridgeReady();
        }
    </script>
</head>
<body>
<div id="app">
</div>
</body>
</html>