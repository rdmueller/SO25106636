package test

class BankAccount {
      String name
      Float amount
			String branch
			
      static constraints = {
        name unique: true
      }
    }