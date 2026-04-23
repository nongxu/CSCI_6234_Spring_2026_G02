package com.onlineorder.onlineorder.controller;

import com.onlineorder.onlineorder.entity.MenuItem;
import com.onlineorder.onlineorder.entity.Restaurant;
import com.onlineorder.onlineorder.entity.User;
import com.onlineorder.onlineorder.model.LoginBody;
import com.onlineorder.onlineorder.model.RegisterRestaurantBody;
import com.onlineorder.onlineorder.model.SignUpBody;
import com.onlineorder.onlineorder.repository.UserRepository;
import com.onlineorder.onlineorder.service.RestaurantOwnerService;
import com.onlineorder.onlineorder.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantOwnerService restaurantOwnerService;
    private final UserRepository userRepository;

    public RestaurantController(RestaurantService restaurantService,
                                RestaurantOwnerService restaurantOwnerService,
                                UserRepository userRepository) {
        this.restaurantService = restaurantService;
        this.restaurantOwnerService = restaurantOwnerService;
        this.userRepository = userRepository;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/owner/restaurants")
    public ResponseEntity<?> getOwnerRestaurants(HttpSession session) {
        Object ownerId = session.getAttribute("ownerId");
        if (ownerId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(restaurantService.getRestaurantsByOwner((Long) ownerId));
    }

    @GetMapping("/restaurants/{restaurantId}/menu")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.getMenuItems(restaurantId));
    }

    @PostMapping("/owner/signup")
    public ResponseEntity<Void> ownerSignUp(@RequestBody SignUpBody body) {
        if (!restaurantOwnerService.validateOwnerCredentials(body)) {
            return ResponseEntity.badRequest().build();
        }
        restaurantOwnerService.registerOwner(body);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/owner/login")
    public ResponseEntity<Void> ownerLogin(@RequestBody LoginBody body, HttpSession session) {
        Optional<User> optionalUser = userRepository.findByEmail(body.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        User user = optionalUser.get();
        if (!user.getPassword().equals(body.getPassword())) {
            return ResponseEntity.status(401).build();
        }
        if (!restaurantOwnerService.isRestaurantOwner(user.getUserId())) {
            return ResponseEntity.status(401).build();
        }
        session.setAttribute("ownerId", user.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/owner/restaurant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerRestaurant(
            @RequestPart("data") RegisterRestaurantBody body,
            MultipartHttpServletRequest request,
            HttpSession session) {

        Object ownerId = session.getAttribute("ownerId");
        if (ownerId == null) {
            return ResponseEntity.status(401).build();
        }

        // Extract restaurant cover image (optional)
        MultipartFile restaurantImage = request.getFile("restaurantImage");

        // Extract menu item images by index: menuItemImage_0, menuItemImage_1, ...
        Map<Integer, MultipartFile> menuItemImages = new HashMap<>();
        if (body.getMenuItems() != null) {
            for (int i = 0; i < body.getMenuItems().size(); i++) {
                MultipartFile f = request.getFile("menuItemImage_" + i);
                if (f != null && !f.isEmpty()) {
                    menuItemImages.put(i, f);
                }
            }
        }

        try {
            if (!restaurantOwnerService.registerRestaurant((Long) ownerId, body, restaurantImage, menuItemImages)) {
                return ResponseEntity.badRequest().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image. Please try again.");
        }

        return ResponseEntity.ok().build();
    }
}
