package psu.lab13.repositories;
import psu.lab13.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends CrudRepository<Product,Integer>, PagingAndSortingRepository<Product,Integer> {}
