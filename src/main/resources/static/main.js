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
				.get('http://localhost:8080/api/clients')
				.then(response => {
					this.datos = response.data;
				})
				.catch(error => console.log(error));
		},
		postClient() {
			axios
				.post('http://localhost:8080/api/clients', {
					firstName: this.firstname,
					lastName: this.lastName,
					email: this.email,
				})
				.then(response => {
					this.loadData();
				})
				.catch(error => console.log(error));
		},
		addClient() {
			this.postClient();
		},
	},
}).mount('#app');
