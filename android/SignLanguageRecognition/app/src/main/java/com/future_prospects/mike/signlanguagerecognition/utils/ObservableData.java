// Copyright (С) ABBYY (BIT Software), 1993 - 2012. All rights reserved.
// Author: Michael Rozumyanskiy

package com.future_prospects.mike.signlanguagerecognition.utils;

import android.os.Handler;

import com.abbyy.mobile.imaging.MIProcessor;
import com.abbyy.mobile.imaging.errors.MIExecutionResult;

public class ObservableData extends HandlerObservable<Observer> {
	/** Last action performed for registered notification recipients. */
	private volatile Action<Observer> _lastAction;

	public ObservableData( final Handler handler ) {
		super( handler );
		clearResult();
	}

	public void notifyProcessingUpdated(final MIProcessor processor, final int index, final int progress ) {
		notifyObserversSticky( new Action<Observer>() {
			@Override
			public void perform( final Observer observer ) {
				observer.onProcessingUpdated( processor, index, progress );
			}
		} );
	}

	public void notifyProcessingFinished(final MIProcessor processor, final MIExecutionResult result ) {
		notifyObserversSticky( new Action<Observer>() {
			@Override
			public void perform( final Observer observer ) {
				observer.onProcessingFinished( processor, result );
			}
		} );
	}

	/**
	 * Notify recipients of events on the events with the MIP-engine.
	 * When registering the new recipients, they will be notified of the latest event, sent out by this
	 * method.
	 *
	 * @param action
	 *            The action to be performed for each recipient.
	 */
	public void notifyObserversSticky( final Action<Observer> action ) {
		// Save the action as the last perfomed to notify recipients that will be registered later.
		_lastAction = action;
		notifyObservers( action );
	}

	@Override
	public void registerObserver( final Observer observer ) {
		super.registerObserver( observer );
		// Notify new recipient about last event.
		// Уведомляем только что подключённого получателя уведомлений о последнем событии.
		notifyObserver( observer, _lastAction );
	}

	public void clearResult() {
		_lastAction = new Action<Observer>() {
			@Override
			public void perform( final Observer observer ) {
				// The default action - empty, so there is nothing to do.
			}
		};
	}
}
