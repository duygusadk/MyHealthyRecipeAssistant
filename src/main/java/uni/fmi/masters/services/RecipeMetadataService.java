package uni.fmi.masters.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.fmi.masters.dto.request.RecipeRequestDTO;
import uni.fmi.masters.dto.response.RecipeResponseDTO;

import uni.fmi.masters.jade.Recipe;
import uni.fmi.masters.models.RecipeMetadata;
import uni.fmi.masters.models.UserPreference;
import uni.fmi.masters.repositories.RecipeMetadataRepo;
import uni.fmi.masters.repositories.UserPreferenceRepo;

import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

@Service
public class RecipeMetadataService extends BaseService<RecipeMetadata, RecipeMetadataRepo> {
    @Autowired
    private RecipeMetadataRepo repository;
    @Autowired
    private UserPreferenceRepo userPreferenceRepo;
    public RecipeMetadataService(RecipeMetadataRepo repository) {
        super(repository);
    }

    public List<RecipeResponseDTO> findAllDto() {
        return findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private RecipeResponseDTO toDto(RecipeMetadata r) {
        return new RecipeResponseDTO(
                r.getId(), r.getRecipeIri(), r.getName(), r.getImageUrl()
        );
    }

    public RecipeResponseDTO findByIdDto(Long id) {
        return toDto(findById(id));
    }

    public RecipeResponseDTO create(RecipeResponseDTO dto) {

        RecipeMetadata recipeMetadata = new RecipeMetadata(dto.getRecipeIri(), dto.getName(), dto.getImageUrl());
        return toDto(save(recipeMetadata));
    }
    public RecipeResponseDTO updateDto(Long id, RecipeRequestDTO dto) {
        RecipeMetadata re = findById(id);
        if (re == null) throw new RuntimeException("Recipe not found");

        re.setRecipeIri(dto.getRecipeIri());
        re.setName(dto.getName());
        re.setImageUrl(dto.getImageUrl());

        return toDto(save(re));
    }
    public List<RecipeResponseDTO> addMetadataToRecipe(List<Recipe> ontologyRecipes) {
        return ontologyRecipes.stream()
                .map(recipe -> {

                    RecipeMetadata meta = repository.findByRecipeIri(recipe.getId())
                            .orElseGet(() -> {

                                RecipeMetadata temp = new RecipeMetadata();
                                temp.setRecipeIri(recipe.getId());
                                temp.setName(recipe.getName());
                                return temp;
                            });

                    return toDto(meta);
                })
                .collect(Collectors.toList());
    }

}
