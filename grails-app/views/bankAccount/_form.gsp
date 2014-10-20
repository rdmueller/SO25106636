<%@ page import="test.BankAccount" %>



<div class="fieldcontain ${hasErrors(bean: bankAccountInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="bankAccount.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${bankAccountInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: bankAccountInstance, field: 'amount', 'error')} required">
	<label for="amount">
		<g:message code="bankAccount.amount.label" default="Amount" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="amount" value="${fieldValue(bean: bankAccountInstance, field: 'amount')}" required=""/>

</div>

