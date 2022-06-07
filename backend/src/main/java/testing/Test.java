package testing;

import java.sql.SQLException;

import models.card.VirtualCard;
import models.crypto.Transaction;
import models.crypto.UserCrypto;
import models.users.UserRegistered;
import services.card.VirtualCardService;
import services.crypto.TransactionService;
import services.users.UserService;

public class Test {
	public static void main(String args[]) throws SQLException  //static method  
	{  
		Transaction t = new Transaction();
		UserService us = new UserService();
		VirtualCard vc = new VirtualCard();
		
		UserCrypto uc = new UserCrypto(1,1,"eth",(float)0.76);
		TransactionService ts = new TransactionService();
		
		ts.buyFiat(1, 10);
		

	}
}
