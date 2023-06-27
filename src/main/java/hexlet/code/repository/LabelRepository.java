package hexlet.code.repository;

import hexlet.code.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {

    List<Label> findAllByOrderByIdAsc();

    Optional<Label> findLabelById(long id);
}
