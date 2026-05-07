package epaw.lab2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import epaw.lab2.model.User;
import epaw.lab2.model.ValidationError;
import epaw.lab2.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@WebServlet("/Register")
@MultipartConfig
public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String PROFILE_UPLOAD_DIR = "uploaded-profile-pictures";
	private UserService userService;

	@Override
	public void init() throws ServletException {
		userService = UserService.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("errors", Collections.emptyList());
		attachCatalogData(request);
		request.getRequestDispatcher("Register.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = new User();
		List<ValidationError> errors = new ArrayList<>();

		user.setUsername(request.getParameter("username"));
		user.setName(request.getParameter("name"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));
		user.setConfirmPassword(request.getParameter("confirmPassword"));
		user.setGender(request.getParameter("gender"));
		user.setDescription(request.getParameter("description"));
		user.setCountry(request.getParameter("country"));

		user.setUsername(userService.normalize(user.getUsername()));
		user.setName(userService.normalize(user.getName()));
		user.setDescription(userService.normalize(user.getDescription()));
		user.setCountry(userService.normalize(user.getCountry()));
		user.setGender(userService.normalize(user.getGender()));

		String groupIdParam = request.getParameter("groupId");
		if (groupIdParam != null && !groupIdParam.isBlank()) {
			try {
				user.setGroupId(Integer.valueOf(groupIdParam));
			} catch (NumberFormatException e) {
				errors.add(new ValidationError("groupId", "Group is not valid."));
			}
		} else {
			user.setGroupId(null);
		}

		String ageValue = request.getParameter("age");
		if (ageValue != null && !ageValue.isBlank()) {
			try {
				user.setAge(Integer.valueOf(ageValue));
			} catch (NumberFormatException e) {
				errors.add(new ValidationError("age", "Age must be a valid number."));
			}
		}

		String savedProfilePath = userService.normalize(request.getParameter("savedProfilePicturePath"));
		boolean removeProfile = "true".equalsIgnoreCase(request.getParameter("removeProfilePicture"));
		if (savedProfilePath != null) {
			user.setProfilePicturePath(savedProfilePath);
		}
		if (removeProfile) {
			user.setProfilePicturePath(null);
			if (savedProfilePath != null) {
				deleteProfileFile(savedProfilePath);
			}
		}

		Part profilePicturePart = request.getPart("profilePicture");
		if (profilePicturePart != null && profilePicturePart.getSize() > 0) {
			errors.addAll(userService.validateProfilePicture(profilePicturePart.getContentType(),
					profilePicturePart.getSize()));
			boolean hasProfilePictureError = errors.stream()
					.anyMatch(error -> "profilePicture".equals(error.getPropertyPath()));
			if (!hasProfilePictureError) {
				if (savedProfilePath != null) {
					deleteProfileFile(savedProfilePath);
				}
				String storedRelativePath = storeProfilePicture(profilePicturePart);
				user.setProfilePicturePath(storedRelativePath);
			}
		}

		if (errors.isEmpty()) {
			errors.addAll(userService.register(user));
		}

		if (errors.isEmpty()) {
			request.setAttribute("user", user);
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		} else {
			request.setAttribute("user", user);
			request.setAttribute("errors", errors);
			attachCatalogData(request);
			request.getRequestDispatcher("Register.jsp").forward(request, response);
		}
	}

	private void attachCatalogData(HttpServletRequest request) {
		request.setAttribute("countries", userService.getAvailableCountries());
		request.setAttribute("groups", userService.getTopTenGroups());
	}

	private void deleteProfileFile(String relativePath) {
		if (relativePath == null || relativePath.isBlank()) {
			return;
		}
		String safe = relativePath.replace("..", "").replace("\\", "/");
		if (safe.startsWith("/")) {
			safe = safe.substring(1);
		}
		String basePath = getServletContext().getRealPath("/");
		if (basePath == null) {
			return;
		}
		Path target = Path.of(basePath, safe);
		try {
			Files.deleteIfExists(target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String storeProfilePicture(Part part) throws IOException {
		String submitted = part.getSubmittedFileName();
		String ext = "";
		if (submitted != null && submitted.contains(".")) {
			ext = submitted.substring(submitted.lastIndexOf('.'));
		}
		String filename = UUID.randomUUID() + ext;
		String basePath = getServletContext().getRealPath("/") + File.separator + PROFILE_UPLOAD_DIR;
		Path uploadDir = Path.of(basePath);
		Files.createDirectories(uploadDir);

		Path target = uploadDir.resolve(filename);
		try (var input = part.getInputStream()) {
			Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
		}
		return PROFILE_UPLOAD_DIR + "/" + filename;
	}
}
