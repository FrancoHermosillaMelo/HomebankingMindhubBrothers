const {createApp} = Vue;

createApp({
	data() {
		return {
			loans: [],
			accounts: [],
			account: '',
			payments: '',
			amount: 0,
			loanID: '',
			paymentsFilter: '',
			loanType: '',
			quotas: 0,
		};
	},
	created() {
		this.loanData();
		this.accountsData();
	},
	methods: {
		loanData() {
			axios
				.get('http://localhost:8080/api/loans')
				.then(response => {
					this.loans = response.data;
				})
				.catch(error => console.log(error));
		},
		accountsData() {
			axios
				.get('http://localhost:8080/api/clients/current')
				.then(response => {
					this.accounts = response.data;
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
		filterPayments() {
			this.paymentsFilter = this.loans.filter(loan => {
				return this.loanType.includes(loan.name);
			})[0];
		},
		loanCreate() {
			this.loanID = this.paymentsFilter.id;
			Swal.fire({
				title: 'Are you sure you want to apply for this loan?',
				inputAttributes: {
					autocapitalize: 'off',
				},
				showCancelButton: true,
				confirmButtonText: 'Sure',
				confirmButtonColor: '#7c601893',
				preConfirm: () => {
					return axios
						.post('/api/loans', {
							loanID: this.loanID,
							amount: this.amount,
							payments: this.payments,
							accountDestiny: this.account,
						})
						.then(response => {
							Swal.fire({
								icon: 'success',
								text: 'Your loan was approved',
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
							console.log(error);
						});
				},
			});
		},
		interests() {
			this.quotas = (this.amount * 1.2) / this.payments;
		},
	},
}).mount('#app');

window.onload = function () {
	$('#onload').fadeOut();
	$('body').removeClass('hidden');
};
