const {createApp} = Vue;

createApp({
	data() {
		return {
			claseagregada: true,
			datos: [],
			account: [],
			accountOrder: [],
			amount: '',
			description: '',
			account1: '',
			account2: '',
		};
	},
	created() {
		this.transaction();
	},
	methods: {
		transaction() {
			axios
				.get('http://localhost:8080/api/clients/current')
				.then(response => {
					this.datos = response.data;
					this.account = this.datos.account;
					this.accountOrder = this.account.sort((a, b) => a.id - b.id);
					console.log(this.account);
				})
				.catch(error =>
					Swal.fire({
						icon: 'error',
						text: error.response.data,
						confirmButtonColor: '#7c601893',
					})
				);
		},
		addTransaction() {
			axios
				.post(
					'/api/clients/current/transaction',
					'amount=' + this.amount + '&description=' + this.description + '&account1=' + this.account1 + '&account2=' + this.account2
				)
				.then(response => (window.location.href = '/web/html/accounts.html'))
				.catch(error =>
					Swal.fire({
						icon: 'error',
						text: error.response.data,
						confirmButtonColor: '#7c601893',
					})
				);
		},

		logout() {
			Swal.fire({
				title: 'Are you sure that you want to log out',
				inputAttributes: {
					autocapitalize: 'off',
				},
				showCancelButton: true,
				confirmButtonText: 'Sure',
				showLoaderOnConfirm: true,
				preConfirm: login => {
					return axios
						.post('/api/logout')
						.then(response => {
							window.location.href = '/web/index.html';
						})
						.catch(error => {
							console.log(error);
						});
				},
				allowOutsideClick: () => !Swal.isLoading(),
			});
		},
		addclass1() {
			this.claseagregada = true;
		},
		addclass2() {
			this.claseagregada = false;
		},
	},
}).mount('#app');
