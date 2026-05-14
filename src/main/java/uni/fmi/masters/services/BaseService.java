package uni.fmi.masters.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class BaseService<T,R extends JpaRepository<T,Long>> {
    private R repository;

    public BaseService(R repository) {
        this.repository = repository;
    }

    public List<T> findAll(){
        return repository.findAll();
    }

    public T findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Optional<T> findByIdOptional(Long id){
        return repository.findById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public long count() {
        return repository.count();
    }

    public R getRepository() {
        return repository;
    }
}

