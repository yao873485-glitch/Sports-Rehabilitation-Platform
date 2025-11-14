const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: '/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: false,
  productionSourceMap: false,
  
  devServer: {
    port: 3000,
    open: ['/dashboard'],
    proxy: {
      '/api': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/api': '/api'
        }
      },
      '/uploads': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        pathRewrite: {
          '^/uploads': '/uploads'
        }
      }
    }
  },

  css: {
    loaderOptions: {
      sass: {
        // 移除重复导入，避免循环导入错误
      }
    }
  },

  chainWebpack: config => {
    config.plugin('html').tap(args => {
      args[0].title = '运动康复医疗管理平台'
      return args
    })

    // 配置Node.js核心模块的polyfill
    config.resolve.fallback = {
      path: require.resolve('path-browserify'),
      net: false,
      tls: false,
      fs: false
    }
  },

  configureWebpack: {
    resolve: {
      alias: {
        path: require.resolve('path-browserify')
      }
    }
  }
})
