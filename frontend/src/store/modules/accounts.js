import axios from 'axios';
import drf from '@/api/drf.js';
// import router from '@/router';

const accountStore = {
   // namespaced: true,

   // state는 직접 접근 금지!
   state: {
      accessToken: sessionStorage.getItem('token') || '',
      userName: '',
      userId: null,
      authError: null,
      refreshToken: sessionStorage.getItem('reToken') || '',
      expire: '',
   },
   getters: {
      isLoggedIn: (state) => !!state.accessToken,
      currentUser: state => state.currentUser,
      authError: (state) => state.authError,
      userName: (state) => state.userName,
      userId: (state) => state.userId,
      accessToken: (state) => state.accessToken,
      refreshToken: (state) => state.refreshToken,
      expire: (state) => state.expire,
   },
   mutations: {
      SET_CURRENT_USER: (state, user) => state.currentUser = user,
      SET_TOKEN: (state, accessToken) => {
         // console.log('mutations accessToken = ', accessToken);
         state.accessToken = accessToken;
      },
      SET_AUTH_ERROR: (state, error) => (state.authError = error),
      SET_USER_NAME: (state, userName) => (state.userName = userName),
      SET_USER_ID: (state, userId) => (state.userId = userId),
      SET_REFRESH_TOKEN: (state, refreshToken) => {
         // console.log('mutations refreshToekn = ', refreshToken);
         state.refreshToken = refreshToken;
      },
      SET_EXPIRE: (state, expire) => {
         // console.log('mutation expire : ', expire);
         state.expire = expire;
      },
   },
   actions: {
      saveToken({ commit }, accessToken) {
         // console.log('accessToken save : ', accessToken);

         /* 
      state.token 추가 
      localStorage에 token 추가
      */
         commit('SET_TOKEN', accessToken);

         // sessionStorage.setItem('token', accessToken)/
         // commit('SET_CURRENT_USER', accessToken)
      },
      saveRefreshToken({ commit }, refreshToken) {
         // console.log('refreshToken save : ', refreshToken);
         commit('SET_REFRESH_TOKEN', refreshToken);
      },
      saveExpiresIn({ commit }, expiresIn) {
         // console.log('action expireIn : ', expiresIn);
         commit('SET_EXPIRE', expiresIn);
      },
      removeToken({ commit }) {
         /* 
      state.token 삭제
      localStorage에 token 추가
      */
         commit('SET_TOKEN', '');
         localStorage.setItem('token', '');
      },
      removeName({ commit }) {
         commit('SET_USER_NAME', null);
         // sessionStorage.setItem('userName')
      },
      saveName({ commit }, userName) {
         commit('SET_USER_NAME', userName);
         // console.log('current username =', userName); //ok

         // localStorage.setItem('userName', userName)
      },
      saveId({ commit }, userId) {
         commit('SET_USER_ID', userId);
         // console.log('current id =', userId); //ok
      },
      async reIssue({ commit }) {
         const accessToken = this.getters.accessToken;
         const refreshToken = this.getters.refreshToken;
         const expire = this.getters.expire;
         // console.log('accessToken : ', accessToken);
         // console.log('refreshToken : ', refreshToken);
         // console.log('expire : ', expire);

         let currentDate = new Date().getTime();
         // console.log('currentDate : ', currentDate);
         if (expire <= currentDate) {
            console.log('만료');
            // await this.$store.dispatch('reIssue', tokens);
            const reaccessToken = accessToken.slice(7);

            await axios({
               url: drf.accounts.reissue(),
               method: 'post',
               headers: {
                  'Access-Token': reaccessToken,
                  'Refresh-Token': refreshToken,
               },
            })
               .then(async (res) => {
                  console.log('reissue : ', res);
                  await commit(
                     'SET_TOKEN',
                     accessToken.slice(0, 7) + res.data.accessToken
                  );
                  await commit('SET_REFRESH_TOKEN', res.data.refreshToken);
                  console.log('재발급');
               })
               .catch((err) => {
                  console.log('에러');
                  console.log(err);
               });
         }
      },

      // expired({ commit }, accessToken, refreshToken, expire){
      //    console.log('expire : ', expire);
      //          const tokens = {
      //             accessToken: accessToken,
      //             refreshToken: refreshToken,
      //          };
      //          let currentDate = new Date().getTime();
      //          console.log('currentDate : ', currentDate);
      //          if (this.expire <= currentDate) {
      //             console.log('만료');
      //             await this.$store.dispatch('reIssue', tokens);
      //          }
      // }

      // removeToken({ commit }) {
      //   /*
      //   state.token 삭제
      //   localStorage에 token 추가
      //   */
      //   commit('SET_TOKEN', '')
      //   localStorage.setItem('token', '')
      // },
   },
   modules: {},
};
export default accountStore;``