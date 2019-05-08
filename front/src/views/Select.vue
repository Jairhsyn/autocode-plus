<template>
    <div style="width: 50%;margin: 0 auto;">
        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
        <div style="margin: 15px 0;"></div>
        <el-checkbox-group v-model="selectedTables" @change="handleCheckedTablesChange">
            <div v-for="(table,index) in tables">
                <el-checkbox :label="table.TABLE_NAME" :key="index">
                    {{table.TABLE_NAME+(table.TABLE_COMMENT&&table.TABLE_COMMENT!==''?('('+table.TABLE_COMMENT+')'):'')}}
                </el-checkbox>
            </div>
        </el-checkbox-group>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                checkAll: false,
                isIndeterminate: false,
                tables: [],
                selectedTables: [],
            }
        },
        name: "Select",
        mounted: function () {
            axios.get('/table/all').then((res) => {
                this.tables = res.data;
                this.selectedTables = this.tables.map(t => t.TABLE_NAME);
                if (this.$store.state.selectedTables.length > 0) {
                    this.selectedTables = this.$store.state.selectedTables;
                }
                this.checkAll = this.selectedTables.length === this.tables.length;
                this.isIndeterminate = this.selectedTables.length > 0 && this.selectedTables.length < this.tables.length;
            })
        },
        computed: {
            currentStep: function () {
                return this.$store.state.currentStep;
            }
        },
        watch: {
            currentStep: function (newV, oldV) {
                console.info('new', newV, 'old', oldV)
                if (oldV === 0 && newV === 1) {
                    this.selectTables();
                }
            }
        },
        methods: {
            handleCheckAllChange(val) {
                this.selectedTables = val ? this.tables.map(t => t.TABLE_NAME) : [];
                this.isIndeterminate = false;
            },
            handleCheckedTablesChange(value) {
                let checkedCount = value.length;
                this.checkAll = checkedCount === this.tables.length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.tables.length;
            },
            selectTables: function () {
                this.$store.commit("updateSelectedTables", this.selectedTables);
                this.$router.push("/config");
            },
        }
    }
</script>

<style scoped>

</style>