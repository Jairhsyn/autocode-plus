<template>
    <div style="width: 50%;margin: 0 auto;">
        <el-form label-position="right" label-width="120px" :model="config" size="mini">
            <el-form-item label="项目根路径">
                <el-input v-model="config.rootDir"></el-input>
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
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                config: {
                    rootDir: 'temp',
                    sourcePath: '/src/main/java/',
                    resourcePath: '/src/main/resources/',
                    modelPackage: 'tech.washmore.model',
                    servicePackage: 'tech.washmore.service',
                    daoPackage: 'tech.washmore.dao',
                    mapperPath: 'mappers',
                    tables: [],
                },
            }
        },
        name: "Config",
        mounted: function () {
            // axios.get('/methods/all').then((res) => {
            //     this.methods = res.data;
            // })
            this.config.tables = this.$store.state.selectedTables;

        },
        computed: {
            currentStep: function () {
                return this.$store.state.currentStep;
            }
        },
        watch: {
            currentStep: function (newV, oldV) {
                if (oldV === 1 && newV === 2) {
                    this.invoke();
                } else if (oldV === 1 && newV === 0) {
                    this.$router.push('/select')
                }
            },
        },
        methods: {
            invoke: function () {
                axios.post('/code/process', this.config).then((res) => {
                    console.info('res', res);
                }).catch((err) => {
                    console.error('err', err);
                })
            }
        }
    }
</script>

<style scoped>

</style>