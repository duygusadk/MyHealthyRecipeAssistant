package uni.fmi.masters.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import uni.fmi.masters.dto.request.UserPreferenceRequestDTO;
import uni.fmi.masters.dto.response.RecipeResponseDTO;
import uni.fmi.masters.dto.response.UserPreferenceResponseDTO;
import uni.fmi.masters.models.RecipeMetadata;
import uni.fmi.masters.models.UserPreference;
import uni.fmi.masters.repositories.RecipeMetadataRepo;
import uni.fmi.masters.repositories.UserPreferenceRepo;

import java.util.List;
import java.util.stream.Collectors;

public class UserPreferenceService extends BaseService<UserPreference, UserPreferenceRepo>{
    @Autowired
    private RecipeMetadataRepo recipeMetadataRepo;
    public UserPreferenceService(UserPreferenceRepo repository) {
        super(repository);
    }
    public List<UserPreferenceResponseDTO> findAllDto() {
        return findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private UserPreferenceResponseDTO toDto(UserPreference u) {
        return new UserPreferenceResponseDTO(
                u.getId(), u.getUsername(),u.getFavoriteRecipes()
        );
    }
    public UserPreferenceResponseDTO findByIdDto(Long id) {
        return toDto(findById(id));
    }

    public UserPreferenceResponseDTO createUser(UserPreferenceRequestDTO dto) {
        UserPreference user = new UserPreference();
        user.setUsername(dto.getUsername());
        return toDto(save(user));
    }
    @Transactional
    public void addRecipeToFavorites(Long userId, Long recipeId) {
        UserPreference user = findById(userId);
        if (user == null) throw new RuntimeException("Потребителят не е намерен");

        RecipeMetadata recipe = recipeMetadataRepo.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Рецептата не съществува"));


        if (!user.getFavoriteRecipes().contains(recipe)) {
            user.getFavoriteRecipes().add(recipe);
            save(user);
        }
    }
    @Transactional
    public void removeRecipeFromFavorites(Long userId, Long recipeId) {
        UserPreference user = findById(userId);
        if (user != null) {
            user.getFavoriteRecipes().removeIf(recipe -> recipe.getId().equals(recipeId));
            save(user);
        }
    }
}
