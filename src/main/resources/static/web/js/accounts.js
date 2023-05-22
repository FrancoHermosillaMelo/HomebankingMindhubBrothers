const {createApp} = Vue;

createApp({
	data() {
		return {
			datos: [],
			loans: [],
			loanID: [],
			quotas: 0,
			payTotal: 0,
			amount: '',
			accounts: [],
			account: '',
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
				.get('/api/clients/current')
				.then(response => {
					this.datos = response.data;
					console.log(this.datos);
					this.accounts = this.datos.account;
					this.accountActive = this.accounts.filter(account => account.activeAccount);
					this.loans = this.datos.loans.filter(loan => loan.totalAmount > 0);
					console.log(this.loans);
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
							Swal.fire({
								icon: 'success',
								text: 'Account deleted successfully',
								showConfirmButton: false,
								timer: 2000,
							}).then(() => (window.location.href = '/web/html/accounts.html'));
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
		loanFilter(id) {
			this.loanID = this.loans.filter(loan => loan.id == id)[0];
			console.log(this.loanID);
			this.quotas = this.loanID.totalAmount / this.loanID.payments;
			console.log(this.quotas);
			this.payTotal = this.loanID.totalAmount;
			console.log(this.payTotal);
		},
		loanPay() {
			Swal.fire({
				title: 'Are you sure you want to repay this loan?',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				confirmButtonText: 'SURE',
			}).then(result => {
				if (result.isConfirmed) {
					axios
						.post('/api/loans/pay', `id=${this.loanID.id}&account=${this.account}&amount=${this.amount}`)
						.then(response => {
							Swal.fire({
								icon: 'success',
								text: 'It was paid correctly',
								showConfirmButton: false,
								timer: 2000,
							}).then(() => (window.location.href = '/web/html/accounts.html'));
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
	},
}).mount('#app');

window.onload = function () {
	$('#onload').fadeOut();
	$('body').removeClass('hidden');
};
