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
import java.util.Collections;

import epaw.lab3.model.Group;
import epaw.lab3.model.User;
import epaw.lab3.repository.UserRepository;
import epaw.lab3.repository.GroupRepository;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserService {

    private static UserService instance;
    private UserRepository userRepository;
    private GroupRepository groupRepository;

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
        this.groupRepository = GroupRepository.getInstance();
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
        } else if (userRepository.existsEmail(email)) {
            errors.put("email", "This email is already registered");
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

        String country = user.getCountry();
        int index = Collections.binarySearch(SORTED_COUNTRIES, country);
        if (index < 0 && country.equals("-")) {
            errors.put("country", "Not a valid country");
        }

        String description = user.getDescription();
        if (description.length() > 300){
            errors.put("descrption", "Maximum length is 300 characters");
        }

        //!!!CODIGO PARA METER CUANDO EL DB ESTÉ BIEN HECHO!!!

        // Integer groupId = user.getGroupId();
        // if(!groupRepository.groupIdExists(groupId) && groupId!=0){
        //     System.err.println("Group:");
        //     System.err.println(groupId);
        //     errors.put("groupId", "Group does not exist");
        // }

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
        Map<String, String> errors = new HashMap<>();
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }

        long maxBytes = 2 * 1024 * 1024; // 2MB
        if (filePart.getSize() > maxBytes) {
            errors.put("profilePicture", "The profile picture cannot exceed 2MB.");
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