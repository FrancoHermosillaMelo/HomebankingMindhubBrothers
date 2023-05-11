const {createApp} = Vue;

createApp({
	data() {
		return {
			account: [],
			transactions: [],
			transactionOrder: [],
			id: new URLSearchParams(location.search).get('id'),
		};
	},
	created() {
		this.loadData();
	},
	methods: {
		loadData() {
			axios
				.get('http://localhost:8080/api/clients/current/accounts/' + this.id)
				.then(response => {
					this.account = response.data;
					console.log(this.account);
					this.transactions = this.account.transactionDTOS;
					console.log(this.transactions);
					this.transactionOrder = this.transactions.sort((a, b) => b.id - a.id);
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
	},
}).mount('#app');

window.onload = function () {
	$('#onload').fadeOut();
	$('body').removeClass('hidden');
};
