<template>
  <div class="dashboard-container" style="position: relative;">
    <el-card class="box-card" style="width: 500px; height: 400px; position: absolute;">
      <p>近7日交易额:</p>
      <h1>￥:<span>{{ sevenDayTrdeSum }}</span></h1>
      <div ref="sevenDayTrade" style="width: 450px; height: 250px;"></div>
    </el-card>
    <el-card class="box-card" style="width: 250px; height: 120px; position: absolute; left: 550px;">
      <p>今日交易额:</p>
      <h1>￥:<span>{{ todayTradeSum }}</span></h1>
    </el-card>
    <el-card class="box-card" style="width: 250px; height: 120px; position: absolute; left: 550px; top: 140px;">
      <p>今日交易笔数:</p>
      <h1><span>{{ todayCount }}</span>笔</h1>
    </el-card>
    <el-card class="box-card" style="width: 250px; height: 120px; position: absolute; left: 550px; top: 280px">
      <p>今日活跃商户数量:</p>
      <h1><span>{{ todayActiveMerchantNum }}</span>个</h1>
    </el-card>
    <el-card class="box-card" style="width: 500px; height: 400px; position: absolute; left: 850px;">
      <p>支付方式:</p>
      <div ref="twiceMethods" style="width: 450px; height: 250px;"></div>
    </el-card>
    <!-- <el-button class="myButton" type="primary" @click="goSupport()">客服</el-button> -->
    <!-- <a class="myButton" href="http://localhost:9065/#/MainView" target="_blank">客服跳转</a> -->
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import * as echarts from 'echarts';
import { GetSevenInfo, GetTwiceInfo } from '@/api/grid';

export default {
  name: 'Dashboard',
  data() {
    return {
      sevenDayTrdeSum:0,//7日交易总额
      todayTradeSum:0,//今日交易总额
      todayActiveMerchantNum:0,//今日活跃商户数
      sevenDayGridX:[],//日期
      sevenDayGridY:[],//数值
      todayCount:0,//今日交易数
      alipayList:[],//支付宝数据列表
      wechatList:[],//微信数据列表
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  created(){
  },
  mounted() {
    this.initSevenDayData()
    this.initTwiceData()
  },
  methods: {
    //客服跳转
    goSupport(){
      // window.location.href="http://www.baidu.com";
      window.open("https://www.baidu.com", "_blank");
    },
    //获得数据
    initSevenDayData(){
      GetSevenInfo().then(res=>{
        this.sevenDayGridX = res.data.date;
        this.todayActiveMerchantNum = res.data.activeCount;
        this.sevenDayGridY = res.data.sevenPrice;
        this.todayTradeSum = res.data.todayPrice;
        this.todayCount = res.data.todayCount;     
        this.sevenDayTrdeSum = res.data.sevenDaySum
        this.initSevenDayChart()
      })
    },
    initTwiceData(){
      GetTwiceInfo().then(res=>{
        this.alipayList = res.data.alipay;
        this.wechatList = res.data.wechat;
        this.initTwiceMethods()
      })
    },

    //初始化图表
    //初始化7日图表
    initSevenDayChart() {    
      const sevenDayTradeGrid = echarts.init(this.$refs.sevenDayTrade)
      const sevenDayTradeOption = {
        grid: {
          left: '10%',    // 左边距
          right: '0%',   // 右边距
          bottom: '10%',  // 底部边距
          top: '10%',     // 顶部边距
        },
        xAxis: {
          type: 'category',
          data: this.sevenDayGridX
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: this.sevenDayGridY,
            type: 'line',
            label: {
              show: false,
              position: 'top',
              textStyle: {
                fontSize: 20
              }
            },
            emphasis: {
              label: {
                show: true
              }
            }
          }
        ]
      }
      sevenDayTradeGrid.setOption(sevenDayTradeOption);
    },
    //初始化两种支付方式的图表
    initTwiceMethods() {
      const twiceMethodsGrid = echarts.init(this.$refs.twiceMethods)
      const twiceMethodsOption = {
        grid: {
          left: '0%',    // 左边距
          right: '0%',   // 右边距
          bottom: '10%',  // 底部边距
          top: '10%',     // 顶部边距
          containLabel: true
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['支付宝', '微信']
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.sevenDayGridX
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '支付宝',
            type: 'line',
            stack: 'Total',
            data: this.alipayList
          },
          {
            name: '微信',
            type: 'line',
            stack: 'Total',
            data: this.wechatList
          }
        ]
      }
      twiceMethodsGrid.setOption(twiceMethodsOption);
      }

    }
  }
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }

}

.myButton{
  position: relative;
  top: 420px;
  border: 1px red solid;
}

</style>
