/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jc.android.contact.presentation.exception;

import android.content.Context;
import android.util.Log;

import com.jc.android.base.data.exception.BusinessException;
import com.jc.android.base.data.exception.NetworkConnectionException;
import com.jc.android.base.data.exception.NoConnectionException;
import com.jc.android.module.contact.R;
import com.jc.android.contact.data.exception.UserNotFoundException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Factory used to create error messages from an Exception as a condition.
 */
public class ErrorMessageFactory {

  private ErrorMessageFactory() {
    //empty
  }

  /**
   * Creates a String representing an error message.
   *
   * @param context Context needed to retrieve string resources.
   * @param exception An exception used as a condition to retrieve the correct error message.
   * @return {@link String} an error message.
   */
  public static String create(Context context, Exception exception) {
    String message = context.getString(R.string.exception_message_generic);

    if (exception instanceof NoConnectionException) {
      message = context.getString(R.string.exception_message_no_connection);
    } else if (exception instanceof ConnectException) {
      message = context.getString(R.string.exception_message_connection);
    } else if (exception instanceof HttpException) {
      message = context.getString(R.string.exception_message_HttpException);
    } else if (exception instanceof SocketTimeoutException) {
      message = context.getString(R.string.exception_message_SocketTimeoutException);
    } else if (exception instanceof NetworkConnectionException) {
      message = context.getString(R.string.exception_message_NetworkConnectionException);
    } else if (exception instanceof BusinessException) {
      message = exception.getMessage();
    }
    Log.e(ErrorMessageFactory.class.getSimpleName(), exception.getMessage(), exception);
    return message;

  }
}
