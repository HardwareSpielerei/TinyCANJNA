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

import de.hardwarespielerei.can.tinycan.jna.NativeAccess;
import de.hardwarespielerei.can.tinycan.jna.NativeAccess.CANMsg;
import de.hardwarespielerei.can.tinycan.jna.NativeAccess.DeviceStatusByReference;
import de.hardwarespielerei.can.tinycan.jna.NativeAccess.NativeReceiveCallback;

/**
 * Represents a channel to a Tiny-CAN adapter.
 * 
 * @author gabriel
 */
public class Channel
{
	// private static final int MSG_SIZE = Native.getNativeSize(CANMsg.class);
	// private static final byte[] DUMMY_BYTE = new byte[1];

	private class ReceiveCallbackTranslator implements NativeReceiveCallback
	{
		private Channel channel;
		private ReceiveCallback callback;

		private ReceiveCallbackTranslator(Channel channel,
				ReceiveCallback callback)
		{
			this.channel = channel;
			this.callback = callback;
		}

		@Override
		public void callback(int index, Pointer msg, int count)
		{
			// for (int i = 0; i < count; i++)
			// {
			// try
			// {
			// int[] content = msg.getIntArray(i * MSG_SIZE, MSG_SIZE / 4);
			// byte length = (byte) (content[1] >> 28);
			// byte[] data = msg.getByteArray(i * MSG_SIZE + 8, length);
			// NativeMessage nativeMsg = new NativeMessage(content[0],
			// false, false, length, data, content[4], content[5]);
			// this.callback.callback(nativeMsg);
			// } catch (Exception e)
			// {
			// // ignore, try to process next message...
			// }
			// }
			int receiveCount = Library.call().CanReceiveGetCount(
					this.channel.index.getIndex());
			while (receiveCount > 0)
			{
				for (int i = 0; i < receiveCount; i++)
				{
					try
					{
						CANMessage nativeMsg = this.channel.read();
						this.callback.callback(nativeMsg);
					} catch (Exception e)
					{
						// ignore, try to process next message...
					}
				}
				receiveCount = Library.call().CanReceiveGetCount(
						this.channel.index.getIndex());
			}
		}
	}

	private Adapter adapter;
	private Index index;

	private NativeReceiveCallback nativeCallBack;

	protected Channel(Adapter adapter, Bitrate bitrate) throws TinyCANException
	{
		this.nativeCallBack = null;
		this.adapter = adapter;
		this.index = new Index(false, false, (byte) 0, (byte) 0, (short) 0);
		TinyCANException.throwOnErrorCode(
				Library.call().CanDeviceOpen(index.getIndex(),
						"Snr=" + adapter.getSerialNumber()),
				"Can't open channel to " + this.adapter + "!");
		try
		{
			TinyCANException.throwOnErrorCode(
					Library.call().CanSetSpeed(this.index.getIndex(),
							bitrate.getCode()), "Can't bitrate to " + bitrate
							+ " kbps on " + this.adapter + "!");

			// CAN Bus Start, clear all FIFOs, filters, buffers and errors
			TinyCANException.throwOnErrorCode(
					Library.call().CanSetMode(this.index.getIndex(),
							NativeAccess.OP_CAN_START,
							NativeAccess.CAN_CMD_ALL_CLEAR),
					"Can't set mode on " + this.adapter + "!");
		} catch (Exception e)
		{
			TinyCANException.throwOnErrorCode(
					Library.call().CanDeviceClose(this.index.getIndex()),
					"Can't close channel to " + this.adapter + "!");
			throw e;
		}
	}

	/**
	 * Closes this channel.
	 * 
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	public void close() throws TinyCANException
	{
		TinyCANException.throwOnErrorCode(
				Library.call().CanDeviceClose(this.index.getIndex()),
				"Can't close channel to " + this.adapter + "!");
	}

	/**
	 * Get adapter status for this channel. If this method is called very often
	 * performance will degrade. It is recommended that it is called at most
	 * once every ten seconds.
	 * 
	 * @return status.
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	public Status getStatus() throws TinyCANException
	{
		DeviceStatusByReference statusReference = new DeviceStatusByReference();
		int status = Library.call().CanGetDeviceStatus(this.index.getIndex(),
				statusReference);
		if (status != 0)
		{
			TinyCANException.throwOnErrorCode(status, "Can't get status from "
					+ this.adapter + "!");
		}
		return new Status(statusReference);
	}

	/**
	 * @return the {@link Index} parameter of this channel.
	 */
	public Index getIndex()
	{
		return this.index;
	}

	/**
	 * Read message from this channel.
	 * 
	 * @return next message.
	 * @throws NoMessageException
	 * @throws TinyCANException
	 */
	public CANMessage read() throws TinyCANException
	{
		// message in receive buffer?
		// int cnt = Library.call().CanReceiveGetCount(this.index.getIndex());
		// if (cnt <= 0)
		// {
		// throw new NoMessageException(cnt,
		// "No messages to read from adapter " + this.adapter + "!");
		// }

		// read from receive buffer
		CANMsg[] buffer = new CANMsg[1];
		int cnt = Library.call().CanReceive(this.index.getIndex(), buffer, 1);
		if (0 == cnt)
		{
			throw new NoMessageException(cnt,
					"No messages to read from adapter " + this.adapter + "!");
		} else if (0 > cnt)
		{
			TinyCANException.throwOnErrorCode(cnt,
					"Can't read message from adapter " + this.adapter + "!");
		}
		return new NativeMessage(buffer[0]);
	}

	/**
	 * Set a receive call back. Set the callback to NULL to reset it. This
	 * channel will keep a Java reference on the native callback object to avoid
	 * it from being disposed.
	 * 
	 * @param callBack
	 *            references the callback to set or NULL to reset it.
	 */
	public void setReceiveCallBack(ReceiveCallback callBack)
	{
		Library.call().CanSetEvents(EventMask.DisableReceiveMessages.getCode());
		NativeReceiveCallback nextNativeCallBack = null;
		if (null != callBack)
		{
			nextNativeCallBack = new ReceiveCallbackTranslator(this, callBack);
			Library.call().CanSetRxEventCallback(nextNativeCallBack);
			Library.call().CanSetEvents(
					EventMask.EnableReceiveMessages.getCode());
		}
		this.nativeCallBack = nextNativeCallBack;
	}

	/**
	 * Write message to this channel.
	 * 
	 * @param msg
	 *            references the message structure.
	 * @throws TinyCANException
	 *             on errors while accessing Tiny-CAN.
	 */
	public void write(CANMessage msg) throws TinyCANException
	{
		NativeMessage nativeMsg;
		if (msg instanceof NativeMessage)
		{
			nativeMsg = (NativeMessage) msg;
		} else
		{
			// long now = System.currentTimeMillis();
			nativeMsg = new NativeMessage(msg.getId(), false, true,
					msg.getLength(), msg.getData(),
					// TODO: time required?
					0, 0);
		}
		CANMsg[] buffer = new CANMsg[1];
		buffer[0] = nativeMsg.getNativeMessage();
		int cnt = Library.call().CanTransmit(this.index.getIndex(), buffer, 1);
		if (0 > cnt)
		{
			TinyCANException.throwOnErrorCode(cnt, "Can't write to adapter "
					+ this.adapter + "!");
		}
		// cnt = Library.call().CanTransmitGetCount(this.index.getIndex());
		// TinyCANException.throwOnErrorCode(
		// Library.call().CanTransmitSet(this.index.getIndex(), (short) 0,
		// 0), "Can't transmit buffer to adapter " + this.adapter
		// + "!");
	}

	/**
	 * @return true if a receive call back was set, false otherwise.
	 */
	public boolean isReceiveCallBackSet()
	{
		return null != this.nativeCallBack;
	}

	@Override
	public String toString()
	{
		return ("Channel " + this.index + " to " + this.adapter);
	}
}
