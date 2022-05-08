<template>
  <div class="write">
    <mavon-editor
      v-model="title.markdown"
      ref="md"
      @save="saveArticle"
      @imgAdd="$imgAdd"
      @imgDel="$imgDel"
    />
  </div>
</template>

<script>
export default {
  props: ["titleId"],
  data() {
    return {
      title: {
        markdown: "",
        html: "",
        titleId: "",
      },
      link: "",
      img_file: {}, // 一次上次多张图片时使用
      /* 新建类型 */
      /* 文件列表 */
      activeName: "",
      /* 添加文件 */
      showEditModal: false,
      /* 菜单栏 */
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true, // 预览
      },
    };
  },
  mounted() {
    // 如果原始md字符串中存在曾上传的图片， 则需要将对应<img>中的src替换为base64
    this.$nextTick(() => {
      // $vm.$imgUpdateByUrl 详情见本页末尾
      this.$refs.md.$imgUpdateByUrl(0, "base64内容");
    });
  },
  created() {
    this.title.titleId = this.titleId;
    this.init();
    console.log(this.titleId);
  },
  methods: {
    // 删除图片
    $imgDel(pos) {
      delete this.img_file[pos];
    },
    // 添加图片
    // 绑定@imgAdd event
    $imgAdd(pos, $file) {
      // 第一步.将图片上传到服务器.
      var formdata = new FormData();
      formdata.append("img", $file);
      this.$axios({
        url: "/title/updateImg/" + this.title.titleId,
        method: "post",
        data: formdata,
        headers: {
          "Content-Type": "multipart/form-data",
          "Jwt-Token": this.$store.getters.getToken,
        },
      }).then((res) => {
        // 第二步.将返回的url替换到文本原位置![...](./0) -> ![...](url)
        /**
         * $vm 指为mavonEditor实例，可以通过如下两种方式获取
         * 1. 通过引入对象获取: `import {mavonEditor} from ...` 等方式引入后，`$vm`为`mavonEditor`
         * 2. 通过$refs获取: html声明ref : `<mavon-editor ref=md ></mavon-editor>，`$vm`为 `this.$refs.md`
         */
        console.log(res);
        let url = res.data.data.imgDir;
        url = url.replace(/\\/g, '/')
        console.log(url);
        this.$refs.md.$img2Url(
          pos,
          this.$store.getters.getUrl + url
        );
      });
    },
    // 保存文章
    saveArticle() {
      const htmlCode = this.$refs.md.d_render;
      const markdownCode = this.$refs.md.d_value;
      // console.log(htmlCode)
      if (htmlCode.length === 0 || markdownCode.length === 0) {
        alert("请填写");
        return;
      }
      // this.exportRaw(this.title.titleName + ".md", markdownCode); // 第一个参数是文件名称,第二个参数为字符串
      this.$axios({
        url: "/title/updateTitle",
        method: "post",
        // data: JSON.stringify({ 'markdown': markdownCode, 'html': htmlCode }),
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleId: this.titleId,
          titleContent: markdownCode,
          isFinish: this.title.isFinish == -1 ? 0 : this.title.isFinish,
        },
      }).then(({data}) => {
        console.log(data)
        if (data.code == 200) {
          this.$message({
            message: "文章保存成功!",
            type: "success",
          });
          this.$router.push("/article/" + this.titleId);
        } else {
          this.$message({
            message: "文章保存失败!",
            type: "error",
          });
        }
      });
    },
    exportRaw (name, data) {
      var urlObject = window.URL || window.webkitURL || window
      var exportBlob = new Blob([data])
      var saveLink = document.createElementNS('http://www.w3.org/1999/xhtml', 'a')
      this.link = saveLink
      console.log(this.link)
      saveLink.href = urlObject.createObjectURL(exportBlob)
      saveLink.download = name
      this.fakeClick(saveLink)
    },
    fakeClick (obj) {
      var ev = document.createEvent('MouseEvents')
      ev.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
      obj.dispatchEvent(ev)
    },
    async getArticle() {
      const data = await this.$axios({
        url: "/title/getTitle",
        method: "post",
        // data: JSON.stringify({ 'markdown': markdownCode, 'html': htmlCode }),
        headers: {
          "Jwt-Token": this.$store.getters.getToken,
        },
        data: {
          titleId: this.titleId,
        },
      });

      return data;
    },
    init() {
      this.getArticle().then(({ data }) => {
        console.log(data);
        this.title = data.data.title;
        this.title.markdown = this.title.titleContent
      });
    },
    
  },
};
</script>

<style>
</style>