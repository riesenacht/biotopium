const webpack = require("webpack")
config.plugins.push(new webpack.ProvidePlugin({
    process: 'process/browser',
}))