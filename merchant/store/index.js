import {
	createStore
} from 'vuex'

export default createStore({
	state: {
		// 定义你的状态
		count: 0, //参考
		nickname: '', //昵称
		merchantName: '', //店铺名
		wallet: 0, //余额
		id: 0, //用户id
		merchantNumber: 0, //商户编号
		token: '' ,//token凭证
		user:{},
	},
	mutations: {
		// 定义改变状态的方法
		increment(state) {
			state.count++;
		},
		setNickname(state, newNickname) {
			state.nickname = newNickname;
		},
		setMerchantName(state, newMerchantName) {
			state.merchantName = newMerchantName
		},
		setWallet(state, newWallet) {
			state.wallet = newWallet
		},
		setId(state, newId) {
			state.id = newId
		},
		setMerchantNumber(state, newMerchantNumber) {
			state.merchantNumber = newMerchantNumber
		},
		setToken(state, newToken) {
			state.token = newToken
		},
		setUser(state,newUser){
			state.user = newUser
		}
	},
	actions: {
		// 定义异步操作
		incrementAsync({
			commit
		}) {
			setTimeout(() => {
				commit('increment');
			}, 1000);
		},
		updateNickname({
			commit
		}, newNickname) {
			commit('setNickname', newNickname);
		},
		updateMerchantName({
			commit
		}, newMerchantName) {
			commit('setMerchantName', newMerchantName);
		},
		updateWallet({
			commit
		}, newWallet) {
			commit('setWallet', newWallet);
		},
		updateId({
			commit
		}, newId) {
			commit('setId', newId);
		},
		updateMerchantNumber({
			commit
		}, newMerchantNumber) {
			commit('setMerchantNumber', newMerchantNumber);
		},
		updateToken({
			commit
		}, newToken) {
			commit('setToken', newToken);
		},
		updateUser({
			commit
		},{newUser}){
			commit('setUser',newUser)
		}
	},
	getters: {
		// 定义获取状态的方法
		getCount(state) {
			return state.count;
		},
		getNickname(state) {
			return state.nickname
		},
		getMerchantName(state) {
			return state.merchantName
		},
		getWallet(state) {
			return state.wallet
		},
		getId(state) {
			return state.id
		},
		getMerchantNumber(state) {
			return state.merchantNumber
		},
		getToken(state){
			return state.token
		},
		getUser(state){
			return state.user
		},
		getQrImg(state){
			return state.user.qrImg
		},
		getAdminId(state){
			return state.user.id
		},
		getBalance(state){
			return state.user.wallet
		},
		getState(state){
			return state.user.state
		}
	}
});