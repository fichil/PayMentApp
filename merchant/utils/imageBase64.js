function urlTobase64(url) {
    return new Promise((resolve, reject) => {
        uni.request({
            url: url,
            responseType: 'arraybuffer', // 设置返回的数据格式为arraybuffer
            success: res => {
                if (res.statusCode === 200) {
                    // 将 arraybuffer 转换成 base64
                    let base64 = wx.arrayBufferToBase64(res.data);
                    // 拼接 MIME 类型前缀
                    base64 = 'data:image/jpeg;base64,' + base64;
                    console.log('base64=>', base64);
                    resolve(base64); // 成功时返回 base64 字符串
                } else {
                    reject(`请求失败，状态码: ${res.statusCode}`);
                }
            },
            fail: err => {
                reject(`请求错误: ${err}`);
            }
        });
    });
}

export default urlTobase64;