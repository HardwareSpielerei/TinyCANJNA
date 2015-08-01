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
 * CAN frame.
 * 
 * @author gabriel
 */
public interface CANMessage
{
	/**
	 * @return message ID.
	 */
	public int getId();

	/**
	 * @return the frame size.
	 */
	public byte getLength();

	/**
	 * @return true if this message has been transmitted, false if it has been
	 *         received. After transmitting a message successfully it is written
	 *         in the receive buffer as acknowledgement. Not all modules support
	 *         this feature and the features must be enabled.
	 */
	public boolean isTransmitted();

	/**
	 * @return false because the flag is reserved and must be set to false
	 *         always.
	 */
	public boolean isReserved();

	/**
	 * @return true if this message is a Remote Transmition Request, false
	 *         otherwise.
	 */
	public boolean isRemoteTransmitionRequest();

	/**
	 * @return true if this message has Extended Frame Format (29 Bit Id's),
	 *         false otherwise (11 Bit Id's).
	 */
	public boolean isExtendedFrameFormat();

	/**
	 * @return the source of this message, i.e. the device.
	 */
	public byte getSource();

	/**
	 * @return the data bytes.
	 */
	public byte[] getData();

	/**
	 * @return the seconds of the message's timestamp.
	 */
	public int getSeconds();

	/**
	 * @return the microseconds of the message's timestamp.
	 */
	public int getMicroSeconds();
}
