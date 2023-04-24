const {createApp} = Vue;

createApp({
	data() {
		return {
			firstname: '',
			lastname: '',
			email: '',
			password: '',
			claseagregada: true,
		};
	},

	methods: {
		signIn() {
			axios
				.post('/api/login', 'email=' + this.email + '&password=' + this.password)
				.then(response => (window.location.href = '/web/html/accounts.html'))
				.catch(error =>
					Swal.fire({
						icon: 'error',
						title: 'Oh!',
						text: 'Your email or your password are incorrect',
					})
				);
		},
		addclass() {
			if (this.claseagregada != false) {
				this.claseagregada = false;
			} else {
				this.claseagregada = true;
			}
		},
		register() {
			axios
				.post('/api/clients', 'firstName=' + this.firstname + '&lastName=' + this.lastname + '&email=' + this.email + '&password=' + this.password)
				.then(response => this.signIn())
				.catch(error =>
					Swal.fire({
						icon: 'error',
						title: 'Oh!',
						text: 'The email used is already in use',
					})
				);
		},
	},
	computed: {
		charP() {
			this.firstname = this.firstname.charAt(0).toUpperCase() + this.firstname.slice(1);
			this.lastname = this.lastname.charAt(0).toUpperCase() + this.firstname.slice(1);
		},
	},
}).mount('#app');
