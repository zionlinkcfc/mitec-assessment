package com.boot.mit.assessment.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.boot.mit.assessment.model.Transaction;
import com.boot.mit.assessment.service.TransactionService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@PostMapping("/save")
	public ResponseEntity<?> saveTransaction(@RequestBody Transaction transaction) {
		try {
			UUID uuid= UUID.randomUUID();
			transaction.setTrackId(uuid.toString());
			Optional<Transaction> transactionDb= transactionService.findByReference(transaction.getReference());
			
			if(transactionDb!=null && transactionDb.isPresent()) {
				throw new IllegalArgumentException("Ya existe una transacción con la misma referencia");
			}
			
			if(transaction.getAmount()<0) {
				throw new IllegalArgumentException("Argumento monto debe ser positivo");
			}
			int longitud =transaction.getPan().length();
			if(longitud<16 || longitud>18) {
				throw new IllegalArgumentException("El número de tarjeta no tiene la longitud correcta");
			}
			
			transactionService.save(transaction);
			return new ResponseEntity<String>("Exito",HttpStatus.OK);
		}  catch (IllegalArgumentException ex) {
			System.out.println("Error: "+ex.getMessage());
			return new ResponseEntity<String>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			System.out.println("Ocurrió una excepcion: " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/consult/{trackID}")
	public ResponseEntity<?> getTransactionByTrackId(@PathVariable(value = "trackID") String trackid) {
		Optional<Transaction> transactionDb= transactionService.findByTrackId(trackid);
		if(transactionDb!= null && transactionDb.isPresent()) {
			return new ResponseEntity<>(transactionDb.get(),HttpStatus.OK);
		}else
			return new ResponseEntity<String>("Transacción no encontrada", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/consult/{startDate}/{endDate}")
	public ResponseEntity<?> getTransactionsByCreateDate(@PathVariable(value = "startDate") String inicio, @PathVariable(value = "endDate") String fin){
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			inicio+=" 00:00:00";
			fin+=" 23:59:59";
			Date startDate = formatter.parse(inicio);
			Date finishDate = formatter.parse(fin);
			List<Transaction> listResult= transactionService.findByCreatedDate(startDate, finishDate);
			if(listResult.size()>0) {
				return new ResponseEntity<>(listResult,HttpStatus.OK);
			}else {
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error con formato de fechas",HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/delete/{trackID}")
	public ResponseEntity<?> deleteTransaction(@PathVariable(value = "trackID") String trackid) {
		Optional<Transaction> transactionDelete=  transactionService.findByTrackId(trackid);
		if(transactionDelete!= null && transactionDelete.isPresent()) {
			transactionService.deleteByTrackId(transactionDelete.get());
			return new ResponseEntity<String>("Registro eliminado",HttpStatus.OK);
		}else
			return new ResponseEntity<String>("No se encontró el registro",HttpStatus.NOT_FOUND);
		
	}
	
}
