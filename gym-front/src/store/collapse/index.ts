import { defineStore } from 'pinia';

// Define collapse store
export const collapseStore = defineStore('collapseStore', {
  state: () => {
    return {
      collapse: false,
    };
  },
  getters: {
    getCollapse(state) {
      return state.collapse;
    },
  },
  actions: {
    setCollapse(collapse: boolean) {
      this.collapse = collapse;
    },
  },
});