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

/**
 * The index parameter contains filter, buffer, device and channel details.
 * 
 * @author gabriel
 * 
 */
public class Index
{
	private int index;

	/**
	 * Constructs an index parameter.
	 * 
	 * @param isSoft
	 *            has to be true for a software filter, false for a hardware
	 *            filter.
	 * @param isTxD
	 *            has to be true for a send buffer, false for a receive buffer.
	 * @param device
	 *            contains the CAN device ID. The parameter is not supported yet
	 *            and always 0.
	 * @param channel
	 *            the CAN channel ID. The parameter is not supported yet and
	 *            always 0.
	 * @param subIndex
	 *            the buffer index on the CAN device / CAN channel.
	 */
	public Index(boolean isSoft, boolean isTxD, byte device, byte channel,
			short subIndex)
	{
		this.index |= isSoft ? 0x2000000 : 0;
		this.index |= isTxD ? 0x1000000 : 0;
		this.index |= (device << 20) & 0xF00000;
		this.index |= (channel << 16) & 0xF0000;
		this.index |= subIndex & 0xFFFF;
	}

	/**
	 * @return the numerical representation of this index.
	 */
	public int getIndex()
	{
		return this.index;
	}

	/**
	 * @return true for a software filter, false for a hardware filter.
	 */
	public boolean isSoft()
	{
		return (this.index & 0x2000000) == 0x2000000;
	}

	/**
	 * @return true for a send buffer, false for a receive buffer.
	 */
	public boolean isTxD()
	{
		return (this.index & 0x1000000) == 0x1000000;
	}

	/**
	 * @return the CAN device ID. It is used when multiple modules are connected
	 *         to the PC. The parameter is not supported yet and always 0.
	 */
	public byte getDevice()
	{
		return (byte) ((this.index >> 20) & 0xF);
	}

	/**
	 * @return the CAN channel ID. The parameter is not supported yet and always
	 *         0.
	 */
	public byte getChannel()
	{
		return (byte) ((this.index >> 16) & 0xF);
	}

	/**
	 * @return the buffer index on the CAN device / CAN channel.
	 */
	public short getSubIndex()
	{
		return (short) (this.index & 0xFFFF);
	}

	@Override
	public String toString()
	{
		return ((this.isSoft() ? "Software Filter, " : "Hardware Filter, ")
				+ (this.isTxD() ? "TxD, " : "RxD, ") + "Device " + getDevice()
				+ ", Channel " + this.getChannel() + ", Sub Index " + this
					.getSubIndex());
	}
}
