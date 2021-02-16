<template>
    <div>
        <div id="custom_bg">
        </div>

        <!-- 头部导航 -->
        <header id="header">
            <div class="container d-flex">
                <div class="logo">
                    <h1 style="color: white">知识图谱</h1>
<!--                    <router-link class="d-flex tytitle" to="/login">登录</router-link>-->
                </div>

            </div>
        </header>
        <!-- 搜素框 -->
        <section id="search-form">
            <div class="container">
                <div class="title d-flex">
                    <p style="padding-left: 0px">服装知识图谱构建和可视化</p>
                </div>
              <div>
                <!-- router-link定义页面中点击触发部分 -->
                <router-link class="d-flex tytitle" to="/kg">体验一下</router-link>

              </div>
            </div>
        </section>
        <!-- router-view定义页面中显示部分 -->
        <router-view></router-view>
    </div>
</template>

<script>

    /* import KGBuilderSample from '@/components/KGBuilderSample.vue' */
    export default {
        name: 'Home',
        components: {}
    }
</script>
<style>

    @import "../../public/static/css/main.css";

    .link {
        margin: 10px;
    }
</style>
