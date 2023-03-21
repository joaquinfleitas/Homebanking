package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	/*@Autowired
	private PasswordEncoder passwordEncoder;*/

	public String cvv(){
		int cvvGen = (int) (Math.random()* 999);
		String cvvGenCompletado = String.format("%03d", cvvGen);
		return cvvGenCompletado;
	}
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner initData(ClientRepositories ClientRepositories, AccountRepositories AccountRepositories, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123456"));
			Client client2 = new Client("joaquin", "Fleitas", "joaquinfleitas71@gmail.com", passwordEncoder.encode("123456"));
			Client client3 = new Client("Admin", "Admin", "admin@gmail.com", passwordEncoder.encode("123456"));

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000 );
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500 );

			Account account3 = new Account("3", LocalDateTime.now(), 102000 );
			Account account4 = new Account("4", LocalDateTime.now().plusDays(1), 75 );

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 10000.00, "tu credito", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -15000.00, "tu debito", LocalDateTime.now());

			Loan hipotecario = new Loan("Hipotecario", 500000, List.of(12,24,36,48,60), 1.2);
			Loan personal = new Loan("Personal", 100000, List.of(6,12,24),1.2);
			Loan automotriz = new Loan("Automotriz", 300000, List.of(6,12,24,36), 1.2);

			ClientLoan clientLoan1 = new ClientLoan( 40000, 60, hipotecario, client1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, personal, client1);
			ClientLoan clientLoan3 = new ClientLoan( 100000, 24, personal, client2);
			ClientLoan clientLoan4 = new ClientLoan(200000,36, automotriz, client2);

			Card card1 = new Card(client1.getFirstName()+" "+client1.getLastName(), numeroAleatorioDeTarjeta(), cvv(), CardType.DEBIT, CardColor.GOLD, LocalDate.now().plusYears(5), LocalDate.now());
			Card card2 = new Card(client1.getFirstName()+" "+client1.getLastName(), numeroAleatorioDeTarjeta(), cvv(), CardType.CREDIT, CardColor.TITANIUM, LocalDate.now().plusYears(5), LocalDate.now());

			Card card3 = new Card(client2.getFirstName()+" "+client2.getLastName(), numeroAleatorioDeTarjeta(), cvv(), CardType.CREDIT, CardColor.SILVER, LocalDate.now().plusYears(5), LocalDate.now());


			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);

			client1.addCard(card1);
			client1.addCard(card2);

			client2.addCard(card3);

			ClientRepositories.save(client1);
			ClientRepositories.save(client2);
			ClientRepositories.save(client3);

			AccountRepositories.save(account1);
			AccountRepositories.save(account2);
			AccountRepositories.save(account3);
			AccountRepositories.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);


			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			hipotecario.addclientloan(clientLoan1);
			personal.addclientloan(clientLoan1);
			personal.addclientloan(clientLoan2);
			automotriz.addclientloan(clientLoan2);

			ClientRepositories.save(client1);
			ClientRepositories.save(client2);
			ClientRepositories.save(client3);

			loanRepository.save(hipotecario);
			loanRepository.save(automotriz);
			loanRepository.save(personal);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
	}*/

	public static String numeroAleatorioDeTarjeta(){
		int first = (int) (Math.random()*(9999 - 1000)+ 1000);
		int second = (int) (Math.random()*(9999 - 1000)+ 1000);
		int third = (int) (Math.random()*(9999 - 1000)+ 1000);
		int fourth = (int) (Math.random()*(9999 - 1000)+ 1000);

		String seccionDeNumeros = first + "-" + second + "-" + third + "-" + fourth;
		return seccionDeNumeros;
	}


}
