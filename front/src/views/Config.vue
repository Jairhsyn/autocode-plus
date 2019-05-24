<template>
    <div style="width: 50%;margin: 0 auto;">
        <el-form label-position="right" label-width="120px" :model="config" size="mini">
            <el-form-item label="项目根路径">
                <el-input v-model="config.rootDir"></el-input>
            </el-form-item>
            <el-form-item label="是否覆盖文件夹">
                <el-switch
                        v-model="config.replaceAll"
                        active-text="开启"
                        inactive-text="关闭">
                </el-switch>
            </el-form-item>
            <el-form-item label="source路径">
                <el-input v-model="config.sourcePath"></el-input>
            </el-form-item>
            <el-form-item label="resource路径">
                <el-input v-model="config.resourcePath"></el-input>
            </el-form-item>
            <el-form-item label="model包">
                <el-input v-model="config.modelPackage"></el-input>
            </el-form-item>
            <el-form-item label="service包">
                <el-input v-model="config.servicePackage"></el-input>
            </el-form-item>
            <el-form-item label="dao包">
                <el-input v-model="config.daoPackage"></el-input>
            </el-form-item>
            <el-form-item label="mapper.xml路径">
                <el-input v-model="config.mapperPath"></el-input>
            </el-form-item>
            <el-form-item label="表名前缀列表">
                <el-select
                        v-model="config.tablePrefixes"
                        multiple
                        filterable
                        allow-create
                        default-first-option
                        placeholder="请输入表前缀(如果需要的话)" style="width: 100%;">
                </el-select>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                config: {},
            }
        },
        name: "Config",
        mounted:
            function () {
                this.config = {...this.config, ...this.$store.state.sysConfig};
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
                if (oldV === 1 && newV === 2) {
                    this.commitSysConfig();
                } else if (oldV === 1 && newV === 0) {
                    this.$router.push('/select')
                }
            }
            ,
        }
        ,
        methods: {
            commitSysConfig: function () {
                this.$store.commit('updateSysConfig', this.config);
                this.$router.push('/custom')
            }
        }
    }
</script>

<style scoped>

</style>