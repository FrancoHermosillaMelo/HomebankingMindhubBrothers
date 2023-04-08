const {createApp} = Vue;

createApp({
	data() {
		return {
			datos: [],
			firstname: '',
			lastName: '',
			email: '',
		};
	},
	created() {
		this.loadData();
	},
	methods: {
		loadData() {
			axios
				.get('http://localhost:8080/api/clients/1')
				.then(response => {
					this.datos = response.data;
					console.log(this.datos);
				})
				.catch(error => console.log(error));
		},
	},
}).mount('#app');
