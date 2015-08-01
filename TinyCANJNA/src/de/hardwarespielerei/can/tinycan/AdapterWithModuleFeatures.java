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

import com.sun.jna.Pointer;

/**
 * Represents a Tiny-CAN adapter with module features. Use
 * {@link AdapterIterator} to iterate over the available adapters or to look for
 * an adapter with a given serial number.
 * 
 * @author gabriel
 */
public class AdapterWithModuleFeatures extends Adapter
{
	private int canClock;
	private int flags;
	private int canChannelsCount;
	private int hwRxFilterCount;
	private int hwTxPufferCount;

	protected AdapterWithModuleFeatures(Pointer p, int index)
	{
		super(p, index);

		if (!this.hasModuleFeatures())
		{
			throw new UnsupportedOperationException("Adapter " + this
					+ " doesn't support module featues!");
		}

		// TODO: consider index
		// TODO: extract integers?
		byte[] data = p.getByteArray(index * SIZE, 363);
		int offset = 343;
		this.canClock = data[offset++] << 24 | data[offset++] << 16
				| data[offset++] << 8 | data[offset++];
		this.flags = data[offset++] << 24 | data[offset++] << 16
				| data[offset++] << 8 | data[offset++];
		this.canChannelsCount = data[offset++] << 24 | data[offset++] << 16
				| data[offset++] << 8 | data[offset++];
		this.hwRxFilterCount = data[offset++] << 24 | data[offset++] << 16
				| data[offset++] << 8 | data[offset++];
		this.hwTxPufferCount = data[offset++] << 24 | data[offset++] << 16
				| data[offset++] << 8 | data[offset++];
	}

	/**
	 * @return the CAN controller's clock frequency of this adapter.
	 */
	public int getCanClock()
	{
		return this.canClock;
	}

	/**
	 * @return the supported features of this adapter.
	 */
	public int getFlags()
	{
		return this.flags;
	}

	/**
	 * @return the number of CAN interfaces of this adapter (reserved for future
	 *         adapters with multiple interfaces).
	 */
	public int getCanChannelsCount()
	{
		return this.canChannelsCount;
	}

	/**
	 * @return the number of receive filters available on this adapter.
	 */
	public int getHardwareReceiveFilterCount()
	{
		return this.hwRxFilterCount;
	}

	/**
	 * @return the number of transmit buffers with timer support available on
	 *         this adapter.
	 */
	public int getHardwareTransmitPufferCount()
	{
		return this.hwTxPufferCount;
	}
}
