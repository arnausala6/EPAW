package epaw.lab2.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import epaw.lab2.model.Group;
import epaw.lab2.model.User;
import epaw.lab2.model.ValidationError;
import epaw.lab2.repository.GroupRepository;
import epaw.lab2.repository.UserRepository;

public class UserService {

	private static UserService instance;
	private UserRepository userRepository;
	private GroupRepository groupRepository;

	private UserService() {
		this.userRepository = UserRepository.getInstance();
		this.groupRepository = GroupRepository.getInstance();
	}

	public static synchronized UserService getInstance() {
		if (instance == null) {
			instance = new UserService();
		}
		return instance;
	}

	private static final String USERNAME_PATTERN = "^[A-Za-z0-9_.-]{1,30}$";
	private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";
	private static final int PROFILE_PICTURE_MAX_BYTES = 2 * 1024 * 1024;
	private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp",
			"image/gif");
	private static final Set<String> ALLOWED_GENDERS = Set.of("male", "female", "other");
	private static final Set<String> VALID_COUNTRIES = buildValidCountries();
	private static final List<String> SORTED_COUNTRIES = buildSortedCountries();

	private static Set<String> buildValidCountries() {
		Set<String> countries = new HashSet<>();
		for (String code : Locale.getISOCountries()) {
			if (code == null || code.isBlank()) {
				continue;
			}
			Locale loc = new Locale.Builder().setRegion(code).build();
			String name = loc.getDisplayCountry(Locale.ENGLISH);
			if (name != null && !name.isBlank()) {
				countries.add(name);
			}
		}
		return Collections.unmodifiableSet(countries);
	}

	private static List<String> buildSortedCountries() {
		Collator collator = Collator.getInstance(Locale.ENGLISH);
		return VALID_COUNTRIES.stream().sorted(collator).toList();
	}

	public List<String> getAvailableCountries() {
		return SORTED_COUNTRIES;
	}

	public List<Group> getTopTenGroups() {
		return groupRepository.findTopTen();
	}

	public List<ValidationError> validate(User user) {
		List<ValidationError> errors = new ArrayList<>();

		String username = normalize(user.getUsername());
		if (username == null) {
			errors.add(new ValidationError("username", "Username is required."));
		} else if (!username.matches(USERNAME_PATTERN)) {
			errors.add(new ValidationError("username",
					"Username must be at most 30 characters and use only letters, numbers, _ . -"));
		} else if (userRepository.existsByUsername(username)) {
			errors.add(new ValidationError("username", "Username already exists."));
		}

		String name = normalize(user.getName());
		if (name == null) {
			errors.add(new ValidationError("name", "Name is required."));
		} else if (name.length() > 30) {
			errors.add(new ValidationError("name", "Name must have at most 30 characters."));
		}

		String email = normalizeEmail(user.getEmail());
		if (email == null) {
			errors.add(new ValidationError("email", "Email is required."));
		} else if (!email.matches(EMAIL_PATTERN)) {
			errors.add(new ValidationError("email", "Email format is not valid."));
		} else if (userRepository.existsByEmail(email)) {
			errors.add(new ValidationError("email", "Email already exists."));
		} else {
			user.setEmail(email);
		}

		String password = user.getPassword();
		if (password == null || !password.matches(PASSWORD_REGEX)) {
			errors.add(new ValidationError("password",
					"Password must be at least 8 characters and include one capital letter and one number."));
		}

		if (password == null || !password.equals(user.getConfirmPassword())) {
			errors.add(new ValidationError("confirmPassword", "Passwords do not match."));
		}

		Integer age = user.getAge();
		if (age == null) {
			errors.add(new ValidationError("age", "Age is required."));
		} else if (age < 16) {
			errors.add(new ValidationError("age", "You must be at least 16 years old."));
		}

		String gender = normalize(user.getGender());
		if (gender == null) {
			errors.add(new ValidationError("gender", "Gender is required."));
		} else if (!ALLOWED_GENDERS.contains(gender)) {
			errors.add(new ValidationError("gender", "Please choose a valid gender."));
		}

		String country = normalize(user.getCountry());
		if (country != null && !VALID_COUNTRIES.contains(country)) {
			errors.add(new ValidationError("country", "Please choose a valid country."));
		}

		String description = normalize(user.getDescription());
		if (description != null && description.length() > 300) {
			errors.add(new ValidationError("description", "Description must have at most 300 characters."));
		}

		Integer groupId = user.getGroupId();
		if (groupId != null && !groupRepository.existsById(groupId)) {
			errors.add(new ValidationError("groupId", "Please choose a valid group."));
		}

		return errors;
	}

	public List<ValidationError> register(User user) {
		List<ValidationError> errors = validate(user);
		if (errors.isEmpty()) {
			userRepository.save(user);
		}
		return errors;
	}

	public List<ValidationError> validateProfilePicture(String contentType, long sizeBytes) {
		List<ValidationError> errors = new ArrayList<>();
		if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
			errors.add(new ValidationError("profilePicture",
					"Profile picture must be a valid image (jpg, png, webp, gif)."));
		}
		if (sizeBytes > PROFILE_PICTURE_MAX_BYTES) {
			errors.add(new ValidationError("profilePicture", "Profile picture must be 2MB or smaller."));
		}
		return errors;
	}

	public List<ValidationError> login(String username, String password) {
		List<ValidationError> errors = new ArrayList<>();
		String u = normalize(username);
		if (u == null) {
			errors.add(new ValidationError("loginUsername", "Username is required."));
			return errors;
		}
		if (password == null || password.isEmpty()) {
			errors.add(new ValidationError("loginPassword", "Password is required."));
			return errors;
		}
		var found = userRepository.findByUsername(u);
		if (found.isEmpty() || !password.equals(found.get().getPassword())) {
			errors.add(new ValidationError("loginPassword", "Wrong username or password."));
		}
		return errors;
	}

	public Optional<User> findByUsername(String username) {
		String u = normalize(username);
		if (u == null) {
			return Optional.empty();
		}
		return userRepository.findByUsername(u);
	}

	public String normalize(String value) {
		if (value == null) {
			return null;
		}
		String normalized = value.trim();
		return normalized.isEmpty() ? null : normalized;
	}

	public String normalizeEmail(String value) {
		String n = normalize(value);
		return n == null ? null : n.toLowerCase(Locale.ROOT);
	}

}
