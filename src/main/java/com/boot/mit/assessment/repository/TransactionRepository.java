package com.boot.mit.assessment.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.boot.mit.assessment.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, ObjectId>{
	
	@Query("{'trackId' : ?0 }")
	public Optional<Transaction> findByTrackId(String trackId);
	
	@Query("{'reference' : ?0 }")
	public Optional<Transaction> findByReference(String reference);
	
	@Query("{'createdAt': {$gte:?0, $lte:?1 }}")
	public List<Transaction> findByCreatedDate(Date inicio, Date fin );
	
	@Query("{'trackId' : ?0 }")
	public void delete(String trackId);
}
