package uni.fmi.masters.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fmi.masters.models.UserPreference;

import java.util.Optional;

public interface UserPreferenceRepo extends JpaRepository<UserPreference,Long> {
    Optional<UserPreference> findByUsername(String username);
}

