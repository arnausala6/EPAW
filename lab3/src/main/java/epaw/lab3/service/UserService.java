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
import epaw.lab3.util.DBManager;
import epaw.lab3.repository.GroupRepository;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    private static UserService instance;
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    private static final List<String> SORTED_COUNTRIES = buildSortedCountries();


    public static List<String> buildSortedCountries() {
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

    public UserService() {
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

    public Map<String, String> validate(User user, Part filePart) {
        Map<String, String> errors = new HashMap<>();

        String username = user.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required.");
        } else if (username.length() > 30) {
            errors.put("username", "Username must have less than 30 characters.");
        } else if (userRepository.existsByUsername(username)) {
            errors.put("username", "Username already exists.");
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
            errors.put("description", "Maximum length is 300 characters");
        }

        if (filePart != null && filePart.getSize() > 0) {
            long maxBytes = 2 * 1024 * 1024; // 2MB
            if (filePart.getSize() > maxBytes) {
                errors.put("profilePicture", "The profile picture cannot exceed 2MB.");
            }
        }

        return errors;
    }

    public Map<String, String> register(User user, Part filePart) {
        Map<String, String> errors = validate(user, filePart);
        if (errors.isEmpty()) {
            if (filePart != null && filePart.getSize() > 0) {
                String picturePath = saveProfilePicture(filePart, user.getUsername());
                user.setPicture(picturePath);
            }

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

    public Map<String, String> checkBlock(Integer blockerId, Integer blockedId){
        Map<String, String> errors = new HashMap<>();
        if(!userRepository.existsById(blockedId)){
            errors.put("blocked_id", "Blocked user does not exist");
        }
        if(!userRepository.existsById(blockerId)){
            errors.put("blocker_id", "Blocker user does not exist");
        }
        return errors;
    }

    public Map<String, String> block(Integer blockerId, Integer blockedId){
        return block(blockerId, blockedId, null);
    }

    public Map<String, String> block(Integer blockerId, Integer blockedId, String reason){
        Map<String, String> errors = checkBlock(blockerId, blockedId);
        if(errors.isEmpty()){
            userRepository.saveBlock(blockerId, blockedId, reason);
        }
        return errors;
    }

    public boolean isPlatformBanned(User user) {
        if (user == null || "admin".equals(user.getRole())) {
            return false;
        }
        return userRepository.isPlatformBanned(user.getId());
    }

    public String getPlatformBanReason(User user) {
        if (user == null) {
            return null;
        }
        return userRepository.findPlatformBanReason(user.getId());
    }

    public Map<String, String> unblock(Integer blockerId, Integer blockedId){
        Map<String, String> errors = checkBlock(blockerId, blockedId);
        if(errors.isEmpty()){
            userRepository.saveUnblock(blockerId, blockedId);
        }
        return errors;
    }

    public Map<String, String> follow(Integer followerId, Integer followedId){
        Map<String, String> errors = checkFollow(followerId, followedId);
        if(errors.isEmpty()){
            userRepository.saveFollow(followerId, followedId);
        }
        return errors;
    }

    public Map<String, String> checkFollow(Integer followerId, Integer followedId){
        Map<String, String> errors = new HashMap<>();
        if(!userRepository.existsById(followedId)){
            errors.put("blocked_id", "Followed user does not exist");
        }
        if(!userRepository.existsById(followerId)){
            errors.put("blocker_id", "Follower user does not exist");
        }
        return errors;
    }

    public Map<String, String> unfollow(Integer followerId, Integer followedId){
        Map<String, String> errors = checkFollow(followerId, followedId);
        if(errors.isEmpty()){
            userRepository.saveUnfollow(followerId, followedId);
        }
        return errors;
    }

    public Map<String, String> joinGroup(Integer userId, Integer groupId){
        Map<String, String> errors = checkUserInGroup(userId, groupId);
        if(errors.isEmpty()){
            userRepository.saveUserJoiningGroup(userId, groupId);
        }
        return errors;
    }

    public Map<String, String> checkUserInGroup(Integer userId, Integer groupId){
        Map<String, String> errors = new HashMap<>();
        if(!userRepository.existsById(userId)){
            errors.put("user_id", "User does not exist");
        }
        if(!groupRepository.groupIdExists(groupId)){ 
            errors.put("group_id", "Group does not exist");
        }
        return errors;
    }

    public Map<String, String> leaveGroup(Integer userId, Integer groupId){
        Map<String, String> errors = checkUserInGroup(userId, groupId);
        if(errors.isEmpty()){
            userRepository.saveUserLeavingGroup(userId, groupId);
        }
        return errors;
    }

    public List<Group> getGroupsByUserId(Integer userId) {
        return userRepository.getGroupsByUserId(userId);
    }

    public Map<String, String> updateProfile(User sessionUser, User updatedUser, Part filePart){
        Map<String, String> errors = new HashMap<>();

        String username = updatedUser.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required.");
        } else if (username.length() > 30) {
            errors.put("username", "Username must have less than 30 characters.");
        } else if (!username.equals(sessionUser.getUsername()) && userRepository.existsByUsername(username)) {
            errors.put("username", "Username already exists.");
        }

        String description = updatedUser.getDescription();
        if (description != null && description.length() > 300) {
            errors.put("description", "Maximum length is 300 characters.");
        }

        if (filePart != null && filePart.getSize() > 0 && filePart.getSize() > 2 * 1024 * 1024) {
            errors.put("profilePicture", "The profile picture cannot exceed 2MB.");
        }

        if (errors.isEmpty()) {
            if (filePart != null && filePart.getSize() > 0) {
                String picturePath = saveProfilePicture(filePart, updatedUser.getUsername());
                updatedUser.setPicture(picturePath);
            }
            else {
                updatedUser.setPicture(sessionUser.getPicture());
            }
            userRepository.updateUser(updatedUser);
        }
        return errors;
    }


}