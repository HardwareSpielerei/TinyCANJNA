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

package de.hardwarespielerei.can.tinycan.util;

import java.util.Date;
import java.util.NoSuchElementException;

import com.sun.jna.Platform;

import de.hardwarespielerei.can.tinycan.Adapter;
import de.hardwarespielerei.can.tinycan.AdapterIterator;
import de.hardwarespielerei.can.tinycan.Bitrate;
import de.hardwarespielerei.can.tinycan.CANMessage;
import de.hardwarespielerei.can.tinycan.Channel;
import de.hardwarespielerei.can.tinycan.Library;
import de.hardwarespielerei.can.tinycan.NoMessageException;
import de.hardwarespielerei.can.tinycan.ReceiveCallback;
import de.hardwarespielerei.can.tinycan.TinyCANException;
import de.hardwarespielerei.can.tinycan.Version;

/**
 * Simple CAN sniffer and logger.
 * 
 * @author gabriel
 */
public class Logger
{
	/**
	 * Logger can either loop and read messages from the channel or register a
	 * call back to do so.
	 * 
	 * @author gabriel
	 */
	public enum Mode
	{
		/**
		 * Logger will loop and read messages from the channel.
		 */
		LOOP(),

		/**
		 * Logger will register a call back to read messages from the channel.
		 */
		CALLBACK();
	}

	private static class LogReceiveCallback implements ReceiveCallback
	{
		@Override
		public void callback(CANMessage msg)
		{
			System.out.println("[" + new Date(System.currentTimeMillis())
					+ "][MSGRECEIVE]");
			System.out.println(msg);
		}
	}

	private static class ShutdownHook extends Thread
	{
		private Channel channel;

		protected ShutdownHook(Channel channel)
		{
			this.channel = channel;
		}

		@Override
		public void run()
		{
			try
			{
				channel.close();
			} catch (TinyCANException e)
			{
				System.err.println("ERROR: Can't close Tiny-CAN properly!");
				e.printStackTrace();
			}
			System.out.println("INFO: Tiny-CAN log ends.");
		}
	}

	/**
	 * @param args
	 *            references command line arguments.
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	public static void main(String[] args) throws TinyCANException
	{
		System.out.println("###############################");
		System.out.println("# Tiny-CAN Logger             #");
		System.out.println("# (C) 2015 by Gabriel Schmidt #");
		System.out.println("#  @see hardwarespielerei.de  #");
		System.out.println("###############################");
		System.out.println();
		System.out.println("Tiny-CAN   V" + Version.VERSION);
		System.out.println("JNA        V" + com.sun.jna.Native.VERSION);
		System.out.println("JNA NATIVE V" + com.sun.jna.Native.VERSION_NATIVE);
		System.out.println();
		if (Platform.isWindows())
		{
			// set defaults
			Mode mode = Mode.CALLBACK;
			Bitrate bitrate = Bitrate.Bitrate250kbps;
			String serialNumber = null;

			// parse command line arguments
			int argPos = 0;
			while (argPos < args.length)
			{
				String arg = args[argPos++];
				try
				{
					switch (arg)
					{
						case "-bitrate":
							String bitrateArg = args[argPos++];
							switch (bitrateArg)
							{
								case "10":
								case "10kbps":
									bitrate = Bitrate.Bitrate10kbps;
									break;
								case "20":
								case "20kbps":
									bitrate = Bitrate.Bitrate20kbps;
									break;
								case "50":
								case "50kbps":
									bitrate = Bitrate.Bitrate50kbps;
									break;
								case "100":
								case "100kbps":
									bitrate = Bitrate.Bitrate100kbps;
									break;
								case "250":
								case "250kbps":
									bitrate = Bitrate.Bitrate250kbps;
									break;
								case "500":
								case "500kbps":
									bitrate = Bitrate.Bitrate500kbps;
									break;
								case "800":
								case "800kbps":
									bitrate = Bitrate.Bitrate800kbps;
									break;
								case "1000":
								case "1Mbps":
									bitrate = Bitrate.Bitrate1Mbps;
									break;
							}
							break;
						case "-mode":
							String modeArg = args[argPos++];
							try
							{
								mode = Mode.valueOf(modeArg.toUpperCase());
							} catch (IllegalArgumentException e)
							{
								System.err.println("WARNING: \"" + modeArg
										+ "\" is not a valid mode!");
							}
							break;
						case "-sn":
							serialNumber = args[argPos++];
							break;
						default:
							System.err.println("WARNING: Unkown argument \""
									+ arg + "\"!");
					}
				} catch (ArrayIndexOutOfBoundsException e)
				{
					System.err
							.println("WARNING: Missing parameter for option \""
									+ arg + "\"!");
				}
			}

			// search for adapter to use...
			Library.load();
			try
			{
				Adapter adapterToUse = null;
				if (null == serialNumber)
				{
					System.out
							.println("No serial number specified - searching for Tiny-CAN adapter with "
									+ bitrate + " kbps...");
					try
					{
						adapterToUse = new AdapterIterator().next();
					} catch (NoSuchElementException e)
					{
						throw new NullPointerException(
								"No Tiny-CAN adapter found!");
					}
				} else
				{
					System.out.println("Searching for Tiny-CAN # "
							+ serialNumber + " with " + bitrate + " kbps...");
					try
					{
						adapterToUse = new AdapterIterator().next(serialNumber);
					} catch (NoSuchElementException e)
					{
						throw new NullPointerException("No Tiny-CAN # "
								+ serialNumber + " found!");
					}
				}

				System.out.println("Start sniffing on Tiny-CAN # "
						+ adapterToUse.getSerialNumber() + " with " + bitrate
						+ " kbps in " + mode.toString().toLowerCase()
						+ " mode...");
				// open channel
				Channel channel = adapterToUse.openChannel(bitrate);
				try
				{
					System.out.println("Press [Control+C] to stop logging...");
					ShutdownHook shutdownHook = new ShutdownHook(channel);
					Runtime.getRuntime().addShutdownHook(shutdownHook);

					// start logging...
					if (mode.equals(Mode.CALLBACK))
					{
						// set log receive callback
						channel.setReceiveCallBack(new LogReceiveCallback());

					}
					try
					{
						boolean goon = true;
						long lastStatusMillis = 0;
						do
						{
							// log status every second
							long now = System.currentTimeMillis();
							if (now - lastStatusMillis >= 10000)
							{
								// try
								// {
								// Status status = channel.getStatus();
								// System.out.println("["
								// + new Date(System.currentTimeMillis())
								// + "][STATUS][" + status + "]");
								// } catch (TinyCANException e)
								// {
								// System.err.println("["
								// + new Date(System.currentTimeMillis())
								// + "][ERROR][" + e.getMessage() + "]");
								// e.printStackTrace(System.err);
								// }
								lastStatusMillis = now;
							}

							if (mode.equals(Mode.LOOP))
							{
								try
								{
									// try to read a message
									CANMessage msg = channel.read();
									System.out.println("["
											+ new Date(System
													.currentTimeMillis())
											+ "][MSGRECEIVE]");
									System.out.println(msg);
								} catch (NoMessageException e)
								{
									// no more messages waiting, stop loop and
									// go to sleep...
									try
									{
										Thread.sleep(100);
									} catch (InterruptedException ie)
									{
										// do nothing
									}
								} catch (TinyCANException e)
								{
									System.err.println("["
											+ new Date(System
													.currentTimeMillis())
											+ "][ERROR][" + e.getMessage()
											+ "]");
									e.printStackTrace(System.err);
								}
							} else
							{
								// sleep for a while...
								try
								{
									Thread.sleep(100);
								} catch (InterruptedException ie)
								{
									// do nothing
								}
							}
						} while (goon);
					} finally
					{
						// exiting do-while-loop unexpectedly...
						System.err
								.println("WARNING: Tiny-CAN log ends unexpectedly!");
						// remove shutdown hook...
						Runtime.getRuntime().removeShutdownHook(shutdownHook);
					}
				} finally
				{
					channel.close();
				}
			} finally
			{
				Library.unload();
			}
		} else
		{
			System.err
					.println("ERROR: Tiny-CAN is not supported on this platform!");
		}
	}
}
