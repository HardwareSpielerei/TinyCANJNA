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
import de.hardwarespielerei.can.tinycan.jna.NativeAccess.CANMsg;

/**
 * CAN frame.
 * 
 * @author gabriel
 */
public class NativeMessage implements CANMessage
{
	private CANMsg msg;

	/**
	 * Constructs a CAN frame.
	 * 
	 * @param id
	 *            contains the message ID.
	 * @param isRemoteTransmitionRequest
	 * @param isExtendedFrameFormat
	 * @param length
	 *            contains the frame size.
	 * @param data
	 *            contains data bytes.
	 * @param seconds
	 * @param useconds
	 * @throws IllegalArgumentException
	 *             is the reserver has more then 6 bits or the data is more than
	 *             8 bytes long.
	 */
	public NativeMessage(int id, boolean isRemoteTransmitionRequest,
			boolean isExtendedFrameFormat, byte length, byte data[],
			int seconds, int useconds)
	{
		if (null != data && data.length > 8)
		{
			throw new IllegalArgumentException(
					"Data is more than 8 bytes long!");
		}
		this.msg = new CANMsg();
		this.msg.id = id;
		this.msg.flags = (length & 0xF)
				| (isRemoteTransmitionRequest ? 0x40 : 0)
				| (isExtendedFrameFormat ? 0x80 : 0);
		// this.msg.data = data;
		for (int i = 0; i < data.length; i++)
		{
			this.msg.data[i] = data[i];
		}
		this.msg.time = new NativeAccess.Time();
		this.msg.time.sec = seconds;
		this.msg.time.usec = useconds;
	}

	protected NativeMessage(CANMsg msg)
	{
		this.msg = msg;
	}

	protected CANMsg getNativeMessage()
	{
		return this.msg;
	}

	/**
	 * @return message ID.
	 */
	public int getId()
	{
		return this.msg.id;
	}

	@Override
	public boolean isTransmitted()
	{
		return (this.msg.flags & 0x80000) == 0x80000;
	}

	@Override
	public boolean isReserved()
	{
		return (this.msg.flags & 0x40000) == 0x40000;
	}

	@Override
	public boolean isRemoteTransmitionRequest()
	{
		return (this.msg.flags & 0x20000) == 0x20000;
	}

	@Override
	public boolean isExtendedFrameFormat()
	{
		return (this.msg.flags & 0x10000) == 0x20000;
	}

	@Override
	public byte getSource()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @return the frame size.
	 */
	public byte getLength()
	{
		// return (byte) ((this.msg.flags >> 28) & 0x8);
		return (byte) (this.msg.flags & 0xF);
	}

	/**
	 * @return the data bytes.
	 */
	public byte[] getData()
	{
		return this.msg.data;
	}

	@Override
	public int getSeconds()
	{
		return this.msg.time.sec;
	}

	@Override
	public int getMicroSeconds()
	{
		return this.msg.time.usec;
	}

	@Override
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		result.append("ID:          0x").append(toHexString(this.getId()))
				.append("\t").append(toBinaryString(this.getId())).append("\n");
		result.append("Length:      ").append(this.getLength()).append("\t0x")
				.append(toHexString(this.getLength())).append("\n");
		String dataPrefix = "Data:\t";
		for (byte data : this.getData())
		{
			result.append(dataPrefix).append(data).append("\t0x")
					.append(toHexString(data)).append("\t")
					.append(toBinaryString(data)).append("\n");
			dataPrefix = "\t";
		}
		return result.toString();
	}

	private static String toHexString(int value, int len)
	{
		String hexString = Integer.toHexString(value);
		if (hexString.length() > len)
		{
			hexString = hexString.substring(hexString.length() - len,
					hexString.length());
		}
		StringBuffer result = new StringBuffer(hexString);
		while (result.length() < len)
		{
			result.insert(0, '0');
		}
		return result.toString();
	}

	private static String toHexString(int value)
	{
		return toHexString(value, 8);
	}

	private static String toHexString(byte value)
	{
		return toHexString(value, 2);
	}

	private static String toBinaryString(int value, int len)
	{
		String hexString = Integer.toBinaryString(value);
		if (hexString.length() > len)
		{
			hexString = hexString.substring(hexString.length() - len,
					hexString.length());
		}
		StringBuffer result = new StringBuffer(hexString);
		while (result.length() < len)
		{
			result.insert(0, '0');
		}
		return result.toString();
	}

	private static String toBinaryString(int value)
	{
		return toBinaryString(value, 32);
	}

	private static String toBinaryString(byte value)
	{
		return toBinaryString(value, 8);
	}
}
