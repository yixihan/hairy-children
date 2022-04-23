<template>
  <div class="meetpets">
    <div class="adoption">
      <h2>领养</h2>
      <ul>
        <li v-for="(item, index) in adoptionAvatar" :key="index">
          <a href="javascript:;">
            <img :src="item" alt="">
            <div class="article">
              <h3 class="title">领养一只小猫咪</h3>
              <p>猫,属于猫科动物,分家猫、野猫, 是全世界家庭中较为广泛的宠物。家猫的祖先据推测是古埃及的沙漠猫,波斯的波斯猫,已经被人类驯化了3500年(但未像狗一样完全地被驯化)。一般的猫：头圆、颜面部短,前肢五指,后肢四趾,趾端具锐利而弯曲的爪,爪能伸缩。夜行性。以伏击的方式猎捕其它动物,大多能攀援上树。猫的趾底有脂肪质肉垫,以免在行走时发出声响,捕猎时也不会惊跑鼠。行进时爪子处于收缩状态,防止爪被磨钝,在捕鼠和攀岩时会伸出来。</p>
              <div class="petstatus">
                <el-tag class="adopted">已领养</el-tag>
              </div>
            </div>
          </a>
        </li>
      </ul>
      <div class="foot"></div>
    </div>
    <div class="findpets">
      <h2>寻宠</h2>
      <ul>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
      </ul>
      <div class="foot"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MeetPets',
  data () {
    return {
      adoptionAvatar: [
        require('@/assets/img/cat/avatar/1.jpg'),
        require('@/assets/img/cat/avatar/2.jpg'),
        require('@/assets/img/cat/avatar/3.jpg'),
        require('@/assets/img/cat/avatar/4.jpg'),
        require('@/assets/img/cat/avatar/5.jpg')
      ],
      findpetsAvatar: [
        require('@/assets/img/cat/avatar/1.jpg'),
        require('@/assets/img/cat/avatar/2.jpg'),
        require('@/assets/img/cat/avatar/3.jpg'),
        require('@/assets/img/cat/avatar/4.jpg'),
        require('@/assets/img/cat/avatar/5.jpg')
      ]
    }
  }
}
</script>

<style scoped lang="scss">
* {
  margin: 0;
  padding: 0;
}
.meetpets {
  margin: 26px auto;
  width: 100%;
  max-width: 1280px;
  // height: 600px;
  background: #fff;
  box-sizing: border-box;
  display: flex;
  .adoption, .findpets {
    border: 2px solid #7da5b3;
    background: #fff;
    width: 50%;
    h2 {
      height: 80px;
      line-height: 80px;
      color: #7da5b3;
      font-size: 26px;
    }
    ul {
      list-style: none;
      padding: 20px;
      width: 100%;
      box-sizing: border-box;
      height: 720px;
      background: #fbfbfb;
      li {
        width: 100%;
        height: 120px;
        padding: 10px;
        box-sizing: border-box;
        margin-bottom: 20px;
        background: #fff;
        a {
          display: flex;
          text-decoration: none;
          padding-right: 10px;
          img {
            width: 100px;
            height: 100px;
            margin-right: 10px;
            object-fit: cover;
          }
          // 帖子条
          .article {
            .title {
              color: #313131;
              height: 30px;
              line-height: 30px;
              font-size: 18px;
              text-align: start;
            }
            p {
              color: #acacac;
              text-align: start;
              font-size: 14px;
              // 文字超出折叠
              overflow:hidden;
              display:-webkit-box; //将对象作为弹性伸缩盒子模型显示;
              text-overflow:ellipsis;//溢出部分用省略号代替
              -webkit-line-clamp:2; //设置文本显示两行
              -webkit-box-orient:vertical;  //从上到下排列子元素;
              white-space:normal;
            }
            .petstatus {
              position: relative;
              ::v-deep .el-tag {
                position: absolute;
                right: 0;
                height: 25px;
                line-height: 25px;
              }
              .adopted {
                color: #1fb1e6;
              }
            }
          }
        }
        a:hover {
          .title {
            color: #7da5b3 !important;
          }
        }
      }
    }
    .foot {
      height: 80px;
      width: 100%;
    }
  }
}
</style>
