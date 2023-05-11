const {createApp} = Vue;

createApp({
	data() {
		return {
			datos: [],
			loans: [],
			accounts: [],
			firstname: '',
			lastName: '',
			email: '',
			accountActive: [],
			accountType: '',
		};
	},
	created() {
		this.loadData();
	},
	methods: {
		loadData() {
			axios
				.get('http://localhost:8080/api/clients/current')
				.then(response => {
					this.datos = response.data;
					console.log(this.datos);
					this.accounts = this.datos.account;
					this.loans = this.datos.loans;
					this.accountActive = this.accounts.filter(account => account.activeAccount);
				})
				.catch(error => console.log(error));
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
							Swal.showValidationMessage(`Request failed: ${error}`);
						});
				},
				allowOutsideClick: () => !Swal.isLoading(),
			});
		},
		createAccount() {
			Swal.fire({
				title: 'Are you sure you want to create another account?',
				text: 'Select the type of account to create',
				input: 'select',
				inputOptions: {
					CURRENT: 'CURRENT',
					SAVING: 'SAVING',
				},
				inputPlaceholder: 'TYPE',
				showCancelButton: true,
				confirmButtonText: 'Create',
			}).then(result => {
				if (result.isConfirmed) {
					this.accountType = result.value;
					axios
						.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
						.then(reponse => {
							window.location.href = '/web/html/accounts.html';
						})
						.catch(error => {
							Swal.fire({
								icon: 'error',
								text: error.response.data,
								confirmButtonColor: '#7c601893',
							});
						});
				}
			});
		},
		// 	Swal.fire({
		// 		title: 'Are you sure you want to create another account?',
		// 		text: 'Select the type of account to create',
		// 		input: 'select',
		// 		inputOptions: {
		// 			CURRENT: 'CURRENT',
		// 			SAVING: 'SAVING',
		// 		},
		// 		inputPlaceholder: 'TYPES',
		// 		inputAttributes: {
		// 			autocapitalize: 'off',
		// 		},
		// 		showCancelButton: true,
		// 		confirmButtonText: 'CREATE',
		// 		showLoaderOnConfirm: true,
		// 		preConfirm: create => {
		// 			return axios
		// 				.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
		// 				.then(response => {
		// 					window.location.href = '/web/html/accounts.html';
		// 				})
		// 				.catch(error => {
		// 					Swal.fire({
		// 						icon: 'error',
		// 						text: error.response.data,
		// 						confirmButtonColor: '#7c601893',
		// 					});
		// 				});
		// 		},
		// 		allowOutsideClick: () => !Swal.isLoading(),
		// 	});
		// },
		accountDelete(id) {
			Swal.fire({
				title: 'Are you sure you want to delete this account?',
				text: 'Remember that your balance to be able to eliminate has to be 0',
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				confirmButtonText: 'DELETE',
			}).then(result => {
				if (result.isConfirmed) {
					axios
						.put(`/api/accounts/${id}`)
						.then(response => {
							Swal.fire('Deleted successfully');
							window.location.href = '/web/html/accounts.html';
						})
						.catch(error => console.log(error));
				}
			});
		},
	},
}).mount('#app');

window.onload = function () {
	$('#onload').fadeOut();
	$('body').removeClass('hidden');
};
