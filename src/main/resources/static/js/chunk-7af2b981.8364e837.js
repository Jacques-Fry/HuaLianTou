(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-7af2b981"],{7229:function(t,e,o){},af29:function(t,e,o){"use strict";o.r(e);var r,n=function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",[o("div",{staticClass:"news"},[o("p",{staticClass:"title"},[t._v("头像设置")]),o("div",{staticClass:"content"},[o("div",[o("div",{staticClass:"ts"},[o("img",{attrs:{src:""==t.formData.photo?t.imgUrl+t.userInfo.photo:t.imgUrl+t.formData.photo,alt:""}}),o("p",{staticStyle:{color:"#2868d1"}},[t._v(t._s(""==t.formData.photo?"*点击按钮上传新头像":"*已添加,想更换直接上传新头像即可"))])]),o("br"),o("br"),o("Upload",{attrs:{accept:"image/*",action:t.fileconfig.fileUrl,headers:t.fileconfig.fileHead,name:"file","on-success":t.photo,"show-upload-list":!1}},[o("Button",{attrs:{icon:"ios-cloud-upload-outline"}},[t._v("选择新的头像")])],1)],1),o("br"),o("br"),o("br"),o("Button",{attrs:{type:"primary"},on:{click:t.submit}},[t._v("确认提交新头像")])],1)])])},c=[],a=(o("efce"),o("4634"),o("ed8b"),o("f498")),i=(o("7cfd"),o("6327")),s=o("c24f"),f=o("025e"),u=o("08c1");function l(t,e){var o=Object.keys(t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(t);e&&(r=r.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),o.push.apply(o,r)}return o}function p(t){for(var e=1;e<arguments.length;e++){var o=null!=arguments[e]?arguments[e]:{};e%2?l(o,!0).forEach((function(e){Object(a["a"])(t,e,o[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(o)):l(o).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(o,e))}))}return t}var b={components:(r={},Object(a["a"])(r,i["Button"].name,i["Button"]),Object(a["a"])(r,i["Upload"].name,i["Upload"]),r),computed:p({},Object(u["c"])("user",["userInfo"]),{},Object(u["c"])("app",["token"])),data:function(){return{imgUrl:f["d"],fileconfig:{fileUrl:f["c"],fileHead:{token:null}},formData:{photo:""}}},methods:p({},Object(u["b"])("user",["getUserInfo"]),{photo:function(t){"SUCCESS"==t.status&&(this.formData.photo=t.data.url)},submit:function(){var t=this;""!=this.formData.photo?Object(s["b"])(this.formData).then((function(e){"SUCCESS"==e.data.status&&t.getUserInfo().then((function(){f["e"].success("头像修改成功"),t.$router.push("/user/set-index")}))})):f["e"].error("还没有上传新头像")}}),created:function(){this.fileconfig.fileHead.token=this.token}},d=b,h=(o("dffb"),o("4e82")),m=Object(h["a"])(d,n,c,!1,null,"b9588cd8",null);e["default"]=m.exports},dffb:function(t,e,o){"use strict";var r=o("7229"),n=o.n(r);n.a}}]);
//# sourceMappingURL=chunk-7af2b981.8364e837.js.map