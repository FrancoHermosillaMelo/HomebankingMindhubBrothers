const {createApp} = Vue;

createApp({
	data() {
		return {
			datos: [],
			loans: [],
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
					this.loans = this.datos.loans;
					console.log(this.loans);
				})
				.catch(error => console.log(error));
		},
	},
}).mount('#app');
