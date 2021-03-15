package com.asikorsky.m3u.service.transactional.impl;

import com.asikorsky.m3u.service.transactional.TransactionalInvoker;
import org.springframework.stereotype.Component;

@Component
public class TransactionalInvokerImpl implements TransactionalInvoker {

	@Override
	public void executeInNewTransaction(Runnable runnable) {

		runnable.run();
	}
}
