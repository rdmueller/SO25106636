class BootStrap {

    def init = { servletContext ->
			new test.BankAccount(name: 'a1', branch: 'b1',  amount: 100).save(flush: true, failOnError: true)
    	new test.BankAccount(name: 'a2', branch: 'b2',  amount: 100).save(flush: true, failOnError: true)    	
    }
    def destroy = {
    }
}
