package uni.fmi.masters.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fmi.masters.models.RecipeMetadata;

public interface RecipeMetadataRepo extends JpaRepository<RecipeMetadata,Long> {

}
