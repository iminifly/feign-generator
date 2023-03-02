import router, { resetRouter } from '@/router'

const state = {
  token: '',
  id: 0,
  name: '',
  avatar: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_ID: (state, id) => {
    state.id = id
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, userId, Authorization } = userInfo
    commit('SET_TOKEN', Authorization || 'token')
    commit('SET_NAME', username)
    commit('SET_ID', userId)
  },

  // get user info
  getInfo({ commit, state }) {
    const data = {
      roles: ['admin']
    }
    commit('SET_ROLES', data.roles)
    return data
  },

  // user logout
  logout({ commit, state, dispatch }) {
    commit('SET_ROLES', [])
    commit('SET_TOKEN', '')
    commit('SET_NAME', '')
    commit('SET_ID', '')
    resetRouter()
    dispatch('tagsView/delAllViews', null, { root: true })
  },

  // remove token
  resetToken({ commit }) {
    commit('SET_ROLES', [])
    commit('SET_TOKEN', '')
    commit('SET_NAME', '')
    commit('SET_ID', '')
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', state.roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
