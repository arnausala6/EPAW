window.App = window.App || {};
App.initRegisterValidation = function (serverErrors)  {
	
	const form = document.getElementById('registerForm');
	const password = document.getElementById('password');
	const confirmPassword = document.getElementById('confirmPassword');
	
	const profilePicture = document.getElementById('profilePicture');
    const btnRemoveProfile = document.getElementById('btnRemoveProfile');

	// check passwords are equal
	confirmPassword.addEventListener('input', () => {
	  confirmPassword.setCustomValidity(
	    confirmPassword.value !== password.value ? "Passwords do not match." : ""
	  );
	});
	
	if (profilePicture) {
        profilePicture.addEventListener('change', () => {
            const file = profilePicture.files[0];
            if (file) {
                const maxSize = 2 * 1024 * 1024;
                if (file.size > maxSize) {
                    profilePicture.setCustomValidity("The profile picture cannot exceed 2MB.");
                    profilePicture.reportValidity();
                    if (btnRemoveProfile) btnRemoveProfile.style.display = 'none';
                } else {
                    profilePicture.setCustomValidity('');
                    if (btnRemoveProfile) btnRemoveProfile.style.display = 'inline-block';
                }
            }
        });
    }

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
	
	  confirmPassword.setCustomValidity( 
		    confirmPassword.value !== password.value ? "Passwords do not match." : ""
	  );
	  
	  if (!form.checkValidity()) {
	    event.preventDefault();
	    form.reportValidity();
	  }
	});

}
