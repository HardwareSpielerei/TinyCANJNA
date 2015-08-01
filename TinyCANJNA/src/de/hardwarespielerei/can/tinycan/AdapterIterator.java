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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import de.hardwarespielerei.can.tinycan.jna.NativeAccess;

/**
 * Use this to iterate over the available Tiny-CAN adapters or to look for an
 * adapter with a given serial number. Using the serial number allows keyless
 * communication in the application using Tiny-CAN.
 * 
 * @author gabriel
 */
public class AdapterIterator implements Iterator<Adapter>
{
	private List<Adapter> adapterList;
	private Iterator<Adapter> iterator;

	/**
	 * Constructs an iterator over the available Tiny-CAN adapters.
	 * 
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	public AdapterIterator() throws TinyCANException
	{
		// this(null);
		// }
		//
		// TODO: Check if we need options...
		// /**
		// * Constructs an iterator over the available Tiny-CAN adapters.
		//
		// * @param options
		// * @throws TinyCANException
		// */
		// public AdapterIterator(String options) throws TinyCANException
		// {
		PointerByReference devicesListReference = new PointerByReference();
		int cnt = Library.call().CanExGetDeviceList(devicesListReference,
				NativeAccess.DEVICE_LIST_TINY_CAN);
		if (cnt < 0)
		{
			TinyCANException.throwOnErrorCode(cnt, "Can't get device list!");
		}
		try
		{
			this.adapterList = new ArrayList<Adapter>(cnt == 0 ? 1 : cnt);
			Pointer p = devicesListReference.getValue();
			for (int i = 0; i < cnt; i++)
			{
				Adapter tinyCan = new Adapter(p, i);
				if (tinyCan.hasModuleFeatures())
				{
					tinyCan = new AdapterWithModuleFeatures(p, i);
				}
				this.adapterList.add(tinyCan);
			}
		} finally
		{
			Library.call().CanExDataFree(devicesListReference);
		}
		this.iterator = this.adapterList.iterator();
	}

	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}

	@Override
	public Adapter next()
	{
		return this.iterator.next();
	}

	/**
	 * Returns the next element in the iteration with the given serial number.
	 * 
	 * @param serialNumber
	 *            references the serial number of a Tiny-CAN adapter.
	 * @return the Adapter with the given serial number.
	 * @throws NoSuchElementException
	 *             if the iteration has no (more) adapters with the given serial
	 *             number.
	 */
	public Adapter next(String serialNumber)
	{
		Adapter result = null;
		while (this.hasNext())
		{
			Adapter next = this.next();
			if (next.getSerialNumber().equals(serialNumber))
			{
				result = next;
			}
		}
		if (null == result)
		{
			throw new NoSuchElementException();
		}
		return result;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException(AdapterIterator.class.getName()
				+ " doesn't support remove!");
	}
}
