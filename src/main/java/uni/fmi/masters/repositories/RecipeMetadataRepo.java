package uni.fmi.masters.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fmi.masters.models.RecipeMetadata;

import java.util.Optional;

public interface RecipeMetadataRepo extends JpaRepository<RecipeMetadata,Long> {
    Optional<RecipeMetadata> findByRecipeIri(String recipeIri);
}
