package app.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import app.documents.Article;

@Repository
public interface ArticleRepository extends MongoRepository<Article, Integer>{

}
