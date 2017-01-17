import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.Kernel32;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.win32.StdCallLibrary;


interface Clibrary extends Library
{
	Clibrary INSTANCE=(Clibrary)Native.loadLibrary("c",Clibrary.class);
	int getpid ();
	void kill(int pid, int signal);
}
interface User32 extends StdCallLibrary {
	User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

	boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);

	WinDef.HWND SetFocus(WinDef.HWND hWnd);

	int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);

	boolean SetForegroundWindow(WinDef.HWND hWnd);
	//boolean IsWindowVisible(HWND hWnd);

	void BringWindowToTop(HWND hWnd);

	void ShowWindow(HWND hWnd, int i);

	boolean IsWindowVisible(HWND hWnd);
	 HWND GetForegroundWindow();  // add this
     boolean CloseWindow(HWND hWnd);

	
}
public class Test {
	

	public static void toForeground(String name) {
		final User32 user32 = User32.INSTANCE;
		//TreeMap<String,HWND> map=new TreeMap();  
		user32.EnumWindows(new WNDENUMPROC() {
			int count = 0;

			public boolean callback(HWND hWnd, Pointer arg1) {
				byte[] windowText = new byte[512];
				user32.GetWindowTextA(hWnd, windowText, 512);
				String wText = Native.toString(windowText);

				// get rid of this if block if you want all windows regardless
				// of whether
				// or not they have text
				if (wText.isEmpty()) {
					return true;
				}
				wText=wText.substring(wText.lastIndexOf('-')+1).trim();
				if (wText.equals(name)) {
					user32.ShowWindow(hWnd, 9);
					user32.SetForegroundWindow(hWnd);
					
			        
					return false;
				}
				
				return true;
			}

		}, null);
		// user32.SetFocus(hWnd);
		
	}
	
	public static ArrayList<String> processList() {
		final User32 user32 = User32.INSTANCE;
		ArrayList<String> map=new ArrayList();  
		user32.EnumWindows(new WNDENUMPROC() {
			int count = 0;

			public boolean callback(HWND hWnd, Pointer arg1) {
				byte[] windowText = new byte[512];
				user32.GetWindowTextA(hWnd, windowText, 512);
				String wText = Native.toString(windowText);

				// get rid of this if block if you want all windows regardless
				// of whether
				// or not they have text
				if (wText.isEmpty()) {
					return true;
				}
				if(user32.IsWindowVisible(hWnd))map.add(wText);
				
				return true;
			}

		}, null);
		// user32.SetFocus(hWnd);
		return map;
	}
	public static void closeProcess(String name) {
		final User32 user32 = User32.INSTANCE;
		//TreeMap<String,HWND> map=new TreeMap();  
		user32.EnumWindows(new WNDENUMPROC() {
			int count = 0;

			public boolean callback(HWND hWnd, Pointer arg1) {
				byte[] windowText = new byte[512];
				user32.GetWindowTextA(hWnd, windowText, 512);
				String wText = Native.toString(windowText);

				// get rid of this if block if you want all windows regardless
				// of whether
				// or not they have text
				if (wText.isEmpty()) {
					return true;
				}
				wText=wText.substring(wText.lastIndexOf('-')+1).trim();
				if (wText.equals(name)) {
				System.out.println("aya");
					com.sun.jna.platform.win32.User32.INSTANCE.PostMessage(hWnd, WinUser.WM_CLOSE, null, null);
					
			        
					return false;
				}
				
				return true;
			}

		}, null);
		// user32.SetFocus(hWnd);
		
	}
	static void minimizeWindow()
	{
		User32 user32=User32.INSTANCE;
		 HWND handle=user32.GetForegroundWindow();
		 
		 user32.CloseWindow(handle);
	}
	static void minimizeAll()
	{

		final User32 user32 = User32.INSTANCE;
		//TreeMap<String,HWND> map=new TreeMap();  
		user32.EnumWindows(new WNDENUMPROC() {
			int count = 0;

			public boolean callback(HWND hWnd, Pointer arg1) {
				byte[] windowText = new byte[512];
				user32.GetWindowTextA(hWnd, windowText, 512);
				String wText = Native.toString(windowText);

				// get rid of this if block if you want all windows regardless
				// of whether
				// or not they have text
				if (wText.isEmpty()) {
					return true;
				}
				//wText=wText.substring(wText.lastIndexOf('-')+1).trim();
				if(user32.IsWindowVisible(hWnd))user32.CloseWindow(hWnd);
				
				return true;
			}

		}, null);
		
	}
}