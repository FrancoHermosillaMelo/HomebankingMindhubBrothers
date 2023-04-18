const {createApp} = Vue;

createApp({
	data() {
		return {
			datos: [],
			cardsCredit: [],
			cardsDebit: [],
			id: new URLSearchParams(location.search).get('id'),
		};
	},
	created() {
		this.loadData();
	},
	methods: {
		loadData() {
			console.log(this.id);
			axios
				.get('http://localhost:8080/api/clients/' + this.id)
				.then(response => {
					this.datos = response.data;
					console.log(this.datos);
					this.cardsCredit = this.datos.cards.filter(card => card.type == 'CREDIT');
					this.cardsDebit = this.datos.cards.filter(card => card.type == 'DEBIT');
				})
				.catch(error => console.log(error));
		},
	},
}).mount('#app');
