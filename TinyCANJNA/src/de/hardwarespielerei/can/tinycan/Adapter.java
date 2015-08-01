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

import java.util.Arrays;

import com.sun.jna.Pointer;

import de.hardwarespielerei.can.tinycan.jna.NativeAccess;

/**
 * Represents a Tiny-CAN adapter. Use {@link AdapterIterator} to iterate over
 * the available adapters or to look for an adapter with a given serial number.
 * 
 * @author gabriel
 */
public class Adapter
{
	protected final int SIZE = 363;

	private int canIdx;
	private int hwId;
	private String deviceName;
	private String serialNumber;
	private String description;

	protected Adapter(Pointer p, int index)
	{
		// TODO: extract integers?
		byte[] data = p.getByteArray(index * SIZE, SIZE);
		int offset = 0;
		this.canIdx = data[offset++] << 24 | data[offset++] << 16
				| data[offset++] << 8 | data[offset++];
		this.hwId = data[offset++] << 24 | data[offset++] << 16
				| data[offset++] << 8 | data[offset++];
		byte[] dataString = Arrays.copyOfRange(data, offset, offset += 255);
		this.deviceName = new String(dataString).trim();
		dataString = Arrays.copyOfRange(data, offset, offset += 16);
		this.serialNumber = new String(dataString).trim();
		dataString = Arrays.copyOfRange(data, offset, offset += 64);
		this.description = new String(dataString).trim();
	}

	/**
	 * @return the device index if this adapter is opened,
	 *         {@link NativeAccess#INDEX_INVALID} otherwise.
	 */
	public int getCanIndex()
	{
		return this.canIdx;
	}

	/**
	 * @return a 32-bit key identifying this adapter. Some adapters have to be
	 *         opened before this value is set.
	 */
	public int getHardwareID()
	{
		return this.hwId;
	}

	/**
	 * @return the device name of this adapter, e. g. /dev/ttyUSB0 (Linux only).
	 */
	public String getDeviceName()
	{
		return this.deviceName;
	}

	/**
	 * @return the serial number of this adapter.
	 */
	public String getSerialNumber()
	{
		return this.serialNumber;
	}

	/**
	 * @return the description of this adapter, e. g. "Tiny-CAN IV-XL". Only
	 *         some adapters support this feature, e. g. Tiny-CAN II-XL, IV-XL
	 *         and M1.
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @return true if this adapter supports module features, false otherwise.
	 *         If an adapter support module features, it can be cast to
	 *         {@link AdapterWithModuleFeatures} to access them.
	 */
	public boolean hasModuleFeatures()
	{
		return this.getHardwareID() > 0;
	}

	/**
	 * Open a channel to this adapter.
	 * 
	 * The adapter's serial number is taken from this adapter and can not be
	 * specified.
	 * 
	 * @param bitrate
	 *            references the bit rate used on this channel.
	 * @return channel.
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	public Channel openChannel(Bitrate bitrate) throws TinyCANException
	{
		return new Channel(this, bitrate);

	}

	@Override
	public String toString()
	{
		return (this.getDescription() + " # " + this.getSerialNumber());
	}
}