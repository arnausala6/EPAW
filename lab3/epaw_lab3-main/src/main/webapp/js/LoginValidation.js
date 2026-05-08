window.App = window.App || {};
App.initLoginValidation = function (serverErrors) {

	const form = document.getElementById('loginForm');
	
	Object.entries(serverErrors).forEach(([field, message]) => {
	  const input = document.getElementsByName(field)[0];
	  if (input) {
	    input.setCustomValidity(message);
	    input.reportValidity();
		input.addEventListener('input', () => {
		  input.setCustomValidity('');
		  input.reportValidity();
		});
	  }
	});
	
	// check before submit
	form.addEventListener('submit', event => {
	
	  if (!form.checkValidity()) {
	    event.preventDefault();
	    form.reportValidity();
	  }
	});

}
