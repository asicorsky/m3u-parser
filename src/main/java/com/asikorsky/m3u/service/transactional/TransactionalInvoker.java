package com.asikorsky.m3u.service.transactional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionalInvoker {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void executeInNewTransaction(Runnable runnable);
}
