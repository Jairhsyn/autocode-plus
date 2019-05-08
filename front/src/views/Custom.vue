<template>
    <div style="width: 50%;margin: 0 auto;">
        <el-form label-position="right" label-width="120px" :model="custom" size="mini">
            努力开发自定义配置中...
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                custom: {},
            }
        },
        name: "Config",
        mounted:
            function () {
                this.custom = this.$store.state.extConfig;
            }
        ,
        computed: {
            currentStep: function () {
                return this.$store.state.currentStep;
            }
        }
        ,
        watch: {
            currentStep: function (newV, oldV) {
                if (oldV === 2 && newV === 3) {
                    this.commitSysConfig();
                } else if (oldV === 2 && newV === 1) {
                    this.$router.push('/config')
                }
            }
        }
        ,
        methods: {
            commitSysConfig: function () {
                const loading = this.$loading({
                    lock: true,
                    text: '拼命生成中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                axios.post('/code/process', this.$store.state.sysConfig).then((res) => {
                    console.info('res', res);
                    loading.close();
                    this.$alert("代码生成已完成,请检查对应的文件夹和文件,如果结果和预期不一致,请查看应用日志排查问题.", "恭喜!", {
                        confirmButtonText:"我知道了"
                    })
                }).catch((err) => {
                    console.error('err', err);
                    this.$store.commit('minus');
                    loading.close();
                })
            }
        }
    }
</script>

<style scoped>

</style>