

<!-- <template> -->
  <!-- <div id="app">
 
         <el-container>
            <el-aside width="200px">
                <el-row class="tac" >
                    <el-col  >
                      <el-menu
                        default-active="2"
                        class="el-menu-vertical-demo"
                        background-color="#545c64"
                        text-color="#fff"
                        active-text-color="#ffd04b">

                        <el-submenu
                        v-for="(item, index) in menuItems"
                        :key="index"
                        :index="`${index}`">
                        <template slot="title">
                          <i class="el-icon-menu"></i>
                          <span>{{ item.menuName }}</span>
                        </template>
                        <router-link
                          v-for="(subItem, subIndex) in item.children"
                          :key="subIndex"
                          :to="subItem.menuPath">
                          <el-menu-item :index="`${index}-${subIndex}`">
                            <i class="el-icon-document"></i>
                            <span>{{ subItem.menuName }}</span>
                          </el-menu-item>
                        </router-link>
                      </el-submenu>
                    

                      </el-menu>
                    </el-col>
                  </el-row>
                
                
            </el-aside>
            <el-container>
              <el-header>Header</el-header>
              <el-main> <router-view></router-view> </el-main>
              <el-footer>Footer</el-footer>
            </el-container>
          </el-container>
  </div> -->


<!-- </template> -->

<!-- <script>
import axios from 'axios';

export default {
  name: 'App',
  data(){
    return {
      menuItems: []
    }
  },
  created() {
      this.menuQuery(); // 在 Vue 实例创建时获取数据
  }, 
  methods:{
    menuQuery(){
      axios.post(
        "http://localhost:8080/project_war_exploded/menu/selectMenu",
          ).then(res => {
                var data = res.data.data;
                this.menuItems = data;
                console.log(this.menuItems)
                })
            }
  }
}


</script>

<style>

html, body {
            height: 100%;
            margin: 0;
        }

        #app {
            height: 100%;
        }

        .el-container {
            height: 100%;
            width: 100%;
        }

        .el-aside, .el-row, .el-col {
            height: 100%;
        }
        .el-menu-vertical-demo {
            height: 100%;
        }

    .el-header, .el-footer {
      background-color: #B3C0D1;
      color: #333;
      text-align: center;
      line-height: 60px;
    }
    
    .el-aside {
      background-color: #D3DCE6;
      color: #333;
      text-align: center;
      line-height: 200px;
    }
    
    .el-main {
      background-color: #E9EEF3;
      color: #333;
      text-align: center;
      line-height: 160px;
    }
    
    body > .el-container {
      margin-bottom: 40px;
    }
    
    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
      line-height: 260px;
    }
    
    .el-container:nth-child(7) .el-aside {
      line-height: 320px;
    }
</style> -->

<template>
   <div id="app">
   <router-view></router-view>
   </div>
</template>
  
  <script>

  export default {
   name: 'App',
  
  }
  </script>
  
  <style>
  #app {
   font-family: Avenir, Helvetica, Arial, sans-serif;
   -webkit-font-smoothing: antialiased;
   -moz-osx-font-smoothing: grayscale;
   text-align: center;
   color: #2c3e50;
  
  }
  body{
   margin: 0;
   padding: 0;
  }
  </style>