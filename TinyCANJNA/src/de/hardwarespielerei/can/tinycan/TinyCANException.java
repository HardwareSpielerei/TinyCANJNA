/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Gabriel Schmidt
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.hardwarespielerei.can.tinycan;

import de.hardwarespielerei.can.tinycan.jna.NativeAccess;

/**
 * Describes an {@link NativeAccess#ERROR_GENERAL} error from Tiny-CAN.
 * Subclasses describe specific errors.
 * 
 * @author gabriel
 */
public class TinyCANException extends java.lang.Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5376569765457518837L;

	/**
	 * @param returnCode
	 *            contains the native return code.
	 * @param message
	 *            references the detail message. The detail message is saved for
	 *            later retrieval by the {@link Throwable#getMessage()} method.
	 */
	protected TinyCANException(int returnCode, String message)
	{
		super(message + " RC = " + returnCode);
	}

	/**
	 * Throws the Tiny-CAN exception corresponding to the given native return
	 * code. Returns without any effect on {@link NativeAccess#ERROR_OK} return
	 * code.
	 * 
	 * @param rc
	 *            contains the native return code.
	 * @param message
	 *            references the detail message. The detail message is saved for
	 *            later retrieval by the {@link Throwable#getMessage()} method.
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	protected static void throwOnErrorCode(int rc, String message)
			throws TinyCANException
	{
		switch (rc)
		{
			case NativeAccess.ERROR_OK:
				// everything OK, do nothing...
				break;
			case NativeAccess.ERROR_DRIVER_NOT_INITIALIZED:
				throw new DriverNotInitalizedException(rc, message);
			case NativeAccess.ERROR_INVALID_PARAMETER:
				throw new InvalidParameterException(rc, message);
			case NativeAccess.ERROR_INVALID_INDEX:
				throw new InvalidIndexException(rc, message);
			case NativeAccess.ERROR_INVALID_CHANNEL:
				throw new InvalidChannelException(rc, message);
			case NativeAccess.ERROR_FIFO_WRITE:
				throw new FIFOWriteException(rc, message);
			case NativeAccess.ERROR_BUFFER_WRITE:
				throw new BufferWriteException(rc, message);
			case NativeAccess.ERROR_FIFO_READ:
				throw new FIFOReadException(rc, message);
			case NativeAccess.ERROR_BUFFER_READ:
				throw new BufferReadException(rc, message);
			case NativeAccess.ERROR_VARIABLE_NOT_FOUND:
				throw new VariableNotFoundException(rc, message);
			case NativeAccess.ERROR_VARIABLE_NOT_READABLE:
				throw new VariableNotReadableException(rc, message);
			case NativeAccess.ERROR_VARIABLE_READ_BUFFER_EXCEEDED:
				throw new VariableReadBufferExceededException(rc, message);
			case NativeAccess.ERROR_VARIABLE_NOT_WRITABLE:
				throw new VariableNotWriteableException(rc, message);
			case NativeAccess.ERROR_STRING_TOO_BIG:
				throw new StringToBigException(rc, message);
			case NativeAccess.ERROR_MINIMUM_UNDERRUN:
				throw new MinimumExceededException(rc, message);
			case NativeAccess.ERROR_MAXIMUM_EXCEEDED:
				throw new MaximumExceededException(rc, message);
			case NativeAccess.ERROR_ACCESS_DENIED:
				throw new AccessDeniedException(rc, message);
			case NativeAccess.ERROR_INVALID_CAN_SPEED:
				throw new InvalidCANSpeedException(rc, message);
			case NativeAccess.ERROR_INVALID_BAUD_RATE:
				throw new InvalidBaudRateException(rc, message);
			case NativeAccess.ERROR_VALUE_NOT_SET:
				throw new ValueNotSetException(rc, message);
			case NativeAccess.ERROR_HARDWARE_CONNECTION:
				throw new HardwareConnectionException(rc, message);
			case NativeAccess.ERROR_HARDWARE_COMMUNICATION:
				throw new HardwareCommunicationException(rc, message);
			case NativeAccess.ERROR_HARDWARE_PARAMETER:
				throw new HardwareParameterException(rc, message);
			case NativeAccess.ERROR_MEMORY_LOW:
				throw new MemoryLowException(rc, message);
			case NativeAccess.ERROR_SYSTEM_RESSOURCES:
				throw new SystemRessourcesException(rc, message);
			case NativeAccess.ERROR_SYSTEM_CALL:
				throw new SystemCallException(rc, message);
			case NativeAccess.ERROR_MAIN_THREAD_BUSY:
				throw new MainThreadBusyException(rc, message);
			case NativeAccess.ERROR_GENERAL:
			default:
				throw new TinyCANException(rc, message);
		}
	}
}
