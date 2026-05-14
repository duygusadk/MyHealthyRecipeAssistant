package uni.fmi.masters.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fmi.masters.models.UserPreference;

public interface UserPreferenceRepo extends JpaRepository<UserPreference,Long> {
}
