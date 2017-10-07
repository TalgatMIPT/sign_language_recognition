// Copyright (С) ABBYY (BIT Software), 1993 - 2012. All rights reserved.
// Author: Michael Rozumyanskiy

package com.future_prospects.mike.signlanguagerecognition.utils;

import android.os.Handler;

/**
 * The base class for the registration of observers and send them notifications.
 *   * Notifications are delivered through a message queue for a given thread.
 * 
 * @param <T>
 *            Type of observer's
 */
public abstract class HandlerObservable<T> extends Observable<T> {
	/** Message handler, which will be sent a notification.. */
	private final Handler _handler;

	/**
	 * @param handler
	 *            Message handler, which will be sent a notification.
	 */
	protected HandlerObservable( final Handler handler ) {
		_handler = handler;
	}

	/**
	 * Notify recipients of events has occurred.
	 * 
	 * @param action
	 *            The action to be performed by each recipient.
	 */
	protected final void notifyObservers( final Action<T> action ) {
		if( _handler.getLooper().getThread() == Thread.currentThread() ) {
			for( final T observer : getObservers() ) {
				action.perform( observer );
			}
		} else {
			for( final T observer : getObservers() ) {
				notifyObserver( observer, action );
			}
		}
	}

	/**
	 * Add the handler to the message queue.
	 * 
	 * @param observer
	 *            Notification recipient, whose method is to be called
	 * @param action
	 *            The action to be performed by the recipient.
	 */
	protected final void notifyObserver( final T observer, final Action<T> action ) {
		_handler.post( new Runnable() {
			@Override
			public void run() {
				action.perform( observer );
			}
		} );
	}
}
