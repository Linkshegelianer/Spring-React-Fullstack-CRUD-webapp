package project.code.repository;

import com.querydsl.core.types.Predicate;
import project.code.domain.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task> {

    List<Task> findAllByOrderByIdAsc();
    Optional<Task> findTaskById(long id);

    List<Task> findAll(Predicate predicate, Sort sort);
}
