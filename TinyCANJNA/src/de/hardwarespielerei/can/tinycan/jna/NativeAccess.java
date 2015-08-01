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

package de.hardwarespielerei.can.tinycan.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;

/**
 * Native access to Tiny-CAN DLL via JNA.
 * 
 * @author gabriel
 */
public interface NativeAccess extends Library
{
	/**
	 * Everything OK.
	 */
	public static final int ERROR_OK = 0;

	/**
	 * Driver not initialized.
	 */
	public static final int ERROR_DRIVER_NOT_INITIALIZED = -1;

	/**
	 * Invalid parameter.
	 */
	public static final int ERROR_INVALID_PARAMETER = -2;

	/**
	 * Invalid index.
	 */
	public static final int ERROR_INVALID_INDEX = -3;

	/**
	 * Invalid CAN channel.
	 */
	public static final int ERROR_INVALID_CHANNEL = -4;

	/**
	 * General error.
	 */
	public static final int ERROR_GENERAL = -5;

	/**
	 * Error while writing to FIFO.
	 */
	public static final int ERROR_FIFO_WRITE = -6;

	/**
	 * Error while writing to buffer.
	 */
	public static final int ERROR_BUFFER_WRITE = -7;

	/**
	 * Error while reading from FIFO.
	 */
	public static final int ERROR_FIFO_READ = -8;

	/**
	 * Error while reading from buffer.
	 */
	public static final int ERROR_BUFFER_READ = -9;

	/**
	 * Variable not found.
	 */
	public static final int ERROR_VARIABLE_NOT_FOUND = -10;

	/**
	 * Variable not readable.
	 */
	public static final int ERROR_VARIABLE_NOT_READABLE = -11;

	/**
	 * Read buffer exceeded.
	 */
	public static final int ERROR_VARIABLE_READ_BUFFER_EXCEEDED = -12;

	/**
	 * Variable not writable.
	 */
	public static final int ERROR_VARIABLE_NOT_WRITABLE = -13;

	/**
	 * String too big.
	 */
	public static final int ERROR_STRING_TOO_BIG = -14;

	/**
	 * Minimum value underrun.
	 */
	public static final int ERROR_MINIMUM_UNDERRUN = -15;

	/**
	 * Maximum value exceeded.
	 */
	public static final int ERROR_MAXIMUM_EXCEEDED = -16;

	/**
	 * Access denied.
	 */
	public static final int ERROR_ACCESS_DENIED = -17;

	/**
	 * Invalid CAN speed.
	 */
	public static final int ERROR_INVALID_CAN_SPEED = -18;

	/**
	 * Invalid baud rate.
	 */
	public static final int ERROR_INVALID_BAUD_RATE = -19;

	/**
	 * Value not set.
	 */
	public static final int ERROR_VALUE_NOT_SET = -20;

	/**
	 * No connection to the hardware.
	 */
	public static final int ERROR_HARDWARE_CONNECTION = -21;

	/**
	 * Hardware communication error.
	 */
	public static final int ERROR_HARDWARE_COMMUNICATION = -22;

	/**
	 * Hardware sends wrong number of parameters.
	 */
	public static final int ERROR_HARDWARE_PARAMETER = -23;

	/**
	 * Lack of memory.
	 */
	public static final int ERROR_MEMORY_LOW = -24;

	/**
	 * Lack of system resources.
	 */
	public static final int ERROR_SYSTEM_RESSOURCES = -25;

	/**
	 * System call returned error.
	 */
	public static final int ERROR_SYSTEM_CALL = -26;

	/**
	 * Main thread busy.
	 */
	public static final int ERROR_MAIN_THREAD_BUSY = -27;

	/**
	 * Driver DLL has't been loaded yet.
	 */
	public static final int DRV_NOT_LOAD = 0;

	/**
	 * Driver DLL has't been initialized yet. Probably
	 * {@link #CanInitDriver(String)} has not been called.
	 */
	public static final int DRV_STATUS_NOT_INIT = 1;

	/**
	 * Driver initialized successfully.
	 */
	public static final int DRV_STATUS_INIT = 2;
	// Treiber erfolgreich initialisiert

	/**
	 * Port not open.
	 */
	public static final int DRV_STATUS_PORT_NOT_OPEN = 3;
	// TODO Die Schnittstelle wurde (NICHT?) geöffnet

	/**
	 * Port open.
	 */
	public static final int DRV_STATUS_PORT_OPEN = 4;
	// TODO Die Schnittstelle wurde nicht (??) geöffnet

	/**
	 * Connection to hardware established.
	 */
	public static final int DRV_STATUS_DEVICE_FOUND = 5;
	// Verbindung zur Hardware wurde hergestellt

	/**
	 * Device opened and initialized successfully.
	 */
	public static final int DRV_STATUS_CAN_OPEN = 6;
	// Device wurde geöffnet und erfolgreich initialisiert

	/**
	 * CAN bus RUN - transmit only. (not in use!)
	 */
	public static final int DRV_STATUS_CAN_RUN_TX = 7;
	// CAN Bus RUN nur Transmitter (wird nicht verwendet!)

	/**
	 * CAN bus RUN.
	 */
	public static final int DRV_STATUS_CAN_RUN = 8;
	// CAN Bus RUN

	/**
	 * CAN controller OK.
	 */
	public static final byte CAN_STATUS_OK = 0;
	// CAN-Controller: Ok

	/**
	 * CAN controller reports CAN error.
	 */
	public static final byte CAN_STATUS_ERROR = 1;
	// CAN-Controller: CAN Error

	/**
	 * CAN controller reports "Error warning".
	 */
	public static final byte CAN_STATUS_WARNING = 2;
	// CAN-Controller: Error warning

	/**
	 * CAN controller reports "Error passive".
	 */
	public static final byte CAN_STATUS_PASSIVE = 3;
	// CAN-Controller: Error passiv

	/**
	 * CAN controller reports bus off.
	 */
	public static final byte CAN_STATUS_BUS_OFF = 4;
	// CAN-Controller: Bus Off

	/**
	 * CAN controller reports unknown status.
	 */
	public static final byte CAN_STATUS_UNKNOWN = 5;
	// CAN-Controller: Status Unbekannt

	public static final byte FIFO_OK = 0;
	// Fifo-Status: Ok

	public static final byte FIFO_OVERRUN = 1;
	// Fifo-Status: Überlauf

	public static final byte FIFO_STATUS_UNBEKANNT = 2;
	// Fifo-Status: Unbekannt

	public static final int INDEX_INVALID = 0xFFFFFFFF;

	public static final int DEVICE_LIST_ALL_FTDI = 0;

	public static final int DEVICE_LIST_TINY_CAN = 1;

	public static final byte OP_CAN_NO_CHANGE = 0; // Aktuellen Zustand nicht
													// ändern

	public static final byte OP_CAN_START = 1; // Startet den CAN-Bus

	public static final byte OP_CAN_STOP = 2; // Stopt den CAN-Bus

	public static final byte OP_CAN_RESET = 3; // Reset CAN Controller (BusOff
												// löschen)

	public static final byte OP_CAN_START_LOM = 4; // Startet den CAN-Bus im
													// Silent Mode (Listen Only
													// Mode)

	public static final byte OP_CAN_START_NO_RETRANS = 5; // Startet den CAN-Bus
															// im Automatic
															// Retransmission
															// disable Mode

	public static final short CAN_CMD_NONE = 0x0000;

	public static final short CAN_CMD_RXD_OVERRUN_CLEAR = 0x0001;

	public static final short CAN_CMD_RXD_FIFOS_CLEAR = 0x0002;

	public static final short CAN_CMD_TXD_OVERRUN_CLEAR = 0x0004;

	public static final short CAN_CMD_TXD_FIFOS_CLEAR = 0x0008;

	public static final short CAN_CMD_HW_FILTER_CLEAR = 0x0010;

	public static final short CAN_CMD_SW_FILTER_CLEAR = 0x0020;

	public static final short CAN_CMD_TXD_PUFFERS_CLEAR = 0x0040;

	public static final short CAN_CMD_ALL_CLEAR = 0x0FFF;

	// Makros für SetEvent

	public static final short EVENT_ENABLE_PNP_CHANGE = 0x0001;

	public static final short EVENT_ENABLE_STATUS_CHANGE = 0x0002;

	public static final short EVENT_ENABLE_RX_FILTER_MESSAGES = 0x0004;

	public static final short EVENT_ENABLE_RX_MESSAGES = 0x0008;

	public static final short EVENT_ENABLE_ALL = 0x00FF;

	public static final short EVENT_DISABLE_PNP_CHANGE = 0x0100;

	public static final short EVENT_DISABLE_STATUS_CHANGE = 0x0200;

	public static final short EVENT_DISABLE_RX_FILTER_MESSAGES = 0x0400;

	public static final short EVENT_DISABLE_RX_MESSAGES = 0x0800;

	public static final short EVENT_DISABLE_ALL = (short) 0xFF00;

	public class Time extends Structure
	{
		// uint32_t Sec;
		// uint32_t USec;

		public int sec;
		public int usec;

		@Override
		protected List<String> getFieldOrder()
		{
			return Arrays.asList(new String[] { "sec", "usec" });
		}
	}

	public class DeviceStatus extends Structure
	{
		public int DrvStatus;

		public byte CanStatus;

		public byte FifoStatus;

		@Override
		protected List getFieldOrder()
		{
			return Arrays.asList(new String[] { "DrvStatus", "CanStatus",
					"FifoStatus" });
		}
	}

	public class DeviceStatusByReference extends DeviceStatus implements
			Structure.ByReference
	{
	}

	public class CANMsg extends Structure
	{
		public int id;

		public int flags;

		public byte data[] = new byte[8];

		public Time time;

		@Override
		protected List<String> getFieldOrder()
		{
			return Arrays
					.asList(new String[] { "id", "flags", "data", "time" });
		}
	}

	/**
	 * Native CAN frame reference.
	 * 
	 * @author gabriel
	 * @see CANMsg
	 */
	public class CANMsgByReference extends CANMsg implements
			Structure.ByReference
	{
	}

	public interface NativeReceiveCallback extends Callback
	{
		// void callback(int index, CANMsg[] msg, int count);
		void callback(int index, Pointer msg, int count);
	}

	// int32_t CanInitDriver(char *options)
	public int CanInitDriver(String options);

	// void CanDownDriver(void)
	public void CanDownDriver();

	// int32_t CanExGetDeviceList(struct TCanDevicesList **devices_list, int
	// flags)
	public int CanExGetDeviceList(PointerByReference devicesList, int flags);

	// void CanExDataFree(void **data)
	public void CanExDataFree(PointerByReference data);

	// int32_t CanDeviceOpen(uint32_t index, char *parameter)
	public int CanDeviceOpen(int index, String parameter);

	// int32_t CanDeviceClose(uint32_t index)
	public int CanDeviceClose(int index);

	// Int CanGetDeviceStatus(uint32_t index, struct TDeviceStatus *status)
	public int CanGetDeviceStatus(int index, DeviceStatusByReference status);

	// int32_t CanReceive(uint32_t index, struct TCanMsg *msg, int32_t count)
	public int CanReceive(int index, CANMsg[] msg, int count);

	// uint32_t CanReceiveGetCount(uint32_t index)
	public int CanReceiveGetCount(int index);

	// int32_t CanTransmit(uint32_t index, struct TCanMsg *msg, int32_t count)
	public int CanTransmit(int index, CANMsg[] msg, int count);

	// uint32_t CanTransmitGetCount(uint32_t index)
	public int CanTransmitGetCount(int index);

	// int32_t CanTransmitSet(uint32_t index, uint16_t cmd, uint32_t time);
	public int CanTransmitSet(int index, short cmd, int time);

	// int32_t CanSetSpeed(uint32_t index, uint16_t speed)
	public int CanSetSpeed(int index, short speed);

	// int32_t CanSetMode(uint32_t index, unsigned char can_op_mode, uint16_t
	// can_command)
	public int CanSetMode(int index, byte mode, short command);

	// void CanSetRxEventCallback(void CALLBACK (*event) (uint32_t index, struct
	// TCanMsg *msg, int32_t count))
	public void CanSetRxEventCallback(NativeReceiveCallback fn);

	// void CanSetEvents(uint16_t events)
	public void CanSetEvents(short events);
}