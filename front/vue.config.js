module.exports = {
    baseUrl: undefined,
    runtimeCompiler: undefined,
    productionSourceMap: undefined,
    parallel: undefined,
    css: undefined,
    outputDir: '../src/main/resources/pages',
    assetsDir: 'static',

    devServer: {
        hot: true,
        proxy: {
            '/*': {
                // target: 'https://family.wangyalan.net',
                target: 'http://localhost:18111',
                secure: false,
                changeOrigin: true,
                // bypass: function (req, res, proxyOptions) {
                //     console.info("req:", req.originalUrl);
                //     if (req.originalUrl.indexOf("/sockjs-node") != -1) {
                //         return false;
                //     }
                // },
                ws: false,
                pathRewrite: {
                    "^/*": "/"
                }
            }
        }
    },
    chainWebpack: config => {
        config
            .plugin('html')
            .tap(args => {
                args[0].inject ='body';
                return args;
            })
    }
}
