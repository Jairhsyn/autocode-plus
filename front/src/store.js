import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        context: {},
        customConfig: {},
        currentStep: 0,
        dbConfig: {},
        selectedTables: [],
    },
    mutations: {
        updateCustomConfig(state, payload) {
            state.customConfig = {...state.customConfig, ...payload};
        },
        updateContext(state, payload) {
            state.context = {...state.context, ...payload};
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
        initDbConfig(state, payload) {
            state.dbConfig = {...payload};
        },
        updateSelectedTables(state, payload) {
            state.selectedTables = payload;
        },

    },
    actions: {}
})
