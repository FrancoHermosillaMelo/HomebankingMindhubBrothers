const {createApp} = Vue;

createApp({
	data() {
		return {
			datos: [],
			cards: [],
			cardsCredit: [],
			cardsDebit: [],
			cardsActive: [],
			actualDate: new Date().toLocaleString().split(',')[0].split('/').reverse().join('-'),
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
					this.cardsCredit = this.datos.cards.filter(card => card.type == 'CREDIT' && card.active);
					this.cardsDebit = this.datos.cards.filter(card => card.type == 'DEBIT' && card.active);
					this.cardsActive = this.cards.filter(card => card.active);
				})
				.catch(error => {
					Swal.fire({
						icon: 'error',
						text: error.response.data,
						confirmButtonColor: '#7c601893',
					});
				});
		},
		cardDelete(id) {
			Swal.fire({
				title: 'Are you sure you want to delete this card?',
				icon: 'warning',
				showCancelButton: true,
				confirmButtonText: 'DELETE',
				showLoaderOnConfirm: true,
				preConfirm: deleteCards => {
					return axios
						.put(`/api/clients/current/cards/${id}`)
						.then(response => {
							window.location.href = '/web/html/cards.html';
						})
						.catch(error => {
							Swal.fire({
								icon: 'error',
								text: error.response.data,
								confirmButtonColor: '#7c601893',
							});
						});
				},
			});
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
