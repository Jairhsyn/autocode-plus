import Vue from 'vue'
import Vuex from 'vuex'
import moment from 'moment'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        sysConfig: {
            replaceAll: true,
            rootDir: 'temp',
            sourcePath: '/src/main/java/',
            resourcePath: '/src/main/resources/',
            modelPackage: 'com.ke.huaqiao.server.entity',
            servicePackage: 'com.ke.huaqiao.server.service',
            daoPackage: 'com.ke.huaqiao.server.service',
            mapperPath: 'mybatis/mappers',
            tablePrefixes: ['t_'],
            tables: [],
        },
        extConfig: {},
        dbConfig: {},
        currentStep: 0,
    },
    mutations: {
        updateAll(state, payload) {
            this.replaceState(payload);
            console.info('state', state);
        },
        updateExtConfig(state, payload) {
            state.extConfig = {...state.customConfig, ...payload};
            let arr = JSON.parse(localStorage.getItem("history") || '[]');
            arr.unshift({
                ...state,
                currentStep: 0,
                timeStamp: ' < ' + state.dbConfig.url.match(/\/\/[^/]+\/([^?]+)\?/)[1] + ' > ' + moment().format("YYYY年MM月DD日 HH:mm:ss")
            });
            if (arr.length > 10) {
                arr.pop();
            }
            localStorage.setItem("history", JSON.stringify(arr));
        },
        updateSysConfig(state, payload) {
            state.sysConfig = {...state.sysConfig, ...payload};
            console.info('state', state, 'payload', payload)
        },
        updateDbConfig(state, payload) {
            state.dbConfig = {...state.dbConfig, ...payload};
        },
        plus(state) {
            state.currentStep++
        },
        minus(state) {
            state.currentStep--;
        },
    },
    actions: {}
})
