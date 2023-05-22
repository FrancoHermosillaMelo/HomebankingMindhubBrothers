const {createApp} = Vue;

createApp({
	data() {
		return {
			loans: [],
			loanName: '',
			loanMaxAmount: '',
			loanPayments: '',
			loanInterest: '',
		};
	},
	created() {
		this.loadData();
	},
	methods: {
		loadData() {
			axios
				.get('http://localhost:8080/api/loans')
				.then(response => {
					this.loans = response.data;
					console.log(this.loans);
				})
				.catch(error => console.log(error));
		},
		createLoan() {
			axios
				.post('http://localhost:8080/api/loans/create', {
					name: this.loanName,
					maxAmount: this.loanMaxAmount,
					payments: this.loanPayments.split(', '),
					interests: this.loanInterest,
				})
				.then(response => {
					window.location.replace = './manager.html';
				})
				.catch(error => {
					Swal.fire({
						icon: 'error',
						text: error.response.data,
						confirmButtonColor: '#7c601893',
					});
				});
		},
	},
}).mount('#app');
