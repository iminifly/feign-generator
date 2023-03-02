{
	path: '/feign/feignClient',
	component: Layout,
	alwaysShow: true,
	meta: {
		title: 'Feign Client',
		icon: 'css'
	},
	children: [
		{
			path: '/feign/feignClient/projects',
			component: () => import(/* webpackChunkName: "FeignProjects" */ '@/pages/feign/feign/index.vue'),
			name: 'FeignProjects',
			meta: {
				title: '工程列表'
			}
		},
		{
			path: '/feign/feignClient/clients',
			hidden: true,
			component: () => import(/* webpackChunkName: "FeignClients" */ '@/pages/feign/feign/clients.vue'),
			name: 'FeignClients',
			meta: {
				title: '接口列表',
				isReturnBtn: true
			}
		},
		{
			path: '/feign/feignClient/models',
			hidden: true,
			component: () => import(/* webpackChunkName: "FeignModels" */ '@/pages/feign/feign/models.vue'),
			name: 'FeignModels',
			meta: {
				title: 'Model列表',
				isReturnBtn: true
			}
		}
	]
}