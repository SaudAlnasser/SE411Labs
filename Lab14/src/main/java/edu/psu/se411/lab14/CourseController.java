package edu.psu.se411.lab14;

import java.util.List;
import edu.psu.se411.lab14.model.Course;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseRepository repository;
    private final JdbcAggregateTemplate template;

    public CourseController(CourseRepository repository, JdbcAggregateTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    @GetMapping
    public List<Course> all() { return repository.findAll(); }

    @GetMapping("/{id}")
    public Course get(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }

    @PostMapping
    public ResponseEntity<Course> create(@Valid @RequestBody Course course) {
        if (repository.existsById(course.id())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Course id already exists");
        }
        try {
            // Explicit insert supports client-assigned IDs (including zero).
            Course created = template.insert(course);
            var location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(created.id()).toUri();
            return ResponseEntity.created(location).body(created);
        } catch (DuplicateKeyException exception) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Course id already exists", exception);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Course course) {
        get(id);
        if (!id.equals(course.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body id must match route id");
        }
        template.update(course);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        get(id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
