const {createApp} = Vue;

createApp({
	data() {
		return {
			account: [],
			transactions: [],
			transactionOrder: [],
			transactionDate: [],
			dateStart: '0000-00-00',
			dateEnd: '0000-00-00',
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
					this.transactions = this.account.transactionDTOS;
					this.transactionOrder = this.transactions.sort((a, b) => b.id - a.id);
					this.dateStart = this.transactionOrder[this.transactionOrder.length - 1].date.split('T')[0];
					this.dateEnd = this.transactionOrder[0].date.split('T')[0];
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
		pdfDow() {
			Swal.fire({
				title: 'Are you sure you want to download these transfers?',
				inputAttributes: {
					autocapitalize: 'off',
				},
				showCancelButton: true,
				confirmButtonText: 'Sure',
				showLoaderOnConfirm: true,
				preConfirm: pdf => {
					return axios
						.post(
							'http://localhost:8080/api/accounts/transactions/pdf',
							'id=' + this.id + '&startDate=' + this.dateStart + '&endDate=' + this.dateEnd
						)
						.then(response => {
							Swal.fire({
								icon: 'success',
								text: 'Downloaded successfully',
							});
						})
						.catch(error => {
							Swal.fire({
								icon: 'error',
								text: error.response.data,
								confirmButtonColor: '#7c601893',
							});
						});
				},
				allowOutsideClick: () => !Swal.isLoading(),
			});
		},
	},
	computed: {
		filterDate() {
			axios
				.get('http://localhost:8080/api/accounts/transactions/date', {
					params: {
						id: this.id,
						startDate: this.dateStart,
						endDate: this.dateEnd,
					},
				})
				.then(response => {
					this.transactionOrder = response.data;
				})
				.catch(error => console.log(error));
		},
	},
}).mount('#app');

window.onload = function () {
	$('#onload').fadeOut();
	$('body').removeClass('hidden');
};
