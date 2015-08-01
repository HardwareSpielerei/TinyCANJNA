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

package de.hardwarespielerei.can.tinycan.test;

import com.sun.jna.Platform;

import de.hardwarespielerei.can.tinycan.Adapter;
import de.hardwarespielerei.can.tinycan.AdapterIterator;
import de.hardwarespielerei.can.tinycan.Library;
import de.hardwarespielerei.can.tinycan.TinyCANException;
import de.hardwarespielerei.can.tinycan.Version;

/**
 * @author gabriel
 * 
 */
public class LibraryTest
{

	/**
	 * Lists all known Tiny-CAN adapters using the Tiny-CAN DLL and JNA.
	 * 
	 * @param args
	 *            command line arguments.
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	public static void main(String[] args) throws TinyCANException
	{
		System.out.println("################################");
		System.out.println("# Tiny-CAN Native Test         #");
		System.out.println("# (C) 2015 by Gabriel Schmidt  #");
		System.out.println("#  @see hardwarespielerei.de   #");
		System.out.println("################################");
		System.out.println();
		System.out.println("Tiny-CAN   V" + Version.VERSION);
		System.out.println("JNA        V" + com.sun.jna.Native.VERSION);
		System.out.println("JNA NATIVE V" + com.sun.jna.Native.VERSION_NATIVE);
		System.out.println();
		if (Platform.isWindows())
		{
			Library.load();
			try
			{
				System.out.println("List of known adapters:");
				AdapterIterator iterator = new AdapterIterator();
				int i = 1;
				while (iterator.hasNext())
				{
					Adapter tinyCan = iterator.next();
					System.out.println(i++ + ". Adapter:\t"
							+ tinyCan.getDescription() + "\tID="
							+ tinyCan.getSerialNumber());
				}
				System.out.println("End of list.");
			} finally
			{
				Library.unload();
			}
		} else
		{
			System.err.println("Tiny-CAN is not supported on this platform!");
		}
	}
}
