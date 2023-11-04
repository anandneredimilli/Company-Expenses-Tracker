package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import app.repositories.ExpenditureRepo;

//@SpringBootApplication
public class JpaTester implements CommandLineRunner {

	@Autowired
	ExpenditureRepo exprepo;
	
	public static void main(String[] args) {
		SpringApplication.run(JpaTester.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
//		displayAll();
//		catname();
//		deptname();
		paymode();
	}

	public void displayAll() {

		for (var c : exprepo.findAll()) {
			System.out.println(c);
		}
	}
	
	public void catname() {
		for(var c: exprepo.findAll()) {
			System.out.println(c.getCategoryCode() +"  -  "+c.getCategory().getCategoryName());
		}
	}
	
	public void deptname() {
		for(var c: exprepo.findAll()) {
			System.out.println(c.getAuthorizedBy()+" - "+c.getDepartmentCode()+" - "+c.getDepartment().getDepartmentName());
		}
	}

	public void paymode() {
		for(var c:exprepo.findAll()) {
			System.out.println(c.getAmount()+" - "+c.getPaymentMode()+" - "+c.getPaymentmode().getPaymentName());
		}
	}
}
