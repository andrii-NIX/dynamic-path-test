package test.querydsl.dynamic_path_test;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.querydsl.core.types.dsl.Expressions;

@RepositoryRestResource
public interface DataRepository extends MongoRepository<Data, String>, QueryDslPredicateExecutor<Data>, QuerydslBinderCustomizer<QData> {

    @Override
    default void customize(QuerydslBindings bindings, QData root) {
        bindings.bind(Expressions.stringPath(QData.data.value, "key1")).as("k1").first((path, value) -> path.contains(value));
    }
}
