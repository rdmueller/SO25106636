package test



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BankAccountController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond BankAccount.list(params), model:[bankAccountInstanceCount: BankAccount.count()]
    }

    def show(BankAccount bankAccountInstance) {
        respond bankAccountInstance
    }

    def create() {
        respond new BankAccount(params)
    }

		def thread1() {
			println '1> User 1 in thread 1'
			def ac1 = BankAccount.findByName('a1')
			println '1> Tracing something about '+ac1+'/'+ac1.branch
			sleep(1000)
			//this is aproc the time when thread2 kicks in
			sleep(1000)
			println '1> waking up'
			def myBranchAccounts = BankAccount.findAllByBranch('b2') 
			println '1> '+myBranchAccounts
			def result = []
			test.BankAccount.findAllByBranch('b2').every{ result<<it.dump().encodeAsHTML()}
			
			render text:result.join(', ')
		}
		def thread2() {
		  println '2> User 2 in thread 2'
		  sleep(1000)
		  println '2> waking up'
			def acc = test.BankAccount.findByName('a1')
			println '2> '+acc.inspect()
			acc.branch = 'b2'
			acc.save(flush: true, failOnError:true)
			println '2> saved'
			render text:acc.toString()
		}

    @Transactional
    def save(BankAccount bankAccountInstance) {
        if (bankAccountInstance == null) {
            notFound()
            return
        }

        if (bankAccountInstance.hasErrors()) {
            respond bankAccountInstance.errors, view:'create'
            return
        }

        bankAccountInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), bankAccountInstance.id])
                redirect bankAccountInstance
            }
            '*' { respond bankAccountInstance, [status: CREATED] }
        }
    }

    def edit(BankAccount bankAccountInstance) {
        respond bankAccountInstance
    }

    @Transactional
    def update(BankAccount bankAccountInstance) {
        if (bankAccountInstance == null) {
            notFound()
            return
        }

        if (bankAccountInstance.hasErrors()) {
            respond bankAccountInstance.errors, view:'edit'
            return
        }

        bankAccountInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'BankAccount.label', default: 'BankAccount'), bankAccountInstance.id])
                redirect bankAccountInstance
            }
            '*'{ respond bankAccountInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(BankAccount bankAccountInstance) {

        if (bankAccountInstance == null) {
            notFound()
            return
        }

        bankAccountInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'BankAccount.label', default: 'BankAccount'), bankAccountInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
