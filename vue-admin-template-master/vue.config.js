'use strict'
const path = require('path')
const defaultSettings = require('./src/settings.js')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const name = defaultSettings.title || 'Vue 管理模板' // 页面的标题

// 如果您的端口设置为 80，
// 请使用管理员权限执行命令行。
// 例如，Mac: sudo npm run
// 您可以通过以下方法更改端口：
// port = 9528 npm run dev 或 npm run dev --port=9528
const port = process.env.port || process.env.npm_config_port || 9528 // 开发端口

// 所有配置项的说明可以在 https://cli.vuejs.org/config/ 找到
module.exports = {
  transpileDependencies: ['vue-echarts', 'resize-detector'],
  devServer:{
    proxy: {
      '/api': {
        target: 'http://202.101.100.17:9090/',
        changeOrigin: true,
        CookieDomain: 'localhost',
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },

  publicPath: '/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'false',
  productionSourceMap: false,

  configureWebpack: {
    // 提供应用的标题到 webpack 的 name 字段，以便
    // 它可以在 index.html 中访问以注入正确的标题。
    name: name,
    resolve: {
      alias: {
        '@': resolve('src')
      }
    }
  },
  chainWebpack(config) {
    // 启用预加载可以提高首屏加载速度，建议开启
    config.plugin('preload').tap(() => [
      {
        rel: 'preload',
        // 忽略 runtime.js
        // https://github.com/vuejs/vue-cli/blob/dev/packages/@vue/cli-service/lib/config/app.js#L171
        fileBlacklist: [/\.map$/, /hot-update\.js$/, /runtime\..*\.js$/],
        include: 'initial'
      }
    ])

    // 当页面很多时，会造成过多无意义的请求
    config.plugins.delete('prefetch')

    // 设置 svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          config
            .plugin('ScriptExtHtmlWebpackPlugin')
            .after('html')
            .use('script-ext-html-webpack-plugin', [{
            // `runtime` 必须与 runtimeChunk 名称相同。默认是 `runtime`
              inline: /runtime\..*\.js$/
            }])
            .end()
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // 仅打包初始依赖的第三方库
                },
                elementUI: {
                  name: 'chunk-elementUI', // 将 elementUI 拆分为单独的包
                  priority: 20, // 权重需要大于 libs 和 app，否则会被打包到 libs 或 app 中
                  test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // 为适应 cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // 可以自定义规则
                  minChunks: 3, // 最小公共数量
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          // https://webpack.js.org/configuration/optimization/#optimizationruntimechunk
          config.optimization.runtimeChunk('single')
        }
      )
  }
}