import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        sysConfig: {
            rootDir: 'temp',
            sourcePath: '/src/main/java/',
            resourcePath: '/src/main/resources/',
            modelPackage: 'tech.washmore.model',
            servicePackage: 'tech.washmore.service',
            daoPackage: 'tech.washmore.dao',
            mapperPath: 'mappers',
            tablePrefixes: [],
            tables: [],
        },
        extConfig: {},
        dbConfig: {},
        currentStep: 0,
    },
    mutations: {
        updateExtConfig(state, payload) {
            state.extConfig = {...state.customConfig, ...payload};
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
