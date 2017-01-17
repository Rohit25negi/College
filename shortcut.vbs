Set wsc = WScript.CreateObject("WScript.Shell")
Set lnk = wsc.CreateShortcut(".\application\" & WScript.Arguments.Item(0))
lnk.targetpath = WScript.Arguments.Item(1)
lnk.description = "Bla bla"
lnk.workingdirectory = WScript.Arguments.Item(1) 
lnk.save