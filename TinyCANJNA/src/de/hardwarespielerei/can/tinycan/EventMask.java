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
 * Event masks for Tiny-CAN usage.
 * 
 * @author gabriel
 */
public enum EventMask
{
	/**
	 * Enable plug & play event callback functions.
	 */
	EnablePnPChange(NativeAccess.EVENT_ENABLE_PNP_CHANGE),

	/**
	 * Enable status event callback functions.
	 */
	EnableStatusChange(NativeAccess.EVENT_ENABLE_STATUS_CHANGE),

	/**
	 * Enable receive event callback functions for filter messages.
	 */
	EnableReceiveFilterMessages(NativeAccess.EVENT_ENABLE_RX_FILTER_MESSAGES),

	/**
	 * Enable receive event callback functions for messages.
	 */
	EnableReceiveMessages(NativeAccess.EVENT_ENABLE_RX_MESSAGES),

	/**
	 * Enable all event callback functions.
	 */
	EnableAll(NativeAccess.EVENT_ENABLE_ALL),

	/**
	 * Disable plug & play event callback functions.
	 */
	DisablePnPChange(NativeAccess.EVENT_DISABLE_PNP_CHANGE),

	/**
	 * Disable status event callback functions.
	 */
	DisableStatusChange(NativeAccess.EVENT_DISABLE_STATUS_CHANGE),

	/**
	 * Disable receive event callback functions for filter messages.
	 */
	DisableReceiveFilterMessages(NativeAccess.EVENT_DISABLE_RX_FILTER_MESSAGES),

	/**
	 * Disable receive event callback functions for messages.
	 */
	DisableReceiveMessages(NativeAccess.EVENT_DISABLE_RX_MESSAGES),

	/**
	 * Disable all event callback functions.
	 */
	DisableAll(NativeAccess.EVENT_DISABLE_ALL);

	private short code;

	private EventMask(short code)
	{
		this.code = code;
	}

	protected short getCode()
	{
		return this.code;
	}

	@Override
	public String toString()
	{
		return this.name();
	}
}
