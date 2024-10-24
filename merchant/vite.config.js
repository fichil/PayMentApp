// vue3 配置
import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'
 
export default defineConfig({
	plugins: [
		uni()
	],
	server: {
		port: 5178,
		proxy: {
			'/apr': {
				target: 'http://202.101.100.17:9090', // 目标服务  
				changeOrigin: true,
				CookieDomain: 'localhost',
				rewrite: path => path.replace(/^\/apr/, ''),
			}
		}
	}
})