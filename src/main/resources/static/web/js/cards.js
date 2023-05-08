const {createApp} = Vue;

createApp({
	data() {
		return {
			datos: [],
			cards: [],
			cardsCredit: [],
			cardsDebit: [],
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
					this.cards = this.datos.cards;
					this.cardsCredit = this.datos.cards.filter(card => card.type == 'CREDIT');
					this.cardsDebit = this.datos.cards.filter(card => card.type == 'DEBIT');
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
							console.log(error);
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
