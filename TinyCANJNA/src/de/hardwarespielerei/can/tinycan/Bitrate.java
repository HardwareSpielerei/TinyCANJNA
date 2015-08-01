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
 * Bit rates available for Tiny-CAN usage.
 * 
 * @author gabriel
 */
public enum Bitrate
{
	/**
	 * 10 kbps
	 */
	Bitrate10kbps((short) 10),

	/**
	 * 20 kbps
	 */
	Bitrate20kbps((short) 20),

	/**
	 * 50 kbps
	 */
	Bitrate50kbps((short) 50),

	/**
	 * 100 kbps
	 */
	Bitrate100kbps((short) 100),

	/**
	 * 125 kbps
	 */
	Bitrate125kbps((short) 125),

	/**
	 * 250 kbps
	 */
	Bitrate250kbps((short) 250),

	/**
	 * 500 kbps
	 */
	Bitrate500kbps((short) 500),

	/**
	 * 800 kbps
	 */
	Bitrate800kbps((short) 800),

	/**
	 * 1 Mbps
	 */
	Bitrate1Mbps((short) 1000);

	private short code;

	private Bitrate(short code)
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
		return this.code + " kbps";
	}
}
