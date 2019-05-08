<template>
    <div style="width: 50%;margin: 0 auto;">
        <el-dialog title="初始化数据库连接" :visible="true"
                   :show-close="false"
        >
            <el-form label-position="right" label-width="80px" :model="dbConfig">
                <el-form-item label="url">
                    <el-input v-model="dbConfig.url"></el-input>
                </el-form-item>
                <el-form-item label="username">
                    <el-input v-model="dbConfig.username"></el-input>
                </el-form-item>
                <el-form-item label="password">
                    <el-input v-model="dbConfig.password"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button>取 消</el-button>
                <el-button type="primary" @click="initDb">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        data() {
            return {
                dbConfig: {
                    url: 'jdbc:mysql://localhost:3306/jyb?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true\n',
                    username: 'root',
                    password: '',
                },
            }
        },
        name: "InitDb",
        mounted: function () {
            if (this.$store.state.dbConfig.url) {
                this.$router.push('/select');
            }
        },

        methods: {
            initDb: function () {
                //jdbc:mysql://localhost:3306/jyb?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true
                axios.post('/db/init', this.dbConfig).then((res) => {
                    console.info('res', res);
                    this.$store.commit("updateDbConfig", res.data);
                    this.$router.push('/select');
                }).catch((err) => {

                })

            }
        }
    }
</script>

<style scoped>

</style>