<template>
    <div style="width: 50%;margin: 0 auto;">
        <el-form label-position="right" label-width="120px" :model="custom" size="mini">
            <el-form-item label="dao自定义模板">
                <el-switch
                        v-model="custom.daoDisabled"
                        active-text="开启"
                        inactive-text="关闭">
                </el-switch>
                <el-upload
                        v-show="custom.daoDisabled"
                        action="/file/parse?type=dao"
                        :on-remove="handleRemove"
                        :before-remove="beforeRemove"
                        :on-exceed="handleExceed"
                        :on-success="uploadSuccess"
                        multiple
                        :limit="3"
                >
                    <el-button size="small" type="primary">点击上传</el-button>
                    <div slot="tip" class="el-upload__tip">只能上传ftl文件，且不超过500kb</div>
                </el-upload>
            </el-form-item>
            <el-form-item label="dao文件名"
                          v-show="custom.daoDisabled"
            >
                <el-select
                        v-model="custom.daoFileNames"
                        multiple
                        filterable
                        allow-create
                        default-first-option
                        @change="fileNameChange($event,'dao')"
                        placeholder="与上面的文件列表对应，freemarker表达式" style="width: 100%;">
                </el-select>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                custom: {
                    daoDisabled: false,
                    daoFiles: [],
                    daoFileNames: [],
                },
            }
        },
        name: "Config",
        mounted: function () {
            this.custom = {...this.custom, ...this.$store.state.extConfig};
        },
        computed: {
            currentStep: function () {
                return this.$store.state.currentStep;
            }
        },
        watch: {
            currentStep: function (newV, oldV) {
                if (oldV === 2 && newV === 3) {
                    this.commitSysConfig();
                } else if (oldV === 2 && newV === 1) {
                    this.$router.push('/config')
                }
            }
        },
        methods: {
            chooseFiles: function (res, fileList) {
                if (res.type === 'dao') {
                    this.custom.daoFiles = fileList.map(f => f.response);
                }


                this.pairFilesAndNames(res.type);
            },
            fileNameChange: function (fileNames, type) {
                if (type === 'dao') {
                    this.custom.daoFileNames = fileNames ;
                }
                this.pairFilesAndNames(type);
            },

            pairFilesAndNames(type) {
                if (type === 'dao') {
                    if (this.custom.daoFileNames.length !== this.custom.daoFiles.length) {
                        return;
                    }
                    this.custom.daoFiles.forEach((v, i) => {
                        v.fileNameExpression = this.custom.daoFileNames[i];
                    })
                }
            },
            uploadSuccess: function (res, file, fileList) {
                this.chooseFiles(res, fileList);
            },
            beforeRemove: function (file, fileList) {
                return this.$confirm(`确定移除 ${ file.name }？`);
            },
            handleRemove(file, fileList) {
                this.chooseFiles(file.response, fileList);
            },
            handleExceed: function (files, fileList) {
                this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },
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
                        confirmButtonText: "我知道了"
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