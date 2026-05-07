const form = document.getElementById('registerForm');
const password = document.getElementById('password');
const confirmPassword = document.getElementById('confirmPassword');
const profilePicture = document.getElementById('profilePicture');
const previewContainer = document.getElementById('previewContainer');
const previewImg = document.getElementById('profilePreviewImg');
const profileFileHint = document.getElementById('profileFileHint');
const btnRemoveProfile = document.getElementById('btnRemoveProfile');
const removeProfileField = document.getElementById('removeProfilePicture');
const maxBytes = 2 * 1024 * 1024;

const ctx = document.body.getAttribute('data-ctx') || '';

const HINT_EMPTY = 'Ningún archivo seleccionado';

const PASSWORD_RULE = /^(?=.*[A-Z])(?=.*[0-9]).{8,}$/;

function savedPictureSrc() {
  if (!previewContainer) return '';
  const rel = previewContainer.getAttribute('data-saved-path') || '';
  return rel ? (ctx ? ctx + '/' + rel : '/' + rel) : '';
}

function updateProfileFileRow() {
  if (!btnRemoveProfile || !profileFileHint) return;
  const showPreview = previewContainer && previewContainer.style.display !== 'none';
  const hasFile = profilePicture && profilePicture.files && profilePicture.files.length > 0;
  const removed = removeProfileField && removeProfileField.value === 'true';
  const showRemove = !removed && (showPreview || hasFile);

  if (showRemove) {
    profileFileHint.style.display = 'none';
    btnRemoveProfile.style.display = 'inline-block';
  } else {
    profileFileHint.style.display = '';
    profileFileHint.textContent = HINT_EMPTY;
    btnRemoveProfile.style.display = 'none';
  }
}

function showPreviewFromFile(file) {
  if (!previewImg || !previewContainer) return;
  const reader = new FileReader();
  reader.onload = () => {
    previewImg.src = reader.result;
    previewContainer.style.display = 'block';
    updateProfileFileRow();
  };
  reader.readAsDataURL(file);
}

function resetPreviewToSaved() {
  if (!previewImg || !previewContainer) return;
  const saved = savedPictureSrc();
  if (saved && removeProfileField && removeProfileField.value !== 'true') {
    previewImg.src = saved;
    previewContainer.style.display = 'block';
  } else {
    previewImg.removeAttribute('src');
    previewContainer.style.display = 'none';
  }
  updateProfileFileRow();
}

function validatePasswordMatch() {
  let msg = '';
  if (confirmPassword.value !== password.value) {
    msg = 'Passwords do not match.';
  } else if (password.value !== '' && !PASSWORD_RULE.test(password.value)) {
    msg = 'La contraseña principal no cumple los requisitos mínimos.';
  }
  confirmPassword.setCustomValidity(msg);
}

confirmPassword.addEventListener('input', validatePasswordMatch);
password.addEventListener('input', validatePasswordMatch);

if (btnRemoveProfile) {
  btnRemoveProfile.addEventListener('click', () => {
    if (removeProfileField) {
      removeProfileField.value = 'true';
    }
    if (profilePicture) {
      profilePicture.value = '';
      profilePicture.setCustomValidity('');
    }
    if (previewContainer) {
      previewContainer.setAttribute('data-saved-path', '');
    }
    const savedHidden = document.getElementById('savedProfilePicturePath');
    if (savedHidden) {
      savedHidden.remove();
    }
    if (previewImg) {
      previewImg.removeAttribute('src');
    }
    if (previewContainer) {
      previewContainer.style.display = 'none';
    }
    updateProfileFileRow();
  });
}

if (profilePicture) {
  profilePicture.addEventListener('change', () => {
    if (removeProfileField) {
      removeProfileField.value = 'false';
    }
    const f = profilePicture.files[0];
    if (!f) {
      profilePicture.setCustomValidity('');
      resetPreviewToSaved();
      return;
    }
    if (f.size > maxBytes) {
      profilePicture.setCustomValidity('File must be 2MB or smaller.');
    } else if (!f.type.startsWith('image/')) {
      profilePicture.setCustomValidity('Choose an image file.');
    } else {
      profilePicture.setCustomValidity('');
      showPreviewFromFile(f);
    }
    profilePicture.reportValidity();
    updateProfileFileRow();
  });
}

Object.entries(serverErrors).forEach(([field, message]) => {
  const input = document.getElementsByName(field)[0];
  if (input) {
    input.setCustomValidity(message);
    input.reportValidity();
    input.addEventListener('input', () => input.setCustomValidity(''), { once: true });
  }
});

validatePasswordMatch();

form.addEventListener('submit', event => {
  validatePasswordMatch();
  if (profilePicture && profilePicture.files[0] && profilePicture.files[0].size > maxBytes) {
    profilePicture.setCustomValidity('File must be 2MB or smaller.');
  }
  if (!form.checkValidity()) {
    event.preventDefault();
    form.reportValidity();
  }
});

updateProfileFileRow();
