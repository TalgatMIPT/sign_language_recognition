// Copyright (c) ABBYY (BIT Software), 1993 - 2012. All rights reserved.
// Author: Yuri Kopachevskiy

package com.future_prospects.mike.signlanguagerecognition.utils;

import com.abbyy.mobile.imaging.MIProcessor;
import com.abbyy.mobile.imaging.errors.MIExecutionResult;

public interface Observer {

	public void onProcessingUpdated(final MIProcessor processor, final int index, final int progress);

	public void onProcessingFinished(final MIProcessor processor, final MIExecutionResult result);
}
