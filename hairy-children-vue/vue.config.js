// module.exports = {
//   publicPath: './',
//   devServer: {
//       open: true, //浏览器自动打开页面
//       host: "localhost", //如果是真机测试，就使用这个IP
//       port: 8080,
//       https: false,
//       hotOnly: false, //热更新（webpack已实现了，这里false即可）
//       proxy: {
//           //配置跨域-将所有/api的请求拦截，代理到target上
//           '/': {
//               target: "http://localhost:9421/",
//               ws:true,
//               changOrigin:true,
//           }
//       }
//   }
// }