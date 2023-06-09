const {createApp} = Vue;

createApp({
	data() {
		return {
			firstname: '',
			lastname: '',
			email: '',
			emailRegister: '',
			password: '',
			passwordRegister: '',
			claseagregada: true,
		};
	},

	methods: {
		signIn() {
			axios
				.post('/api/login', 'email=' + this.email + '&password=' + this.password)
				.then(response => {
					if (this.email == 'hermosilla@gmail.com') {
						window.location.replace('/manager.html');
					} else {
						window.location.replace('./html/accounts.html');
					}
				})
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
				.post(
					'/api/clients',
					'firstName=' + this.firstname + '&lastName=' + this.lastname + '&email=' + this.emailRegister + '&password=' + this.passwordRegister
				)
				.then(response => {
					this.password = this.passwordRegister;
					this.email = this.emailRegister;
					this.signIn();
				})
				.catch(error =>
					Swal.fire({
						icon: 'error',
						text: error.response.data,
						confirmButtonColor: '#7c601893',
					})
				);
		},
	},
	computed: {
		charP() {
			this.firstname = this.firstname.charAt(0).toUpperCase() + this.firstname.slice(1);
			this.lastname = this.lastname.charAt(0).toUpperCase() + this.lastname.slice(1);
		},
	},
}).mount('#app');

window.onload = function () {
	$('#onload').fadeOut();
	$('body').removeClass('hidden');
};
