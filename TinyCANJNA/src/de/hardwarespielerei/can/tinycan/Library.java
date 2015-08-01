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

import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

import java.io.File;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;

import de.hardwarespielerei.can.tinycan.jna.NativeAccess;

/**
 * Controls loading and unloading of the library and provides access to the
 * library.
 * 
 * @author gabriel
 */
public class Library
{
	private static final String REG_TINY_CAN_API_X64 = "Software\\Wow6432Node\\Tiny-CAN\\API";
	private static final String REG_TINY_CAN_API = "Software\\Tiny-CAN\\API";
	private static final String REG_TINY_CAN_API_PATH_ENTRY = "PATH";
	private static final String API_DRIVER_X64_DIR = "x64";
	private static final String API_DRIVER_NAME = "mhstcan";

	// private static final String API_DRIVER_DLL = "mhstcan.dll";

	private static String getPathToDLL()
	{
		String res = null;
		try
		{
			String key = (Platform.is64Bit() ? REG_TINY_CAN_API_X64
					: REG_TINY_CAN_API);
			StringBuffer path = new StringBuffer(
					Advapi32Util.registryGetStringValue(HKEY_LOCAL_MACHINE,
							key, REG_TINY_CAN_API_PATH_ENTRY));
			if (Platform.is64Bit())
			{
				path.append(File.separator);
				path.append(API_DRIVER_X64_DIR);
			}
			res = path.toString();
		} catch (Win32Exception e)
		{
			// return null...
		}
		return res;
	}

	private static NativeAccess nativeAccess;

	/**
	 * Loads the library.
	 * 
	 * @throws TinyCANException
	 */
	public static void load() throws TinyCANException
	{
		Library.load(null);
	}

	/**
	 * Loads the library using options to the driver.
	 * 
	 * @param options
	 *            contains a key-value-list of options for the driver (see
	 *            chapter 3.5.3 of the Tiny-CAN API Referenz-Handbuch for format
	 *            of the list and available options).
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	public static void load(String options) throws TinyCANException
	{
		if (null == nativeAccess)
		{
			if (Platform.isWindows())
			{
				String pathToDLL = Library.getPathToDLL();
				if (null != pathToDLL)
				{
					String jnaLibraryPath = System
							.getProperty("jna.library.path");
					if (null == jnaLibraryPath
							|| jnaLibraryPath.indexOf(pathToDLL) < 0)
					{
						jnaLibraryPath = pathToDLL
								+ (null == jnaLibraryPath ? ""
										: File.pathSeparator + jnaLibraryPath);
						System.setProperty("jna.library.path", jnaLibraryPath);
					}
				}
			}
			Library.nativeAccess = (NativeAccess) Native.loadLibrary(
					API_DRIVER_NAME, NativeAccess.class);
			TinyCANException.throwOnErrorCode(
					nativeAccess.CanInitDriver(options), "Can't init driver!");
		}
	}

	/**
	 * Unloads the library.
	 */
	public static void unload()
	{
		nativeAccess.CanDownDriver();
		nativeAccess = null;
	}

	/**
	 * Provides access to the library.
	 * 
	 * @return an interface to the library.
	 */
	public static NativeAccess call()
	{
		return Library.nativeAccess;
	}
}
