package com.boot.mit.assessment.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.mit.assessment.model.Transaction;
import com.boot.mit.assessment.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;

	public void save (Transaction transaction) {
		transactionRepository.save(transaction);
	}
	
	public Optional<Transaction> findByTrackId(String trackId) {
		return transactionRepository.findByTrackId(trackId);
	}
	
	public Optional<Transaction> findByReference(String reference){
		return transactionRepository.findByReference(reference);
	}
	
	public List<Transaction> findByCreatedDate(Date inicio, Date fin){
		return transactionRepository.findByCreatedDate(inicio, fin);
	}
	
	public void deleteByTrackId(Transaction transaction) {
		transactionRepository.delete(transaction);
	}
}
