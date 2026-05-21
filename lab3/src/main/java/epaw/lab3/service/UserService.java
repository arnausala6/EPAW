package epaw.lab3.service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import epaw.lab3.model.Group;
import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserService {

    private static UserService instance;
    private UserRepository userRepository;

    private static final List<String> SORTED_COUNTRIES = buildSortedCountries();

    private static final List<Group> MOCK_GROUPS = Arrays.asList(
        group(1, "Web Engineering"),
        group(2, "AI Projects"),
        group(3, "Campus Events")
    );

    private static Group group(int id, String name) {
        Group g = new Group();
        g.setId(id);
        g.setName(name);
        return g;
    }

    private static List<String> buildSortedCountries() {
        Set<String> countries = new HashSet<>();
        for (String code : Locale.getISOCountries()) {
            String name = new Locale("", code).getDisplayCountry(Locale.ENGLISH);
            if (name != null && !name.isBlank()) {
                countries.add(name);
            }
        }
        Collator collator = Collator.getInstance(Locale.ENGLISH);
        return countries.stream().sorted(collator).toList();
    }

    private UserService() {
        this.userRepository = UserRepository.getInstance();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public List<String> getAvailableCountries() {
        return SORTED_COUNTRIES;
    }

    public List<Group> getTopTenGroups() {
        return MOCK_GROUPS;
    }

    public Map<String, String> validate(User user) {
        Map<String, String> errors = new HashMap<>();

        String username = user.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required.");
        } else if (username.length() > 30) {
            errors.put("username", "Username must have less than 30 characters.");
        } else if (userRepository.existsByUsername(username)) {
            errors.put("username", "Username already exists.");
        }

        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Name cannot be empty.");
        } else if (name.length() > 30) {
            errors.put("name", "Name must have less than 30 characters.");
        }

        String password = user.getPassword();
        if (password == null || !password.matches(PASSWORD_REGEX)) {
            errors.put("password", "Minimum requirements: 8 characters, one uppercase letter, and one number.");
        }

        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email is required.");
        } else if (!email.matches(EMAIL_REGEX)) {
            errors.put("email", "This must have email form.");
        } else if (email.length() > 255) {
            errors.put("email", "Maxlength reached.");
        }

        String gender = user.getGender();
        if (gender == null || gender.trim().isEmpty()) {
            errors.put("gender", "Gender is required.");
        }

        Integer age = user.getAge();
        if (age == null) {
            errors.put("age", "Age is required.");
        } else if (age < 16) {
            errors.put("age", "You must be at least 16 to register");
        }

        return errors;
    }

    public Map<String, String> register(User user) {
        Map<String, String> errors = validate(user);
        if (errors.isEmpty()) {
            userRepository.save(user);
        }
        return errors;
    }

    public Map<String, String> login(User user) {
        Map<String, String> errors = new HashMap<>();
        if (!userRepository.checkLogin(user)) {
            errors.put("password", "The combination of name and password does not match in our database");
        }
        return errors;
    }

    public String saveProfilePicture(Part filePart, String username) {
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }
        try {
            String fileName = filePart.getSubmittedFileName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = username + extension;
            String resourcesDir = "EXTERNAL_RESOURCES";
            Files.createDirectories(Paths.get(resourcesDir));
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(resourcesDir, newFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}