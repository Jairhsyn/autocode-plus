<template>
    <div style="width: 50%;margin: 0 auto;">
        <el-form label-position="right" label-width="150px" :model="custom" size="mini">
            <div
                    style="border: #aae6d8 1px solid; margin:5px 0;"
            >
                <el-form-item label="dao自定义模板">
                    <el-switch
                            v-model="custom.daoEnable"
                            active-text="开启"
                            inactive-text="关闭">
                    </el-switch>
                    <el-upload
                            v-show="custom.daoEnable"
                            action="/file/parse?type=dao"
                            :on-remove="handleRemove"
                            :before-remove="beforeRemove"
                            :on-exceed="handleExceed"
                            :on-success="uploadSuccess"
                            multiple
                            :limit="3"
                            style="float: left;"
                    >
                        <el-button size="small" type="primary">点击上传</el-button>
                        <div slot="tip" class="el-upload__tip">推荐上传ftl文件，且不超过500kb</div>
                    </el-upload>

                </el-form-item>
                <el-form-item label="dao文件名"
                              v-show="custom.daoEnable"
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

                <el-form-item label="mapper自定义模板">
                    <el-switch
                            v-model="custom.mapperEnable"
                            active-text="开启"
                            inactive-text="关闭">
                    </el-switch>
                    <el-upload
                            v-show="custom.mapperEnable"
                            action="/file/parse?type=mapper"
                            :on-remove="handleRemove"
                            :before-remove="beforeRemove"
                            :on-exceed="handleExceed"
                            :on-success="uploadSuccess"
                            multiple
                            :limit="3"
                            style="float: left;"
                    >
                        <el-button size="small" type="primary">点击上传</el-button>
                        <div slot="tip" class="el-upload__tip">推荐上传ftl文件，且不超过500kb</div>
                    </el-upload>

                </el-form-item>
                <el-form-item label="mapper文件名"
                              v-show="custom.mapperEnable"
                >
                    <el-select
                            v-model="custom.mapperFileNames"
                            multiple
                            filterable
                            allow-create
                            default-first-option
                            @change="fileNameChange($event,'mapper')"
                            placeholder="与上面的文件列表对应，freemarker表达式" style="width: 100%;">
                    </el-select>
                </el-form-item>

                <el-form-item label="model自定义模板">
                    <el-switch
                            v-model="custom.modelEnable"
                            active-text="开启"
                            inactive-text="关闭">
                    </el-switch>
                    <el-upload
                            v-show="custom.modelEnable"
                            action="/file/parse?type=model"
                            :on-remove="handleRemove"
                            :before-remove="beforeRemove"
                            :on-exceed="handleExceed"
                            :on-success="uploadSuccess"
                            multiple
                            :limit="3"
                            style="float: left;"
                    >
                        <el-button size="small" type="primary">点击上传</el-button>
                        <div slot="tip" class="el-upload__tip">推荐上传ftl文件，且不超过500kb</div>
                    </el-upload>

                </el-form-item>
                <el-form-item label="model文件名"
                              v-show="custom.modelEnable"
                >
                    <el-select
                            v-model="custom.modelFileNames"
                            multiple
                            filterable
                            allow-create
                            default-first-option
                            @change="fileNameChange($event,'model')"
                            placeholder="与上面的文件列表对应，freemarker表达式" style="width: 100%;">
                    </el-select>
                </el-form-item>
            </div>
            其他自定义模板:
            <el-button size="small" type="primary" @click="addOther">添加模板</el-button>
            <a style="margin-left: 20%" href="/file/template/templates.zip">
                打包下载所有默认模板(供参考)
            </a>
            <el-form-item
                    v-for="(file, index) in custom.otherFiles"
                    :label="'模板' + (index+1)"
                    :key="index"
                    style="border: #aae6d8 1px solid;"
            >
                <el-form-item label="为每张表创建">
                    <el-switch
                            v-model="file.eachTable"
                            active-text="是"
                            inactive-text="否">
                    </el-switch>
                </el-form-item>
                <el-form-item label="是否覆盖文件">
                    <el-switch
                            v-model="file.override"
                            active-text="是"
                            inactive-text="否">
                    </el-switch>
                </el-form-item>
                <el-form-item label="文件名路径">
                    <el-input v-model="file.filePath"></el-input>
                </el-form-item>
                <el-form-item label="文件名表达式">
                    <el-input v-model="file.fileNameExpression"></el-input>
                </el-form-item>
                <el-upload
                        :action="'/file/parse?type=other&index='+index"
                        :on-remove="handleRemove"
                        :before-remove="beforeRemove"
                        :on-exceed="handleExceed"
                        :on-success="uploadSuccess"
                        multiple
                        :limit="1"
                        style="float: left;"
                >
                    <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
                <el-button style="margin-left: 20%" size="small" type="primary" @click="deleteOneOfOthers(index)">删除
                </el-button>
            </el-form-item>

            <div
                    style="border: #aae6d8 1px solid; margin:5px 0;"
            >
                用户自定义变量(在模板中用${ext.key}访问对应的value值):
                <el-button size="small" type="primary" @click="addVariable">添加变量</el-button>
                <el-form-item
                        v-for="(variable, index) in custom.variables"
                        :label="'变量' + (index+1)"
                        :key="index"
                >
                    <el-form-item label="key">
                        <el-input v-model="variable.key"></el-input>
                    </el-form-item>
                    <el-form-item label="value">
                        <el-input v-model="variable.value"></el-input>
                    </el-form-item>
                    <el-button style="margin-left: 20%" size="small" type="primary"
                               @click="deleteOneOfVariables(index)">删除
                    </el-button>
                </el-form-item>
            </div>
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                custom: {
                    daoEnable: false,
                    daoFiles: [],
                    daoFileNames: [],

                    mapperEnable: false,
                    mapperFiles: [],
                    mapperFileNames: [],

                    modelEnable: false,
                    modelFiles: [],
                    modelFileNames: [],

                    otherFiles: [{
                        fileNameExpression: '',
                        eachTable: false,
                        filePath: '',
                        content: '',
                        override: true,
                    }],
                    variables: [
                        {
                            key: '',
                            vale: '',
                        }
                    ]
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
                if (res.type === 'other') {
                    this.custom.otherFiles[res.index].content = res.content;
                    return;
                }

                if (res.type === 'dao') {
                    this.custom.daoFiles = fileList.map(f => f.response);
                } else if (res.type === 'mapper') {
                    this.custom.mapperFiles = fileList.map(f => f.response);
                } else if (res.type === 'model') {
                    this.custom.modelFiles = fileList.map(f => f.response);
                }
                this.pairFilesAndNames(res.type);
            },
            fileNameChange: function (fileNames, type) {
                if (type === 'dao') {
                    this.custom.daoFileNames = fileNames;
                } else if (type === 'mapper') {
                    this.custom.mapperFileNames = fileNames;
                } else if (type === 'model') {
                    this.custom.modelFileNames = fileNames;
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
                } else if (type === 'mapper') {
                    if (this.custom.mapperFileNames.length !== this.custom.mapperFiles.length) {
                        return;
                    }
                    this.custom.mapperFiles.forEach((v, i) => {
                        v.fileNameExpression = this.custom.mapperFileNames[i];
                    })
                } else if (type === 'model') {
                    if (this.custom.modelFileNames.length !== this.custom.modelFiles.length) {
                        return;
                    }
                    this.custom.modelFiles.forEach((v, i) => {
                        v.fileNameExpression = this.custom.modelFileNames[i];
                    })
                }
            },
            deleteOneOfOthers: function (index) {
                this.custom.otherFiles.splice(index, 1);
            },
            addOther: function () {
                this.custom.otherFiles.push({
                    fileNameExpression: '',
                    eachTable: false,
                    filePath: '',
                    content: '',
                    override: true,
                })
            },
            deleteOneOfVariables: function (index) {
                this.custom.variables.splice(index, 1);
            },
            addVariable: function () {
                this.custom.variables.push({
                    key: '',
                    value: '',
                })
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
                this.$message.warning(`当前限制选择 ${fileList.length} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },
            commitSysConfig: function () {
                const loading = this.$loading({
                    lock: true,
                    text: '拼命生成中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                axios.post('/code/process', {
                    sysConfig: this.$store.state.sysConfig,
                    extConfig: this.custom,
                }).then((res) => {
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