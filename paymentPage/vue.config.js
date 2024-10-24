const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: './',
  devServer: {
    allowedHosts: [
      'hutu.natapp1.cc',
      'localhost',
      '127.0.0.1',
    ],
    // "proxy" 定义代理服务器的主机名称和端口
    proxy: {
      // '/healthCheck_war_exploded': {
      //   target: "http://localhost:8090/healthCheck_war_exploded",
      //   changeOrigin: true,
      //   pathRewrite: {
      //     "^/healthCheck_war_exploded": ""
      //   }
      // },
      '/api': {
        target: "http://ccj.nat300.top",
        changeOrigin: true,
        pathRewrite: {
          "^/api": ""
        }
      }

    }
  }



})
