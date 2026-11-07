package edu.psu.se411.lab14;

import edu.psu.se411.lab14.model.Course;
import org.springframework.data.repository.ListCrudRepository;

public interface CourseRepository extends ListCrudRepository<Course, Long> {
}
