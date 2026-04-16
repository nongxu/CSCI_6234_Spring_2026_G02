package com.onlineorder.onlineorder.service;

import com.onlineorder.onlineorder.entity.MenuItem;
import com.onlineorder.onlineorder.entity.Restaurant;
import com.onlineorder.onlineorder.entity.RestaurantOwner;
import com.onlineorder.onlineorder.entity.User;
import com.onlineorder.onlineorder.model.RegisterRestaurantBody;
import com.onlineorder.onlineorder.model.SignUpBody;
import com.onlineorder.onlineorder.repository.MenuItemRepository;
import com.onlineorder.onlineorder.repository.RestaurantOwnerRepository;
import com.onlineorder.onlineorder.repository.RestaurantRepository;
import com.onlineorder.onlineorder.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantOwnerService {

    private final UserRepository userRepository;
    private final RestaurantOwnerRepository restaurantOwnerRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final ImageService imageService;

    public RestaurantOwnerService(UserRepository userRepository,
                                  RestaurantOwnerRepository restaurantOwnerRepository,
                                  RestaurantRepository restaurantRepository,
                                  MenuItemRepository menuItemRepository,
                                  ImageService imageService) {
        this.userRepository = userRepository;
        this.restaurantOwnerRepository = restaurantOwnerRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.imageService = imageService;
    }

    public boolean validateOwnerCredentials(SignUpBody body) {
        if (body.getEmail() == null || body.getEmail().isBlank()) return false;
        if (body.getPassword() == null || body.getPassword().isBlank()) return false;
        if (body.getFirstName() == null || body.getFirstName().isBlank()) return false;
        if (body.getLastName() == null || body.getLastName().isBlank()) return false;
        if (userRepository.findByEmail(body.getEmail()).isPresent()) return false;
        return true;
    }

    public void registerOwner(SignUpBody body) {
        User user = new User(body.getEmail(), body.getPassword(), body.getFirstName(), body.getLastName());
        User savedUser = userRepository.save(user);

        RestaurantOwner owner = new RestaurantOwner(savedUser.getUserId(), null);
        owner.setNew(true);
        restaurantOwnerRepository.save(owner);
    }

    public boolean isRestaurantOwner(Long userId) {
        return restaurantOwnerRepository.findById(userId).isPresent();
    }

    public boolean registerRestaurant(Long ownerId,
                                      RegisterRestaurantBody body,
                                      MultipartFile restaurantImageFile,
                                      Map<Integer, MultipartFile> menuItemImageFiles) throws IOException {
        if (body.getRestaurantName() == null || body.getRestaurantName().isBlank()) return false;
        if (body.getAddress() == null || body.getAddress().isBlank()) return false;

        Restaurant savedRestaurant = createRestaurant(ownerId, body, restaurantImageFile);

        List<RegisterRestaurantBody.MenuItemBody> items = body.getMenuItems();
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                MultipartFile imageFile = menuItemImageFiles != null ? menuItemImageFiles.get(i) : null;
                createdMenuItem(savedRestaurant.getRestaurantId(), items.get(i), imageFile);
            }
        }

        return true;
    }

    // Corresponds to Restaurant.createRestaurant() in the Class Diagram
    private Restaurant createRestaurant(Long ownerId,
                                        RegisterRestaurantBody body,
                                        MultipartFile restaurantImageFile) throws IOException {
        Restaurant restaurant = new Restaurant(ownerId, body.getRestaurantName(), body.getAddress(), body.getPhone(), null);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        String imagePath = imageService.saveRestaurantImage(restaurantImageFile, savedRestaurant.getRestaurantId());
        if (imagePath != null) {
            savedRestaurant.setImage(imagePath);
            restaurantRepository.save(savedRestaurant);
        }

        return savedRestaurant;
    }

    // Corresponds to MenuItem.createdMenuItem() in the Class Diagram
    private void createdMenuItem(Long restaurantId,
                                 RegisterRestaurantBody.MenuItemBody itemBody,
                                 MultipartFile imageFile) throws IOException {
        MenuItem menuItem = new MenuItem(
                restaurantId,
                itemBody.getName(),
                itemBody.getDescription(),
                itemBody.getPrice(),
                null
        );
        MenuItem savedItem = menuItemRepository.save(menuItem);

        String imagePath = imageService.saveMenuItemImage(imageFile, restaurantId, savedItem.getMenuItemId());
        if (imagePath != null) {
            savedItem.setImage(imagePath);
            menuItemRepository.save(savedItem);
        }
    }
}
