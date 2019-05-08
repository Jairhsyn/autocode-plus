<template>
    <el-container>
        <el-header>
            <el-steps
                    :active="$store.state.currentStep"
                    finish-status="success"
                    style="width: 60%;margin: 0 auto;"
                    align-center>
                <el-step v-for="(step,index) in steps" :key="index" :title="step.title"></el-step>
            </el-steps>
        </el-header>
        <el-main>
            <router-view/>
        </el-main>
        <el-footer>
            <div style="width: 60%;margin: 0 auto;display: flex;justify-content: space-around">
                <el-button style="margin-top: 12px;" @click="prev">上一步</el-button>
                <el-button style="margin-top: 12px;" @click="next">{{$store.state.currentStep>=2?"完成":"下一步"}}
                </el-button>
            </div>
        </el-footer>
    </el-container>
</template>

<script>
    export default {
        data() {
            return {
                steps: [

                    {
                        title: '选择目标表',
                        route: '/choose',
                    },
                    {
                        title: '生成方案配置',
                        route: '/config',
                    },
                    {
                        title: '开始执行',
                        route: '/invoke',
                    },
                ],
                active: 0
            };
        },
        created: function () {
            console.info('route', this.$route);
            if (this.$store.state.dbConfig.url === undefined && this.$route.path !== '/init') {
                this.$router.push('/init');
                return;
            }
        },
        methods: {
            prev() {
                if (this.$store.state.currentStep > 0) {
                    this.$store.commit('minus');
                }
            },
            next() {
                if (this.$store.state.currentStep < 3) {
                    this.$store.commit('plus');
                }
            }
        },
        name: 'home',
    }
</script>
